package com.fossgalaxy.games.fireworks;

import com.fossgalaxy.games.fireworks.state.actions.Action;
import com.fossgalaxy.games.fireworks.state.events.GameEvent;

public interface Player {

	Action getAction();

	void sendMessage(GameEvent msg);

}