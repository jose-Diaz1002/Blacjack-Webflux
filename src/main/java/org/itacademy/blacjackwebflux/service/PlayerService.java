package org.itacademy.blacjackwebflux.service;

import org.itacademy.blacjackwebflux.model.mysql.Player;
import org.itacademy.blacjackwebflux.repositorio.mysql.PlayerRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }


    public Flux<Player> getPlayerRanking() {

        return playerRepository.findAll(Sort.by(Sort.Direction.DESC, "gamesWon", "totalPoints"));
    }

    public Mono<Player> updatePlayerName(Long playerId, String newName) {
        return playerRepository.findById(playerId)
                .switchIfEmpty(Mono.error(new RuntimeException("Jugador no encontrado con ID: " + playerId)))
                .flatMap(player -> {
                    player.setName(newName);
                    return playerRepository.save(player);
                });
    }


}
