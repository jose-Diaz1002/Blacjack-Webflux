package org.itacademy.blacjackwebflux.controllers;

import jakarta.validation.Valid;
import org.itacademy.blacjackwebflux.dto.UpdatePlayerNameRequest;
import org.itacademy.blacjackwebflux.model.mysql.Player;
import org.itacademy.blacjackwebflux.service.PlayerService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/ranking")
    public Flux<Player> getPlayerRanking() {
        return playerService.getPlayerRanking();
    }

    @PutMapping("/player/{playerId}")
    public Mono<Player> updatePlayerName(@PathVariable Long playerId, @Valid @RequestBody UpdatePlayerNameRequest request) {
        return playerService.updatePlayerName(playerId, request.getNewName());
    }
}
