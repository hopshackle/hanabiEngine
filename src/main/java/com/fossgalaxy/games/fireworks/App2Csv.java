package com.fossgalaxy.games.fireworks;

import com.fossgalaxy.games.fireworks.ai.Agent;
import com.fossgalaxy.games.fireworks.ai.AgentPlayer;
import com.fossgalaxy.games.fireworks.players.Player;
import com.fossgalaxy.games.fireworks.utils.AgentUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

/**
 * @deprecated This class should be migrated away from
 */
@Deprecated
public class App2Csv {
    public static final Integer GAME_SIZE = 3;
    public static final Integer DEFAULT_NUM_RUNS = 100;
    protected static final String[] AGENT_NAMES = {"pure_random", "internal", "outer", "legal_random", "cautious", "mcts", "cautiousMCTS"};

    //Utility class - instances not permitted.
    private App2Csv() {

    }

    public static void main(String[] args) {

        int runCount = DEFAULT_NUM_RUNS;

        //allow setting of run count via env variable
        String runCountEnv = System.getenv("FIREWORKS_RUN_COUNT");
        if (runCountEnv != null) {
            runCount = Integer.parseInt(runCountEnv);
        }

        //agents which will be playing
        String[] agentNames = App2Csv.AGENT_NAMES;
        String envAgents = System.getenv("FIREWORKS_AGENTS");
        if (envAgents != null) {
            agentNames = envAgents.split(",");
        }

        Random random = new Random();

        //play the games
        System.out.println("name,seed,players,information,lives,moves,score");

        for (int run = 0; run < runCount; run++) {
            Agent[] agents = new Agent[GAME_SIZE];
            String[] agentStr = new String[5]; //an array containing all 5 agent names

            //use the same seed for 1 game for all agents (for fairness)
            long seed = random.nextLong();

            for (int i = 0; i < agents.length; i++) {
                for (int agent = 0; agent < agents.length; agent++) {
                    agents[agent] = AgentUtils.buildAgent(agentNames[i]);
                    agentStr[agent] = agentNames[i];
                }

                playGame(agentStr, seed, agents);
            }
        }
    }

    public static GameStats playGame(String[] names, Long seed, Player... players) {
        UUID id = UUID.randomUUID();
        try (
                FileOutputStream fos = new FileOutputStream(String.format("trace_%s.csv", id));
                PrintStream ps = new PrintStream(fos)
        ) {
            GameRunner runner = new GameRunner(id, players.length);

            for (int i = -0; i < players.length; i++) {
                runner.addPlayer(players[i]);
                players[i].setID(i, players.length);
            }

            GameStats stats = runner.playGame(seed);
            ps.println("DEBUG,game is over");
            System.out.println(String.format("%s,%d,%d,%d,%d,%d,%d,%d", String.join(",", Arrays.asList(names)), seed, stats.nPlayers, stats.infomation, stats.lives, stats.moves, stats.score, stats.disqal));
            return stats;
        } catch (IOException ex) {
            System.err.println("error: " + ex.toString());
        }
        return null;
    }

    public static GameStats playGameErrTrace(String gameID, String[] name, Long seed, Player... players) {
        UUID id = UUID.randomUUID();
        try {
            GameRunner runner = new GameRunner(id, players.length);

            for (int i = -0; i < players.length; i++) {
                runner.addPlayer(players[i]);
                players[i].setID(i, players.length);
            }

            GameStats stats = runner.playGame(seed);
            System.out.println(String.format("%s,%s,%d,%d,%d,%d,%d,%d,%d", gameID, String.join(",", Arrays.asList(name)), seed, stats.nPlayers, stats.infomation, stats.lives, stats.moves, stats.score, stats.disqal));
            return stats;
        } catch (Exception ex) {
            System.err.println("error: " + ex.toString());
        }
        return null;
    }

    public static GameStats playGameErrTrace(String gameID, String[] name, Long seed, Agent... agents) {
        Player[] wrapper = new Player[agents.length];
        for (int i = 0; i < agents.length; i++) {
            wrapper[i] = new AgentPlayer(name[i], agents[i]);
        }
        return playGameErrTrace(gameID, name, seed, wrapper);
    }

    public static GameStats playGame(String[] name, Long seed, Agent... agents) {
        Player[] wrapper = new Player[agents.length];
        for (int i = 0; i < agents.length; i++) {
            wrapper[i] = new AgentPlayer(name[i], agents[i]);
        }
        return playGame(name, seed, wrapper);
    }


}
