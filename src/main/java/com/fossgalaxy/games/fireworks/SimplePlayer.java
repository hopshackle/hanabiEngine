package com.fossgalaxy.games.fireworks;

import java.io.PrintStream;
import java.util.Scanner;

import com.fossgalaxy.games.fireworks.state.actions.Action;
import com.fossgalaxy.games.fireworks.state.events.GameEvent;

public class SimplePlayer implements Player {

	private String name;
	private Scanner in;
	private PrintStream out;

	public SimplePlayer(String name, Scanner in, PrintStream out) {
		this.name = name;
		this.in = in;
		this.out = out;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.fossgalaxy.hanabi.Player#getAction()
	 */
	@Override
	public Action getAction() {
		out.println("your move");
		return TextProtocol.stringToAction(in.nextLine());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.fossgalaxy.hanabi.Player#sendMessage(java.lang.String)
	 */
	@Override
	public void sendMessage(GameEvent msg) {
		out.println("[" + name + "] " + msg);
	}

}
