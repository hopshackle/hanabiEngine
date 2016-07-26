package com.fossgalaxy.hanabi;

import java.io.PrintStream;
import java.util.Scanner;

public class TextPlayer {

	private String name;
	private Scanner in;
	private PrintStream out;
	
	public TextPlayer(String name, Scanner in, PrintStream out) {
		this.name = name;
		this.in = in;
		this.out = out;
	}
	
	public void sendMessage(String msg) {
		out.println("["+name+"] "+msg);
	}
	
	public String getAction() {
		sendMessage(TextProtocol.buildMessage(-1, TextProtocol.PROTOCOL_REQ_MOVE, ""));
		return in.nextLine();
	}
	
}
