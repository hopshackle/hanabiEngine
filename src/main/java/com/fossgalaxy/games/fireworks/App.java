package com.fossgalaxy.games.fireworks;

import com.fossgalaxy.games.fireworks.ai.Agent;
import com.fossgalaxy.games.fireworks.ai.AgentPlayer;
import com.fossgalaxy.games.fireworks.ai.RandomAgent;
import com.fossgalaxy.games.fireworks.ai.iggi.IGGIFactory;
import com.fossgalaxy.games.fireworks.ai.mcts.MCTS;
import com.fossgalaxy.games.fireworks.ai.mcts.MCTSPredictor;
import com.fossgalaxy.games.fireworks.ai.osawa.OsawaFactory;
import com.fossgalaxy.games.fireworks.ai.rule.ProductionRuleAgent;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.UUID;

/**
 * Hello world!
 *
 */
public class App {

	public static void main(String[] args) {

		double sum = 0;
		int games = 0;
		System.out.println("Start");
		
		for (int run=0; run<1; run++) {
			GameStats stats = playGame();
			sum += stats.score;
			games++;
		}
		
		System.out.println("avg: "+sum/games);
	}
	
	public static GameStats playGame() {
		GameRunner runner = new GameRunner(UUID.randomUUID(), 4, null);
		//runner.addPlayer(new AgentPlayer(0, new RandomAgent()));
//		runner.addPlayer(new AgentPlayer(0, new ProductionRuleAgent()));
		//runner.addPlayer(new AgentPlayer(1, new RandomAgent()));
		runner.addPlayer(new AgentPlayer(0, new MCTS()));
//		runner.addPlayer(new AgentPlayer(0, new MCTSPredictor(new Agent[]{null, IGGIFactory.buildCautious(), IGGIFactory.buildCautious(), IGGIFactory.buildCautious()})));
		runner.addPlayer(new AgentPlayer(1, IGGIFactory.buildCautious()));
		runner.addPlayer(new AgentPlayer(2, IGGIFactory.buildCautious()));
		runner.addPlayer(new AgentPlayer(3, IGGIFactory.buildCautious()));
//		runner.addPlayer(new AgentPlayer(1, new ProductionRuleAgent()));
//		runner.addPlayer(new AgentPlayer(2, new ProductionRuleAgent()));
//		runner.addPlayer(new AgentPlayer(3, new ProductionRuleAgent()));
		//runner.addPlayer(new AgentPlayer(2, new RandomAgent()));
//		runner.addPlayer(new AgentPlayer(1, new MCTS()));
//		runner.addPlayer(new AgentPlayer(2, new MCTS()));
//		runner.addPlayer(new AgentPlayer(3, new MCTS()));

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
		
		throw new IllegalArgumentException("unknown agent type "+name);
	}

	public static Agent buildAgent(String name, int agentID, String paired, int size) {
		if ("predictorMCTS".equals(name)) {
			Agent[] agents = new Agent[size];
			for (int i=0; i<size; i++) {
				if (i==agentID) {
					agents[i] = null;
				}
				agents[i] = buildAgent(paired);
			}
			return new MCTSPredictor(agents);
		}

		return buildAgent(name);
	}

}
