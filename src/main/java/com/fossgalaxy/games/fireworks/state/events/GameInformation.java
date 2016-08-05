package com.fossgalaxy.games.fireworks.state.events;

import com.fossgalaxy.games.fireworks.TextProtocol;
import com.fossgalaxy.games.fireworks.state.GameState;

public class GameInformation extends GameEvent {
	public final int players;
	public final int handSize;
	public final int infoTokens;
	public final int lives;

	public GameInformation(int players, int handSize, int infoTokens, int lives) {
		super(MessageType.GAME_INFO);
		this.players = players;
		this.handSize = handSize;
		this.infoTokens = infoTokens;
		this.lives = lives;
	}

	@Override
	public void apply(GameState state) {
		// TODO handle this nicely
	}

	@Override
	public String toTextProtocol() {
		return TextProtocol.buildMessage(0, TextProtocol.EFFECT_GAMEINFO,
				players + " " + handSize + " " + infoTokens + " " + lives);
	}

}