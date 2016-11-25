package com.fossgalaxy.games.fireworks.state;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junitparams.JUnitParamsRunner.$;
import static org.junit.Assert.assertArrayEquals;
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
        basicHand.init();
    }

    public static CardColour[] c(CardColour... colours) {
        return colours;
    }

    public static Integer[] v(Integer... integers) {
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
        setKnowledge(basicHand, values, colours);
        assertEquals(expected, basicHand.isPossible(slot, card));
    }

    @Test
    @Parameters(method = "parametersForTestIsPossible")
    public void testIsCompletePossible(int slot, Card card, Integer[] values, CardColour[] colours, boolean expected) {
        setKnowledge(basicHand, values, colours);

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
        setKnowledge(basicHand, values);

        assertArrayEquals(expected, basicHand.getPossibleValues(slot));
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
    public void testGetPossibleColours(int slot, CardColour[] colours, CardColour[] expected) {
        setKnowledge(basicHand, colours);

        assertArrayEquals(expected, basicHand.getPossibleColours(slot));
    }

    public Object[] parametersForTestHasColour() {
        return $(
                $(c(BLUE, null, RED, ORANGE), BLUE, true),
                $(c(BLUE, null, RED, ORANGE), RED, true),
                $(c(BLUE, null, RED, ORANGE), ORANGE, true),
                $(c(BLUE, null, RED, ORANGE), GREEN, false),
                $(c(BLUE, null, RED, ORANGE), WHITE, false)
        );
    }

    @Test
    @Parameters(method = "parametersForTestHasColour")
    public void testHasColour(CardColour[] colours, CardColour testColour, boolean expected) {
        setKnowledge(basicHand, colours);

        assertEquals(expected, basicHand.hasColour(testColour));
    }

    public Object[] parametersForTestHasValue() {
        return $(
                $(v(1, 2, 3, 4), 1, true),
                $(v(1, 2, 3, 4), 2, true),
                $(v(1, 2, 3, 4), 3, true),
                $(v(1, 2, 3, 4), 4, true),
                $(v(1, 2, 3, 4), 5, false)
        );
    }

    @Test
    @Parameters(method = "parametersForTestHasValue")
    public void testHasValue(Integer[] values, int testValue, boolean expected) {
        for (int i = 0; i < values.length; i++) {
            basicHand.setKnownValue(values[i], new Integer[]{i});
        }

        assertEquals(expected, basicHand.hasValue(testValue));
    }

    public static void setKnowledge(Hand hand, Integer[] values) {
        setKnowledge(hand, values, null);
    }

    public static void setKnowledge(Hand hand, CardColour[] colours) {
        setKnowledge(hand, null, colours);
    }

    public static void setKnowledge(Hand hand, Integer[] values, CardColour[] colours) {
        if (values == null && colours == null) return;
        int length = (colours == null) ? values.length : colours.length;
        for (int i = 0; i < length; i++) {
            if (values != null && values[i] != null) {
                hand.setKnownValue(values[i], new Integer[]{i});
            }
            if (colours != null && colours[i] != null) {
                hand.setKnownColour(colours[i], new Integer[]{i});
            }
            if (values != null && colours != null) {
                if (values[i] != null && colours[i] != null) {
                    hand.bindCard(i, new Card(values[i], colours[i]));
                }
            }
        }
    }
}
