package com.fossgalaxy.games.fireworks;

import com.fossgalaxy.games.fireworks.ai.AgentPlayer;
import com.fossgalaxy.games.fireworks.ai.ProductionRuleAgent;

/**
 * Hello world!
 *
 */
public class App {

	public static void main(String[] args) {

		double sum = 0;
		int games = 0;
		
		for (int run=0; run<500; run++) {
			GameStats stats = playGame();
			sum += stats.score;
			games++;
		}
		
		System.out.println("avg: "+sum/games);
	}
	
	public static GameStats playGame() {
		GameRunner runner = new GameRunner(4);
		//runner.addPlayer(new AgentPlayer(0, new RandomAgent()));
		runner.addPlayer(new AgentPlayer(0, new ProductionRuleAgent()));
		//runner.addPlayer(new AgentPlayer(1, new RandomAgent()));
		runner.addPlayer(new AgentPlayer(1, new ProductionRuleAgent()));
		runner.addPlayer(new AgentPlayer(2, new ProductionRuleAgent()));
		runner.addPlayer(new AgentPlayer(3, new ProductionRuleAgent()));
		//runner.addPlayer(new AgentPlayer(2, new RandomAgent()));
		
		GameStats stats = runner.playGame();
		System.out.println("the agents scored: "+stats);
		return stats;
	}

}
