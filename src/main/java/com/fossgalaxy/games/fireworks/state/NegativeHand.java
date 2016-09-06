package com.fossgalaxy.games.fireworks.state;

import java.util.*;

public class NegativeHand extends Hand {

	//what the agent can infer about it's hand (based on negative information)
	private final Map<Integer, EnumSet<CardColour>> possibleColours;
	private final Map<Integer, Set<Integer>> possibleValues;

	public NegativeHand(NegativeHand hand) {
		super(hand);

		//TODO these should be deep copies
		this.possibleColours = new HashMap<>(hand.possibleColours);
		this.possibleValues = new HashMap<>(hand.possibleValues);
	}

	public NegativeHand(int size) {
		super(size);
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
		super.clear(slot);
		possibleColours.put(slot, EnumSet.allOf(CardColour.class));
		possibleValues.put(slot, new HashSet<Integer>(Arrays.asList(1,2,3,4,5)));
	}


	// From this players perspective
	public CardColour getKnownColour(int slot) {
			EnumSet<CardColour> c = possibleColours.get(slot);
			if (c != null && c.size() == 1) {
				return c.iterator().next();
			}
			return super.getKnownColour(slot);
	}

	public Integer getKnownValue(int slot) {
			Set<Integer> c = possibleValues.get(slot);
			if (c != null && c.size() == 1) {
				return c.iterator().next();
			}
			return super.getKnownValue(slot);
	}

	public void setKnownColour(CardColour colour, Integer[] slots){
		int index = 0;
		for (int slot=0; slot<getSize(); slot++) {
			if (index < slots.length && slots[index] == slot) {
				//we found a matching slot
				possibleColours.put(slot, EnumSet.of(colour));
				index++;
			} else {
				EnumSet<CardColour> colours = possibleColours.get(slot);
				if (colours != null) {
					colours.remove(colour);
				}
			}
		}
		super.setKnownColour(colour, slots);
	}
	
	public void setKnownValue(Integer value, Integer[] slots) {
		int index = 0;
		for (int slot=0; slot<getSize(); slot++) {
			if (index < slots.length && slots[index] == slot) {
				//we found a matching slot
				possibleValues.put(slot, new HashSet<Integer>(Arrays.asList(value)));
				index++;
			} else {
				Set<Integer> values = possibleValues.get(slot);
				if (values != null) {
					values.remove(value);
				}
			}
		}
		super.setKnownValue(value, slots);
	}


	public boolean isPossible(int slot, Card card) {
		Set<CardColour> possibleColour = possibleColours.get(slot);
		Set<Integer> possibleValue = possibleValues.get(slot);
		return possibleColour.contains(card.colour) && possibleValue.contains(card.value);
	}

}
