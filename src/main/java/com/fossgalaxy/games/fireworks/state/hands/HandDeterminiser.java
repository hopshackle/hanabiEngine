package com.fossgalaxy.games.fireworks.state.hands;

import com.fossgalaxy.games.fireworks.ai.rule.logic.DeckUtils;
import com.fossgalaxy.games.fireworks.state.*;
import com.fossgalaxy.games.fireworks.state.actions.*;

import java.util.*;
import java.util.stream.IntStream;

public class HandDeterminiser {

    private int slotLastUsed;
    private List<List<Card>> handRecord;
    private int playerCount, rootAgent;

    public HandDeterminiser(GameState state, int rootID) {
        playerCount = state.getPlayerCount();
        rootAgent = rootID;
        slotLastUsed = -1;
        handRecord = new ArrayList<>(playerCount);
        for (int i = 0; i < playerCount; i++) {
            handRecord.add(new ArrayList<>());
        }
        // we then do our one-off determinisation of the root players cards
        bindNewCards(rootID, state);
    }

    public void determiniseHandFor(int agentID, GameState state) {
        Hand myHand = state.getHand(agentID);
        Deck deck = state.getDeck();

        // record cards currently in hand for agent, so we can go back to these after their turn
        List<Card> handDetail = new ArrayList<>(state.getHandSize());
        handRecord.set(agentID, handDetail);
        Hand currentHand = state.getHand(agentID);
        for (int slot = 0; slot < state.getHandSize(); slot++) {
            handDetail.add(currentHand.getCard(slot));
        }

        // reset the hand of the previous agent (if not root) to known values where possible
        reset(agentID, state);

        // put current hand back into deck (except for the root agent)
        // and then bind new values
        if (agentID != rootAgent) {
            for (int slot = 0; slot < myHand.getSize(); slot++) {
                Card card = myHand.getCard(slot);
                if (card != null) deck.add(card);
            }

            // we then bind new cards (same for root and !root)
            bindNewCards(agentID, state);
        }
        deck.shuffle();
    }

    public void reset(int agentID, GameState state) {
        // the aim here is to reset all cards to their known versions before rollout
        // reset the hand of the previous agent (if not root) to known values where possible
        int previousAgent = (agentID + state.getPlayerCount() - 1) % state.getPlayerCount();
        Hand previousHand = state.getHand(previousAgent);
        Deck deck = state.getDeck();
        if (previousAgent != rootAgent) {
            List<Card> previousCards = handRecord.get(previousAgent);
            if (!previousCards.isEmpty()) {
                for (int slot = 0; slot < previousHand.getSize(); slot++) {
                    if (slot == slotLastUsed) {
                        // in this case we keep the current card, as the old one has been played or discarded
                        continue;
                    }
                    previousHand.bindCard(slot, previousCards.get(slot));   // re-bind card
                    deck.add(previousCards.get(slot));  // and remove it from the deck
                }
            }
        }
    }

    private void bindNewCards(int agentID, GameState state) {
        Hand myHand = state.getHand(agentID);
        Deck deck = state.getDeck();
        List<Card> toChooseFrom = state.getDeck().toList();
            IntStream.range(0, myHand.getSize()).mapToObj(myHand::getCard).filter(Objects::nonNull).forEach(toChooseFrom::add);

        Map<Integer, List<Card>> possibleCards = DeckUtils.bindBlindCard(agentID, state.getHand(agentID), toChooseFrom);
        List<Integer> bindOrder = DeckUtils.bindOrder(possibleCards);
        Map<Integer, Card> myHandCards = DeckUtils.bindCards(bindOrder, possibleCards);
        for (int slot = 0; slot < myHand.getSize(); slot++) {
            Card hand = myHandCards.get(slot);
            myHand.bindCard(slot, hand);
            deck.remove(hand);
        }
    }

    public void recordAction(Action action) {
        slotLastUsed = -1;
        if (action instanceof PlayCard) slotLastUsed = ((PlayCard) action).slot;
        if (action instanceof DiscardCard) slotLastUsed = ((DiscardCard) action).slot;
    }

    public Card getHandRecord(int agent, int slot) {
        if (handRecord.get(agent).size() <= slot) return null;
        return handRecord.get(agent).get(slot);
    }
    public int getSlotLastUsed() {
        return slotLastUsed;
    }
}
