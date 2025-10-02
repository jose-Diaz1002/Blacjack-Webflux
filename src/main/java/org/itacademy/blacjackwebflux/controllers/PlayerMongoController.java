package org.itacademy.blacjackwebflux.controllers;

import org.itacademy.blacjackwebflux.model.mongo.PlayerMongo;
import org.itacademy.blacjackwebflux.service.PlayerMongoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class PlayerMongoController {

    PlayerMongoService playerMongoService;
    public PlayerMongoController(PlayerMongoService playerMongoService) {
        this.playerMongoService = playerMongoService;
    }

    @GetMapping("/ranking/score-mongo")
    public Flux<PlayerMongo> getPlayerScoreRankingMongo() {
        return playerMongoService.getPlayerScoreRankingFromMongo();
    }

}
