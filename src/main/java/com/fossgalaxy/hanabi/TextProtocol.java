package com.fossgalaxy.hanabi;

import java.util.Collection;
import java.util.Iterator;

import com.fossgalaxy.hanabi.engine.Card;
import com.fossgalaxy.hanabi.engine.CardColour;

//Server -> Client Action Descriptions
//System.out.println("$WHO PLAY $SLOT");
//System.out.println("$WHO DISCARD $SLOT");
//System.out.println("$WHO TELL_COLOUR $COLOUR $PLAYERID $SLOT,...,$SLOT");
//System.out.println("$WHO TELL_VALUE $VALUE $PLAYERID $SLOT,...,$SLOT");

//Result of drawing a card (happens after play or discard)
//System.out.println("$WHO DRAW SLOT COLOUR VALUE");

public class TextProtocol {
	public static String ACTION_PLAY = "play";
	public static String ACTION_DISCARD = "discard";
	public static String ACTION_TELL_COLOUR = "tell_colour";
	public static String ACTION_TELL_VALUE = "tell_value";
	
	public static String EFFECT_DRAW = "draw";
	
	public static String PROTOCOL_REQ_MOVE = "DOMOVE";
	

	public static String playCard(int player, int slot, Card card){
		return buildMessage(player, ACTION_PLAY, ""+slot+" "+card.colour+" "+card.value);
	}
	
	public static String discardCard(int player, int slot, Card card) {
		return buildMessage(player, ACTION_DISCARD, ""+slot+" "+card.colour+" "+card.value);
	}
	
	public static String tellPlayer(int player, int who, CardColour colour, Collection<Integer> slots) {
		return buildMessage(player, ACTION_TELL_COLOUR, colour+" "+join(slots));
	}
	
	public static String tellPlayer(int player, int who, Integer value, Collection<Integer> slots) {
		return buildMessage(player, ACTION_TELL_VALUE, value+" "+join(slots));
	}
	
	public static String drawMessage(int player, int slot, CardColour colour, int value) {
		return buildMessage(player, EFFECT_DRAW, slot+" "+colour+" "+value);
	}
	
	public static String buildMessage(int player, String action, String args) {
		return player+" "+action+" "+args;
	}
	
	private static String join(Collection<Integer> args) {
		StringBuilder builder = new StringBuilder();
		Iterator<Integer> itr = args.iterator();
		
		while(itr.hasNext()) {
			builder.append(itr.next());
			if (itr.hasNext()) {
				builder.append(",");
			}
		}
		
		return builder.toString();
	}
	
}
