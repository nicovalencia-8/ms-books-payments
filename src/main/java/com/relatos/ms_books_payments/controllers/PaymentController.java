package com.relatos.ms_books_payments.controllers;

import com.relatos.ms_books_payments.controllers.request.CreateOrderRequest;
import com.relatos.ms_books_payments.controllers.request.OrderItemRequest;
import com.relatos.ms_books_payments.controllers.response.OrderResponse;
import com.relatos.ms_books_payments.controllers.response.commons.GeneralResponse;
import com.relatos.ms_books_payments.services.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Pagos", description = "Controlador para administrar los pedidos y pagos")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    @Operation(summary = "Crear orden", description = "Crea un nuevo pedido (carrito) con uno o más libros")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Orden creada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado o sin stock")
    })
    public ResponseEntity<GeneralResponse<OrderResponse>> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        try {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(paymentService.createOrder(request));
        } catch (IllegalArgumentException ex) {
            GeneralResponse<OrderResponse> response = new GeneralResponse<>(
                    HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    ex.getMessage(),
                    null
            );
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }
    }

    @PatchMapping("/{orderId}/add-item")
    @Operation(summary = "Añadir ítem a orden", description = "Añade un nuevo libro a una orden existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ítem añadido exitosamente"),
            @ApiResponse(responseCode = "404", description = "Orden o libro no encontrado")
    })
    public ResponseEntity<GeneralResponse<OrderResponse>> addItemToOrder(
            @PathVariable Long orderId,
            @Valid @RequestBody OrderItemRequest request) {
        try {
            return ResponseEntity.ok(paymentService.addItemToOrder(orderId, request));
        } catch (IllegalArgumentException ex) {
            GeneralResponse<OrderResponse> response = new GeneralResponse<>(
                    HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    ex.getMessage(),
                    null
            );
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }
    }

    @PatchMapping("/{orderId}/pay")
    @Operation(summary = "Pagar orden", description = "Finaliza y paga una orden si todo está en orden")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Orden pagada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Orden o libro no encontrado")
    })
    public ResponseEntity<GeneralResponse<OrderResponse>> payOrder(@PathVariable Long orderId) {
        try{
            return ResponseEntity.ok(paymentService.payOrder(orderId));
        } catch (IllegalArgumentException ex){
            GeneralResponse<OrderResponse> response = new GeneralResponse<>(
                    HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    ex.getMessage(),
                    null
            );
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Consultar órdenes de usuario", description = "Devuelve todas las órdenes de un usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Órdenes encontradas"),
            @ApiResponse(responseCode = "204", description = "Sin órdenes registradas")
    })
    public ResponseEntity<GeneralResponse<List<OrderResponse>>> getOrdersByUser(@PathVariable Long userId) {
        try{
            return ResponseEntity.ok(paymentService.getOrdersByUser(userId));
        } catch (IllegalArgumentException ex){
            GeneralResponse<List<OrderResponse>> response = new GeneralResponse<>(
                    HttpStatus.NO_CONTENT.value(),
                    HttpStatus.NO_CONTENT.getReasonPhrase(),
                    ex.getMessage(),
                    null
            );
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .body(response);
        }
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "Consultar detalle de orden", description = "Consulta la información completa de una orden")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Orden encontrada"),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada")
    })
    public ResponseEntity<GeneralResponse<OrderResponse>> getOrderById(@PathVariable Long orderId) {
        try{
            return ResponseEntity.ok(paymentService.getOrderById(orderId));
        } catch (IllegalArgumentException ex){
            GeneralResponse<OrderResponse> response = new GeneralResponse<>(
                    HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    ex.getMessage(),
                    null
            );
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }
    }

    @DeleteMapping("/{orderId}")
    @Operation(summary = "Eliminar orden no pagada", description = "Elimina una orden solo si está en estado PENDING")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Orden eliminada"),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada")
    })
    public ResponseEntity<GeneralResponse<?>> deleteOrder(@PathVariable Long orderId) {
        try{
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .body(paymentService.deleteOrder(orderId));
        } catch (IllegalArgumentException ex){
            GeneralResponse<?> response = new GeneralResponse<>(
                    HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    ex.getMessage(),
                    null
            );
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }
    }

}
