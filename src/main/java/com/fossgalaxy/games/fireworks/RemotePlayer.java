package com.fossgalaxy.games.fireworks;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

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
	public void sendMessage(String msg) {
		ps.println(msg);
	}

	@Override
	public String getAction() {
		//TODO implement timeout for waiting for response from agent.
		sendMessage(TextProtocol.buildMessage(-1, TextProtocol.PROTOCOL_REQ_MOVE, ""));
		return in.nextLine();
	}

}
