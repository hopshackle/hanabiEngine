package com.fossgalaxy.games.fireworks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.fossgalaxy.games.fireworks.players.Player;
import com.fossgalaxy.games.fireworks.state.BasicState;
import com.fossgalaxy.games.fireworks.state.Card;
import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.Hand;
import com.fossgalaxy.games.fireworks.state.RulesViolation;
import com.fossgalaxy.games.fireworks.state.actions.Action;
import com.fossgalaxy.games.fireworks.state.events.CardDrawn;
import com.fossgalaxy.games.fireworks.state.events.GameEvent;

public class GameRunner {
	private static final int[] HAND_SIZE = { -1, -1, 5, 5, 4, 4 };
	
	private final Player[] players;
	private final GameState state;
	private final List<GameEvent> eventLog;
	
	private int nPlayers;
	private int moves;
	
	private int nextPlayer;
	
	public GameRunner(int expectedPlayers) {
		assert expectedPlayers < 2 : "too few players";
		assert expectedPlayers > HAND_SIZE.length : "too many players";
		
		this.players = new Player[expectedPlayers];
		this.state = new BasicState(HAND_SIZE[expectedPlayers], expectedPlayers);
		this.nPlayers = 0;
		this.eventLog = new ArrayList<GameEvent>();
		
		this.nextPlayer = 0;
		this.moves = 0;
	}
	
	public void addPlayer(Player player) {
		players[nPlayers++] = player;
	}

	public void init() {
		eventLog.clear();
		state.init();
		
		//tell players about their hands
		for (int player = 0; player<players.length; player++) {
			Hand hand = state.getHand(player);

			for (int slot = 0; slot < hand.getSize(); slot++) {
				Card cardInSlot = hand.getCard(slot);
				send(new CardDrawn(player, slot, cardInSlot.colour, cardInSlot.value));
			}
		}
	}
	
	//TODO time limit the agent
	public void nextMove() {
		Player player = players[nextPlayer];
		assert player != null : "that player is not valid";
		
		//get the action and try to apply it
		Action action = player.getAction();
		if (!action.isLegal(nextPlayer, state)) {
			//TODO deal with the user giving us invalid answers
			throw new RulesViolation(action);
		}
		
		//perform the action and get the effects
		moves++;
		Collection<GameEvent> events = action.apply(nextPlayer, state);
		for (GameEvent event : events) {
			send(event);
		}
		
		//make sure it's the next player's turn
		nextPlayer = (nextPlayer + 1) % players.length;
	}
	
	public GameStats playGame() {
		assert nPlayers == players.length;
		init();
		
		int strikes = 3;
		while (!state.isGameOver()) {
			try {
				nextMove();
			} catch (RulesViolation rv) {
				
				//House rule: mess up 3 times and you lose a life (and your go)
				if (strikes == 0) {
					state.setLives(state.getLives()-1);
					nextPlayer = (nextPlayer + 1) % players.length;
					
					System.err.println("player "+nextPlayer+" got 3 strikes - lose a life");
					continue;
				}
				
				//decrement strikes and last player gets another go
				strikes--;
				//System.err.println("user broke rules by prompting again "+rv);
			}
			state.tick();
		}
		
		GameStats stats = new GameStats();
		stats.nPlayers = players.length;
		stats.moves = moves;
		stats.lives = state.getLives();
		stats.infomation = state.getInfomation();
		stats.score = state.getScore();
		
		return stats;
	}
	
	//send messages as soon as they are available
	private void send(GameEvent event) {
		eventLog.add(event);
		for (int i=0; i<players.length; i++) {
			if (event.isVisibleTo(i)) {
				players[i].sendMessage(event);
			}
		}
	}
	
}
