package com.fossgalaxy.games.fireworks.ai.rule;

import com.fossgalaxy.games.fireworks.ai.osawa.rules.TellPlayableCardOuter;
import com.fossgalaxy.games.fireworks.state.BasicState;
import com.fossgalaxy.games.fireworks.state.Card;
import com.fossgalaxy.games.fireworks.state.CardColour;
import com.fossgalaxy.games.fireworks.state.actions.Action;
import com.fossgalaxy.games.fireworks.state.actions.TellColour;
import com.fossgalaxy.games.fireworks.state.actions.TellValue;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by piers on 22/11/16.
 */
public class TestTellPlayableCardOuter {

    @Test
    public void testTellPlayableCard(){
        BasicState state = new BasicState(2);
        TellPlayableCardOuter instance = new TellPlayableCardOuter();

        // something we want
        state.getHand(0).setCard(0, new Card(1, CardColour.BLUE));

        assertEquals(true, instance.canFire(1, state));
        Action action = instance.execute(1, state);
        assertEquals(true, action != null);
        assertEquals(true, action instanceof TellColour || action instanceof TellValue);
        if(action instanceof  TellColour){
            TellColour tellColour = (TellColour) action;
            assertEquals(true, tellColour.colour == CardColour.BLUE);
        }
        if(action instanceof TellValue){
            TellValue tellValue = (TellValue) action;
            assertEquals(true, tellValue.value == 1);
        }
    }
}
