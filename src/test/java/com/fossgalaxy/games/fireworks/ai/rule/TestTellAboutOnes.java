package com.fossgalaxy.games.fireworks.ai.rule;

import com.fossgalaxy.games.fireworks.state.BasicState;
import com.fossgalaxy.games.fireworks.state.Card;
import com.fossgalaxy.games.fireworks.state.CardColour;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.assertEquals;

/**
 * Created by webpigeon on 26/10/16.
 */
public class TestTellAboutOnes {

    /**
     * A tell action is illegal if there is no information, the rule shouild report it cannot fire in this case.
     */
    @Test
    public void tellIsIllegalWithNoInfo() {
        BasicState state = new BasicState(2);
        state.setInfomation(0);

        TellAboutOnes instance = new TellAboutOnes();
        boolean result = instance.canFire(0, state);

        assertEquals(result, false);
    }

    /**
     * If the next player has a one they do not know about, the rule can fire.
     */
    @Test
    public void tellNextPlayerHasOnes() {
        BasicState state = new BasicState(2);
        state.setInfomation(8);

        state.getHand(1).setCard(0, new Card(1, CardColour.RED));
        state.getHand(1).setCard(1, new Card(1, CardColour.RED));

        TellAboutOnes instance = new TellAboutOnes();
        boolean result = instance.canFire(0, state);

        assertEquals(result, true);
    }

    /**
     * If the next player has a one, but they know about it, the rule should not fire.
     */
    @Test
    public void tellNextPlayerHasOnesButTheyKnowAlready() {
        BasicState state = new BasicState(2);
        state.setInfomation(8);

        state.getHand(1).setCard(0, new Card(1, CardColour.RED));
        state.getHand(1).setKnownValue(1, new Integer[]{0});
        state.getHand(1).setKnownColour(CardColour.RED, new Integer[]{0});

        TellAboutOnes instance = new TellAboutOnes();
        boolean result = instance.canFire(0, state);

        assertEquals(result, false);
    }

}
