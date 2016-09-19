package com.fossgalaxy.games.fireworks;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.fossgalaxy.games.fireworks.players.Player;
import com.fossgalaxy.games.fireworks.state.BasicState;
import com.fossgalaxy.games.fireworks.state.Card;
import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.Hand;
import com.fossgalaxy.games.fireworks.state.RulesViolation;
import com.fossgalaxy.games.fireworks.state.actions.Action;
import com.fossgalaxy.games.fireworks.state.events.CardDrawn;
import com.fossgalaxy.games.fireworks.state.events.GameEvent;
import com.fossgalaxy.games.fireworks.state.events.GameInformation;

public class GameRunner {
	private static final int[] HAND_SIZE = { -1, -1, 5, 5, 4, 4 };
	
	private final Player[] players;
	private final GameState state;

	private final PrintStream gameOut;
	private final UUID gameID;

	private int nPlayers;
	private int moves;
	
	private int nextPlayer;
	
	public GameRunner(UUID gameID, int expectedPlayers, PrintStream gameOut) {
		assert expectedPlayers > 2 : "too few players";
		assert expectedPlayers < HAND_SIZE.length : "too many players";
		
		this.players = new Player[expectedPlayers];
		this.state = new BasicState(HAND_SIZE[expectedPlayers], expectedPlayers);
		this.nPlayers = 0;
		this.gameOut = gameOut;
		
		this.nextPlayer = 0;
		this.moves = 0;
		this.gameID = gameID;
	}
	
	public void addPlayer(Player player) {
		players[nPlayers++] = player;
	}

	public void init(Long seed) {
		state.init(seed);

		send(new GameInformation(nPlayers, HAND_SIZE[nPlayers], 8, 3));

		//tell players about their hands
		for (int player = 0; player<players.length; player++) {
			Hand hand = state.getHand(player);

			for (int slot = 0; slot < hand.getSize(); slot++) {
				Card cardInSlot = hand.getCard(slot);
				send(new CardDrawn(player, slot, cardInSlot.colour, cardInSlot.value));
			}
		}
	}

	private void writeMove(Action action) {
		if (gameOut == null) {
			return;
		}
		gameOut.println(String.format("MOVE,%s,%d,%d,\"%s\"", gameID, moves, nextPlayer, action));
	}

	private void writeEvent(GameEvent event) {
		if (gameOut == null) {
			return;
		}
		gameOut.println(String.format("EVENT,%s,\"%s\"", gameID, event));
	}

	private void writeState(GameState state) {
		if (gameOut == null) {
			return;
		}
		gameOut.println(String.format("STATE,%s,%d,%d,%d,%d,%d", gameID, moves, state.getLives(), state.getInfomation(), state.getScore(), state.getPlayerCount()));
	}

	//TODO time limit the agent
	public void nextMove() {
		Player player = players[nextPlayer];
		assert player != null : "that player is not valid";
		
		//get the action and try to apply it
		Action action = player.getAction();
		writeMove(action);
		if (!action.isLegal(nextPlayer, state)) {
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
	
	public GameStats playGame(Long seed) {
		assert nPlayers == players.length;
		init(seed);

		int disquals = 0;

		int strikes = 3;
		while (!state.isGameOver()) {
			try {
				writeState(state);
				nextMove();
				state.tick();
			} catch (RulesViolation rv) {

				//House rule: mess up 3 times and you end the game
				if (strikes == 0) {
					disquals++;
					System.err.println("player "+nextPlayer+" got 3 strikes - lose a life");
					break;
				}
				
				//decrement strikes and last player gets another go
				strikes--;
				//System.err.println("user broke rules by prompting again "+rv);
			}
		}

		return new GameStats(gameID, players.length, state.getScore(), state.getLives(), moves, state.getInfomation(), disquals);
	}
	
	//send messages as soon as they are available
	private void send(GameEvent event) {
		writeEvent(event);
		for (int i = 0; i < players.length; i++) {
			if (event.isVisibleTo(i)) {
				players[i].sendMessage(event);
			}
		}
	}
	
}
