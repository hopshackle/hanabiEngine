package com.fossgalaxy.games.fireworks.engine;

import static junitparams.JUnitParamsRunner.$;
import static org.junit.Assert.*;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.fossgalaxy.games.fireworks.engine.CardColour;
import com.fossgalaxy.games.fireworks.engine.Hanabi;

@RunWith(JUnitParamsRunner.class)
public class HanabiTest {

	@Test
	@Parameters(method="parametersForStartingValues")
	public void testStartingValues(int value, CardColour colour) {
		Hanabi h = new Hanabi(1, 5);
		assertEquals(value, h.getCurrentTable(colour));
	}

	public Object[] parametersForStartingValues(){
		return $(
				$(0, CardColour.BLUE),
				$(0, CardColour.RED),
				$(0, CardColour.GREEN),
				$(0, CardColour.ORANGE),
				$(0, CardColour.WHITE)
		);
	}

}
