package com.fossgalaxy.games.fireworks.ai.rule;

import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.actions.Action;

public interface Rule {

	public boolean canFire(int playerID, GameState state);
	public Action execute(int playerID, GameState state);

}
