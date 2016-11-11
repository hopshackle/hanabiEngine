package com.fossgalaxy.games.fireworks.ai;

import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.actions.Action;
import com.fossgalaxy.games.fireworks.state.events.GameEvent;

public interface Agent {

    Action doMove(int agentID, GameState state);
    default void receiveEvent(GameEvent event){

    }

    default void receiveID(int agentID){

    }
}
