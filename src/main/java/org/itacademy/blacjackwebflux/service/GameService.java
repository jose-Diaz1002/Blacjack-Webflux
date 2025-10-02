package org.itacademy.blacjackwebflux.service;


import org.itacademy.blacjackwebflux.exeption.GameNotFoundException;
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

    public Mono<Game> createNewGame(String playerName) {

        String actualPlayerName = (playerName == null || playerName.trim().isEmpty()) ? "Player-" + UUID.randomUUID().toString().substring(0, 8) : playerName;

        return playerRepository.findByNameContainingIgnoreCase(actualPlayerName)
                .next()
                .switchIfEmpty(Mono.defer(() -> {
                    Player newPlayer = new Player(null, actualPlayerName, 0, 0, 0);
                    return playerRepository.save(newPlayer);
                }))
                .flatMap(player -> {

                    Game newGame = new Game(null, player.getId(), Game.GameStatus.IN_PROGRESS, LocalDateTime.now(), null);
                    return gameRepository.save(newGame);
                });
    }


    public Mono<Game> getGameDetails(Long gameId) {
        return gameRepository.findById(gameId)
                .switchIfEmpty(Mono.error(new GameNotFoundException(gameId)));
    }

    public Mono<Move> makePlay(Long gameId, Move.MoveType moveType, BigDecimal bet) {
        return gameRepository.findById(gameId)
                .switchIfEmpty(Mono.error(new RuntimeException("Partida no encontrada con ID: " + gameId)))
                .flatMap(game -> {
                    if (game.getStatus() == Game.GameStatus.FINISHED) {
                        return Mono.error(new GameNotFoundException(gameId));
                    }

                    Move newMove = new Move(null, gameId, moveType, bet, Move.MoveResult.DRAW, LocalDateTime.now());
                    return moveRepository.save(newMove)
                            .flatMap(savedMove -> {

                                if (moveType == Move.MoveType.STAND) {
                                    game.setStatus(Game.GameStatus.FINISHED);
                                    game.setEndTime(LocalDateTime.now());
                                    return gameRepository.save(game).thenReturn(savedMove);
                                }
                                return Mono.just(savedMove);
                            });
                });
    }

    public Mono<Void> deleteGame(Long gameId) {
        return gameRepository.findById(gameId)
                .switchIfEmpty(Mono.error(new GameNotFoundException(gameId)))
                .flatMap(gameRepository::delete);
    }
}



