package com.fossgalaxy.hanabi;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import uk.ac.essex.csee.iggi.hanabi.CardColour;
import uk.ac.essex.csee.iggi.hanabi.Hand;

public class InteractivePlayer implements Player {
	private Map<Integer, Hand> hands;
	private Map<CardColour, Integer> table;
	private int lives;
	private int infomation;
	
	private Scanner in;
	
	public InteractivePlayer(Scanner in) {
		this.in = in;
		this.hands = new HashMap<Integer, Hand>();
		this.table = new EnumMap<CardColour, Integer>(CardColour.class);
		
		this.lives = 3;
		this.infomation = 8;
		
		for (int i=0; i<3; i++) {
			hands.put(i, new Hand(5));
		}
	}
	
	@Override
	public void sendMessage(String msg) {
		String[] parts = msg.split(" ");
		int who = Integer.parseInt(parts[0]);
		String action = parts[1];
		
		switch(action) {
			case "draw": {
				int slot = Integer.parseInt(parts[2]);
				CardColour colour = CardColour.valueOf(parts[3]);
				int value = Integer.parseInt(parts[4]);
				
				Hand h = hands.get(who);
				h.setKnownColour(slot, colour);
				h.setKnownValue(slot, value);
				break;
			}
			case "play": {
				int slot = Integer.parseInt(parts[2]);
				CardColour colour = CardColour.valueOf(parts[3]);
				int value = Integer.parseInt(parts[4]);
				
				Integer current = table.get(colour);
				current = current==null?0:current;
				if (value == current+1) {
					table.put(colour, value);
				} else {
					lives--;
				}
				
				Hand h = hands.get(who);
				h.setKnownColour(slot, null);
				h.setKnownValue(slot, null);
				break;
			}
			case "discard": {
				int slot = Integer.parseInt(parts[2]);
				CardColour colour = CardColour.valueOf(parts[3]);
				int value = Integer.parseInt(parts[4]);
				
				if (infomation > 8) {
					infomation++;
				}
				
				Hand h = hands.get(who);
				h.setKnownColour(slot, null);
				h.setKnownValue(slot, null);
				break;
			}
			default: {
				System.err.println("unknown action "+action+" parts: "+Arrays.toString(parts));
			}
				
		}
		
	}

	@Override
	public String getAction() {
		for (Map.Entry<Integer, Hand> playerInfo : hands.entrySet()) {
			Integer id = playerInfo.getKey();
			Hand hand = playerInfo.getValue();
			System.out.println("Player "+id+": "+hand);
		}
		
		System.out.println("what move? ");
		return in.nextLine();
	}

}
