package com.fossgalaxy.games.fireworks.ai;

import com.fossgalaxy.games.fireworks.GameRunner;
import com.fossgalaxy.games.fireworks.GameStats;
import com.fossgalaxy.games.fireworks.ai.AgentPlayer;
import com.fossgalaxy.games.fireworks.players.Player;
import com.fossgalaxy.games.fireworks.utils.AgentUtils;
import com.fossgalaxy.stats.BasicStats;
import com.fossgalaxy.stats.StatsSummary;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.*;
import java.util.stream.Collectors.*;

/**
 * Game runner for testing.
 * <p>
 * This will run a bunch of games with your agent so you can see how it does.
 */
public class AppParamSearch {

    private static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("HH:mm:ss 'on' dd-LLL");

    public static void main(String[] args) {
        String policy = (args.length == 0) ? "outer" : args[0];
        String basePolicy;
        String rolloutPolicy = "";
        String C_options = (args.length < 2) ? "1.0" : args[1];
        String R_options = (args.length < 3) ? "100" : args[2];
        String D_options = (args.length < 4) ? "1" : args[3];
        String T_options = (args.length < 5) ? "1000" : args[4];
        List<String> agentNames = new ArrayList<>();
        String[] corePolicy = policy.split("\\|");
        if (corePolicy.length == 1) {
            basePolicy = corePolicy[0];
        } else {
            rolloutPolicy = corePolicy[1];
            basePolicy = corePolicy[0];
        }
        List<String> explorationC = Arrays.stream(C_options.split("\\|")).collect(Collectors.toList());
        //            Arrays.asList("0.1", "0.3", "1.0", "3.0");
        List<String> timeLimit = Arrays.stream(T_options.split("\\|")).collect(Collectors.toList());
        //Arrays.asList("100", "300", "1000", "3000", "10000", "30000");
        List<String> treeDepth = Arrays.stream(D_options.split("\\|")).collect(Collectors.toList());
        List<String> rollouts = Arrays.stream(R_options.split("\\|")).collect(Collectors.toList());

        for (String T : timeLimit) {
            for (String C : explorationC) {
                for (String R : rollouts) {
                    for (String D : treeDepth) {
                        String agentName = (basePolicy + "[" + C + ":" + R + ":" + D + ":" + T);
                        if (rolloutPolicy.equals("")) {
                            agentName += "]";
                        } else {
                            agentName += ":" + rolloutPolicy + "]";
                        }
                        agentNames.add(agentName);
                    }
                }
            }
        }
        agentNames.forEach(AppParamSearch::runGamesAndLogResults);
    }

    private static void runGamesAndLogResults(String agentDescriptor) {

        int numPlayers = 4;
        int numGames = 100;

        System.out.println("Starting run for " + agentDescriptor + " at " + dateFormat.format(ZonedDateTime.now(ZoneId.of("UTC"))));

        Random random = new Random();
        StatsSummary statsSummary = new BasicStats();

        for (int i = 0; i < numGames; i++) {
            //         System.out.println("Game " + i + " starting");
            GameRunner runner = new GameRunner("test-game", numPlayers);

            //add your agents to the game
            for (int j = 0; j < numPlayers; j++) {
                // the player class keeps track of our state for us...
                Player player = new AgentPlayer(agentDescriptor, AgentUtils.buildAgent(agentDescriptor));
                runner.addPlayer(player);
            }

            GameStats stats = runner.playGame(random.nextLong());
            statsSummary.add(stats.score);
        }

        //print out the stats
        System.out.println(String.format("%s: Avg: %.2f, std err %.2f min: %.1f, max: %.1f",
                agentDescriptor,
                statsSummary.getMean(),
                statsSummary.getStdErr(),
                statsSummary.getMin(),
                statsSummary.getMax()));
    }
}
