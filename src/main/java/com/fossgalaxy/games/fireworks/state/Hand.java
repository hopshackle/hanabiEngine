package com.fossgalaxy.games.fireworks.state;

import java.util.Arrays;
import java.util.Iterator;

public class Hand implements Iterable<Card> {
	private int size;
	
	private final CardColour[] colours;
	private final Integer[] values;
	private final Card[] cards;

	public Hand(Hand hand) {
		this.size = hand.size;
		this.colours = Arrays.copyOf(hand.colours, size);
		this.values = Arrays.copyOf(hand.values, size);
		this.cards = Arrays.copyOf(hand.cards, size);
	}

	public Hand(int size) {
		this.size = size;
		this.colours = new CardColour[size];
		this.values = new Integer[size];
		this.cards = new Card[size];

	}

	public void init(){
		for (int i=0; i<size; i++) {
			clear(i);
		}
	}

	/**
	 * Reset all information about a slot
	 */
	void clear(int slot) {
		cards[slot] = null;
		values[slot] = null;
		colours[slot] = null;
	}

	// From the Game's (perfect information) perspective
	public Card getCard(int slot) {
		return cards[slot];
	}

	// From this players perspective
	public CardColour getKnownColour(int slot) {
		return colours[slot];
	}

	public Integer getKnownValue(int slot) {
		return values[slot];
	}

	public int getSize() {
		return size;
	}

	@Override
	public Iterator<Card> iterator() {
		return Arrays.asList(cards).iterator();
	}

	public void setCard(int slot, Card card) {
		clear(slot);
		cards[slot] = card;
	}

	public void bindCard(int slot, Card card){
		cards[slot] = card;
	}

	public void setKnownColour(CardColour colour, Integer[] slots){
		for (Integer slot : slots) {
			assert colours[slot] == null || colours[slot].equals(colour) : "told about contradictory colours: "+colours[slot]+ " "+colour;
			colours[slot] = colour;
		}
	}
	
	public void setKnownValue(Integer value, Integer[] slots) {
		for (Integer slot : slots) {
			assert values[slot] == null || values[slot].equals(value) : "told about contradictory values for "+slot+": "+values[slot]+ " "+value;
			values[slot] = value;
		}
	}

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();

		for (int i = 0; i < size; i++) {
			buf.append(colours[i]);
			buf.append(" ");
			buf.append(values[i]);

			if (i < size - 1) {
				buf.append(", ");
			}
		}

		return "I think I have: " +buf.toString();
	}
	
	public boolean isCompletePossible(int slot, Card card) {

			if (cards[slot] != null) {
				return cards[slot].equals(card);
			}

			return isPossible(slot, card);
	}

	public boolean isPossible(int slot, Card card) {
		boolean possibleColour = colours[slot] == null || colours[slot].equals(card.colour);
		boolean possibleValue = values[slot] == null || values[slot].equals(card.value);
		return possibleColour && possibleValue;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Hand cards1 = (Hand) o;

		if (size != cards1.size) return false;
		// Probably incorrect - comparing Object[] arrays with Arrays.equals
		if (!Arrays.equals(colours, cards1.colours)) return false;
		// Probably incorrect - comparing Object[] arrays with Arrays.equals
		if (!Arrays.equals(values, cards1.values)) return false;
		// Probably incorrect - comparing Object[] arrays with Arrays.equals
		return Arrays.equals(cards, cards1.cards);
	}

	@Override
	public int hashCode() {
		int result = 0;
		result = 31 * result + size;
		result = 31 * result + Arrays.hashCode(colours);
		result = 31 * result + Arrays.hashCode(values);
		result = 31 * result + Arrays.hashCode(cards);
		return result;
	}

    public boolean hasColour(CardColour colour) {
    	for (Card c : cards){
    		if (c != null && colour.equals(c.colour)){
    			return true;
			}
		}
		return false;
    }

	public boolean hasValue(Integer value) {
		for (Card c : cards){
			if (c != null && value.equals(c.value)){
				return true;
			}
		}
		return false;
	}
}
