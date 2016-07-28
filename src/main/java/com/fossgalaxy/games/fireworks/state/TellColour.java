package com.fossgalaxy.games.fireworks.state;

import com.fossgalaxy.games.fireworks.TextProtocol;
import com.fossgalaxy.games.fireworks.engine.CardColour;

public class TellColour implements Action {
	public final int player;
	public final CardColour colour;
	
	public TellColour(int player, CardColour colour) {
		this.player = player;
		this.colour = colour;
	}
	
	@Override
	public String toProtocol() {
		return String.format("%s %d %s", TextProtocol.ACTION_TELL_COLOUR, player, colour);
	}
	
}
