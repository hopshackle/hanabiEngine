package com.fossgalaxy.games.fireworks;

import com.fossgalaxy.games.fireworks.ai.Agent;
import com.fossgalaxy.games.fireworks.ai.AgentPlayer;
import com.fossgalaxy.games.fireworks.ai.RandomAgent;
import com.fossgalaxy.games.fireworks.ai.iggi.IGGIFactory;
import com.fossgalaxy.games.fireworks.ai.mcts.MCTS;
import com.fossgalaxy.games.fireworks.ai.mcts.MCTSPredictor;
import com.fossgalaxy.games.fireworks.ai.osawa.OsawaFactory;

import java.util.UUID;

/**
 * Hello world!
 */
public class App {

    //utility class - don't create instances of it
    private App() {

    }

    public static void main(String[] args) {

        double sum = 0;
        int games = 0;
        System.out.println("Start");

        for (int run = 0; run < 1; run++) {
            GameStats stats = playGame();
            sum += stats.score;
            games++;
        }

        System.out.println("avg: " + sum / games);
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
        System.out.println("the agents scored: " + stats);
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
                return OsawaFactory.buildOuterState();
            case "cautious":
                return IGGIFactory.buildCautious();
            case "iggi":
                return IGGIFactory.buildIGGIPlayer();
            case "legal_random":
                return IGGIFactory.buildRandom();
            case "mcts":
                return new MCTS();
            case "mctsND":
                return new MCTS(50_000, 100, 100);
            case "cautiousMCTS":
            case "cautiousMCTSND":
                Agent[] a = new Agent[]{buildAgent("cautious"), buildAgent("cautious"), buildAgent("cautious"), buildAgent("cautious"), buildAgent("cautious")};
                if (name.contains("ND")) {
                    return new MCTSPredictor(a, 50_000, 100, 100);
                }
                return new MCTSPredictor(a);
        }

        throw new IllegalArgumentException("unknown agent type " + name);
    }

    public static Agent buildAgent(String name, int agentID, String paired, int size) {
        switch (name) {
            case "predictorMCTS":
            case "predictorMCTSND":
                Agent[] agents = new Agent[size];
                for (int i = 0; i < size; i++) {
                    if (i == agentID) {
                        agents[i] = null;
                    }
                    agents[i] = buildAgent(paired);
                }
                if (name.contains("ND")) {
                    return new MCTSPredictor(agents, 50_000, 100, 100);
                }
                return new MCTSPredictor(agents);
            default:
                return buildAgent(name);
        }
    }

    public static Agent buildAgent(String name, int agentID, String[] paired, int size) {
        switch (name) {
            case "predictorMCTS":
            case "predictorMCTSND":
                Agent[] agents = new Agent[size];
                for (int i = 0; i < size; i++) {
                    if (i == agentID) {
                        agents[i] = null;
                    } else {
                        agents[i] = buildAgent(paired[i]);
                    }
                }

                if (name.contains("ND")) {
                    return new MCTSPredictor(agents, 50_000, 100, 100);
                }
                return new MCTSPredictor(agents);
            default:
                return buildAgent(name);
        }
    }


    public static Agent buildAgent(String name, int roundLength, int rolloutDepth, int treeDepth) {
        switch (name) {
            case "mcts":
                return new MCTS(roundLength, rolloutDepth, treeDepth);
            default:
                return buildAgent(name);
        }
    }

    public static Agent buildAgent(String name, int agentID, String paired, int size, int roundLength, int rolloutDepth, int treeDepth) {
        switch (name) {
            case "predictorMCTS":
                Agent[] agents = new Agent[size];
                for (int i = 0; i < size; i++) {
                    if (i == agentID) {
                        agents[i] = null;
                    }
                    agents[i] = buildAgent(paired, roundLength, rolloutDepth, treeDepth);
                }
                return new MCTSPredictor(agents);
            default:
                return buildAgent(name);
        }
    }
}
