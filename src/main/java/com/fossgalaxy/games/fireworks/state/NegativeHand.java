package com.fossgalaxy.games.fireworks.state;

import java.util.*;

public class NegativeHand extends Hand {

	//what the agent can infer about it's hand (based on negative information)
	private final Map<Integer, Set<CardColour>> possibleColours;
	private final Map<Integer, Set<Integer>> possibleValues;

	public NegativeHand(NegativeHand hand) {
		super(hand);

		this.possibleColours = copyEnumMap(hand.possibleColours);
		this.possibleValues = copyMap(hand.possibleValues);
	}

	public NegativeHand(int size) {
		super(size);
		this.possibleColours = new HashMap<>();
		this.possibleValues = new HashMap<>();
	}

	/**
	 * Reset all information about a slot
	 */
	void clear(int slot) {
		super.clear(slot);
		possibleColours.put(slot, EnumSet.allOf(CardColour.class));
		possibleValues.put(slot, new HashSet<>(Arrays.asList(1,2,3,4,5)));
	}


	// From this players perspective
	public CardColour getKnownColour(int slot) {
			Set<CardColour> c = possibleColours.get(slot);
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
				Set<CardColour> colours = possibleColours.get(slot);
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

	private static <T> Map<Integer, Set<T>> copyMap(Map<Integer, Set<T>> map) {
		Map<Integer, Set<T>> mapCopy = new HashMap<>();
		for (Map.Entry<Integer, Set<T>> entry : map.entrySet()) {
			mapCopy.put(entry.getKey(), new HashSet<>(entry.getValue()));
		}
		return mapCopy;
	}

	private static <T extends Enum> Map<Integer, Set<T>> copyEnumMap(Map<Integer, Set<T>> map) {
		Map<Integer, Set<T>> mapCopy = new HashMap<>();
		for (Map.Entry<Integer, Set<T>> entry : map.entrySet()) {
			mapCopy.put(entry.getKey(), EnumSet.copyOf(entry.getValue()));
		}
		return mapCopy;
	}
}
