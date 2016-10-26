package com.fossgalaxy.games.fireworks.ai;

import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.actions.Action;

public interface Agent {

    Action doMove(int agentID, GameState state);

}
