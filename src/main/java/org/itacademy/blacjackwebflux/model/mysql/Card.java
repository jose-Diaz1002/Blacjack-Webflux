package org.itacademy.blacjackwebflux.model.mysql;

import lombok.Value; // Importar @Value
import java.util.List;

// Usamos @Value para crear una clase inmutable sin setters.
@Value
public class Card {


    private Suit suit; // Se omite 'final' porque @Value lo hace implícitamente
    private Rank rank;

    // ... La lógica de getValue es correcta

    /** Retorna el valor base de la carta (10 para caras, 11/1 para As). */
    public int getValue() {
        if (rank.ordinal() <= 7) { // Rango 2 a 9 (índices 0 a 7 en el enum)
            return rank.ordinal() + 2;
        } else if (rank.ordinal() <= 11) { // Rango 10, J, Q, K
            return 10;
        } else { // Rango ACE
            return 11;
        }
    }

    // Los Enums son correctos
    public enum Suit {
        CLUBS, DIAMONDS, HEARTS, SPADES
    }
    public enum Rank {
        TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE
    }

}