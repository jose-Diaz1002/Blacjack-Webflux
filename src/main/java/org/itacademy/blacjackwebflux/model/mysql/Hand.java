package org.itacademy.blacjackwebflux.model.mysql;

import java.util.ArrayList;
import java.util.List;

public class Hand {
    private final List<Card> cards = new ArrayList<>();

    public void addCard(Card card) {
        this.cards.add(card);
    }

    public List<Card> getCards() {
        return cards;
    }

    /**
     * Calcula la puntuación con la lógica flexible del As (1 u 11).
     */
    public int getScore() {
        int score = 0;
        int aceCount = 0;

        for (Card card : cards) {
            if (card.getRank() == Card.Rank.ACE) {
                aceCount++;
                score += 11; // Sumamos 11 inicialmente
            } else {
                score += card.getValue();
            }
        }

        // Si la puntuación excede 21, cambia los Ases de 11 a 1 hasta que sea seguro.
        while (score > 21 && aceCount > 0) {
            score -= 10; // Cambia el valor de un As de 11 a 1
            aceCount--;
        }
        return score;
    }
}
