package com.fossgalaxy.games.fireworks.state;

import com.fossgalaxy.games.fireworks.TextProtocol;
import com.fossgalaxy.games.fireworks.engine.CardColour;

public class DiscardCard implements Action {
	public final int slot;
	
	public DiscardCard(int slot) {
		this.slot = slot;
	}
	
	@Override
	public String toProtocol() {
		return String.format("%s %d", TextProtocol.ACTION_DISCARD, slot);
	}
	
}
