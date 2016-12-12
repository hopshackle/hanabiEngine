package com.fossgalaxy.games.fireworks.ai.rule;

import com.fossgalaxy.games.fireworks.ai.Agent;
import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.actions.Action;
import com.fossgalaxy.games.fireworks.state.actions.DiscardCard;
import com.fossgalaxy.games.fireworks.state.actions.PlayCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProductionRuleAgent implements Agent {
    private List<Rule> rules;

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
                return rule.execute(agentID, state);
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

}
