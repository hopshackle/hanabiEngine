package com.fossgalaxy.games.fireworks.state.hands;

import com.fossgalaxy.games.fireworks.ai.rule.RuleUtils;
import com.fossgalaxy.games.fireworks.state.*;
import com.fossgalaxy.games.fireworks.state.actions.Action;
import com.fossgalaxy.games.fireworks.state.actions.DiscardCard;
import com.fossgalaxy.games.fireworks.state.actions.PlayCard;
import com.fossgalaxy.games.fireworks.state.actions.TellValue;
import com.fossgalaxy.games.fireworks.state.events.CardInfoValue;
import org.junit.*;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

public class TestHandDeterminiser {

    HandDeterminiser hd;
    GameState state;

    @Before
    public void setup() {
        state = new BasicState(4);
        state.init(45L);
    }

    @Test
    public void simpleSetUpOfNewGame() {
        Card[][] startingCards = new Card[2][4];
        for (int i = 0; i < 4; i++) {
            startingCards[0][i] = state.getCardAt(0, i);
            startingCards[1][i] = state.getCardAt(1, i);
        }

        hd = new HandDeterminiser(state, 0);

        assertEquals(hd.getSlotLastUsed(), -1);
        assertTrue(hd.getHandRecord(0, 0) == null);

        for (int i = 0; i < 4; i++) {
            assertTrue(state.getCardAt(0, i) != startingCards[0][i]);
            assertTrue(state.getCardAt(1, i) == startingCards[1][i]);
        }
    }

    @Test
    public void rootPlayerIsNeverRedeterminised() {
        hd = new HandDeterminiser(state, 0);

        Card[] startingCards = new Card[4];
        for (int i = 0; i < 4; i++) {
            startingCards[i] = state.getCardAt(0, i);
            assertFalse(startingCards[i] == null);
        }

        hd.determiniseHandFor(0, state); // should not change any cards
        for (int i = 0; i < 4; i++) {
            assertTrue(state.getCardAt(0, i) == startingCards[i]);
        }
    }

    @Test
    public void determinisationAccountsForKnownInformationForRoot() {
        state.getHand(0).setKnownColour(CardColour.RED, new Integer[]{1, 3});
        state.getHand(0).setKnownValue(1, new Integer[]{0});
        hd = new HandDeterminiser(state, 0);

        assertEquals(state.getCardAt(0, 0).value.intValue(), 1);
        assertTrue(state.getCardAt(0, 1).colour == CardColour.RED);
        assertTrue(state.getCardAt(0, 3).colour == CardColour.RED);
    }

    @Test
    public void determinisationForOtherPlayers() {
        state.getDeck().add(new Card(5, CardColour.GREEN)); // add another Green 5 to ensure that there is one to bind
        state.getHand(2).setKnownColour(CardColour.GREEN, new Integer[]{1, 2, 3});
        state.getHand(2).setKnownValue(5, new Integer[]{3});
        hd = new HandDeterminiser(state, 0);

        hd.determiniseHandFor(2, state);

        assertTrue(state.getCardAt(2, 1).colour == CardColour.GREEN);
        assertTrue(state.getCardAt(2, 2).colour == CardColour.GREEN);
        assertTrue(state.getCardAt(2, 3).colour == CardColour.GREEN);
        assertEquals(state.getCardAt(2, 3).value.intValue(), 5);
    }

    @Test
    public void otherPlayersHandIsResetAfterTheirGo() {
        state.getDeck().add(new Card(5, CardColour.GREEN)); // add another Green 5 to ensure that there is one to bind
        state.getHand(2).setKnownColour(CardColour.GREEN, new Integer[]{3});
        state.getHand(2).setKnownValue(5, new Integer[]{3});
        hd = new HandDeterminiser(state, 0);

        Card[] startingCards = new Card[4];
        for (int i = 0; i < 4; i++) {
            startingCards[i] = state.getCardAt(2, i);
        }

        hd.determiniseHandFor(2, state);

        for (int i = 0; i < 4; i++) {
            assertTrue(state.getCardAt(2, i) != startingCards[i]);
        }
        assertTrue(state.getCardAt(2, 3).colour == CardColour.GREEN);
        assertEquals(state.getCardAt(2, 3).value.intValue(), 5);

        hd.recordAction(new TellValue(0, state.getCardAt(0, 0).value));

        hd.determiniseHandFor(3, state);

        for (int i = 0; i < 4; i++) {
            assertTrue(state.getCardAt(2, i) == startingCards[i]);
        }
    }

    @Test
    public void ifACardIsPlayedThenTheOneDrawnStaysInHand() {
        hd = new HandDeterminiser(state, 0);

        Card[] startingCards = new Card[4];
        for (int i = 0; i < 4; i++) {
            startingCards[i] = state.getCardAt(1, i);
        }

        hd.determiniseHandFor(1, state);

        Action playCard = new PlayCard(2);
        playCard.apply(1, state);
        hd.recordAction(playCard);
        Card drawnCard = state.getCardAt(1, 2);
        assertFalse(drawnCard == startingCards[2]);

        hd.determiniseHandFor(2, state);

        for (int i = 0; i < 4; i++) {
            if (i == 2) {
                assertTrue(drawnCard == state.getCardAt(1, i));
            } else {
                assertTrue(state.getCardAt(1, i) == startingCards[i]);
            }
        }
    }

    @Test
    public void ifACardIsDiscardedThenTheOneDrawnStaysInHand() {
        hd = new HandDeterminiser(state, 0);

        Card[] startingCards = new Card[4];
        for (int i = 0; i < 4; i++) {
            startingCards[i] = state.getCardAt(1, i);
        }

        hd.determiniseHandFor(1, state);
        state.setInformation(7);

        Action discardCard = new DiscardCard(2);
        discardCard.apply(1, state);
        hd.recordAction(discardCard);
        Card drawnCard = state.getCardAt(1, 2);
        assertFalse(drawnCard == startingCards[2]);

        hd.determiniseHandFor(2, state);

        for (int i = 0; i < 4; i++) {
            if (i == 2) {
                assertTrue(drawnCard == state.getCardAt(1, i));
            } else {
                assertTrue(state.getCardAt(1, i) == startingCards[i]);
            }
        }
    }
}
