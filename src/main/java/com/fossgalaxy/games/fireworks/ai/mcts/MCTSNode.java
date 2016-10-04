package com.fossgalaxy.games.fireworks.ai.mcts;

import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.actions.Action;

import java.util.*;
import java.util.stream.Collectors;

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
    private final Collection<Action> allUnexpandedActions;
    private final Random random;

    private double score;
    private int visits;

    public MCTSNode(Collection<Action> allUnexpandedActions) {
        this(null, -1, null, allUnexpandedActions);
    }

    public MCTSNode(int agentID, Action moveToState, Collection<Action> allUnexpandedActions) {
        this(null, agentID, moveToState, allUnexpandedActions);
    }

    public MCTSNode(MCTSNode parent, int agentId, Action moveToState, Collection<Action> allUnexpandedActions) {
        this.parent = parent;
        this.agentId = agentId;
        this.moveToState = moveToState;
        this.score = 0;
        this.visits = 0;
        this.children = new ArrayList<>();
        this.allUnexpandedActions = allUnexpandedActions;
        this.random = new Random();
        assert (parent != null && moveToState != null) || (parent == null && moveToState == null);
    }

    protected void addChild(MCTSNode node) {
        allUnexpandedActions.remove(node.getAction());
        children.add(node);
    }

    public double getUCTValue() {
        if (parent == null) {
            return 0;
        }

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

    public MCTSNode getUCTNode(GameState state) {
        assert !children.isEmpty() : "no valid child nodes";

        double bestScore = -Double.MAX_VALUE;
        MCTSNode bestChild = null;

        for (MCTSNode child : children) {
            double childScore = child.getUCTValue() + (random.nextDouble() * EPSILON);

            //XXX Hack to check if the move is legal in this version
            Action moveToMake = child.moveToState;
            if(!moveToMake.isLegal(child.agentId, state)){
                continue;
            }

            if (childScore > bestScore) {
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
        double bestScore = -Double.MAX_VALUE;
        MCTSNode bestChild = null;

        for (MCTSNode child : children) {
            double childScore = child.score / child.visits + (random.nextDouble() * EPSILON);
            if (childScore > bestScore ) {
                bestScore = childScore;
                bestChild = child;
            }
        }

        assert bestChild != null;
        return bestChild;
    }

    public int getDepth() {
        if (parent == null) {
            return 0;
        }
        return 1 + parent.getDepth();
    }

    public String toString() {
        return String.format("NODE(%d: %s %f)", getDepth(), moveToState, score);
    }

    public int getChildSize() {
        return children.size();
    }

    public boolean containsChild(Action moveToChild){
        for(MCTSNode child : children){
            if(child.moveToState.equals(moveToChild)) return true;
        }
        return false;
    }

    public boolean fullyExpanded(GameState state, int nextId){
        if(allUnexpandedActions.isEmpty()) return true;
        for(Action action : allUnexpandedActions){
            if(action.isLegal(nextId, state)) return false;
        }
        return true;
    }

    public Collection<Action> getLegalMoves(GameState state, int nextId){
        return allUnexpandedActions.stream().filter(action -> action.isLegal(nextId, state)).collect(Collectors.toList());
    }

    public Collection<Action> getAllActionsExpandedAlready(){
        ArrayList<Action> actions = new ArrayList<>();
        for(MCTSNode node : children) actions.add(node.getAction());
        return actions;
    }
}
