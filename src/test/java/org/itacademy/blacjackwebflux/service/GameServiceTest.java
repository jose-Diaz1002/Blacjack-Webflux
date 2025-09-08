// src/test/java/org/ItAcademy/BlacjackWebflux/service/GameServiceTest.java
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
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

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
        // Inicializa un jugador y una partida de prueba para usar en los tests
        testPlayer = new Player(1L, "TestPlayer", 0, 0, 0);
        testGame = new Game(101L, 1L, Game.GameStatus.IN_PROGRESS, null, null);
    }

    @Test
    void testCreateNewGame_NewPlayer() {
        String playerName = "NewTestPlayer";
        // Simula el comportamiento del repositorio:
        // 1. Cuando se busca por nombre, no encuentra nada
        when(playerRepository.findByNameContainingIgnoreCase(playerName)).thenReturn(Mono.empty());
        // 2. Cuando se guarda un nuevo jugador, devuelve un Mono con el jugador guardado
        when(playerRepository.save(any(Player.class))).thenReturn(Mono.just(testPlayer));
        // 3. Cuando se guarda un nuevo juego, devuelve un Mono con el juego guardado
        when(gameRepository.save(any(Game.class))).thenReturn(Mono.just(testGame));

        Mono<Game> resultMono = gameService.createNewGame(playerName);

        // Verifica que el resultado es el esperado
        StepVerifier.create(resultMono)
                .expectNextMatches(game -> game.getId().equals(101L) && game.getPlayerId().equals(1L))
                .verifyComplete();
    }

    @Test
    void testMakePlay_SuccessfulMove() {
        Move testMove = new Move(1001L, 101L, Move.MoveType.HIT, new BigDecimal("10.00"), null, null);

        // Simula el comportamiento del repositorio:
        // 1. Cuando se busca una partida, devuelve el Mono con la partida de prueba
        when(gameRepository.findById(101L)).thenReturn(Mono.just(testGame));
        // 2. Cuando se guarda un movimiento, devuelve el Mono con el movimiento guardado
        when(moveRepository.save(any(Move.class))).thenReturn(Mono.just(testMove));

        Mono<Move> resultMono = gameService.makePlay(101L, Move.MoveType.HIT, new BigDecimal("10.00"));

        // Verifica que la jugada se guarda y el resultado es el esperado
        StepVerifier.create(resultMono)
                .expectNextMatches(move -> move.getId().equals(1001L) && move.getGameId().equals(101L))
                .verifyComplete();
    }

    @Test
    void testDeleteGame_GameExists() {
        // Simula el comportamiento del repositorio:
        // 1. Cuando se busca por ID, la partida existe
        when(gameRepository.findById(101L)).thenReturn(Mono.just(testGame));
        // 2. Cuando se llama a delete, no devuelve nada (Void)
        when(gameRepository.delete(any(Game.class))).thenReturn(Mono.empty());

        Mono<Void> resultMono = gameService.deleteGame(101L);

        // Verifica que el flujo se completa sin errores
        StepVerifier.create(resultMono)
                .verifyComplete();
    }
}
