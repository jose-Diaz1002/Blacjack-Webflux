package org.itacademy.blacjackwebflux.controllers;

import org.itacademy.blacjackwebflux.dto.UpdatePlayerNameRequest;
import org.itacademy.blacjackwebflux.model.mysql.Player;
import org.itacademy.blacjackwebflux.service.PlayerService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

    @RestController
    public class PlayerController { // No hay un RequestMapping base para este, ya que /ranking es raíz

        private final PlayerService playerService;

        public PlayerController(PlayerService playerService) {
            this.playerService = playerService;
        }

        /**
         * GET /ranking
         * Descripción: Obtiene el ranking de los jugadores basado en su rendimiento en las partidas de Blackjack.
         * Parámetros de entrada: Ninguna
         * Respuesta exitosa: Código 200 OK con la lista de jugadores ordenada por su posición en el ranking y su puntaje.
         */
        @GetMapping("/ranking")
        public Flux<Player> getPlayerRanking() {
            return playerService.getPlayerRanking();
        }

        /**
         * PUT /player/{playerId}
         * Descripción: Cambia el nombre de un jugador en una partida de Blackjack existente.
         * Cuerpo de la solicitud: { "newName": "Nuevo Nombre Jugador" }
         * Parámetros de entrada: identificador único del jugador (playerId).
         * Respuesta exitosa: Código 200 OK con información actualizada del jugador.
         */
        @PutMapping("/player/{playerId}")
        public Mono<Player> updatePlayerName(@PathVariable Long playerId, @RequestBody UpdatePlayerNameRequest request) {
            // Puedes añadir validación para 'request.newName' (no nulo, no vacío)
            return playerService.updatePlayerName(playerId, request.getNewName());
        }
    }
