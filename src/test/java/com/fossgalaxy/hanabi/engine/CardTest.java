package com.fossgalaxy.hanabi.engine;

import static junitparams.JUnitParamsRunner.$;
import static org.junit.Assert.*;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.fossgalaxy.hanabi.engine.Card;
import com.fossgalaxy.hanabi.engine.CardColour;

@RunWith(JUnitParamsRunner.class)
//TODO these should really be permaterised tests - You mean like this?
public class CardTest {

	@Test
	@Parameters(method = "parametersForEqualsAndHashCode")
	public void testEqualsAndHashCode(CardColour fc, int fv, CardColour sc, int sv, boolean expected) {
				Card c1 = new Card(fv, fc);
				Card c2 = new Card(sv, sc);
				assertEquals(expected, c1.equals(c2));
				assertEquals(expected, c2.equals(c1));
				assertEquals(expected, c1.hashCode() == c2.hashCode());
	}

	public Object[] parametersForEqualsAndHashCode(){
		return $(
				$(CardColour.BLUE, 0, CardColour.BLUE, 0, true),
				$(CardColour.BLUE, 0, CardColour.GREEN, 0, false),
				$(CardColour.BLUE, 1, CardColour.BLUE, 0, false),
				$(CardColour.BLUE, 0, CardColour.BLUE, 1, false)
		);
	}

	@Test
	public void testEqualsEdgeCases(){
		Card card = new Card(0, CardColour.BLUE);
		Card c2 = new Card(null, CardColour.BLUE);
		assertEquals(true, card.equals(card));
		assertEquals(false, card.equals(null));
		assertEquals(false, card.equals(new Object()));
		assertEquals(true, c2.equals(c2));
		assertEquals(false, c2.equals(card));
	}


}
