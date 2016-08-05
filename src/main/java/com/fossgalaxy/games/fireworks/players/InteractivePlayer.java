package com.fossgalaxy.games.fireworks.players;

import java.io.PrintStream;
import java.util.Scanner;

import com.fossgalaxy.games.fireworks.TextProtocol;
import com.fossgalaxy.games.fireworks.state.BasicState;
import com.fossgalaxy.games.fireworks.state.CardColour;
import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.Hand;
import com.fossgalaxy.games.fireworks.state.actions.Action;
import com.fossgalaxy.games.fireworks.state.events.GameEvent;

public class InteractivePlayer implements Player {
	private GameState state;
	private Scanner in;
	private PrintStream out;

	public InteractivePlayer(Scanner in, PrintStream out) {
		this.in = in;
		this.out = out;
		this.state = new BasicState(5, 3);
	}

	@Override
	public Action getAction() {

		out.println("Your move");
		for (int player = 0; player < state.getPlayerCount(); player++) {
			Hand hand = state.getHand(player);
			out.println("  Player " + player + ": " + hand);
		}
		out.println("  You have " + state.getLives() + " lives");
		out.println("  You have " + state.getInfomation() + " infomation token(s)");
		out.println("  The table is: " + state.getTableValue(CardColour.BLUE));
		out.println();
		out.println("  What would you like to do? ");
		return TextProtocol.stringToAction(in.nextLine());
	}

	@Override
	public void sendMessage(GameEvent msg) {
		msg.apply(state);
	}

	@Override
	public void setID(int id, int nPlayers) {
		// TODO Auto-generated method stub
		
	}

}
