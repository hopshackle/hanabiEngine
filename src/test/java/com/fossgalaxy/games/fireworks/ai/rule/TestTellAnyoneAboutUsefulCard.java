package com.fossgalaxy.games.fireworks.ai.rule;

import com.fossgalaxy.games.fireworks.state.BasicState;
import com.fossgalaxy.games.fireworks.state.Card;
import com.fossgalaxy.games.fireworks.state.CardColour;
import com.fossgalaxy.games.fireworks.state.actions.Action;
import com.fossgalaxy.games.fireworks.state.actions.DiscardCard;
import com.fossgalaxy.games.fireworks.state.actions.TellColour;
import com.fossgalaxy.games.fireworks.state.actions.TellValue;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by piers on 22/11/16.
 */
public class TestTellAnyoneAboutUsefulCard {

    private BasicState state;
    private TellAnyoneAboutUsefulCard instance;

    @Before
    public void setup() {
        state = new BasicState(5);
        instance = new TellAnyoneAboutUsefulCard();
    }

    @Test
    public void testAll() {
        // For each player, test if a useful card in each other players hand will be told
        for (int first = 0; first < 5; first++) {
            for (int second = 0; second < 5; second++) {
                if (first == second) continue;
                state = new BasicState(5);
                // Set it up so that second has a useful card
                state.getHand(second).setCard(0, new Card(1, CardColour.RED));

                assertEquals(true, instance.canFire(first, state));
                Action action = instance.execute(first, state);
                assertEquals(true, action != null);
                assertEquals(true, action instanceof TellValue || action instanceof TellColour);
                if(action instanceof TellValue){
                    TellValue tellValue = (TellValue) action;
                    assertEquals(true, tellValue.value == 1);
                    assertEquals(true, tellValue.player == second);
                }
                if(action instanceof TellColour){
                    TellColour tellColour = (TellColour) action;
                    assertEquals(true, tellColour.colour == CardColour.RED);
                    assertEquals(true, tellColour.player == second);
                }
            }
        }
    }
}
