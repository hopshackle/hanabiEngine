package com.fossgalaxy.games.fireworks.state;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;

import static junitparams.JUnitParamsRunner.$;
import static org.junit.Assert.assertEquals;
import static com.fossgalaxy.games.fireworks.state.CardColour.*;

/**
 * Created by piers on 25/11/16.
 */
@RunWith(JUnitParamsRunner.class)
public class TestBasicHand {

    private BasicHand basicHand;

    @Before
    public void setup() {
        basicHand = new BasicHand(5);
    }

    private CardColour[] c(CardColour... colours) {
        return colours;
    }

    private Integer[] v(Integer... integers) {
        return integers;
    }

    public Object[] parametersForTestIsPossible() {
        return $(
                $(0, new Card(1, BLUE), v(1, null, null, null), c(BLUE, null, null, null), true),
                $(0, new Card(1, BLUE), v(2, null, null, null), c(BLUE, null, null, null), false),
                $(0, new Card(1, BLUE), v(1, null, null, null), c(RED, null, null, null), false),
                $(0, new Card(1, BLUE), v(2, null, null, null), c(RED, null, null, null), false),
                $(0, new Card(1, BLUE), v(1, null, null, null), c(null, null, null, null), true),
                $(0, new Card(1, BLUE), v(null, null, null, null), c(BLUE, null, null, null), true)
        );
    }

    @Test
    @Parameters(method = "parametersForTestIsPossible")
    public void testIsPossible(int slot, Card card, Integer[] values, CardColour[] colours, boolean expected) {
        for (int i = 0; i < colours.length; i++) {
            basicHand.setKnownValue(values[i], new Integer[]{i});
            basicHand.setKnownColour(colours[i], new Integer[]{i});
        }

        assertEquals(expected, basicHand.isPossible(slot, card));
    }

    @Test
    @Parameters(method = "parametersForTestIsPossible")
    public void testIsCompletePossible(int slot, Card card, Integer[] values, CardColour[] colours, boolean expected) {
        for (int i = 0; i < colours.length; i++) {
            basicHand.setKnownValue(values[i], new Integer[]{i});
            basicHand.setKnownColour(colours[i], new Integer[]{i});
        }

        assertEquals(expected, basicHand.isCompletePossible(slot, card));
    }


    public Object[] parametersForTestGetPossibleValues() {
        return $(
                $(0, v(null, null, null, null), new int[]{1, 2, 3, 4, 5}),
                $(0, v(1, null, null, null), new int[]{1}),
                $(0, v(2, null, null, null), new int[]{2}),
                $(1, v(null, null, null, null), new int[]{1, 2, 3, 4, 5}),
                $(1, v(null, 1, null, null), new int[]{1}),
                $(1, v(null, 2, null, null), new int[]{2})
        );
    }

    @Test
    @Parameters(method = "parametersForTestGetPossibleValues")
    public void testGetPossibleValues(int slot, Integer[] values, int[] expected) {
        for (int i = 0; i < values.length; i++) {
            basicHand.setKnownValue(values[i], new Integer[]{i});
        }

        assertEquals(true, Arrays.equals(expected, basicHand.getPossibleValues(slot)));
    }

    public Object[] parametersForTestGetPossibleColours() {
        return $(
                $(0, c(null, null, null, null), CardColour.values()),
                $(0, c(RED, null, null, null), c(RED)),
                $(0, c(BLUE, null, null, null), c(BLUE)),
                $(1, c(null, null, null, null), CardColour.values()),
                $(1, c(null, RED, null, null), c(RED)),
                $(1, c(null, BLUE, null, null), c(BLUE))
        );
    }

    @Test
    @Parameters(method = "parametersForTestGetPossibleColours")
    public void testGetPossibleColours(int slot, CardColour[] colours, CardColour[] expected){
        for (int i = 0; i < colours.length; i++) {
            basicHand.setKnownColour(colours[i], new Integer[]{i});
        }

        assertEquals(true, Arrays.equals(expected, basicHand.getPossibleColours(slot)));
    }
}
