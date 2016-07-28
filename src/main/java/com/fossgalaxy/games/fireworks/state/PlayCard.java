package com.fossgalaxy.games.fireworks.state;

import com.fossgalaxy.games.fireworks.TextProtocol;
import com.fossgalaxy.games.fireworks.engine.CardColour;

public class PlayCard implements Action {
	public final int slot;
	
	public PlayCard(int slot) {
		this.slot = slot;
	}
	
	@Override
	public String toProtocol() {
		return String.format("%s %d", TextProtocol.ACTION_PLAY, slot);
	}
	
}
