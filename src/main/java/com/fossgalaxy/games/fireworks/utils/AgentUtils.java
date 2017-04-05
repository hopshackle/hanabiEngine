package com.fossgalaxy.games.fireworks.utils;

import com.fossgalaxy.games.fireworks.ai.Agent;
import com.fossgalaxy.games.fireworks.ai.RandomAgent;
import com.fossgalaxy.games.fireworks.ai.ga.RMHC;
import com.fossgalaxy.games.fireworks.ai.hat.HatGuessing;
import com.fossgalaxy.games.fireworks.ai.iggi.IGGIFactory;
import com.fossgalaxy.games.fireworks.ai.mcs.MonteCarloSearch;
import com.fossgalaxy.games.fireworks.ai.mcts.MCTS;
import com.fossgalaxy.games.fireworks.ai.mcts.NoisyPredictor;
import com.fossgalaxy.games.fireworks.ai.osawa.OsawaFactory;
import com.fossgalaxy.games.fireworks.ai.rule.ProductionRuleAgent;
import com.fossgalaxy.games.fireworks.ai.rule.Rule;
import com.fossgalaxy.games.fireworks.ai.rule.RuleSet;
import com.fossgalaxy.games.fireworks.ai.vanDenBergh.VanDenBerghFactory;
import com.fossgalaxy.games.fireworks.annotations.Beta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by webpigeon on 01/12/16.
 */
public class AgentUtils {
    private static final Map<String, Supplier<Agent>> agents = buildMap();

    @Beta(responsible="Piers", reason="Replacing old system of kludge methods with one stop shop")
    private static final Map<String, Function<String[], Agent>> functions = buildFunctions();
    private AgentUtils() {

    }

    @Beta(responsible="Piers", reason="Replacing old system of kludge methods with one stop shop")
    private static Map<String, Function<String[], Agent>> buildFunctions(){
        Map<String, Function<String[], Agent>> map = new HashMap<>();

        // Wrapped the old supplier method to Functions that ignore arguments
        buildMap().entrySet().forEach(x -> map.put(x.getKey(), i(x.getValue())));

        // Add better way of things here

        return map;
    }


    /**
     * Register your customer creators here!
     * @param key The key you want to use for our runners
     * @param function The function needed to convert a String[] into the agent
     */
    @Beta(responsible="Piers", reason="Replacing old system of kludge methods with one stop shop")
    public static void addAgentFunction(String key, Function<String[], Agent> function){
        if(!functions.containsKey(key)){
            functions.put(key, function);
        }
    }
    @Beta(responsible="Piers", reason="Replacing old system of kludge methods with one stop shop")
    private static Function<String[], Agent> i(Supplier<Agent> s){
        return (x -> s.get());
    }

    private static Map<String, Supplier<Agent>> buildMap() {
        Map<String, Supplier<Agent>> map = new HashMap<>();
        map.put("pure_random", RandomAgent::new);
        map.put("random", OsawaFactory::buildRandom);
        map.put("internal", OsawaFactory::buildInternalState);
        map.put("outer", OsawaFactory::buildOuterState);
        map.put("cautious", IGGIFactory::buildCautious);
        map.put("iggi", IGGIFactory::buildIGGIPlayer);
        map.put("iggi_risky", IGGIFactory::buildRiskyPlayer);
        map.put("legal_random", IGGIFactory::buildRandom);
        map.put("mcts", MCTS::new);
        map.put("cautiousMCTS", IGGIFactory::buildCautiousMCTS);
        map.put("hat", HatGuessing::new);
        map.put("piers", IGGIFactory::buildPiersPlayer);
        map.put("flatmc-legal_random", () -> new MonteCarloSearch(buildAgent("legal_random")));
        map.put("flatmc-inner", () -> new MonteCarloSearch(buildAgent("inner")));
        map.put("flatmc-iggi", () -> new MonteCarloSearch(buildAgent("iggi")));
        map.put("flatmc-flawed", () -> new MonteCarloSearch(buildAgent("flawed")));
        map.put("vandenbergh", VanDenBerghFactory::buildAgent);
        map.put("flawed", IGGIFactory::buildFlawedPlayer);
        map.put("rmhc", RMHC::new);


        //Non-depth limited mcts versions
        map.put("mctsND", () -> new MCTS(MCTS.DEFAULT_ITERATIONS, MCTS.NO_LIMIT, MCTS.NO_LIMIT));

        return map;
    }

    @Beta(responsible="Piers", reason="Replacing old system of kludge methods with one stop shop")
    public static Agent buildAgent(String name, String... args){
        Function<String[], Agent> agentFunction = functions.get(name);
        if(agentFunction == null){
            throw new IllegalArgumentException("Unknown agent type: " + name);
        }
        return agentFunction.apply(args);
    }


    public static Agent buildAgent(String name) {
        Supplier<Agent> agentSupplier = agents.get(name);
        if (agentSupplier == null) {
            throw new IllegalArgumentException("unknown agent type " + name);
        }
        return agentSupplier.get();
    }

    /**
     * Allow creation of other forms of predictors
     *
     * This allows the creation of noisey/learned models to be injected into the agent.
     *
     * @param name the name to generate the predictor from
     * @return the new predictor
     */
    public static Agent buildPredictor(String name) {
        if (name.startsWith("noisy")) {
            String[] parts = name.split(":");
            double th = Double.parseDouble(parts[1]);
            return new NoisyPredictor(th, buildAgent(parts[2]));
        }

        return buildAgent(name);
    }

    public static Agent[] buildPredictors(int myID, int size, String paired) {
        Agent[] agents = new Agent[size];
        for (int i = 0; i < size; i++) {
            if (i == myID) {
                agents[i] = null;
            } else {
                agents[i] = buildPredictor(paired);
            }
        }

        return agents;
    }

    public static Agent[] buildPredictors(int myID, String... paired) {
        Agent[] agents = new Agent[paired.length];
        for (int i = 0; i < paired.length; i++) {
            if (i == myID) {
                agents[i] = null;
            } else {
                agents[i] = buildAgent(paired[i]);
            }
        }

        return agents;
    }

    public static Agent buildAgent(int[] rules){
        ProductionRuleAgent pra = new ProductionRuleAgent();
        ArrayList<Rule> actualRules = RuleSet.getRules();
        for(int rule : rules){
            if(rule == -1) break;
            pra.addRule(actualRules.get(rule));
        }
        return pra;
    }

}
