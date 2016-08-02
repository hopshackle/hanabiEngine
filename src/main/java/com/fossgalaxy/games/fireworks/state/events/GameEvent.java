package com.fossgalaxy.games.fireworks.state.events;

import java.io.Serializable;

import com.fossgalaxy.games.fireworks.state.GameState;

public abstract class GameEvent implements Serializable {
	private final MessageType id;

	public GameEvent(MessageType id) {
		this.id = id;
	}

	public abstract void apply(GameState state);

	public MessageType getEvent() {
		return id;
	}

	public boolean isVisibleTo(int playerID) {
		return true;
	}

	public abstract String toTextProtocol();

}
