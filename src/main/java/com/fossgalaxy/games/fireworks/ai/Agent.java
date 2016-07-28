package com.fossgalaxy.games.fireworks.ai;

import com.fossgalaxy.games.fireworks.state.Action;
import com.fossgalaxy.games.fireworks.state.GameState;

public interface Agent {

	Action doMove(GameState state);
	
}
