package org.itacademy.blacjackwebflux.controllers;

import lombok.RequiredArgsConstructor;
import org.itacademy.blacjackwebflux.repositorio.jugadorRepository;
import org.itacademy.blacjackwebflux.model.Jugador;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("jugadores")
@RequiredArgsConstructor
public class JugadorController {
    private final jugadorRepository jugadorRepository;

    @GetMapping
    public Flux<Jugador> findAll() {
        return jugadorRepository.findAll();
    }
}
