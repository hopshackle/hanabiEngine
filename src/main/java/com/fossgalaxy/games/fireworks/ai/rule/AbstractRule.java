package com.fossgalaxy.games.fireworks.ai.rule;

import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.actions.Action;

public abstract class AbstractRule implements Rule {

	public int selectPlayer(int playerID, GameState state) {
		return (playerID + 1) % state.getPlayerCount();
	}
	
	@Override
	public boolean canFire(int playerID, GameState state) {
		Action action = execute(playerID, state);
		return action != null;
	}
	

}
