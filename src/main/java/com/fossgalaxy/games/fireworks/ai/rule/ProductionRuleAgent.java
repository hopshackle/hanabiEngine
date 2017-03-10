package com.fossgalaxy.games.fireworks.ai.rule;

import com.fossgalaxy.games.fireworks.ai.Agent;
import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.actions.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ProductionRuleAgent implements Agent {
    private final Logger logger = LoggerFactory.getLogger(ProductionRuleAgent.class);

    protected List<Rule> rules;

    public ProductionRuleAgent() {
        this.rules = new ArrayList<>();
    }

    public void addRule(Rule rule) {
        rules.add(rule);
    }

    @Override
    public Action doMove(int agentID, GameState state) {

        for (Rule rule : rules) {
            if (rule.canFire(agentID, state)) {
                Action selected = rule.execute(agentID, state);
                if (selected == null) {
                    logger.warn("rule "+rule+" reported it could fire, but then did not.");
                    continue;
                }

                return selected;
            }
        }

        return doDefaultBehaviour(agentID, state);
    }

    //default rule based behaviour, discard random if legal, else play random
    public Action doDefaultBehaviour(int playerID, GameState state) {
       throw new IllegalStateException("No rule fired - your rules are incomplete.");
    }

    @Override
    public String toString() {
        return "ProductionRuleAgent";
    }

    public List<Rule> getRules() {
        return rules;
    }
}
