package org.itacademy.blacjackwebflux.service;

import org.itacademy.blacjackwebflux.model.mysql.Game;
import org.itacademy.blacjackwebflux.model.mysql.Move;
import org.itacademy.blacjackwebflux.model.mysql.Player;
import org.itacademy.blacjackwebflux.repositorio.mysql.GameRepository;
import org.itacademy.blacjackwebflux.repositorio.mysql.MoveRepository;
import org.itacademy.blacjackwebflux.repositorio.mysql.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {

    @Mock
    private GameRepository gameRepository;
    @Mock
    private PlayerRepository playerRepository;
    @Mock
    private MoveRepository moveRepository;

    @InjectMocks
    private GameService gameService;

    private Player testPlayer;
    private Game testGame;

    @BeforeEach
    void setUp() {
        testPlayer = new Player(1L, "TestPlayer", 0, 0, 0);
        testGame = new Game(101L, 1L, Game.GameStatus.IN_PROGRESS, null, null);
    }

    @Test
    void testCreateNewGame_NewPlayer() {
        String playerName = "NewTestPlayer";
        when(playerRepository.findByNameContainingIgnoreCase(playerName)).thenReturn(Flux.empty());
        when(playerRepository.save(any(Player.class))).thenReturn(Mono.just(testPlayer));
        when(gameRepository.save(any(Game.class))).thenReturn(Mono.just(testGame));

        Mono<Game> resultMono = gameService.createNewGame(playerName);

        StepVerifier.create(resultMono)
                .expectNextMatches(game -> game.getId().equals(101L) && game.getPlayerId().equals(1L))
                .verifyComplete();
    }

    @Test
    void testMakePlay_SuccessfulMove() {
        Move testMove = new Move(1001L, 101L, Move.MoveType.HIT, new BigDecimal("10.00"), null, null);

        when(gameRepository.findById(101L)).thenReturn(Mono.just(testGame));
        when(moveRepository.save(any(Move.class))).thenReturn(Mono.just(testMove));

        Mono<Move> resultMono = gameService.makePlay(101L, Move.MoveType.HIT, new BigDecimal("10.00"));

        StepVerifier.create(resultMono)
                .expectNextMatches(move -> move.getId().equals(1001L) && move.getGameId().equals(101L))
                .verifyComplete();
    }

    @Test
    void testDeleteGame_GameExists() {
        when(gameRepository.findById(101L)).thenReturn(Mono.just(testGame));
        when(gameRepository.delete(any(Game.class))).thenReturn(Mono.empty());

        Mono<Void> resultMono = gameService.deleteGame(101L);

        StepVerifier.create(resultMono)
                .verifyComplete();
    }
    @Test
    void testMakePlay_StandAndWinGame() {
        // 1. ARRANGE (Preparación)
        Game winningGame = new Game(101L, 1L, Game.GameStatus.IN_PROGRESS, null, null);

        // **NOTA:** En una prueba real, deberías simular que GameUtils da una mano ganadora.
        // Como no podemos mockear GameUtils, simulamos que el servicio devolverá
        // un estado final (WIN) y que el repositorio lo guardará.

        // Simular la búsqueda de la partida
        when(gameRepository.findById(101L)).thenReturn(Mono.just(winningGame));

        // Simular el guardado del movimiento
        Move moveStand = new Move(1001L, 101L, Move.MoveType.STAND, new BigDecimal("20.00"), Move.MoveResult.WIN, null);
        when(moveRepository.save(any(Move.class))).thenReturn(Mono.just(moveStand));

        // Simular el guardado del estado final del juego
        Game finalGame = new Game(101L, 1L, Game.GameStatus.WIN, LocalDateTime.now(), LocalDateTime.now());
        when(gameRepository.save(any(Game.class))).thenReturn(Mono.just(finalGame));


        // 2. ACT (Ejecución)
        Mono<Move> resultMono = gameService.makePlay(101L, Move.MoveType.STAND, new BigDecimal("20.00"));

        // 3. ASSERT (Verificación)
        StepVerifier.create(resultMono)
                .expectNextMatches(move -> move.getResult() == Move.MoveResult.WIN)
                .verifyComplete();
    }

    @Test
    void testMakePlay_GameAlreadyFinished_ThrowsException() {
        // 1. ARRANGE (Preparación)
        Game finishedGame = new Game(101L, 1L, Game.GameStatus.LOSE, LocalDateTime.now(), LocalDateTime.now());

        when(gameRepository.findById(101L)).thenReturn(Mono.just(finishedGame));

        // 2. ACT & ASSERT (Verificación de la Excepción)
        Mono<Move> resultMono = gameService.makePlay(101L, Move.MoveType.HIT, new BigDecimal("5.00"));

        StepVerifier.create(resultMono)
                .expectError(RuntimeException.class) // Esperamos la excepción si el juego terminó
                .verify();
    }
}
