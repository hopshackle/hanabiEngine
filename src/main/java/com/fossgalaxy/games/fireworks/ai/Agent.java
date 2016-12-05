package com.fossgalaxy.games.fireworks.ai;

import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.actions.Action;
import com.fossgalaxy.games.fireworks.state.events.GameEvent;

public interface Agent {

    /**
     * Standardised interface for game playing agents.
     * <p>
     * The agent gets a copy of the game state and it's agent ID and should return a move.
     *
     * @param agentID the ID of this agent
     * @param state   the current state of the game
     * @return the move this agent would like to make
     */
    Action doMove(int agentID, GameState state);

    /**
     * optional hook to allow the agent to act on event data
     *
     * @param event the event that the agent received.
     */
    default void receiveEvent(GameEvent event) {

    }

    /**
     * optional hook for when the agent gets their ID.
     * <p>
     * This indicates that the game has started.
     *
     * @param agentID this agent's ID.
     */
    default void receiveID(int agentID) {

    }

    default void onState(GameState state) {

    }
}
