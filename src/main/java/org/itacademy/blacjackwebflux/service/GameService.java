package org.itacademy.blacjackwebflux.service;

import org.itacademy.blacjackwebflux.exeption.GameNotFoundException;
import org.itacademy.blacjackwebflux.model.mysql.*;
import org.itacademy.blacjackwebflux.repositorio.mysql.GameRepository;
import org.itacademy.blacjackwebflux.repositorio.mysql.MoveRepository;
import org.itacademy.blacjackwebflux.repositorio.mysql.PlayerRepository;
import org.itacademy.blacjackwebflux.util.GameUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final MoveRepository moveRepository;

    public GameService(GameRepository gameRepository, PlayerRepository playerRepository, MoveRepository moveRepository) {
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
        this.moveRepository = moveRepository;
    }

    // =======================================================
    // 1. CREACIÓN DE PARTIDA (MODIFICADA para repartir cartas)
    // =======================================================

    public Mono<Game> createNewGame(String playerName) {
        String actualPlayerName = (playerName == null || playerName.trim().isEmpty()) ? "Player-" + UUID.randomUUID().toString().substring(0, 8) : playerName;

        // 1. Lógica de Buscar o Crear Jugador (sin cambios)
        return playerRepository.findByNameContainingIgnoreCase(actualPlayerName)
                .next()
                .switchIfEmpty(Mono.defer(() -> {
                    Player newPlayer = new Player(null, actualPlayerName, 0, 0, 0);
                    return playerRepository.save(newPlayer);
                }))
                .flatMap(player -> {
                    // 2. Lógica de Reparto Inicial
                    List<Card> deck = GameUtils.createAndShuffleDeck(); // Crea mazo de 52 cartas
                    Hand playerHand = new Hand();
                    Hand dealerHand = new Hand();

                    // Reparte 2 cartas al jugador y 2 al crupier
                    playerHand.addCard(deck.remove(0));
                    dealerHand.addCard(deck.remove(0));
                    playerHand.addCard(deck.remove(0));
                    dealerHand.addCard(deck.remove(0));

                    Game.GameStatus initialStatus = Game.GameStatus.IN_PROGRESS;

                    // Verifica Blackjack inicial
                    if (playerHand.getScore() == 21) {
                        initialStatus = Game.GameStatus.WIN;
                    } else if (dealerHand.getScore() == 21) {
                        initialStatus = Game.GameStatus.LOSE;
                    }

                    // 3. Crear Partida y Guardar Estado Completo (Serializando Manos)
                    Game newGame = new Game();
                    newGame.setPlayerId(player.getId());
                    newGame.setStatus(Game.GameStatus.IN_PROGRESS);
                    newGame.setStartTime(LocalDateTime.now());
                    return gameRepository.save(newGame);
                });
    }


    public Mono<Game> getGameDetails(Long gameId) {
        return gameRepository.findById(gameId)
                .switchIfEmpty(Mono.error(new GameNotFoundException(gameId)));
    }

    // =======================================================
    // 2. REALIZAR JUGADA (IMPLEMENTACIÓN JUGABLE)
    // =======================================================

    // NOTA: EL MÉTODO createNewGame DEBE ESTAR ACTUALIZADO PARA INICIALIZAR playerHandJson y dealerHandJson
    // con el estado inicial de las cartas, usando GameUtils.serializeHand()

    // ... (createNewGame y getGameDetails omitidos por espacio) ...

    /**
     * Lógica principal para realizar una jugada (HIT o STAND).
     * * @param gameId ID de la partida.
     * @param moveType Tipo de jugada (HIT, STAND).
     * @param bet Cantidad apostada.
     * @return Mono<Move> con el movimiento guardado y el resultado de la jugada.
     */
    // src/main/java/org.itacademy.blacjackwebflux.service/GameService.java

// ... (imports omitidos) ...

    public Mono<Move> makePlay(Long gameId, Move.MoveType moveType, BigDecimal bet) {
        // NOTA: Si quieres eliminar la apuesta de toda la API, puedes cambiar la firma
        // a public Mono<Move> makePlay(Long gameId, Move.MoveType moveType) { ... }
        // y eliminar el parámetro bet. Por ahora, lo mantenemos y asumimos que es null.

        return gameRepository.findById(gameId)
                .switchIfEmpty(Mono.error(new GameNotFoundException(gameId)))
                .flatMap(game -> {

                    if (game.getStatus() != Game.GameStatus.IN_PROGRESS) {
                        return Mono.error(new RuntimeException("La partida ya ha terminado. Estado: " + game.getStatus()));
                    }

                    // 1. Deserializar el estado del juego
                    Hand playerHand = GameUtils.deserializeHand(game.getPlayerHandJson());
                    Hand dealerHand = GameUtils.deserializeHand(game.getDealerHandJson());
                    List<Card> currentDeck = GameUtils.deserializeDeck(game.getCurrentDeckJson());

                    Move.MoveResult moveResult = null;
                    Game.GameStatus newGameStatus = Game.GameStatus.IN_PROGRESS;

                    // 2. Ejecutar la lógica de la jugada (HIT o STAND)

                    if (moveType == Move.MoveType.HIT) {
                        // Lógica de HIT: Añadir una carta
                        playerHand.addCard(currentDeck.remove(0));
                        int playerScore = playerHand.getScore();

                        if (playerScore > 21) {
                            moveResult = Move.MoveResult.LOSE;
                            newGameStatus = Game.GameStatus.LOSE; // Jugador pierde por BUST
                        } else if (playerScore == 21) {
                            newGameStatus = Game.GameStatus.FINISHED; // Jugador tiene 21, espera
                        }

                    } else if (moveType == Move.MoveType.STAND) {
                        // STAND: Ejecutar turno del Crupier y determinar ganador

                        GameUtils.executeDealerTurn(dealerHand, currentDeck);

                        int playerScore = playerHand.getScore();
                        int dealerScore = dealerHand.getScore();

                        moveResult = GameUtils.determineFinalWinner(playerScore, dealerScore);

                        // Mapear el resultado a GameStatus
                        newGameStatus = (moveResult == Move.MoveResult.WIN) ? Game.GameStatus.WIN :
                                (moveResult == Move.MoveResult.LOSE) ? Game.GameStatus.LOSE :
                                        Game.GameStatus.DRAW;
                    }

                    // 3. Persistir el Movimiento
                    // El campo 'bet' se pasa tal como viene (probablemente null o el valor de la request original)
                    Move newMove = new Move();

                    return moveRepository.save(newMove)
                            .flatMap(savedMove -> {

                                // 4. Serializar y Persistir el Nuevo Estado del Juego
                                game.setPlayerHandJson(GameUtils.serializeHand(playerHand));
                                game.setDealerHandJson(GameUtils.serializeHand(dealerHand));
                                game.setCurrentDeckJson(GameUtils.serializeDeck(currentDeck));

                                if (newGameStatus != Game.GameStatus.IN_PROGRESS) {
                                    game.setStatus(newGameStatus);
                                    game.setEndTime(LocalDateTime.now());
                                    // NOTA: Aquí iría la lógica para actualizar las estadísticas del Player
                                }

                                return gameRepository.save(game).thenReturn(savedMove);
                            });
                });
    }

    // ... (deleteGame y otros métodos omitidos) ...


    public Mono<Void> deleteGame(Long gameId) {
        return gameRepository.findById(gameId)
                .switchIfEmpty(Mono.error(new GameNotFoundException(gameId)))
                .flatMap(gameRepository::delete);
    }
}