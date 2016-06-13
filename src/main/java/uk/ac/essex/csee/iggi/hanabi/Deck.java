package uk.ac.essex.csee.iggi.hanabi;

import java.util.List;
import java.util.Stack;

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
	
	public void shuffle() {
		
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

}
