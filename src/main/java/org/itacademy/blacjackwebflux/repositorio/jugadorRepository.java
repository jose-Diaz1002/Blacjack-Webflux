package org.itacademy.blacjackwebflux.repositorio;

import org.itacademy.blacjackwebflux.model.Jugador;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface jugadorRepository extends ReactiveCrudRepository<Jugador, Long> {
}
