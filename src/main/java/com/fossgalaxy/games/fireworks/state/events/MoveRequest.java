package com.fossgalaxy.games.fireworks.state.events;

import com.fossgalaxy.games.fireworks.state.GameState;

public class MoveRequest extends GameEvent {

	public MoveRequest() {
		super(MessageType.MOVE_REQUEST);
	}

	@Override
	public void apply(GameState state) {
		// TODO Auto-generated method stub

	}

	@Override
	public String toTextProtocol() {
		// TODO Auto-generated method stub
		return null;
	}

}
