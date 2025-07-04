package com.relatos.ms_books_payments.services;

import com.relatos.ms_books_payments.controllers.request.CreateOrderRequest;
import com.relatos.ms_books_payments.controllers.request.OrderItemRequest;
import com.relatos.ms_books_payments.controllers.response.OrderResponse;
import com.relatos.ms_books_payments.controllers.response.commons.GeneralResponse;
import com.relatos.ms_books_payments.domains.Order;
import com.relatos.ms_books_payments.domains.OrderItem;
import com.relatos.ms_books_payments.domains.OrderStatus;
import com.relatos.ms_books_payments.domains.OrderStatusEnum;
import com.relatos.ms_books_payments.externals.CatalogueService;
import com.relatos.ms_books_payments.externals.request.StockRequest;
import com.relatos.ms_books_payments.externals.response.BookResponse;
import com.relatos.ms_books_payments.externals.response.WrapperResponse;
import com.relatos.ms_books_payments.repositories.OrderItemRepository;
import com.relatos.ms_books_payments.repositories.OrderRepository;
import com.relatos.ms_books_payments.repositories.OrderStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final CatalogueService catalogueService;

    // 1. Crear nueva orden
    @Transactional
    public GeneralResponse<OrderResponse> createOrder(CreateOrderRequest orderRequest) {
        OrderStatus orderStatus = orderStatusRepository.findByName(OrderStatusEnum.PENDING.name());
        if (orderStatus == null) {
            orderStatus = orderStatusRepository.save(new OrderStatus(OrderStatusEnum.PENDING.name()));
        }
        orderRequest.getItems().forEach(this::getBookStock);
        Order order = new Order(orderRequest.getUserId(), orderStatus);
        Order orderSaved = orderRepository.save(order);
        List<OrderItem> orderItems = orderRequest.getItems().stream()
                .map(itemRequest -> mapToOrderItem(itemRequest, orderSaved))
                .toList();
        List<OrderItem> orderItemsSaved = orderItemRepository.saveAll(orderItems);
        return new GeneralResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                "Orden creada exitosamente",
                new OrderResponse(order, orderItemsSaved)
        );
    }

    private OrderItem mapToOrderItem(OrderItemRequest itemRequest, Order order) {
        return new OrderItem(
                itemRequest,
                order
        );
    }

    @Transactional
    public GeneralResponse<OrderResponse> addItemToOrder(Long orderId, OrderItemRequest request) {
        Order order = orderRepository.findByIdC(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Orden no encontrada");
        }
        OrderStatus orderStatus = orderStatusRepository.findByName(OrderStatusEnum.PAID.name());
        if (order.getStatus().equals(orderStatus)) {
            throw new IllegalArgumentException("No se puede añadir a una orden ya pagada");
        }
        OrderItem orderItem = orderItemRepository.findOrderItemByOrderAndBookId(order, request.getBookId());
        if (orderItem == null) {
            getBookStock(request);
            orderItemRepository.save(new OrderItem(request, order));
            List<OrderItem> items = orderItemRepository.findOrderItemByOrder(order);
            return new GeneralResponse<>(
                    HttpStatus.OK.value(),
                    HttpStatus.OK.getReasonPhrase(),
                    "Ítem añadido exitosamente",
                    new OrderResponse(order, items)
            );
        }

        orderItem.setQuantity(orderItem.getQuantity() + request.getQuantity());
        orderItem.setPrice(orderItem.getPrice() + request.getPrice());
        request.setQuantity(orderItem.getQuantity());
        getBookStock(request);
        orderItemRepository.save(orderItem);
        List<OrderItem> items = orderItemRepository.findOrderItemByOrder(order);
        return new GeneralResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                "Ítem añadido exitosamente",
                new OrderResponse(order, items)
        );
    }

    // 3. Pagar orden
    @Transactional
    public GeneralResponse<OrderResponse> payOrder(Long orderId) {
        Order order = orderRepository.findByIdC(orderId);
        List<OrderItem> orderItems = orderItemRepository.findOrderItemByOrder(order);

        if (order == null) {
            throw new IllegalArgumentException("Orden no encontrada");
        }

        OrderStatus orderStatus = orderStatusRepository.findByName(OrderStatusEnum.PAID.name());
        if (orderStatus == null) {
            orderStatus = orderStatusRepository.save(new OrderStatus(OrderStatusEnum.PAID.name()));
        }
        if (order.getStatus().equals(orderStatus)) {
            throw new IllegalArgumentException("La orden ya está pagada");
        }
        orderItems.forEach(orderItem -> {
            BookResponse book = getBookStock(new OrderItemRequest(orderItem));
            updateStock(new OrderItemRequest(orderItem), book.getStock()-orderItem.getQuantity());
        });
        order.setStatus(orderStatus);
        order = orderRepository.save(order);

        return new GeneralResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                "Orden pagada exitosamente",
                new OrderResponse(order, orderItems)
        );

    }

    // 4. Consultar órdenes por usuario
    public GeneralResponse<List<OrderResponse>> getOrdersByUser(Long userId) {
        List<Order> orderUser = orderRepository.findByUserId(userId);
        if (orderUser.isEmpty()) {
            throw new IllegalArgumentException("Sin ordenes asignadas al usuario");
        }
        List<OrderResponse> responseList = new ArrayList<>();

        for (Order order : orderUser) {
            List<OrderItem> items = orderItemRepository.findOrderItemByOrder(order);
            responseList.add(new OrderResponse(order, items));
        }

        return new GeneralResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                "Órdenes encontradas",
                responseList
        );
    }

    // 5. Consultar detalle de orden
    public GeneralResponse<OrderResponse> getOrderById(Long orderId) {
        Order order = orderRepository.findByIdC(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Orden no encontrada");
        }
        List<OrderItem> items = orderItemRepository.findOrderItemByOrder(order);

        return new GeneralResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                "Orden encontrada",
                new OrderResponse(order, items)
        );
    }

    // 6. Eliminar orden pendiente
    @Transactional
    public GeneralResponse<?> deleteOrder(Long orderId) {
        Order order = orderRepository.findByIdC(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Orden no encontrada");
        }

        OrderStatus orderStatus = orderStatusRepository.findByName(OrderStatusEnum.PAID.name());
        if (orderStatus == null) {
            orderStatus = orderStatusRepository.save(new OrderStatus(OrderStatusEnum.PAID.name()));
        }
        if (order.getStatus().equals(orderStatus)) {
            throw new IllegalArgumentException("No se puede eliminar una orden pagada");
        }

        orderRepository.softDelete(orderId);

        return new GeneralResponse<>(
                HttpStatus.NO_CONTENT.value(),
                HttpStatus.NO_CONTENT.getReasonPhrase(),
                "Orden eliminada",
                null
        );
    }

    private BookResponse getBookStock(OrderItemRequest item) {
        try {
            WrapperResponse response = catalogueService.getBook(item.getBookId());
            if (response.getBody().getStock() < item.getQuantity()) {
                throw new IllegalArgumentException(String.format("No hay suficiente stock para el libro %s", response.getBody().getTitle()));
            }
            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new IllegalArgumentException(String.format("El libro %d no existe", item.getBookId()));
        }
    }

    private void updateStock(OrderItemRequest item, Integer quantity) {
        try {
            StockRequest stock = new StockRequest(quantity);
            catalogueService.updateStock(item.getBookId(), stock);
        } catch(HttpClientErrorException e) {
            throw new IllegalArgumentException(String.format("El libro %d no existe", item.getBookId()));
        }
    }

}
