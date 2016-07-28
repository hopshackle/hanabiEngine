package com.fossgalaxy.games.fireworks.state;

import com.fossgalaxy.games.fireworks.TextProtocol;
import com.fossgalaxy.games.fireworks.engine.CardColour;

public class TellValue implements Action {
	public final int player;
	public final int value;
	
	public TellValue(int player, int value) {
		this.player = player;
		this.value = value;
	}
	
	@Override
	public String toProtocol() {
		return String.format("%s %d %d", TextProtocol.ACTION_TELL_VALUE, player, value);
	}
	
}
