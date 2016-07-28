package com.fossgalaxy.games.fireworks.ai;

import java.util.Random;

import com.fossgalaxy.games.fireworks.engine.CardColour;
import com.fossgalaxy.games.fireworks.state.Action;
import com.fossgalaxy.games.fireworks.state.DiscardCard;
import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.PlayCard;
import com.fossgalaxy.games.fireworks.state.TellColour;
import com.fossgalaxy.games.fireworks.state.TellValue;

/**
 * Make a random (possibly illegal) move.
 */
public class RandomAgent implements Agent {
	private Random random;
	
	public RandomAgent() {
		this.random = new Random();
	}

	@Override
	public Action doMove(GameState state) {
		int moveType = random.nextInt(4);
		
		if (moveType == 0) {
			return new PlayCard(random.nextInt(state.getHandSize()));
		} else if (moveType == 1) {
			return new DiscardCard(random.nextInt(state.getHandSize()));
		} else if (moveType == 2) {
			return new TellValue(random.nextInt(state.getPlayerCount()), random.nextInt(5));
		} else {
			CardColour[] possible = CardColour.values();
			int selected = random.nextInt(possible.length);
			return new TellColour(random.nextInt(state.getPlayerCount()), possible[selected]);
		}
		
	}

}
