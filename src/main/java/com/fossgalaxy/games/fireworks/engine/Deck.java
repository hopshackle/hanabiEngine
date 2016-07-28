package com.fossgalaxy.games.fireworks.engine;

import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * Represents a deck of Hanabi cards.
 *
 */
public class Deck {
	private Stack<Card> cards;
	
	public Deck() {
		this.cards = new Stack<Card>();
	}

    /**
     * Provides a clone of the given deck
     *
     * A shallow copy - but the cards themselves are immutable so no problem.
     * @param deck The given deck to be cloned
     */
	public Deck(Deck deck) {
		this.cards = (Stack<Card>)deck.cards.clone();
	}

	/**
	 * Initialises the deck of cards with a complete set for the game
     */
	public void init(){
		for (CardColour c : CardColour.values()) {
			for (int i=1; i<=5; i++) {
				cards.add(new Card(i, c));

				// there are at least 2 of every non-5 card
				if ( i <= 4 ) {
					cards.add(new Card(i, c));
				}

				// there are are 3 ones
				if (i == 1) {
					cards.add(new Card(i, c));
				}
			}
		}
	}

	/**
	 * shuffle this deck of cards.
	 * 
	 */
	public void shuffle() {
		Collections.shuffle(cards);
	}

    /**
     * Gets and removes the top card from the deck
     * @return The card that was on top of the deck
     */
	public Card getTopCard() {
		return cards.pop();
	}

    /**
     * Gets the number of cards left in the deck
     * @return int number of cards left
     */
	public int getCardsLeft() {
		return cards.size();
	}

    /**
     * Are there any cards left in the deck?
     * @return boolean are there any cards left?
     */
	public boolean hasCardsLeft() {
		return cards.isEmpty();
	}

	/**
	 * Add a new card to the deck.
	 * 
	 * @param card the card to add
	 */
	public void add(Card card) {
		cards.push(card);
	}

}
