package org.itacademy.blacjackwebflux.controllers;


import org.itacademy.blacjackwebflux.dto.UpdatePlayerNameRequest;
import org.itacademy.blacjackwebflux.model.mysql.Player;
import org.itacademy.blacjackwebflux.service.PlayerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebFluxTest(PlayerController.class)
public class PlayerControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private PlayerService playerService;

    @Test
    void testUpdatePlayerName_Success() {
        // Objeto de jugador de prueba
        Player mockPlayer = new Player(1L, "NuevoNombre", 0, 0, 0);

        // Simulamos la respuesta del servicio cuando se llama a 'updatePlayerName'
        when(playerService.updatePlayerName(anyLong(), anyString()))
                .thenReturn(Mono.just(mockPlayer));

        // Cuerpo de la solicitud para la prueba
        UpdatePlayerNameRequest request = new UpdatePlayerNameRequest("NuevoNombre");

        // Ejecutamos la prueba con WebTestClient
        webTestClient.put()
                .uri("/player/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange() // Ejecuta la solicitud
                .expectStatus().isOk() // Esperamos un código de estado 200 OK
                .expectBody(Player.class) // Esperamos que la respuesta sea un objeto Player
                .isEqualTo(mockPlayer); // Comprobamos que el objeto sea el que simulamos
    }
}