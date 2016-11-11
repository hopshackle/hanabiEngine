package com.fossgalaxy.games.fireworks.ai.hat;

import com.fossgalaxy.games.fireworks.ai.Agent;
import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.actions.Action;
import com.fossgalaxy.games.fireworks.state.actions.TellColour;
import com.fossgalaxy.games.fireworks.state.actions.TellValue;
import com.fossgalaxy.games.fireworks.state.events.CardInfoColour;
import com.fossgalaxy.games.fireworks.state.events.CardInfoValue;
import com.fossgalaxy.games.fireworks.state.events.GameEvent;
import com.fossgalaxy.games.fireworks.state.events.GameInformation;

/**
 * Created by piers on 11/11/16.
 */
public class HatGuessing implements Agent {

    private int playerID;
    private int lastToldAction;

    @Override
    public Action doMove(int agentID, GameState state) {

        // 1. If the most recent recommendation was to play a card and no card has been played since the lat hint, player the recommended card

        // 2. If the most recent recommendation was to play a card, one card has been played
        //    since the hint was given, and the players have made fewer than two errors, play the
        //    recommended card

        // 3. If the players have a hint token, give a hint.

        // 4. If the most recent recommendation was to discard a card, discard the requested card.

        // 5. Discard card c 1 (Oldest Card)
        return null;
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

    public Action doRecommend(int agentID, GameState state) {
        // 1. Recommend that the playable card of rank 5 with lowest index be played.
        // 2. Recommend that the playable card with lowest rank be played. If there is a tie for
        //    lowest rank, recommend the one with lowest index.

        // 3. Recommend that the dead card with lowest index be discarded.

        // 4. Recommend that the card with highest rank that is not indispensable be discarded.
        //    If there is a tie, recommend the one with lowest index.

        // 5. Recommend that C1 be Discarded

        return null;
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
                break;
            case CARD_INFO_VALUE:
                CardInfoValue tellValue = (CardInfoValue) event;
                lastToldAction = tellValue.getPlayerId() + (tellValue.getPlayerId() < playerID ? -1 : 1);
                break;

        }
    }
}
