package org.itacademy.blacjackwebflux.model.mysql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("player")
public class Player {
    @Id
    private Long id;

    private String name;

    @Column("games_played")
    private int gamesPlayed;

    @Column("games_won")
    private int gamesWon;

    @Column("total_points")
    private int totalPoints;
}
