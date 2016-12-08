package com.fossgalaxy.games.fireworks;

import com.fossgalaxy.games.fireworks.ai.Agent;
import com.fossgalaxy.games.fireworks.ai.iggi.IGGIFactory;
import com.fossgalaxy.games.fireworks.ai.mcts.MCTS;
import com.fossgalaxy.games.fireworks.ai.mcts.MCTSPredictor;
import com.fossgalaxy.games.fireworks.utils.AgentUtils;
import com.fossgalaxy.games.fireworks.utils.GameUtils;
import com.fossgalaxy.games.fireworks.utils.SetupUtils;

import java.util.Random;

/**
 * Hello world!
 */
public class App {

    //utility class - don't create instances of it
    private App() {

    }

    /**
     * Plays a series of games with a single agent mixed with another agent
     * @param args Ignored
     */
    public static void main(String[] args) {

        double sum = 0;
        int games = 0;
        System.out.println("Start");

        for (int run = 0; run < 10_000; run++) {
            GameStats stats = playMixed("mctsND", "hat");
            sum += stats.score;
            games++;
        }

        if (games == 0) {
            return;
        }

        System.out.println("avg: " + sum / games);
    }

    /**
     * Plays a game with the given agent
     * @param agent The given agent to play the game
     * @return GameStats for the game.
     */
    public static GameStats playGame(String agent) {
        String[] names = new String[5];
        Agent[] players = new Agent[5];
        for (int i = 0; i < 5; i++) {
            names[i] = agent;
            players[i] = AgentUtils.buildAgent(names[i]);
        }

        GameStats stats = GameUtils.runGame("", null, SetupUtils.toPlayers(names, players));
        System.out.println("the agents scored: " + stats);
        return stats;
    }

    /**
     *Plays a mixed game with the agent under test and all other agents as the agent
     * @param agentUnderTest The agent to be player 0
     * @param agent The agent to make all the others
     * @return GameStats for the game
     */
    public static GameStats playMixed(String agentUnderTest, String agent) {
        Random r = new Random();
        int whereToPlace = r.nextInt(5);

        String[] names = new String[5];
        for (int i=0; i<names.length; i++){
            names[i] = whereToPlace == i ? agentUnderTest : agent;
        }

        Agent[] players = new Agent[5];
        for (int i = 0; i < names.length; i++) {
            players[i] = buildAgent(names[i], i, agent, names.length);
        }

        GameStats stats = GameUtils.runGame("", null, SetupUtils.toPlayers(names, players));
        System.out.println("the agents scored: " + stats);
        return stats;
    }

    /**
     *Build an agent
     * @param name The name the agent will believe it has
     * @param agentID The AgentID it will have
     * @param paired Who it is paired with
     * @param size The size of the game
     * @return The agent created
     */
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

    /**
     *Build an agent
     * @param name The name the agent will believe it has
     * @param agentID The AgentID it will have
     * @param paired Who it is paired with
     * @param size The size of the game
     * @return The agent created
     */
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


    /**
     * Allows for creating MCTS specifically with some fields
     * @param name The name of the agent
     * @param roundLength The round length to use for MCTS
     * @param rolloutDepth The rollout depth to use for MCTS
     * @param treeDepth The tree depth to use for MCTS
     * @return The Agent
     */
    public static Agent buildAgent(String name, int roundLength, int rolloutDepth, int treeDepth) {
        switch (name) {
            case "mcts":
                return new MCTS(roundLength, rolloutDepth, treeDepth);
            default:
                return AgentUtils.buildAgent(name);
        }
    }

    /**
     *Allows for creating Predictor MCTS with some fields
     * @param name The name for the agent
     * @param agentID The agent id
     * @param paired Who the agent is paired with
     * @param size The size of the game
     * @param roundLength The round length to use for MCTS
     * @param rolloutDepth The rollout depth to use for MCTS
     * @param treeDepth The tree depth to use for MCTS
     * @return The agent
     */
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

    /**
     * Builds a risky agent with a given threshold
     * @param name The name for the agent
     * @param threshold The threshold to give to the agent
     * @return The agent
     */
    public static Agent buildAgent(String name, double threshold) {
        switch (name) {
            case "iggi_risky":
                return IGGIFactory.buildRiskyPlayer(threshold);
            default:
                return AgentUtils.buildAgent(name);
        }
    }
}
