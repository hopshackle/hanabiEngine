package com.fossgalaxy.hanabi;

import java.util.Scanner;

import uk.ac.essex.csee.iggi.hanabi.Hanabi;

/**
 * Hello world!
 *
 */
public class App 
{
	private static final int HAND_SIZE = 1;
	
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the number of players: ");
        int nPlayers = scanner.nextInt();
        
        Hanabi game = new Hanabi(nPlayers, HAND_SIZE);
        
        //TODO write game logic
        
        scanner.close();
    }
}
