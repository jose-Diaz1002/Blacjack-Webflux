package org.itacademy.blacjackwebflux.model.mysql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("game")
public class Game {
    @Id
    private Long id;

    @Column("player_id")
    private Long playerId;

    private GameStatus status;

    // --- CAMPOS NUEVOS PARA LA LÓGICA JUGABLE ---
    @Column("player_hand_json")
    private String playerHandJson;

    @Column("dealer_hand_json")
    private String dealerHandJson;

    @Column("current_deck_json") // Para guardar el estado del mazo restante
    private String currentDeckJson;


    @Column("start_time")
    private LocalDateTime startTime;

    @Column("end_time")
    private LocalDateTime endTime;

    public enum GameStatus {
        IN_PROGRESS,
        WIN,
        LOSE,
        DRAW,
        FINISHED
    }
}
