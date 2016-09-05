package com.fossgalaxy.games.fireworks.ai.mcts;

import com.fossgalaxy.games.fireworks.state.actions.Action;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by webpigeon on 22/08/16.
 */
public class MCTSNode {
    private static final double EXP_CONST = Math.sqrt(2);
    private static final int MAX_SCORE = 25;
    private static final double EPSILON = 1e-6;

    private final Action moveToState;
    private final int agentId;
    private final MCTSNode parent;
    private final List<MCTSNode> children;
    private final Random random;

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
        this.random = new Random();
    }

    protected void addChild(MCTSNode node) {
        children.add(node);
    }

    public double getUCTValue() {
        if (parent == null) {
            return 0;
        }

        System.out.println(score+" "+visits+" "+parent.visits);

        return ( (score/MAX_SCORE) / visits) + EXP_CONST * Math.sqrt( Math.log(parent.visits) / visits );
    }

    public void backup(double score) {
        MCTSNode current = this;
        while (current != null) {
            current.score += score;
            current.visits++;
            current = current.parent;
        }
    }

    public boolean isLeaf() {
        return children.isEmpty();
    }

    public MCTSNode getUCTNode() {
        assert !children.isEmpty();

        double bestScore = -Double.MAX_VALUE;
        MCTSNode bestChild = null;

        if (children.isEmpty()) {
            System.out.println("EMPTY CHILDREN?!");
        }

        for (MCTSNode child : children) {
            double childScore = child.getUCTValue() + (random.nextDouble() * EPSILON);
            System.out.println(childScore);
            if (childScore > bestScore) {
                bestScore = childScore;
                bestChild = child;
            }
        }

        assert bestChild != null;
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

    public int getDepth() {
        if (parent == null) {
            return 0;
        }
        return 1 + parent.getDepth();
    }
}
