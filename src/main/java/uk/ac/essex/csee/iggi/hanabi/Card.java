package uk.ac.essex.csee.iggi.hanabi;

public class Card {
	public final Integer value;
	public final CardColour colour;
	
	public Card(Integer value, CardColour colour) {
		this.value = value;
		this.colour = colour;
	}
	
	public String toString() {
		return colour+" "+value;
	}
}
