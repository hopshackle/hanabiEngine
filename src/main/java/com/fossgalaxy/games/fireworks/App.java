package com.fossgalaxy.games.fireworks;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.fossgalaxy.games.fireworks.ai.AgentPlayer;
import com.fossgalaxy.games.fireworks.ai.RandomAgent;
import com.fossgalaxy.games.fireworks.engine.Card;
import com.fossgalaxy.games.fireworks.engine.CardColour;
import com.fossgalaxy.games.fireworks.engine.Hanabi;
import com.fossgalaxy.games.fireworks.engine.Hand;

/**
 * Hello world!
 *
 */
public class App 
{
	private static final int HAND_SIZE = 5;
	
    public static void main( String[] args )
    {
        Scanner scanner = new Scanner(System.in);
        
        //this could be MUCH better, but it should work for testing
        Map<Integer, Player> players = new HashMap<Integer, Player>();
        players.put(0, new InteractivePlayer(scanner));
        players.put(1, new AgentPlayer(new RandomAgent()));
        players.put(2, new AgentPlayer(new RandomAgent()));
        
        //build the game
        Hanabi game = new Hanabi(players.size(), HAND_SIZE);
        
        //Phase 1: deal cards and tell players the other players cards
        for (int player=0; player<players.size(); player++) {
        	Hand hand = game.getHand(player);
        	
        	for (int slot=0; slot<hand.getSize(); slot++) {
        		Card cardInSlot = hand.getCard(slot);
            	sendToAllBut(player, TextProtocol.drawMessage(player, slot, cardInSlot.colour, cardInSlot.value), players);
        	}
        }
        
        //Phase 2: play the game until the game is over
        int playerID = 0;
        while(!game.isOver()) {
        	
        	Player playerInput = players.get(playerID);
        	String move = playerInput.getAction();
        	String[] moveArgs = move.split(" ");
        	
        	switch (moveArgs[0]) {
        		case "play":
        		case "PLAY": {
        			//PLAY $SLOT
        			int slot = Integer.parseInt(moveArgs[1]);
        			
        			Card oldCard = game.getCard(playerID, slot);
        			Card newCard = game.play(playerID, slot);
        			
        			sendToAll(TextProtocol.playCard(playerID, slot, oldCard), players.values());
                	sendToAllBut(playerID, TextProtocol.drawMessage(playerID, slot, newCard.colour, newCard.value), players);
        			break;
        		}
        		case "discard":
        		case "DISCARD": {
        			//DISCARD $SLOT
        			int slot = Integer.parseInt(moveArgs[1]);
        			
        			Card oldCard = game.getCard(playerID, slot);
        			Card newCard = game.discard(playerID, slot);
        			
        			sendToAll(TextProtocol.discardCard(playerID, slot, oldCard), players.values());
                	sendToAllBut(playerID, TextProtocol.drawMessage(playerID, slot, newCard.colour, newCard.value), players);
        			break;
        		}
        		case "tell_colour":
        		case "TELL_COLOUR": {
        			//TELL_COLOUR $PLAYER $COLOUR
        			
        			int player = Integer.parseInt(moveArgs[1]);
        			CardColour colour = CardColour.valueOf(moveArgs[2]);
        			Collection<Integer> slots = game.tell(player, player, colour);
        			
        			sendToAll(TextProtocol.tellPlayer(playerID, player, colour, slots), players.values());
        			break;
        		}
        		case "tell_value":
        		case "TELL_VALUE": {
        			//TELL_VALUE $PLAYER $VALUE
        			
        			int player = Integer.parseInt(moveArgs[1]);
        			int value = Integer.parseInt(moveArgs[2]);
        			Collection<Integer> slots = game.tell(playerID, player, value);
        			sendToAll(TextProtocol.tellPlayer(playerID, player, value, slots), players.values());
        			break;
        		}
        		default: {
    				System.err.println("unknown action "+move+" for "+playerID+" parts: "+Arrays.toString(moveArgs));
        			break;
        		}
        	}
        	
        	//Server -> Client Action Descriptions
        	//System.out.println("$WHO PLAY $SLOT");
        	//System.out.println("$WHO DISCARD $SLOT");
        	//System.out.println("$WHO TELL_COLOUR $COLOUR $PLAYERID $SLOT,...,$SLOT");
        	//System.out.println("$WHO TELL_VALUE $VALUE $PLAYERID $SLOT,...,$SLOT");
        	
        	//Result of drawing a card (happens after play or discard)
        	//System.out.println("$WHO DRAW SLOT COLOUR VALUE");
        	
        	playerID = (playerID + 1) % players.size();
        }
        
        //Phase 3: tell players the final score
        System.out.println("final score: "+game.getScore());
        
        scanner.close();
    }
    
    private static void sendToAll(String msg, Collection<Player> players) {
    	for (Player player : players) {
    		player.sendMessage(msg);
    	}
    }
    
    public static void sendToAllBut(int pid, String msg, Map<Integer, Player> players) {
    	for (Map.Entry<Integer, Player> entry : players.entrySet()) {
    		Integer id = entry.getKey();
    		if (id != pid) {
    			Player p = entry.getValue();
    			p.sendMessage(msg);
    		}
    	}
    }
}
