package org.itacademy.blacjackwebflux.repositorio.mysql;

import org.itacademy.blacjackwebflux.model.mysql.Move;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface MoveRepository extends R2dbcRepository<Move, Long> {
    Flux<Move> findByGameId(Long gameId);
}
