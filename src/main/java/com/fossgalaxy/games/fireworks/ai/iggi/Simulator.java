package com.fossgalaxy.games.fireworks.ai.iggi;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.fossgalaxy.games.fireworks.state.BasicState;
import com.fossgalaxy.games.fireworks.state.Deck;
import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.Hand;
import com.fossgalaxy.games.fireworks.state.actions.Action;

public class Simulator {

	
	public static void main(String[] args) {
		GameState state = new BasicState(3,5);
		state.init();
		
		Deck deck = state.getDeck();
		//deck.init();
		
		Simulator s = new Simulator();
		double score = s.simulate(0, state);
		System.out.println(score);
	}
	
	public double simulate(int startingPlayer, GameState state) {
		
		//shuffle the cards currently in the deck
		Deck deck = state.getDeck();
		deck.shuffle();
		
		//TODO assign 5 compatible cards to the hand.
		
		int player = startingPlayer;
		while (!state.isGameOver()) {
			Action action = selectAction(player, state);
			System.out.println(action);
			action.apply(player, state);
			
			player = (player + 1) % state.getPlayerCount();
		}
		
		return state.getScore();
	}
	
	public Action selectAction(int player, GameState state) {
		List<Action> actions = new ArrayList<>(Utils.generateActions(player, state));
		
		Random r = new Random();
		return actions.get(r.nextInt(actions.size()));
	}
	
}
