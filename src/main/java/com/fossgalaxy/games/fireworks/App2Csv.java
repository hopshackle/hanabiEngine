package com.fossgalaxy.games.fireworks;

import com.fossgalaxy.games.fireworks.ai.Agent;
import com.fossgalaxy.games.fireworks.ai.AgentPlayer;
import com.fossgalaxy.games.fireworks.ai.ProductionRuleAgent;
import com.fossgalaxy.games.fireworks.ai.RandomAgent;
import com.fossgalaxy.games.fireworks.ai.osawa.OsawaFactory;
import com.fossgalaxy.games.fireworks.players.Player;

/**
 * Hello world!
 *
 */
public class App2Csv {

	public static void main(String[] args) {

		
		
		System.out.println("name,players,information,lives,moves,score");
		for (int run=0; run<500; run++) {
			playGame("random", new RandomAgent(), new RandomAgent(), new RandomAgent());
			playGame("productionRule1", ProductionRuleAgent.buildTest1(), ProductionRuleAgent.buildTest1(), ProductionRuleAgent.buildTest1());
			playGame("productionRule2", ProductionRuleAgent.buildTest2(), ProductionRuleAgent.buildTest2(), ProductionRuleAgent.buildTest2());
			playGame("internal", OsawaFactory.buildInternalState(), OsawaFactory.buildInternalState(), OsawaFactory.buildInternalState());
			playGame("outer", OsawaFactory.buildOuterState(), OsawaFactory.buildOuterState(), OsawaFactory.buildOuterState());
		}
		
		//System.out.println("avg: "+sum/games);
	}
	
	public static GameStats playGame(String name, Player ... players) {
		GameRunner runner = new GameRunner(players.length);
		
		for (int i=-0; i<players.length; i++) {
			runner.addPlayer(players[i]);
			players[i].setID(i, players.length);
		}
		
		GameStats stats = runner.playGame();
		System.out.println(String.format("%s,%d,%d,%d,%d,%d", name,stats.nPlayers, stats.infomation, stats.lives, stats.moves, stats.score));
		return stats;
	}
	
	public static GameStats playGame(String name, Agent ... agents) {
		Player[] wrapper = new Player[agents.length];
		for (int i=0; i<agents.length; i++) {
			wrapper[i] = new AgentPlayer(i, agents[i]);
		}
		return playGame(name, wrapper);
	}

}
