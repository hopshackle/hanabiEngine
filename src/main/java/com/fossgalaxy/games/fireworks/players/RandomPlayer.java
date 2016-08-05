package com.fossgalaxy.games.fireworks.players;

import java.util.Random;

import com.fossgalaxy.games.fireworks.TextProtocol;
import com.fossgalaxy.games.fireworks.state.actions.Action;
import com.fossgalaxy.games.fireworks.state.events.GameEvent;

//TODO I need to know hand size, number of players and my playerID
public class RandomPlayer implements Player {
	private Random random;

	public RandomPlayer() {
		this.random = new Random();
	}

	@Override
	public Action getAction() {
		return TextProtocol.stringToAction(getTextAction());
	}

	public String getTextAction() {
		int n = random.nextInt(2);
		int slot = random.nextInt(5);

		if (n == 0) {
			return "PLAY " + slot;
		} else if (n == 1) {
			return "DISCARD " + slot;
		}

		return "PLAY 0";
	}

	@Override
	public void sendMessage(GameEvent msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setID(int id, int nPlayers) {
		// TODO Auto-generated method stub
		
	}

}
