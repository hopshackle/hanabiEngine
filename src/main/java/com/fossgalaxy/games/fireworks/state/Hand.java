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
	private boolean negativeInfomation = false;
	private int size;
	
	private final CardColour[] colours;
	private final Integer[] values;
	private final Card[] cards;
	
	//what the agent can infer about it's hand (based on negative information)
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

	public void setCard(int slot, Card card) {
		clear(slot);
		cards[slot] = card;
	}

	public void setKnownColour(CardColour colour, Integer[] slots){
		int index = 0;
		for (int slot=0; slot<size; slot++) {
			if (index < slots.length && slots[index] == slot) {
				//we found a matching slot
				possibleColours.put(slot, EnumSet.of(colour));
				assert colours[slot] == null || colours[slot].equals(colour) : "told about contradictory colours: "+colours[slot]+ " "+colour;
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
				assert values[slot] == null || values[slot].equals(value) : "told contradictary values "+values[slot] + " "  +value;
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
		System.out.println(Arrays.toString(cards));

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
	
	public boolean isPossible(int slot, Card card) {

		if (negativeInfomation) {
			Set<CardColour> possibleColour = possibleColours.get(slot);
			Set<Integer> possibleValue = possibleValues.get(slot);
			return possibleColour.contains(card.colour) && possibleValue.contains(card.value);
		} else {
			if (cards[slot] != null) {
				return cards[slot].equals(card);
			}

			boolean possibleColour = colours[slot] == null || colours[slot].equals(card.colour);
			boolean possibleValue = values[slot] == null || values[slot].equals(card.value);
			return possibleColour && possibleValue;
		}

	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Hand cards1 = (Hand) o;

		if (negativeInfomation != cards1.negativeInfomation) return false;
		if (size != cards1.size) return false;
		// Probably incorrect - comparing Object[] arrays with Arrays.equals
		if (!Arrays.equals(colours, cards1.colours)) return false;
		// Probably incorrect - comparing Object[] arrays with Arrays.equals
		if (!Arrays.equals(values, cards1.values)) return false;
		// Probably incorrect - comparing Object[] arrays with Arrays.equals
		if (!Arrays.equals(cards, cards1.cards)) return false;
		if (possibleColours != null ? !possibleColours.equals(cards1.possibleColours) : cards1.possibleColours != null)
			return false;
		return possibleValues != null ? possibleValues.equals(cards1.possibleValues) : cards1.possibleValues == null;

	}

	@Override
	public int hashCode() {
		int result = (negativeInfomation ? 1 : 0);
		result = 31 * result + size;
		result = 31 * result + Arrays.hashCode(colours);
		result = 31 * result + Arrays.hashCode(values);
		result = 31 * result + Arrays.hashCode(cards);
		result = 31 * result + (possibleColours != null ? possibleColours.hashCode() : 0);
		result = 31 * result + (possibleValues != null ? possibleValues.hashCode() : 0);
		return result;
	}
}
