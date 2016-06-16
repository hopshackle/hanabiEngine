package uk.ac.essex.csee.iggi.hanabi;

import static org.junit.Assert.*;

import org.junit.Test;

public class HanabiTest {

	@Test
	public void test() {
		int handSize = 5;
		
		Hanabi h = new Hanabi(1, handSize);
		int zero = 0;
		int startingLives = 3;
		
		assertEquals(zero, h.getCurrentTable(CardColour.BLUE));
		assertEquals(zero, h.getCurrentTable(CardColour.RED));
		assertEquals(zero, h.getCurrentTable(CardColour.GREEN));
		assertEquals(zero, h.getCurrentTable(CardColour.ORANGE));
		assertEquals(zero, h.getCurrentTable(CardColour.WHITE));
		assertEquals(startingLives, h.getLives());
	}

}
