package com.fossgalaxy.games.fireworks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.fossgalaxy.games.fireworks.ai.AgentPlayer;
import com.fossgalaxy.games.fireworks.ai.RandomAgent;
import com.fossgalaxy.games.fireworks.players.InteractivePlayer;
import com.fossgalaxy.games.fireworks.players.Player;
import com.fossgalaxy.games.fireworks.state.BasicState;
import com.fossgalaxy.games.fireworks.state.Card;
import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.Hand;
import com.fossgalaxy.games.fireworks.state.actions.Action;
import com.fossgalaxy.games.fireworks.state.events.CardDrawn;
import com.fossgalaxy.games.fireworks.state.events.GameEvent;

/**
 * Hello world!
 *
 */
public class App {
	// hand size per number of players (-1 indicates illegal number of players)
	private static final int[] HAND_SIZE = { -1, -1, 5, 5, 4, 4 };

	public static void main(String[] args) {
		List<GameEvent> log = new ArrayList<GameEvent>();
		List<Action> actionLog = new ArrayList<Action>();

		// this could be MUCH better, but it should work for testing
		Map<Integer, Player> players = new HashMap<Integer, Player>();
		players.put(0, new InteractivePlayer(new Scanner(System.in), System.out));
		players.put(1, new AgentPlayer(new RandomAgent()));
		players.put(2, new AgentPlayer(new RandomAgent()));

		// build the game
		GameState game = new BasicState(players.size(), HAND_SIZE[players.size()]);

		// Phase 1: deal cards and tell players the other players cards
		for (int player = 0; player < players.size(); player++) {
			Hand hand = game.getHand(player);

			for (int slot = 0; slot < hand.getSize(); slot++) {
				Card cardInSlot = hand.getCard(slot);
				GameEvent draw = new CardDrawn(player, slot, cardInSlot.colour, cardInSlot.value);
				send(draw, players);
			}
		}

		// Phase 2: play the game until the game is over
		int playerID = 0;
		while (!game.isGameOver()) {

			Player playerInput = players.get(playerID);
			Action move = playerInput.getAction();
			actionLog.add(move);

			List<GameEvent> effects = move.apply(playerID, game);
			send(effects, players);
			log.addAll(effects);

			playerID = (playerID + 1) % players.size();
		}

		// Phase 3: tell players the final score
		System.out.println("final score: " + game.getScore());
	}

	private static void send(GameEvent event, Map<Integer, Player> players) {
		for (Map.Entry<Integer, Player> entry : players.entrySet()) {
			Integer id = entry.getKey();
			if (event.isVisibleTo(id)) {
				Player p = entry.getValue();
				p.sendMessage(event);
			}
		}
	}

	private static void send(List<GameEvent> events, Map<Integer, Player> players) {
		for (GameEvent event : events) {
			send(event, players);
		}
	}

	public static void sendToAllBut(int pid, GameEvent msg, Map<Integer, Player> players) {
		for (Map.Entry<Integer, Player> entry : players.entrySet()) {
			Integer id = entry.getKey();
			if (id != pid) {
				Player p = entry.getValue();
				p.sendMessage(msg);
			}
		}
	}
}
