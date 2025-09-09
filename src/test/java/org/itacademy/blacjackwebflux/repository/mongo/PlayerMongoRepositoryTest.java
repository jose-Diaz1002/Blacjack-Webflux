package org.itacademy.blacjackwebflux.repository.mongo;


import org.itacademy.blacjackwebflux.model.mongo.PlayerMongo;
import org.itacademy.blacjackwebflux.repositorio.mongo.PlayerMongoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@DataMongoTest
public class PlayerMongoRepositoryTest {

    @Autowired
    private PlayerMongoRepository playerMongoRepository;

    @Test
    void testSaveAndFindPlayer() {
        PlayerMongo newPlayer = new PlayerMongo();
        newPlayer.setId("test-id-123");
        newPlayer.setName("TestPlayerMongo");
        newPlayer.setScore(100);

        Mono<PlayerMongo> savedPlayer = playerMongoRepository.save(newPlayer);

        StepVerifier.create(savedPlayer)
                .expectNextMatches(player -> player.getId() != null)
                .verifyComplete();
    }
}