package com.fossgalaxy.games.fireworks.ai.mcts;

import com.fossgalaxy.games.fireworks.ai.Agent;
import com.fossgalaxy.games.fireworks.annotations.AgentBuilderStatic;
import com.fossgalaxy.games.fireworks.annotations.AgentConstructor;
import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.actions.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A version of the MCTS agent that replaces the random rollout with policy based rollouts.
 */
public class MCTSPolicy extends MCTSBugFix {
    private final Logger LOG = LoggerFactory.getLogger(MCTSPolicy.class);
    private final Agent rolloutPolicy;

    public MCTSPolicy(Agent rolloutPolicy) {
        this.rolloutPolicy = rolloutPolicy;
    }

    @AgentConstructor("mctsPolicy")
    public MCTSPolicy(double explorationC, int rolloutDepth, int treeDepthMul, int timeLimit, Agent rollout) {
        super(explorationC, rolloutDepth, treeDepthMul, timeLimit);
        this.rolloutPolicy = rollout;
    }

    @AgentBuilderStatic("mctsPolicyND")
    public static MCTSPolicy buildPolicyND(Agent rolloutPolicy) {
        return new MCTSPolicy(MCTSNode.DEFAULT_EXP_CONST, MCTS.NO_LIMIT, MCTS.NO_LIMIT, DEFAULT_TIME_LIMIT, rolloutPolicy);
    }

    /**
     * Rather than perform a random move, query a policy for one.
     *
     * @param state
     * @param playerID
     * @return
     */
    @Override
    protected Action selectActionForRollout(GameState state, int playerID) {
        try {
            return rolloutPolicy.doMove(playerID, state);
        } catch (IllegalArgumentException ex) {
            LOG.error("warning, agent failed to make move: {}", ex);
            return super.selectActionForRollout(state, playerID);
        }
    }

    @Override
    public String toString() {
        return String.format("policyMCTS(%s)", rolloutPolicy);
    }
}
