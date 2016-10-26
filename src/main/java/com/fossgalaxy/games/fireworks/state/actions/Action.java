package com.fossgalaxy.games.fireworks.state.actions;

import java.io.Serializable;
import java.util.List;

import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.events.GameEvent;

public interface Action extends Serializable {

	List<GameEvent> apply(int playerID, GameState state);

	boolean isLegal(int playerID, GameState state);

	String toProtocol();

}
