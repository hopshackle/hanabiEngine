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

import java.util.function.Function;
import java.util.stream.*;
import java.util.*;

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

        assertEquals(state.getDeck().toList().size(), 50 - 16 + 1);

        hd = new HandDeterminiser(state, 0);

        assertEquals(state.getDeck().toList().size(), 50 - 16 + 1);

        Card[] startingCards = new Card[4];
        int[] countOfStartingCardsInDeck = new int[4];
        List<Card> cardsInDeck = state.getDeck().toList();
        for (int i = 0; i < 4; i++) cardsInDeck.add(state.getCardAt(2, i));
        for (int i = 0; i < 4; i++) {
            startingCards[i] = state.getCardAt(2, i);
            final int count = i;
            countOfStartingCardsInDeck[i] = (int) cardsInDeck.stream().filter(c -> c.equals(startingCards[count])).count();
        }

        hd.determiniseHandFor(2, state);

        // should be no change to deck size
        assertEquals(state.getDeck().toList().size(), 50 - 16 + 1);

        // and hand has been shuffled into deck
        cardsInDeck = state.getDeck().toList();
        for (int i = 0; i < 4; i++) cardsInDeck.add(state.getCardAt(2, i));
        for (int i = 0; i < 4; i++) {
            assertTrue(state.getCardAt(2, i) != startingCards[i]);
            assertTrue(cardsInDeck.contains(startingCards[i]));
            final int count = i;
            int cardCount = (int) cardsInDeck.stream().filter(c -> c.equals(startingCards[count])).count();
            assertEquals(cardCount, countOfStartingCardsInDeck[i]);
        }

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

        assertEquals(state.getDeck().toList().size(), 50 - 16 + 1);
        hd = new HandDeterminiser(state, 0);

        // now track the contents of P3 + deck for comparison after reset of P2
        Card[] startingCards = new Card[4];
        int[] countOfStartingCardsInDeck = new int[4];
        List<Card> cardsInDeck = state.getDeck().toList();
        for (int i = 0; i < 4; i++) cardsInDeck.add(state.getCardAt(3, i));
        for (int i = 0; i < 4; i++) startingCards[i] = state.getCardAt(2, i);
        Map<Card, Long> cardCountBefore = cardsInDeck.stream().collect(Collectors.groupingBy(c -> c, Collectors.counting()));

        hd.determiniseHandFor(2, state);

        // should be no change to deck size
        assertEquals(state.getDeck().toList().size(), 50 - 16 + 1);

        assertTrue(state.getCardAt(2, 3).colour == CardColour.GREEN);
        assertEquals(state.getCardAt(2, 3).value.intValue(), 5);

        hd.recordAction(new TellValue(0, state.getCardAt(0, 0).value), 2, state);

        hd.determiniseHandFor(3, state);

        for (int i = 0; i < 4; i++) {
            assertTrue(state.getCardAt(2, i) == startingCards[i]);
        }
        assertEquals(state.getDeck().toList().size(), 50 - 16 + 1);
        // and then check that the count of cards between deck and P3 new hand has not changed
        cardsInDeck = state.getDeck().toList();
        for (int i = 0; i < 4; i++) cardsInDeck.add(state.getCardAt(3, i));
        Map<Card, Long> cardCountAfter = cardsInDeck.stream().collect(Collectors.groupingBy(c -> c, Collectors.counting()));
        assertEquals(cardCountBefore.size(), cardCountAfter.size());
        for (Card key : cardCountBefore.keySet()) {
            assertEquals(cardCountBefore.get(key), cardCountAfter.get(key));
        }
    }

    @Test
    public void ifACardIsPlayedThenNewOneIsDrawn() {
        hd = new HandDeterminiser(state, 0);

        Card[] startingCards = new Card[4];
        for (int i = 0; i < 4; i++) {
            startingCards[i] = state.getCardAt(1, i);
        }
        assertEquals(state.getDeck().toList().size(), 34);
        assertEquals(state.getDiscards().size(), 0);

        hd.determiniseHandFor(1, state);

        Action playCard = new PlayCard(2);
        Card cardPlayed = state.getCardAt(1, 2);
        hd.recordAction(playCard, 1, state);
        playCard.apply(1, state);
        Card drawnCard = state.getCardAt(1, 2);
        assertFalse(drawnCard == startingCards[2]);

        hd.determiniseHandFor(2, state);
        assertEquals(state.getDeck().toList().size(), 33);
        if (cardPlayed.value == 1) {
            assertEquals(state.getDiscards().size(), 0);
            assertEquals(state.getTableValue(cardPlayed.colour), 1);
        } else {
            assertEquals(state.getDiscards().size(), 1);
            assertEquals(state.getTableValue(cardPlayed.colour), 0);
        }
        for (int i = 0; i < 4; i++) {
            if (i == 2) {
                assertFalse(state.getCardAt(1, i) == startingCards[i]);
            } else {
                assertTrue(state.getCardAt(1, i) == startingCards[i]);
            }
        }
    }

    @Test
    public void ifACardIsDiscardedThenTheNewOneIsDrawn() {
        hd = new HandDeterminiser(state, 0);

        Card[] startingCards = new Card[4];
        for (int i = 0; i < 4; i++) {
            startingCards[i] = state.getCardAt(1, i);
        }
        assertEquals(state.getDeck().toList().size(), 34);
        assertEquals(state.getDiscards().size(), 0);

        hd.determiniseHandFor(1, state);
        state.setInformation(7);

        Action discardCard = new DiscardCard(2);
        hd.recordAction(discardCard, 1, state);
        discardCard.apply(1, state);
        Card drawnCard = state.getCardAt(1, 2);
        assertFalse(drawnCard == startingCards[2]);

        hd.determiniseHandFor(2, state);

        assertEquals(state.getDeck().toList().size(), 33);
        assertEquals(state.getDiscards().size(), 1);
        for (int i = 0; i < 4; i++) {
            if (i == 2) {
                assertFalse(state.getCardAt(1, i) == startingCards[i]);
            } else {
                assertTrue(state.getCardAt(1, i) == startingCards[i]);
            }
        }
    }

    @Test
    public void ifDeterminisedCardIsPlayedThenWeReplaceItWithANewOneAndItStaysPlayed() {
        hd = new HandDeterminiser(state, 0);

        Card[] startingCards = new Card[4];
        for (int i = 0; i < 4; i++) {
            startingCards[i] = state.getCardAt(1, i);
        }

        List<Card> cardsInDeck = state.getDeck().toList();
        for (int i = 0; i < 4; i++) {
            // add in both P1 and P2, as with re-D of P1, any one of their current cards could be re-D'd to slot 2 and discarded
            cardsInDeck.add(state.getCardAt(1, i));
            cardsInDeck.add(state.getCardAt(2, i));
        }
        // cardsInDeck now consists of the deck, plus P2's original hand (before re-determinisation)
        Map<Card, Long> cardCountBefore = cardsInDeck.stream().collect(Collectors.groupingBy(c -> c, Collectors.counting()));
        assertEquals(cardsInDeck.size(), 42);

        hd.determiniseHandFor(1, state);
        state.setInformation(7);

        assertEquals(state.getDeck().toList().size(), 34);

        Card discardedCard = state.getCardAt(1, 2);
        Action discardCard = new DiscardCard(2);
        hd.recordAction(discardCard, 1, state);
        discardCard.apply(1, state);

        Card drawnCard = state.getCardAt(1, 2);
        assertFalse(drawnCard == discardedCard);

        assertEquals(state.getDeck().toList().size(), 33); // 1 card dioscarded
        hd.determiniseHandFor(2, state);

        for (int i = 0; i < 4; i++) {
            if (i == 2) {
                assertFalse(state.getCardAt(1, i) == startingCards[i]);
            } else {
                assertTrue(state.getCardAt(1, i) == startingCards[i]);
            }
        }
        assertTrue(state.getDiscards().contains(discardedCard));
        assertEquals(state.getDeck().toList().size(), 33); // 1 card discarded still
        cardsInDeck = state.getDeck().toList();
        for (int i = 0; i < 4; i++) {
            cardsInDeck.add(state.getCardAt(1, i));
            cardsInDeck.add(state.getCardAt(2, i));
        }
        cardsInDeck.add(discardedCard);  // the actual card discarded by P1 was either in original deck, or P1
        // cardsInDeck now consists of the deck, the actual card discarded by P1, plus P2's hand (just re-determinised)
        Map<Card, Long> cardCountAfter = cardsInDeck.stream().collect(Collectors.groupingBy(c -> c, Collectors.counting()));
        assertEquals(cardsInDeck.size(), 42); // we have one additional card, being the card in Slot 2 for P1 before re-determinisation
        assertEquals(cardCountBefore.size(), cardCountAfter.size());
        for (Card key : cardCountBefore.keySet()) {
            assertEquals(cardCountBefore.get(key), cardCountAfter.get(key));
        }
    }

    @Test
    public void ifDeterminisedCardIsPlayedAndThatCardIsElsewhereInOriginalHandThenWeRedrawThatSlotToo() {
        Card problemCard = new Card(6, CardColour.RED);
        state.setCardAt(1, 3, problemCard);
            // we know this is unique, and over-writes a random other card
        assertEquals(state.getDeck().toList().size(), 34);

        hd = new HandDeterminiser(state, 0);
        assertEquals(state.getDeck().toList().size(), 34);
        Card[] startingCards = new Card[4];
        for (int i = 0; i < 4; i++) {
            startingCards[i] = state.getCardAt(1, i);
        }

        hd.determiniseHandFor(1, state);
        state.setInformation(7);
        // we now hack the determinisation, so that RED 6 is actually in slot 2
        for (int slot = 0; slot < 4; slot++) {
            if (slot == 2) continue;
            if (state.getCardAt(1, slot).equals(problemCard)) {
                state.setCardAt(1, slot, state.getCardAt(1, 2));
                state.setCardAt(1, 2, problemCard);
            }
        }
        if (!state.getCardAt(1, 2).equals(problemCard)) {
            state.getDeck().add(state.getCardAt(1, 2));
            state.setCardAt(1, 2, problemCard);
            state.getDeck().remove(problemCard);
        }

        assertEquals(state.getDeck().toList().size(), 34);

        Action discardCard = new DiscardCard(2);
        hd.recordAction(discardCard, 1, state);
        discardCard.apply(1, state);
        Card drawnCard = state.getCardAt(1, 2);
        assertFalse(drawnCard == problemCard);

        assertEquals(state.getDeck().toList().size(), 33); // 1 card discarded
        hd.determiniseHandFor(2, state);

        assertEquals(state.getDeck().toList().size(), 33); // 1 card discarded, still

        for (int i = 0; i < 4; i++) {
            if (i == 2 || i == 3) {
                assertFalse(state.getCardAt(1, i) == startingCards[i]);
            } else {
                assertTrue(state.getCardAt(1, i) == startingCards[i]);
            }
        }
    }
}

