package com.fossgalaxy.games.fireworks.ai;

import java.util.Arrays;
import java.util.Scanner;

import com.fossgalaxy.games.fireworks.Player;
import com.fossgalaxy.games.fireworks.engine.Card;
import com.fossgalaxy.games.fireworks.engine.CardColour;
import com.fossgalaxy.games.fireworks.engine.Hand;
import com.fossgalaxy.games.fireworks.state.Action;
import com.fossgalaxy.games.fireworks.state.GameState;

public class AgentPlayer implements Player {
	private GameState state;	
	private Agent agent;
	
	public AgentPlayer(Agent agent) {
		this.agent = agent;
		this.state = new GameState(3, 5);
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
				
				state.setInfomation(who, slot, value, colour);
				break;
			}
			case "play": {
				int slot = Integer.parseInt(parts[2]);
				CardColour colour = CardColour.valueOf(parts[3]);
				int value = Integer.parseInt(parts[4]);
				
				state.play(who, slot, new Card(value, colour));
				break;
			}
			case "discard": {
				int slot = Integer.parseInt(parts[2]);
				CardColour colour = CardColour.valueOf(parts[3]);
				int value = Integer.parseInt(parts[4]);
				
				state.discard(who, slot, new Card(value, colour));
				break;
			}
			case "tell_colour": {
				int playerID = Integer.parseInt(parts[2]);
				CardColour colour = CardColour.valueOf(parts[3]);
				
				String[] slotStrs = parts[4].split(",");
				int[] slots = new int[slotStrs.length];
				for (int i=0; i<slotStrs.length; i++) {
					slots[i] = Integer.parseInt(slotStrs[i]);
				}
				
				state.tell(playerID, colour, slots);		
				break;
			}
			case "tell_value": {
				int playerID = Integer.parseInt(parts[2]);
				int value = Integer.parseInt(parts[3]);
				
				String[] slotStrs = parts[4].split(",");
				int[] slots = new int[slotStrs.length];
				for (int i=0; i<slotStrs.length; i++) {
					slots[i] = Integer.parseInt(slotStrs[i]);
				}
				
				state.tell(playerID, value, slots);		
				break;
			}
			default: {
				System.err.println("unknown action "+action+" parts: "+Arrays.toString(parts));
			}
				
		}
		
	}

	@Override
	public String getAction() {
		Action act = agent.doMove(state);
		String action = act.toProtocol();
		System.out.println(action);
		return action;
	}

}
