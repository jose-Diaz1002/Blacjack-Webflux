package org.itacademy.blacjackwebflux.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // --- MANEJADOR 1: RECURSO NO ENCONTRADO (404 Not Found) ---
    // Captura específicamente la excepción personalizada para recursos no encontrados.
    @ExceptionHandler(GameNotFoundException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleNotFoundException(GameNotFoundException ex) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", System.currentTimeMillis());
        errorDetails.put("status", HttpStatus.NOT_FOUND.value());
        errorDetails.put("error", "Not Found");
        errorDetails.put("message", ex.getMessage());

        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails));
    }

    // --- MANEJADOR 2: ERROR INTERNO DEL SERVIDOR (500 Internal Server Error) ---
    // Captura cualquier otra RuntimeException no mapeada.
    @ExceptionHandler(RuntimeException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleGenericRuntimeException(RuntimeException ex) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", System.currentTimeMillis());
        errorDetails.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorDetails.put("error", "Internal Server Error");
        // Exponemos un mensaje genérico por seguridad y el mensaje real para depuración.
        errorDetails.put("message", "Ocurrió un error inesperado. Contacte al administrador.");
        // Si necesitas ver el detalle del error en desarrollo:
        // errorDetails.put("debugMessage", ex.getMessage());

        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDetails));
    }
}