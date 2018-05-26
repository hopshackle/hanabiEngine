package com.fossgalaxy.games.fireworks.ai.mcts;

import com.fossgalaxy.games.fireworks.state.*;
import com.fossgalaxy.games.fireworks.state.actions.*;
import com.fossgalaxy.games.fireworks.ai.rule.*;

import java.util.*;
import java.util.stream.*;
import java.util.stream.Collectors;

/**
 * Created by hopshackle on 26/05/2018.
 */
public class MCTSRuleNode extends MCTSNode {

    protected final List<Rule> allRules;

    public MCTSRuleNode(Collection<Rule> possibleRules) {
        this(null, -1, null, DEFAULT_EXP_CONST, possibleRules);
    }

    public MCTSRuleNode(double expConst, Collection<Rule> possibleRules) {
        this(null, -1, null, expConst, possibleRules);
    }

    public MCTSRuleNode(int agentID, Action moveToState, Collection<Rule> possibleRules) {
        this(null, agentID, moveToState, DEFAULT_EXP_CONST, possibleRules);
    }

    public MCTSRuleNode(int agentID, Action moveToState, double expConst, Collection<Rule> possibleRules) {
        this(null, agentID, moveToState, expConst, possibleRules);
    }

    public MCTSRuleNode(MCTSRuleNode parent, int agentId, Action moveToState, Collection<Rule> possibleRules) {
        this(parent, agentId, moveToState, DEFAULT_EXP_CONST, possibleRules);
    }

    public MCTSRuleNode(MCTSRuleNode parent, int agentId, Action moveToState, double expConst, Collection<Rule> possibleRules) {
        super(parent, agentId, moveToState, expConst, new ArrayList<>());
        allRules = new ArrayList(possibleRules);
        assert (parent != null && moveToState != null) || (parent == null && moveToState == null);
    }

    @Override
    public void addChild(MCTSNode node) {
        children.add(node);
    }

    @Override
    public MCTSNode getUCTNode(GameState state) {
        double bestScore = -Double.MAX_VALUE;
        MCTSNode bestChild = null;

        int agentToAct = (getAgent() + 1) % state.getPlayerCount();
        Collection<Action> legalMoves = getAllLegalMoves(state, agentToAct).collect(Collectors.toList());
        for (Action legalAction : legalMoves) incrementParentVisit(legalAction);

        List<MCTSNode> validChildren = children.stream()
                .filter(c -> legalMoves.contains(c.moveToState))
                .collect(Collectors.toList());

        for (MCTSNode child : validChildren) {
            double childScore = child.getUCTValue() + (random.nextDouble() * EPSILON);

            if (childScore > bestScore) {
                bestScore = childScore;
                bestChild = child;
            }
        }

        return bestChild;
    }

    @Override
    public boolean fullyExpanded(GameState state, int nextId) {
        return getLegalUnexpandedMoves(state, nextId).isEmpty();
    }

    public Stream<Action> getAllLegalMoves(GameState state, int nextID) {
        return allRules.stream()
                .map(r -> r.execute(nextID, state))
                .filter(Objects::nonNull)
                .filter(a -> a.isLegal(nextID, state))
                .distinct();
    }

    @Override
    public List<Action> getLegalUnexpandedMoves(GameState state, int nextId) {
        return getAllLegalMoves(state, nextId)
                .filter(a -> !containsChild(a))
                .collect(Collectors.toList());
    }

}
