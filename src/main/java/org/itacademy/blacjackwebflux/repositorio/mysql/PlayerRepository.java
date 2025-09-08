package org.itacademy.blacjackwebflux.repositorio.mysql;

import org.itacademy.blacjackwebflux.model.mysql.Player;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface PlayerRepository extends R2dbcRepository<Player, Long> {

    Flux<Player> findByNameContainingIgnoreCase(String name);
}
