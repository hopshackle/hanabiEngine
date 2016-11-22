package com.fossgalaxy.games.fireworks.ai.rule;

import com.fossgalaxy.games.fireworks.state.BasicState;
import com.fossgalaxy.games.fireworks.state.Card;
import com.fossgalaxy.games.fireworks.state.CardColour;
import com.fossgalaxy.games.fireworks.state.Deck;
import com.fossgalaxy.games.fireworks.state.actions.Action;
import com.fossgalaxy.games.fireworks.state.actions.DiscardCard;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by piers on 22/11/16.
 */
public class testDiscardUselessCard {

    private BasicState state;
    private DiscardUselessCard instance;

    @Before
    public void setup() {
        state = new BasicState(2);
        instance = new DiscardUselessCard();
    }

    @Test
    public void testAll3sDiscardedSoWillDiscard4() {

        state.getHand(0).setCard(0, new Card(4, CardColour.BLUE));
        state.getHand(0).setKnownValue(4, new Integer[]{0});
        state.getHand(0).setKnownColour(CardColour.BLUE, new Integer[]{0});

        // Need to discard the correct cards
        state.getDeck().toList()
                .stream()
                .filter(card -> card.colour == CardColour.BLUE && card.value == 3)
                .forEach(card -> state.addToDiscard(card));

        assertEquals(true, instance.canFire(0, state));
        Action action = instance.execute(0, state);
        assertEquals(true, action != null);
        assertEquals(true, action instanceof DiscardCard);
        DiscardCard discardCard = (DiscardCard) action;

        assertEquals(true, discardCard.slot == 0);
    }


}
