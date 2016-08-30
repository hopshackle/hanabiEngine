package com.fossgalaxy.games.fireworks.ai.mcts;

import com.fossgalaxy.games.fireworks.state.actions.Action;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by webpigeon on 22/08/16.
 */
public class MCTSNode {
    private final Action moveToState;
    private final int agentId;
    private final MCTSNode parent;
    private final List<MCTSNode> children;

    private double score;
    private int visits;

    public MCTSNode() {
        this(null, -1, null);
    }

    public MCTSNode(int agentID, Action moveToState) {
        this(null, agentID, moveToState);
    }

    public MCTSNode(MCTSNode parent, int agentId, Action moveToState) {
        this.parent = parent;
        this.agentId = agentId;
        this.moveToState = moveToState;
        this.score = 0;
        this.visits = 0;
        this.children = new ArrayList<>();

        if (parent != null) {
            parent.addChild(this);
        }
    }

    protected void addChild(MCTSNode node) {
        children.add(node);
    }

    public double getUCTValue() {
        return 0.0;
    }

    public void update(double score) {

    }

    public void backup(double score) {
        MCTSNode current = this;
        while (current != null) {
            current.backup(score);
            current = current.parent;
        }
    }

    public boolean isLeaf() {
        return children.isEmpty();
    }

    public MCTSNode getUCTNode() {
        double bestScore = 0.0;
        MCTSNode bestChild = null;
        for (MCTSNode child : children) {
            double childScore = child.getUCTValue();
            if (childScore < bestScore) {
                bestScore = childScore;
                bestChild = child;
            }
        }
        return bestChild;
    }

    public int getAgent() {
        return agentId;
    }

    public Action getAction() {
        return moveToState;
    }

    public MCTSNode getBestNode() {
        double bestScore = 0.0;
        MCTSNode bestChild = null;
        for (MCTSNode child : children) {
            double childScore = child.score / child.visits;
            if (childScore < bestScore ) {
                bestScore = childScore;
                bestChild = child;
            }
        }
        return bestChild;
    }
}
