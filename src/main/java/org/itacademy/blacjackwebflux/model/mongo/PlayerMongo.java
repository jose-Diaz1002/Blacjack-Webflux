package org.itacademy.blacjackwebflux.model.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "players")
public class PlayerMongo {
    @Id
    private String id;

    private String name;
    private int score;

    private LocalDateTime createdAt;
}
