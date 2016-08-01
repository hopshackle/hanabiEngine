package com.fossgalaxy.games.fireworks.ai.rule;

import com.fossgalaxy.games.fireworks.state.Action;
import com.fossgalaxy.games.fireworks.state.GameState;

public interface Rule {
	
	public Action execute(int playerID, GameState state);

}
