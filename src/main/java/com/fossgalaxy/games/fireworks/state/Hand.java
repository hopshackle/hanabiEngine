package com.fossgalaxy.games.fireworks.state;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Hand implements Iterable<Card> {
	//flag for controlling if players remember what they haven't been told
	private boolean negativeInfomation = true;
	private int size;

	private final CardColour[] colours;
	private final Integer[] values;
	private final Card[] cards;
	
	private final Map<Integer, EnumSet<CardColour>> possibleColours;
	private final Map<Integer, Set<Integer>> possibleValues;

	public Hand(Hand hand) {
		this.size = hand.size;
		this.colours = Arrays.copyOf(hand.colours, size);
		this.values = Arrays.copyOf(hand.values, size);
		this.cards = Arrays.copyOf(hand.cards, size);
		
		//TODO these should be deep copies
		this.possibleColours = new HashMap<>(hand.possibleColours);
		this.possibleValues = new HashMap<>(hand.possibleValues);
	}

	public Hand(int size) {
		this.size = size;
		this.colours = new CardColour[size];
		this.values = new Integer[size];
		this.cards = new Card[size];
		this.possibleColours = new HashMap<>();
		this.possibleValues = new HashMap<>();
	}

	void clear(int slot) {
		values[slot] = null;
		colours[slot] = null;
		possibleColours.put(slot, EnumSet.allOf(CardColour.class));
		possibleValues.put(slot, new HashSet<Integer>(Arrays.asList(1,2,3,4,5)));
	}

	// From the Game's (perfect information) perspective
	public Card getCard(int slot) {
		return cards[slot];
	}

	// From this players perspective
	public CardColour getKnownColour(int slot) {
		if (negativeInfomation) {
			EnumSet<CardColour> c = possibleColours.get(slot);
			if (c != null && c.size() == 1) {
				return c.iterator().next();
			}
		}
		
		return colours[slot];
	}

	public Integer getKnownValue(int slot) {
		if (negativeInfomation) {
			Set<Integer> c = possibleValues.get(slot);
			if (c != null && c.size() == 1) {
				return c.iterator().next();
			}
		}
		
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

	public void setKnownColour(CardColour colour, Integer[] slots){
		int index = 0;
		for (int slot=0; slot<size; slot++) {
			if (index < slots.length && slots[index] == slot) {
				//we found a matching slot
				possibleColours.put(slot, EnumSet.of(colour));
				colours[slot] = colour;
				index++;
			} else {
				EnumSet<CardColour> colours = possibleColours.get(slot);
				if (colours != null) {
					colours.remove(colour);
				}
			}
		}
	}
	
	public void setKnownValue(Integer value, Integer[] slots) {
		int index = 0;
		for (int slot=0; slot<size; slot++) {
			if (index < slots.length && slots[index] == slot) {
				//we found a matching slot
				possibleValues.put(slot, new HashSet<Integer>(Arrays.asList(value)));
				values[slot] = value;
				index++;
			} else {
				Set<Integer> values = possibleValues.get(slot);
				if (values != null) {
					values.remove(value);
				}
			}
		}
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
