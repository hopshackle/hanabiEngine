package com.fossgalaxy.games.fireworks.state;

import com.fossgalaxy.games.fireworks.state.actions.TellColour;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by piers on 23/11/16.
 */
public class TestTellColour {

    private BasicState state;

    @Before
    public void setup() {
        state = new BasicState(2);
    }

    @Test(expected = RulesViolation.class)
    public void testTellingSelf() {
        TellColour tellColour = new TellColour(1, CardColour.BLUE);
        tellColour.apply(1, state);
    }
}
