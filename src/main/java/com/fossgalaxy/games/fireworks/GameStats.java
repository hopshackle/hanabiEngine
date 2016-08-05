package com.fossgalaxy.games.fireworks;

public class GameStats {
	public int nPlayers;
	
	public int score;
	public int lives;
	public int moves;

	public int infomation;

	
	public String toString() {
		return String.format("%d in %d moves (%d lives left)", score, moves, lives);
	}
}
