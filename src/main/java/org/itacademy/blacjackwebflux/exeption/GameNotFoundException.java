package org.itacademy.blacjackwebflux.exeption;

public class GameNotFoundException extends RuntimeException {
    public GameNotFoundException(Long id) {
        super("El recurso con ID " + id + " no fue encontrado.");
    }
}
