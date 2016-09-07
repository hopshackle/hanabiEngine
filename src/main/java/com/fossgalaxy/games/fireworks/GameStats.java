package com.fossgalaxy.games.fireworks;

public class GameStats {
	public final int nPlayers;
	
	public final int score;
	public final int lives;
	public final int moves;

	public final int infomation;

	public GameStats(int players, int score, int lives, int moves, int infomation) {
		this.nPlayers = players;
		this.score = score;
		this.lives = lives;
		this.moves = moves;
		this.infomation = infomation;
	}

	
	public String toString() {
		return String.format("%d in %d moves (%d lives left)", score, moves, lives);
	}
}
