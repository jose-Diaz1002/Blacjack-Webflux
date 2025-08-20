package org.itacademy.blacjackwebflux.model.mysql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("move")
public class Move {
    @Id
    private Long id;

    @Column("game_id")
    private Long gameId;

    @Column("move_type")
    private MoveType moveType;

    private BigDecimal bet;

    private MoveResult result;

    private LocalDateTime timestamp;

    public enum MoveType {
        HIT,
        STAND,
        DOUBLE,
        SPLIT
    }

    public enum MoveResult {
        WIN,
        LOSE,
        DRAW
    }
}
