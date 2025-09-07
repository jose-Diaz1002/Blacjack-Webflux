package org.itacademy.blacjackwebflux.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Blackjack Reactive API",
                version = "1.0",
                description = "API para un juego de Blackjack desarrollado con Spring WebFlux y dos bases de datos.",
                contact = @Contact(
                        name = "Jose Diaz",
                        email = "https://github.com/jose-Diaz1002"
                )
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Servidor Local")
        }
)

public class OpenApiConfig {
}
