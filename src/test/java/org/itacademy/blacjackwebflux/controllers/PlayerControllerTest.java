package org.itacademy.blacjackwebflux.controllers;

import org.itacademy.blacjackwebflux.dto.UpdatePlayerNameRequest;
import org.itacademy.blacjackwebflux.model.mysql.Player;
import org.itacademy.blacjackwebflux.service.PlayerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@WebFluxTest(PlayerController.class)
class PlayerControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private PlayerService playerService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public PlayerService playerService() {
            return Mockito.mock(PlayerService.class);
        }
    }

    @Test
    void testUpdatePlayerName_Success() {
        // Arrange
        Player mockPlayer = new Player(1L, "NuevoNombre", 10, 5, 100);

        when(playerService.updatePlayerName(eq(1L), eq("NuevoNombre")))
                .thenReturn(Mono.just(mockPlayer));

        // Act & Assert
        webTestClient.put()
                .uri("/player/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UpdatePlayerNameRequest("NuevoNombre"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.name").isEqualTo("NuevoNombre")
                .jsonPath("$.gamesPlayed").isEqualTo(10)
                .jsonPath("$.gamesWon").isEqualTo(5)
                .jsonPath("$.totalPoints").isEqualTo(100);
    }

}
