package org.itacademy.blacjackwebflux.repository.mysql;

import org.itacademy.blacjackwebflux.model.mysql.Game;
import org.itacademy.blacjackwebflux.repositorio.mysql.GameRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

@DataR2dbcTest
public class GameRepositoryTest {

    @Autowired
    private GameRepository gameRepository;

    @Test
    void testSaveAndFindGame() {
        Game newGame = new Game();
        newGame.setPlayerId(1L);
        newGame.setStatus(Game.GameStatus.IN_PROGRESS);
        newGame.setStartTime(LocalDateTime.now());

        Mono<Game> savedGame = gameRepository.save(newGame);

        StepVerifier.create(savedGame)
                .expectNextMatches(game -> game.getId() != null)
                .verifyComplete();
    }

    @Test
    void testFindAllGamesByPlayerId() {
        Mono<Long> gameCount = gameRepository.findByPlayerId(1L).count();

        StepVerifier.create(gameCount)
                .expectNext(2L)
                .verifyComplete();
    }
}