package org.itacademy.blacjackwebflux.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "hands")
public class Hand {

    @Id
    private String id;  // MongoDB document ID

    private String gameId;  // ID of the game this hand belongs to

    private List<String> cards;  // List of cards, e.g. "A♠", "10♥"

    private boolean dealer;  // True if this hand belongs to the dealer
}
