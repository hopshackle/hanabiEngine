package uk.ac.essex.csee.iggi.hanabi;

import static org.junit.Assert.*;

import org.junit.Test;

public class CardTest {

	
	@Test
	//TODO these should really be permaterised tests
	public void testEqualsAndHashCode() {
		for (CardColour colour : CardColour.values()) {
			for (int n = 0; n<5; n++) {
				Card c1 = new Card(n, colour);
				Card c2 = new Card(n, colour);
				assertEquals(c1, c2);
			}
		}
	}
	
	@Test
	//TODO these should really be paramterised tests
	public void testEqualsAndHashCodeNotMatchNumbers() {
		for (CardColour colour : CardColour.values()) {
			for (int n = 0; n<5; n++) {
				for (int i = 0; i<5; i++) {
					if (i==n) {
						continue;
					}
					Card c1 = new Card(n, colour);
					Card c2 = new Card(i, colour);
					assertNotEquals(c1, c2);
				}
			}
		}
	}
	
	@Test
	public void testEqualsAndHashCodeNotMatchColours() {
		for (CardColour colour : CardColour.values()) {
			for (CardColour colour2 : CardColour.values()) {
				if (colour.equals(colour2)) {
					continue;
				}
				
				for (int i=0; i<5; i++) {
					Card c1 = new Card(i, colour);
					Card c2 = new Card(i, colour2);
					assertNotEquals(c1, c2);
				}
			}
		}
	}

}
