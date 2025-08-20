package org.itacademy.blacjackwebflux.service;


import org.itacademy.blacjackwebflux.model.mysql.Game;
import org.itacademy.blacjackwebflux.model.mysql.Move;
import org.itacademy.blacjackwebflux.model.mysql.Player;
import org.itacademy.blacjackwebflux.repositorio.mysql.GameRepository;
import org.itacademy.blacjackwebflux.repositorio.mysql.MoveRepository;
import org.itacademy.blacjackwebflux.repositorio.mysql.PlayerRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    /**
     * Crea una nueva partida de Blackjack.
     * Si el nombre del jugador no existe, crea un nuevo jugador.
     *
     * @param playerName El nombre del jugador para la nueva partida.
     * @return Mono<Game> que emite la partida creada.
     */
    public Mono<Game> createNewGame(String playerName) {
        // 1. Encontrar o crear el jugador
        // Por ahora, asumimos que si el playerName no existe, lo creamos.
        // En un escenario real, deberías buscar el jugador y crearlo solo si no existe.
        // Para simplificar, si no se proporciona un nombre, generamos uno.
        String actualPlayerName = (playerName == null || playerName.trim().isEmpty()) ? "Player-" + UUID.randomUUID().toString().substring(0, 8) : playerName;

        return playerRepository.findByNameContainingIgnoreCase(actualPlayerName)
                .next() // Toma el primer jugador si hay varios con el mismo nombre (poco probable con UUID)
                .switchIfEmpty(Mono.defer(() -> { // Si no encuentra el jugador, crea uno nuevo
                    Player newPlayer = new Player(null, actualPlayerName, 0, 0, 0);
                    return playerRepository.save(newPlayer);
                }))
                .flatMap(player -> {
                    // 2. Crear la nueva partida asociada a ese jugador
                    Game newGame = new Game(null, player.getId(), Game.GameStatus.IN_PROGRESS, LocalDateTime.now(), null);
                    return gameRepository.save(newGame);
                });
    }

    /**
     * Obtiene los detalles de una partida específica.
     *
     * @param gameId El ID de la partida.
     * @return Mono<Game> que emite los detalles de la partida.
     */
    public Mono<Game> getGameDetails(Long gameId) {
        return gameRepository.findById(gameId)
                .switchIfEmpty(Mono.error(new RuntimeException("Partida no encontrada con ID: " + gameId))); // Manejo de partida no encontrada
    }

    /**
     * Realiza una jugada en una partida existente.
     * Esta es una implementación simplificada. La lógica real de Blackjack (repartir cartas,
     * calcular puntos, determinar ganador) iría aquí.
     *
     * @param gameId El ID de la partida.
     * @param moveType El tipo de jugada (HIT, STAND, DOUBLE, SPLIT).
     * @param bet La cantidad apostada (si aplica, p.ej. para DOUBLE).
     * @return Mono<Move> que emite el resultado de la jugada.
     */
    public Mono<Move> makePlay(Long gameId, Move.MoveType moveType, BigDecimal bet) {
        return gameRepository.findById(gameId)
                .switchIfEmpty(Mono.error(new RuntimeException("Partida no encontrada con ID: " + gameId)))
                .flatMap(game -> {
                    if (game.getStatus() == Game.GameStatus.FINISHED) {
                        return Mono.error(new RuntimeException("La partida ya ha terminado."));
                    }

                    // --- Aquí iría la lógica compleja del juego de Blackjack ---
                    // 1. Lógica para repartir cartas al jugador y a la casa.
                    // 2. Lógica para aplicar la jugada (HIT, STAND, etc.).
                    // 3. Cálculo de puntuaciones.
                    // 4. Determinación del resultado de la mano (ganar, perder, empatar).
                    // 5. Actualización del estado de la partida (si ha terminado).
                    // 6. Actualización de los puntos del jugador.
                    // 7. Guardar las cartas de la mano actual (podría ser en MongoDB o una estructura más compleja).

                    // Por ahora, solo crearemos un registro de la jugada con un resultado ficticio.
                    Move newMove = new Move(null, gameId, moveType, bet, Move.MoveResult.DRAW, LocalDateTime.now()); // Resultado ficticio
                    return moveRepository.save(newMove)
                            .flatMap(savedMove -> {
                                // Si la jugada implica el fin de la partida (ej. STAND, o bust), actualiza el estado de la partida
                                // Esto es solo un placeholder, la lógica real sería más compleja.
                                if (moveType == Move.MoveType.STAND) {
                                    game.setStatus(Game.GameStatus.FINISHED);
                                    game.setEndTime(LocalDateTime.now());
                                    return gameRepository.save(game).thenReturn(savedMove); // Guarda la partida y devuelve la jugada
                                }
                                return Mono.just(savedMove);
                            });
                });
    }

    /**
     * Elimina una partida existente.
     *
     * @param gameId El ID de la partida a eliminar.
     * @return Mono<Void> que indica la finalización de la operación.
     */
    public Mono<Void> deleteGame(Long gameId) {
        return gameRepository.findById(gameId)
                .switchIfEmpty(Mono.error(new RuntimeException("Partida no encontrada con ID: " + gameId)))
                .flatMap(gameRepository::delete); // Elimina la partida si existe
    }
}



