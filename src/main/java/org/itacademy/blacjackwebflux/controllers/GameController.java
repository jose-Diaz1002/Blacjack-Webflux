package org.itacademy.blacjackwebflux.controllers;

import org.itacademy.blacjackwebflux.dto.CreateGameRequest;
import org.itacademy.blacjackwebflux.dto.MakePlayRequest;
import org.itacademy.blacjackwebflux.model.mysql.Game;
import org.itacademy.blacjackwebflux.model.mysql.Move;
import org.itacademy.blacjackwebflux.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * POST /game/new
     * Descripción: Crear una nueva partida de Blackjack.
     * Cuerpo de la solicitud: { "playerName": "Nuevo nombre del jugador" }
     * Respuesta exitosa: Código 201 Created con información sobre la partida creada.
     */
    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED) // Código 201 para creación exitosa
    public Mono<Game> createNewGame(@RequestBody(required = false) CreateGameRequest request) {
        String playerName = (request != null) ? request.getPlayerName() : null;
        return gameService.createNewGame(playerName);
    }

    /**
     * GET /game/{id}
     * Descripción: Obtiene los detalles de una partida específica de Blackjack.
     * Parámetros de entrada: Identificador único de la partida (id)
     * Respuesta exitosa: Código 200 OK con información detallada sobre la partida.
     */
    @GetMapping("/{id}")
    public Mono<Game> getGameDetails(@PathVariable Long id) {
        return gameService.getGameDetails(id);
    }

    /**
     * POST /game/{id}/play
     * Descripción: Realiza una jugada en una partida de Blackjack existente.
     * Parámetros de entrada: Identificador único de la partida (id), datos de la jugada (por ejemplo, tipos de jugada y cantidad apostada).
     * Cuerpo de la solicitud: { "moveType": "HIT", "bet": 10.00 }
     * Respuesta exitosa: Código 200 OK con el resultado de la jugada y el estado actual de la partida.
     */
    @PostMapping("/{id}/play")
    public Mono<Move> makePlay(@PathVariable Long id, @RequestBody MakePlayRequest request) {
        // Podrías añadir validaciones aquí para 'request.moveType' y 'request.bet'
        return gameService.makePlay(id, request.getMoveType(), request.getBet());
    }

    /**
     * DELETE /game/{id}/delete
     * Descripción: Elimina una partida de Blackjack existente.
     * Parámetros de entrada: Identificador único de la partida (id).
     * Respuesta exitosa: Código 204 No Content si la partida se elimina correctamente.
     */
    @DeleteMapping("/{id}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Código 204 para eliminación exitosa sin contenido de respuesta
    public Mono<Void> deleteGame(@PathVariable Long id) {
        return gameService.deleteGame(id);
    }
}
