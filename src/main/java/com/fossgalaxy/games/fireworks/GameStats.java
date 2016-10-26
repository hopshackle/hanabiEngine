package com.fossgalaxy.games.fireworks;

import java.util.UUID;

public class GameStats {
    public final UUID gameID;
    public final int nPlayers;

    public final int score;
    public final int lives;
    public final int moves;

    public final int infomation;
    public final int disqal;

    public GameStats(UUID gameID, int players, int score, int lives, int moves, int information, int disqual) {
        this.gameID = gameID;
        this.nPlayers = players;
        this.score = score;
        this.lives = lives;
        this.moves = moves;
        this.infomation = information;
        this.disqal = disqual;
    }


    public String toString() {
        return String.format("%d in %d moves (%d lives left)", score, moves, lives);
    }
}
