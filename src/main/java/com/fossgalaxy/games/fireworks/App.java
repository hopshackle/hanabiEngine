package com.fossgalaxy.games.fireworks;

import com.fossgalaxy.games.fireworks.ai.Agent;
import com.fossgalaxy.games.fireworks.ai.AgentPlayer;
import com.fossgalaxy.games.fireworks.ai.RandomAgent;
import com.fossgalaxy.games.fireworks.ai.iggi.IGGIFactory;
import com.fossgalaxy.games.fireworks.ai.mcts.MCTS;
import com.fossgalaxy.games.fireworks.ai.mcts.MCTSPredictor;
import com.fossgalaxy.games.fireworks.ai.osawa.OsawaFactory;
import com.fossgalaxy.games.fireworks.ai.rule.ProductionRuleAgent;

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
		
		GameStats stats = runner.playGame(null);
		System.out.println("the agents scored: "+stats);
		return stats;
	}
	
	public static Agent buildAgent(String name) {
		switch (name) {
			case "pure_random":
				return new RandomAgent();
			case "random":
				return OsawaFactory.buildRandom();
			case "internal":
				return OsawaFactory.buildInternalState();
			case "outer":
				return  OsawaFactory.buildOuterState();
			case "cautious":
				return IGGIFactory.buildCautious();
			case "legal_random":
				return IGGIFactory.buildRandom();
			case "mcts":
				return new MCTS();
			case "cautiousMCTS":
				Agent[] a = new Agent[]{buildAgent("cautious"),buildAgent("cautious"),buildAgent("cautious"),buildAgent("cautious"),buildAgent("cautious")};
				return new MCTSPredictor(a);
		}
		
		throw new RuntimeException("unknown agent type "+name);
	}

}
