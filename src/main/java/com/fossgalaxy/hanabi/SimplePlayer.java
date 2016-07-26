package com.fossgalaxy.hanabi;

import java.io.PrintStream;
import java.util.Scanner;

public class SimplePlayer implements Player {

	private String name;
	private Scanner in;
	private PrintStream out;
	
	public SimplePlayer(String name, Scanner in, PrintStream out) {
		this.name = name;
		this.in = in;
		this.out = out;
	}
	
	/* (non-Javadoc)
	 * @see com.fossgalaxy.hanabi.Player#sendMessage(java.lang.String)
	 */
	@Override
	public void sendMessage(String msg) {
		out.println("["+name+"] "+msg);
	}
	
	/* (non-Javadoc)
	 * @see com.fossgalaxy.hanabi.Player#getAction()
	 */
	@Override
	public String getAction() {
		sendMessage(TextProtocol.buildMessage(-1, TextProtocol.PROTOCOL_REQ_MOVE, ""));
		return in.nextLine();
	}
	
}
