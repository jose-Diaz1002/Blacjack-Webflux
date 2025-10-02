package org.itacademy.blacjackwebflux.service;

import org.itacademy.blacjackwebflux.model.mongo.PlayerMongo;
import org.itacademy.blacjackwebflux.repositorio.mongo.PlayerMongoRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class PlayerMongoService {
    PlayerMongoRepository playerMongoRepository;

    public PlayerMongoService(PlayerMongoRepository playerMongoRepository) {
        this.playerMongoRepository = playerMongoRepository;
    }
    public Flux<PlayerMongo> getPlayerScoreRankingFromMongo() {
        return playerMongoRepository.findAll(Sort.by(Sort.Direction.DESC, "score"));
    }


}
