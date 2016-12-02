package com.fossgalaxy.games.fireworks.ai.rule.logic;

import com.fossgalaxy.games.fireworks.state.BasicState;
import com.fossgalaxy.games.fireworks.state.Card;
import com.fossgalaxy.games.fireworks.state.CardColour;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junitparams.JUnitParamsRunner.$;
import static org.junit.Assert.assertEquals;

/**
 * Created by piers on 02/12/16.
 */
@RunWith(JUnitParamsRunner.class)
public class testHandUtils {

    private BasicState state = new BasicState(2);

    @Before
    public void setup() {
        state = new BasicState(2);
        state.setInformation(7);
    }

    public Object[] parametersForHighestScorePossible() {
        return $(
                $(1, 0, 5),
                $(1, 3, 0),
                $(2, 2, 1),
                $(3, 2, 2),
                $(4, 2, 3),
                $(5, 1, 4)
        );
    }

    @Test
    @Parameters(method = "parametersForHighestScorePossible")
    public void testHighestScorePossible(int cardValue, int numToRemove, int maxScore) {
        state.init();
        final CardColour colour = CardColour.BLUE;
        for (int i = 0; i < numToRemove; i++) {
            state.addToDiscard(new Card(cardValue, colour));
        }

        assertEquals(maxScore, HandUtils.getHighestScorePossible(state, colour));
    }
}
