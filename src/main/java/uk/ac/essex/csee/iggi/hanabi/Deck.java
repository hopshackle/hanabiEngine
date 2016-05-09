package uk.ac.essex.csee.iggi.hanabi;

import java.util.List;
import java.util.Stack;

public class Deck {
	private Stack<Card> cards;
	
	public Deck() {
		this.cards = new Stack<Card>();
	}
	
	public Deck(Deck deck) {
		this.cards = (Stack<Card>)deck.cards.clone();
	}
	
	public void shuffle() {
		
	}
	
	public Card getTopCard() {
		return cards.pop();
	}
	
	public int getCardsLeft() {
		return cards.size();
	}

	public boolean hasCardsLeft() {
		return cards.isEmpty();
	}

}
