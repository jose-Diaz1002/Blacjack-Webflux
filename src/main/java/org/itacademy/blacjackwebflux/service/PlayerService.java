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

    /**
     * Obtiene el ranking de los jugadores.
     * Nota: En tu schema.sql tienes una vista `player_ranking`. Para usarla directamente
     * necesitarías un repositorio y entidad específicos para la vista, o ejecutar una consulta nativa.
     * Por ahora, ordenamos por `gamesWon` y `totalPoints` directamente desde la tabla `player`.
     *
     * @return Flux<Player> que emite la lista de jugadores ordenada para el ranking.
     */
    public Flux<Player> getPlayerRanking() {
        // Ordena por gamesWon (descendente) y totalPoints (descendente) para el ranking
        // Si quisieras usar la vista player_ranking, la forma más sencilla en R2DBC
        // sería una consulta @Query en el repositorio o una consulta nativa.
        return playerRepository.findAll(Sort.by(Sort.Direction.DESC, "gamesWon", "totalPoints"));
    }

    /**
     * Cambia el nombre de un jugador existente.
     *
     * @param playerId El ID del jugador.
     * @param newName El nuevo nombre del jugador.
     * @return Mono<Player> que emite el jugador actualizado.
     */
    public Mono<Player> updatePlayerName(Long playerId, String newName) {
        return playerRepository.findById(playerId)
                .switchIfEmpty(Mono.error(new RuntimeException("Jugador no encontrado con ID: " + playerId)))
                .flatMap(player -> {
                    player.setName(newName);
                    return playerRepository.save(player);
                });
    }
}
