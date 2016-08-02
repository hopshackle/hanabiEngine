package com.fossgalaxy.games.fireworks;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

import com.fossgalaxy.games.fireworks.state.actions.Action;
import com.fossgalaxy.games.fireworks.state.events.GameEvent;
import com.fossgalaxy.games.fireworks.state.events.MoveRequest;

public class RemotePlayer implements Player {
	private Process p;
	private PrintStream ps;
	private Scanner in;

	public RemotePlayer(String progName) throws IOException {
		ProcessBuilder pb = new ProcessBuilder(progName);
		this.p = pb.start();
		this.ps = new PrintStream(p.getOutputStream());
		this.in = new Scanner(p.getInputStream());
	}

	@Override
	public Action getAction() {
		// TODO implement timeout for waiting for response from agent.
		sendMessage(new MoveRequest());
		return TextProtocol.stringToAction(in.nextLine());
	}

	@Override
	public void sendMessage(GameEvent msg) {
		ps.println(msg.toTextProtocol());

	}

}
