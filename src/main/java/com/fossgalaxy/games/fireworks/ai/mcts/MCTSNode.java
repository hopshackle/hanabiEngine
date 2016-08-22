package com.fossgalaxy.games.fireworks.ai.mcts;

import com.fossgalaxy.games.fireworks.state.actions.Action;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by webpigeon on 22/08/16.
 */
public class MCTSNode {
    private final Action moveToState;
    private final MCTSNode parent;
    private final List<MCTSNode> children;

    private double score;
    private int visits;

    public MCTSNode() {
        this(null, null);
    }

    public MCTSNode(Action moveToState) {
        this(null, moveToState);
    }

    public MCTSNode(MCTSNode parent, Action moveToState) {
        this.parent = parent;
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
}
