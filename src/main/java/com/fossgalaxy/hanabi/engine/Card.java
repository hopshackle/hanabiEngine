package com.fossgalaxy.hanabi.engine;

/**
 * A card for the Hanabi card game
 */
public class Card {
	public final Integer value;
	public final CardColour colour;
	
	public Card(Integer value, CardColour colour) {
		this.value = value;
		this.colour = colour;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((colour == null) ? 0 : colour.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Card other = (Card) obj;
		if (colour != other.colour)
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}


	public String toString() {
		return colour+" "+value;
	}
}
