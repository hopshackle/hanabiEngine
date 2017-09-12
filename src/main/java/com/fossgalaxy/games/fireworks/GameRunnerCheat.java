package com.fossgalaxy.games.fireworks;

import com.fossgalaxy.games.fireworks.state.events.CardDrawn;
import com.fossgalaxy.games.fireworks.state.events.CheatEvent;
import com.fossgalaxy.games.fireworks.state.events.GameEvent;

/**
 * Game runner that allows the agents to know what cards are in their hands.
 */
public class GameRunnerCheat extends GameRunner {

    public GameRunnerCheat(String gameID, int expectedPlayers) {
        super(gameID, expectedPlayers);
    }

    /**
     * When a card is drawn, all agents get told what the card was.
     *
     * This allows the agents to know exactly what cards are in their own hand and tells the other agents about it.
     * This is implemented as the equivalent of a free tell action.
     *
     * @param event the event to send to the agents.
     */
    @Override
    protected void send(GameEvent event) {
        for (int i = 0; i < players.length; i++) {
            if (event.isVisibleTo(i)) {
                players[i].sendMessage(event);
            }

            if (event instanceof CardDrawn) {
                send(new CheatEvent(i, state.getHand(i)));
            }
        }
    }

}
