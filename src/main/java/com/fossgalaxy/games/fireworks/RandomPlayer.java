package com.fossgalaxy.games.fireworks;

import java.util.Random;

//TODO I need to know hand size, number of players and my playerID
public class RandomPlayer implements Player {
	private Random random;
	
	public RandomPlayer() {
		this.random = new Random();
	}
	
	@Override
	public void sendMessage(String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getAction() {
		int n = random.nextInt(2);
		int slot = random.nextInt(5);
		
		if (n == 0) {
			return "PLAY "+slot;
		} else if (n == 1) {
			return "DISCARD "+slot;
		}
		
		return "PLAY 0";
	}

}
