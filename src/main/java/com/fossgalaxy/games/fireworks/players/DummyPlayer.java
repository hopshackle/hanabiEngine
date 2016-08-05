package com.fossgalaxy.games.fireworks.players;

import com.fossgalaxy.games.fireworks.state.actions.Action;
import com.fossgalaxy.games.fireworks.state.actions.PlayCard;
import com.fossgalaxy.games.fireworks.state.events.GameEvent;

public class DummyPlayer implements Player {

	@Override
	public Action getAction() {
		return new PlayCard(1);
	}

	@Override
	public void sendMessage(GameEvent msg) {
		// TODO Auto-generated method stub

	}

}
