package com.fossgalaxy.games.fireworks;

import com.fossgalaxy.games.fireworks.ai.Agent;
import com.fossgalaxy.games.fireworks.ai.AgentPlayer;
import com.fossgalaxy.games.fireworks.ai.iggi.IGGIFactory;
import com.fossgalaxy.games.fireworks.ai.mcts.MCTS;
import com.fossgalaxy.games.fireworks.ai.mcts.MCTSPredictor;
import com.fossgalaxy.games.fireworks.utils.AgentUtils;
import com.fossgalaxy.games.fireworks.utils.GameUtils;
import com.fossgalaxy.games.fireworks.utils.SetupUtils;

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

        for (int run = 0; run < 1000; run++) {
            GameStats stats = playGame();
            sum += stats.score;
            games++;
        }

        if (games == 0) {
            return;
        }

        System.out.println("avg: " + sum / games);
    }

    public static GameStats playGame() {
        String[] names = new String[5];
        Agent[] players = new Agent[5];
        for (int i=0; i<5; i++) {
            names[i] = "iggi";
            players[i] = AgentUtils.buildAgent(names[i]);
        }

        GameStats stats = GameUtils.runGame("", null, SetupUtils.toPlayers(names, players));
        System.out.println("the agents scored: " + stats);
        return stats;
    }

    public static Agent buildAgent(String name, int agentID, String paired, int size) {
        switch (name) {
            case "predictorMCTS":
            case "predictorMCTSND":
                Agent[] agents = AgentUtils.buildPredictors(agentID, size, paired);
                if (name.contains("ND")) {
                    return new MCTSPredictor(agents, 50_000, 100, 100);
                }
                return new MCTSPredictor(agents);
            default:
                return AgentUtils.buildAgent(name);
        }
    }

    public static Agent buildAgent(String name, int agentID, String[] paired, int size) {
        switch (name) {
            case "predictorMCTS":
            case "predictorMCTSND":
                Agent[] agents = AgentUtils.buildPredictors(agentID, paired);
                if (name.contains("ND")) {
                    return new MCTSPredictor(agents, 50_000, 100, 100);
                }
                return new MCTSPredictor(agents);
            default:
                return AgentUtils.buildAgent(name);
        }
    }


    public static Agent buildAgent(String name, int roundLength, int rolloutDepth, int treeDepth) {
        switch (name) {
            case "mcts":
                return new MCTS(roundLength, rolloutDepth, treeDepth);
            default:
                return AgentUtils.buildAgent(name);
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
                    //TODO is this ever paired with MCTS? if not this should be AgentUtils.buildAgent(agentID, size, paired)
                    agents[i] = buildAgent(paired, roundLength, rolloutDepth, treeDepth);
                }
                return new MCTSPredictor(agents);
            default:
                return AgentUtils.buildAgent(name);
        }
    }

    public static Agent buildAgent(String name, double threshold){
        switch(name){
            case "iggi_risky":
                return IGGIFactory.buildRiskyPlayer(threshold);
            default:
                return AgentUtils.buildAgent(name);
        }
    }
}
