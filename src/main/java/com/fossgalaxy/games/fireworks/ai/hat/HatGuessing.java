package com.fossgalaxy.games.fireworks.ai.hat;

import com.fossgalaxy.games.fireworks.ai.Agent;
import com.fossgalaxy.games.fireworks.ai.rule.DiscardOldestFirst;
import com.fossgalaxy.games.fireworks.ai.rule.Rule;
import com.fossgalaxy.games.fireworks.state.Card;
import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.TimedHand;
import com.fossgalaxy.games.fireworks.state.actions.Action;
import com.fossgalaxy.games.fireworks.state.events.CardInfoColour;
import com.fossgalaxy.games.fireworks.state.events.CardInfoValue;
import com.fossgalaxy.games.fireworks.state.events.GameEvent;

import java.util.Arrays;

/**
 * Created by piers on 11/11/16.
 */
public class HatGuessing implements Agent {
    private static final int NOT_FOUND = 99;
    private static final int[] copies = {0,3,2,2,2,1};

    private int playerID;

    private int lastToldAction;
    private int cardsPlayedSinceHint = 0;

    private Rule discardOldest;

    public HatGuessing() {
        this.discardOldest = new DiscardOldestFirst();
    }

    @Override
    public Action doMove(int agentID, GameState state) {
        Recommendation[] recommendations = Recommendation.values();

        // 1. If the most recent recommendation was to play a card and no card has been played since the lat hint, player the recommended card
        if (lastToldAction > 4) {
            if (cardsPlayedSinceHint == 0 || state.getLives() != 1) {
                return recommendations[lastToldAction].recommended;
            }
        }

        // 2. If the most recent recommendation was to play a card, one card has been played
        //    since the hint was given, and the players have made fewer than two errors, play the
        //    recommended card

        // 3. If the players have a hint token, give a hint.
        if (state.getInfomation() > 0) {
            Recommendation myIdea = doRecommend(agentID, state);
            return Recommendation.encode(myIdea, agentID, state);
        }

        // 4. If the most recent recommendation was to discard a card, discard the requested card.
        if (lastToldAction <= 4) {
            return recommendations[lastToldAction].recommended;
        }

        // 5. Discard card c 1 (Oldest Card)
        if (discardOldest.canFire(agentID, state)) {
            return discardOldest.execute(agentID, state);
        }

        //ok, what happens now? :S
        return null;
    }

    public boolean isDead(GameState state, Card card) {
	return state.getTableValue(card.colour) >= card.value; 
    }

    public boolean isPlayable(GameState state, Card card) {
	return state.getTableValue(card.colour) == card.value - 1;
    }

    public boolean isIndispensable(GameState state, Card card) {
        long copiesInDeck = state.getDeck().toList().stream().filter(card::equals).count();

        //if there is at least 1 copy in the deck we're fine
        if (copiesInDeck > 0) {
            return false;
        }

        int totalCopies = copies[card.value];
        //TODO figure out how many have been played already.
        // ok, that might be quite difficault if they are already in place
        return false;
    }


    /**
     * Playable: a card that can be successfully played with the current game state.
     * <p>
     * Dead: a card that has the same rank and suit of a successfully played card.
     * <p>
     * Indispensable: a card for which all other identical copies have been removed from
     * the game, i.e. a card that if removed from the game will imply a perfect score cannot
     * be obtained.
     *
     * @param agentID
     * @param state
     * @return
     */
    public Recommendation doRecommend(int agentID, GameState state) {
        int agentToTell = (agentID + 1) % state.getPlayerCount();
        TimedHand hand = (TimedHand)state.getHand(agentToTell);

        //order the hand fro oldest to newest
        //whenever the rules talk about "lowest index" they mean oldest due to
        //the way this paper manages the hands.
        Integer[] handOrder = new Integer[]{1,2,3,4,5};
        Arrays.sort(handOrder, (c1, c2) -> Integer.compare(hand.getAge(c1), hand.getAge(c2)));


        //track the stuff we'll need for the rules
        int lowestPlayable = NOT_FOUND;
        int lowestValue = 99;
        int lowestDead = NOT_FOUND;

        int highestDispensible = NOT_FOUND;
        int highestDispensibleValue = -1;

        //because of the order of iteration, we can ignore
        //cards with the same value (ties), as the correct card will have
        //already been selected.
        for (int slot : handOrder) {
            Card card = hand.getCard(slot);

            //guard against the card being missing (end of the game)
            if (card == null) {
                continue;
            }

            // 1. Recommend that the playable card of rank 5 with lowest index be played.
            // we can shortcut everything else, we know we're going to do this.
            if (card.value == 5 && isPlayable(state, card)) {
                return Recommendation.playSlot(slot);
            }

            //oldest playable
            if (card.value < lowestValue && isPlayable(state, card)) {
                lowestPlayable = slot;
                lowestValue = card.value;
            }

            //oldest dead
            if (lowestDead == NOT_FOUND && isDead(state, card)) {
                lowestDead = slot;
            }

            //highest rank dispensible (not indispensable)
            if (card.value > highestDispensibleValue && !isIndispensable(state, card)){
                highestDispensible = slot;
                highestDispensibleValue = card.value;
            }
        }

        // 2. Recommend that the playable card with lowest rank (value) be played. If there is a tie for
        //    lowest rank, recommend the one with lowest index.
        if (lowestPlayable != NOT_FOUND) {
            return Recommendation.playSlot(lowestPlayable);
        }

        // 3. Recommend that the dead card with lowest index (oldest) be discarded.
        if (lowestDead != NOT_FOUND) {
            return Recommendation.discardSlot(lowestDead);
        }

        // 4. Recommend that the card with highest rank (value) that is not indispensable be discarded.
        //    If there is a tie, recommend the one with lowest index.
        if (highestDispensible != NOT_FOUND) {
            return Recommendation.discardSlot(highestDispensible);
        }

        // 5. Recommend that oldest (c1) be Discarded
        return Recommendation.discardSlot(handOrder[0]);
    }


    @Override
    public void receiveID(int agentID) {
        playerID = agentID;
    }

    @Override
    public void receiveEvent(GameEvent event) {
        switch (event.getEvent()) {
            case CARD_INFO_COLOUR:
                CardInfoColour tellColour = (CardInfoColour) event;
                lastToldAction = 4 + tellColour.getPlayerId() + (tellColour.getPlayerId() < playerID ? -1 : 1);
                cardsPlayedSinceHint = 0;
                break;
            case CARD_INFO_VALUE:
                CardInfoValue tellValue = (CardInfoValue) event;
                lastToldAction = tellValue.getPlayerId() + (tellValue.getPlayerId() < playerID ? -1 : 1);
                cardsPlayedSinceHint = 0;
                break;
            case CARD_PLAYED:
                cardsPlayedSinceHint++;
        }
    }
}
