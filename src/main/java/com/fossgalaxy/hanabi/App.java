package com.fossgalaxy.hanabi;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import uk.ac.essex.csee.iggi.hanabi.Card;
import uk.ac.essex.csee.iggi.hanabi.CardColour;
import uk.ac.essex.csee.iggi.hanabi.Hanabi;
import uk.ac.essex.csee.iggi.hanabi.Hand;

/**
 * Hello world!
 *
 */
public class App 
{
	private static final int HAND_SIZE = 5;
	
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        
        Scanner scanner = new Scanner(System.in);
        
        //this could be MUCH better, but it should work for testing
        Map<Integer, TextPlayer> players = new HashMap<Integer, TextPlayer>();
        players.put(0, new TextPlayer("bob", scanner, System.out));
        players.put(1, new TextPlayer("ed", scanner, System.out));
        players.put(2, new TextPlayer("dave", scanner, System.out));
        
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
        int nextPlayer = 0;
        while(!game.isOver()) {
        	
        	int playerID = 0; //TODO track current player
        	String move = scanner.nextLine();
        	String[] moveArgs = move.split(" ");
        	
        	List<String> playerInfo = new ArrayList<String>();
        	List<String> groupInfo = new ArrayList<String>();
        	
        	switch (moveArgs[0]) {
        		case "PLAY": {
        			//PLAY $SLOT
        			int slot = Integer.parseInt(moveArgs[1]);
        			
        			Card newCard = game.play(playerID, slot);
        			
        			sendToAll(TextProtocol.playCard(playerID, slot), players.values());
                	sendToAllBut(playerID, TextProtocol.drawMessage(playerID, slot, newCard.colour, newCard.value), players);
        			break;
        		}
        		case "DISCARD": {
        			//DISCARD $SLOT
        			int slot = Integer.parseInt(moveArgs[1]);
        			
        			Card newCard = game.discard(playerID, slot);
        			
        			sendToAll(TextProtocol.discardCard(playerID, slot), players.values());
                	sendToAllBut(playerID, TextProtocol.drawMessage(playerID, slot, newCard.colour, newCard.value), players);
        			break;
        		}
        		case "TELL_COLOUR": {
        			//TELL_COLOUR $PLAYER $COLOUR
        			
        			//tell all players about information
        			
        			int player = Integer.parseInt(moveArgs[1]);
        			CardColour colour = CardColour.valueOf(moveArgs[2]);
        			Collection<Integer> slots = game.tell(player, player, colour);
        			
        			sendToAll(TextProtocol.tellPlayer(playerID, player, colour, slots), players.values());
        			break;
        		}
        		
        		case "TELL_VALUE": {
        			//TELL_VALUE $PLAYER $VALUE
        			
        			//TODO
        			//tell all players about information
        			
        			int player = Integer.parseInt(moveArgs[1]);
        			int value = Integer.parseInt(moveArgs[2]);
        			Collection<Integer> slots = game.tell(playerID, player, value);
        			sendToAll(TextProtocol.tellPlayer(playerID, player, value, slots), players.values());
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
        	
        }
        
        //Phase 3: tell players the final score
        //TODO write game logic
        
        scanner.close();
    }
    
    private static void sendToAll(String msg, Collection<TextPlayer> players) {
    	for (TextPlayer player : players) {
    		player.sendMessage(msg);
    	}
    }
    
    public static void sendToAllBut(int pid, String msg, Map<Integer, TextPlayer> players) {
    	for (Map.Entry<Integer, TextPlayer> entry : players.entrySet()) {
    		Integer id = entry.getKey();
    		if (id != pid) {
    			TextPlayer p = entry.getValue();
    			p.sendMessage(msg);
    		}
    	}
    }
}
