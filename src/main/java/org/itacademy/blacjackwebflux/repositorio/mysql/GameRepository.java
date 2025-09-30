package org.itacademy.blacjackwebflux.repositorio.mysql;

import org.itacademy.blacjackwebflux.model.mysql.Game;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface GameRepository extends R2dbcRepository<Game, Long> {
    Flux<Game> findByPlayerId(Long playerId);

}
