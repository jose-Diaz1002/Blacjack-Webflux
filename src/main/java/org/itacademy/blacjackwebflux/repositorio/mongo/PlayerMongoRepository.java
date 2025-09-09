package org.itacademy.blacjackwebflux.repositorio.mongo;

import org.itacademy.blacjackwebflux.model.mongo.PlayerMongo;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface PlayerMongoRepository extends ReactiveMongoRepository<PlayerMongo, String> {

    Flux<PlayerMongo> findByNameContainingIgnoreCase(String name);
}
