package org.itacademy.blacjackwebflux.util;


import org.itacademy.blacjackwebflux.model.mysql.Card;
import org.itacademy.blacjackwebflux.model.mysql.Hand;
import org.itacademy.blacjackwebflux.model.mysql.Move;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utilitarios del juego de Blackjack para mazo, barajar y serialización.
 */
public class GameUtils {

    // --- Funciones del Mazo ---

    public static List<Card> createAndShuffleDeck() {
        List<Card> deck = new ArrayList<>();
        // Crea las 52 cartas del mazo
        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Rank rank : Card.Rank.values()) {
                deck.add(new Card(suit, rank));
            }
        }
        Collections.shuffle(deck); // Baraja el mazo
        return deck;
    }

    // --- LÓGICA DE SERIALIZACIÓN ---

    // **NOTA:** Asumimos que el formato de serialización es "RANK_SUIT|RANK_SUIT|..."

    /** Serializa la mano a un String para guardarla en la DB. (Tu función original) */
    public static String serializeHand(Hand hand) {
        if (hand == null) return "";
        return hand.getCards().stream()
                .map(c -> c.getRank().toString() + "_" + c.getSuit().toString())
                .collect(Collectors.joining("|"));
    }

    /** Deserializa la cadena de la DB de vuelta a un objeto Hand. (Tu función original) */
    public static Hand deserializeHand(String handJson) {
        Hand hand = new Hand();
        if (handJson == null || handJson.isEmpty()) return hand;

        Arrays.stream(handJson.split("\\|"))
                .forEach(cardStr -> {
                    String[] parts = cardStr.split("_");
                    if (parts.length == 2) {
                        try {
                            Card.Rank rank = Card.Rank.valueOf(parts[0]);
                            Card.Suit suit = Card.Suit.valueOf(parts[1]);
                            hand.addCard(new Card(suit, rank));
                        } catch (IllegalArgumentException e) {
                            // Ignorar carta malformada
                        }
                    }
                });
        return hand;
    }

    // --- NUEVA LÓGICA REQUERIDA ---

    /**
     * Serializa el mazo restante (List<Card>) a un String para guardarlo en la DB.
     */
    public static String serializeDeck(List<Card> deck) {
        if (deck == null) return "";
        return deck.stream()
                .map(c -> c.getRank().toString() + "_" + c.getSuit().toString())
                .collect(Collectors.joining("|"));
    }

    /**
     * Deserializa la cadena del mazo guardada en la DB de vuelta a un List<Card>.
     */
    public static List<Card> deserializeDeck(String deckJson) {
        if (deckJson == null || deckJson.isEmpty()) {
            return new ArrayList<>();
        }

        return Arrays.stream(deckJson.split("\\|"))
                .map(cardStr -> {
                    String[] parts = cardStr.split("_");
                    if (parts.length == 2) {
                        try {
                            Card.Rank rank = Card.Rank.valueOf(parts[0]);
                            Card.Suit suit = Card.Suit.valueOf(parts[1]);
                            return new Card(suit, rank);
                        } catch (IllegalArgumentException e) {
                            // Error: el elemento no es una carta válida (se omite)
                            return null;
                        }
                    }
                    return null;
                })
                .filter(card -> card != null) // Solo devuelve cartas válidas
                .collect(Collectors.toList());
    }

    // --- LÓGICA DE JUEGO AUXILIAR (FALTA IMPLEMENTAR) ---

    /**
     * Ejecuta el turno del crupier hasta que se plante (score >= 17) o se pase (score > 21).
     */
    public static void executeDealerTurn(Hand dealerHand, List<Card> currentDeck) {
        // Lógica: Pedir carta si el puntaje es 16 o menos.
        while (dealerHand.getScore() < 17 && !currentDeck.isEmpty()) {
            dealerHand.addCard(currentDeck.remove(0));
        }
    }

    /**
     * Determina el ganador final al hacer STAND.
     */
    public static Move.MoveResult determineFinalWinner(int playerScore, int dealerScore) {
        if (playerScore > 21) {
            return Move.MoveResult.LOSE; // Jugador se pasó (Bust)
        }
        if (dealerScore > 21) {
            return Move.MoveResult.WIN; // Crupier se pasó (Bust)
        }

        if (playerScore > dealerScore) {
            return Move.MoveResult.WIN;
        } else if (playerScore < dealerScore) {
            return Move.MoveResult.LOSE;
        } else {
            return Move.MoveResult.DRAW;
        }
    }
}