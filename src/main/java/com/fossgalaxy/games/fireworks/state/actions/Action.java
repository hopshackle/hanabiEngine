package com.fossgalaxy.games.fireworks.state.actions;

import java.util.List;

import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.events.GameEvent;

public interface Action {

	List<GameEvent> apply(int playerID, GameState state);

	boolean isLegal(GameState state);

	String toProtocol();

}
