# Blackjack Reactive API with Spring Boot

## Description
This project is a reactive REST API for a Blackjack game, built with **Spring Boot 3** and **Spring WebFlux**. It demonstrates reactive programming concepts, multi-database integration, and modern API best practices. The API allows you to create and manage Blackjack games, track player rankings, and interact with game sessions in real time.

## Features
* Reactive API with **Spring WebFlux**
* Connection to two databases (**MySQL** and **MongoDB**)
* Global exception handling
* API documentation with **Swagger/OpenAPI**
* Unit and integration testing

## Requirements
To run this project, you need:
* **Java 17** or higher
* **Maven 3.x**
* **Docker** and **Docker Compose**

---

## Setup
Follow these steps to run the project locally:

1.  Clone the repository:
    ```bash
    https://github.com/jose-Diaz1002/Blacjack-Webflux.git
    ```

2.  Start the databases with Docker:
    ```bash
    docker-compose up -d
    ```

3.  Configure your database credentials in `src/main/resources/application.properties`.

4.  Run the application:
    ```bash
    ./mvnw spring-boot:run  
    ```
5.  API Documentation (Swagger)
    ```bash
    http://localhost:8080/swagger-ui.html
    ```

---

## API Endpoints
All endpoints are available at the base URL: `http://localhost:8080`

| Method | Endpoint | Description | Example `cURL` Command |
| :--- | :--- | :--- | :--- |
| `POST` | `/game/new` | Creates a new game session. | `curl -X POST http://localhost:8080/game/new -H "Content-Type: application/json" -d '{"playerName": "YourPlayerName"}'` |
| `GET` | `/ranking` | Retrieves the player ranking. | `curl http://localhost:8080/ranking` |
| `PUT` | `/player/{playerId}` | Updates a player's name. | `curl -X PUT http://localhost:8080/player/123 -H "Content-Type: application/json" -d '{"newName": "Alice"}'` |
| `GET` | `/game/{id}` | Retrieves the details of a specific game. | `curl http://localhost:8080/game/101` |
| `POST`| `/game/{id}/play` | Performs a move in a game session. | `curl -X POST http://localhost:8080/game/104/play -H "Content-Type: application/json" -d '{"moveType": "HIT"}'` |
| `DELETE`| `/game/{id}/delete`| Deletes a game session. | `curl -X DELETE http://localhost:8080/game/101/delete` |