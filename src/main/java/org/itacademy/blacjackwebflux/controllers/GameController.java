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


    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED) // Código 201 para creación exitosa
    public Mono<Game> createNewGame(@RequestBody(required = false) CreateGameRequest request) {
        String playerName = (request != null) ? request.getPlayerName() : null;
        return gameService.createNewGame(playerName);
    }


    @GetMapping("/{id}")
    public Mono<Game> getGameDetails(@PathVariable Long id) {
        return gameService.getGameDetails(id);
    }


    @PostMapping("/{id}/play")
    public Mono<Move> makePlay(@PathVariable Long id, @RequestBody MakePlayRequest request) {
        // Podrías añadir validaciones aquí para 'request.moveType' y 'request.bet'
        return gameService.makePlay(id, request.getMoveType(), request.getBet());
    }


    @DeleteMapping("/{id}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteGame(@PathVariable Long id) {
        return gameService.deleteGame(id);
    }
}
