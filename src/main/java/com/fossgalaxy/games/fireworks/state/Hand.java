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

	void clear(int slot) {
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

	void setCard(int slot, Card card) {
		cards[slot] = card;
		clear(slot);
	}

	public void setKnownColour(int slot, CardColour colour) {
		colours[slot] = colour;
	}

	public void setKnownValue(int slot, Integer value) {
		values[slot] = value;
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();

		for (int i = 0; i < size; i++) {
			buf.append(colours[i]);
			buf.append(" ");
			buf.append(values[i]);

			if (i < size - 1) {
				buf.append(", ");
			}
		}

		return buf.toString();
	}

}
