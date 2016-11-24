package com.fossgalaxy.games.fireworks.ai.rule;

import com.fossgalaxy.games.fireworks.ai.rule.random.TellRandomly;
import com.fossgalaxy.games.fireworks.state.BasicState;
import com.fossgalaxy.games.fireworks.state.actions.Action;
import com.fossgalaxy.games.fireworks.state.actions.TellColour;
import com.fossgalaxy.games.fireworks.state.actions.TellValue;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by piers on 24/11/16.
 */
public class TestTellRandomly {

    @Test
    public void testTellsWhenAbleTo(){
        BasicState state = new BasicState(2);
        state.setInfomation(8);
        state.init();
        TellRandomly instance = new TellRandomly();
        assertEquals(true, instance.canFire(0, state));
        Action action = instance.execute(0, state);
        assertEquals(true, action != null);
        assertEquals(true, action instanceof TellColour || action instanceof TellValue);
    }
}
