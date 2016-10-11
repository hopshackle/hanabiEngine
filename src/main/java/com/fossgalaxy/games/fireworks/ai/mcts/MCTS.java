package com.fossgalaxy.games.fireworks.ai.mcts;

import com.fossgalaxy.games.fireworks.DebugUtils;
import com.fossgalaxy.games.fireworks.ai.Agent;
import com.fossgalaxy.games.fireworks.ai.iggi.Utils;
import com.fossgalaxy.games.fireworks.ai.rule.logic.DeckUtils;
import com.fossgalaxy.games.fireworks.state.*;
import com.fossgalaxy.games.fireworks.state.actions.Action;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by WebPigeon on 09/08/2016.
 */
public class MCTS implements Agent {
    private final int roundLength;
    private final int rolloutDepth;
    private final int treeDepthMul;
    protected Random random;

    private boolean printDebug = true;

    public MCTS() {
        this(50_000, 18, 1);
    }

    public MCTS(int roundLength){
        this(roundLength, 18, 1);
    }

    public MCTS(int roundLength, int rolloutDepth, int treeDepthMul){
        this.roundLength = roundLength;
        this.rolloutDepth = rolloutDepth;
        this.treeDepthMul = treeDepthMul;
        this.random = new Random();
    }

    @Override
    public Action doMove(int agentID, GameState state) {
        assert !state.isGameOver() : "why are you asking me for a move?";
        MCTSNode root = new MCTSNode(
                (agentID + state.getPlayerCount() - 1) % state.getPlayerCount(),
                null,
                Utils.generateAllActions(agentID, state.getPlayerCount())
        );

        Map<Integer, List<Card>> possibleCards = DeckUtils.bindCard(agentID, state.getHand(agentID), state.getDeck().toList());
        List<Integer> bindOrder = DeckUtils.bindOrder(possibleCards);

        if (printDebug) {
            System.err.println("possible bindings");
            possibleCards.forEach((slot, cards) -> System.err.format("\t %d %s\n", slot, DebugUtils.getHistStr(DebugUtils.histogram(cards))) );
        }

        if (printDebug) {
            // Guaranteed cards
            System.err.println("Guaranteed Cards");
            possibleCards.entrySet().stream()
                    .filter(x -> x.getValue().size() == 1)
                    .forEach(MCTS::printCard);

            System.err.println("We know the value of these");
            possibleCards.entrySet().stream()
                    .filter(x -> x.getValue().stream().allMatch(y -> y.value.equals(x.getValue().get(0).value)))
                    .forEach(MCTS::printCard);
        }
        GameState invarCheck = state.getCopy();

        int treeDepth = (state.getPlayerCount()*treeDepthMul) + 1;
        if (printDebug) {
            DebugUtils.printTable(System.err, state);
            System.err.println();
        }

        for (int round = 0; round < roundLength; round++) {
            //find a leaf node
            GameState currentState = state.getCopy();
            IterationObject iterationObject = new IterationObject(agentID);

            Map<Integer, Card> myHandCards = DeckUtils.bindCards(bindOrder, possibleCards);

            Deck deck = currentState.getDeck();
            Hand myHand = currentState.getHand(agentID);
            for (int slot = 0; slot < myHand.getSize(); slot++) {
                Card hand = myHandCards.get(slot);
                myHand.setCard(slot, hand);
                deck.remove(hand);
            }
            deck.shuffle();

            MCTSNode current = select(root, currentState, iterationObject);
            if (current.getDepth() < treeDepth) {
                current = expand(current, currentState);
            }

            int score = rollout(currentState, agentID);
            int scoreGained = score - state.getScore();
            int livesLost = iterationObject.getLivesLostMyGo();

            int backupScore = (livesLost * -1000) +
                    //(scoreGained * 100) +
                    (iterationObject.getPointsGainedMyGo() * 1000);

            current.backup(score);
        }

        if (printDebug) {
            System.err.println("\t next player's moves considerations: ");
            for (MCTSNode level1 : root.getChildren()) {
                System.err.println(level1.getAction()+"'s children");
                level1.printChildren();
            }
        }


        assert invarCheck.getHand(agentID).equals(state.getHand(agentID)) : "state was not invariant";
        Action chosenOne = root.getBestNode().getAction();
        if (printDebug) {
            System.err.format("Move Chosen by %d was %s", agentID, chosenOne);
            System.err.println();
            root.printChildren();
        }
        return chosenOne;
    }

    protected MCTSNode select(MCTSNode root, GameState state, IterationObject iterationObject) {
        MCTSNode current = root;
        while (!state.isGameOver() && current.fullyExpanded(state, (current.getAgent() + 1) % state.getPlayerCount())) {
            int agent = current.getAgent();
            int lives = state.getLives();
            int score = state.getScore();
            Action action = current.getAction();
            if (action != null) {
                action.apply(agent, state);
            }
            if (iterationObject.isMyGo(agent)) {
                if (state.getLives() < lives) iterationObject.incrementLivesLostMyGo();
                if (state.getScore() > score) iterationObject.incrementPointsGainedMyGo();
            }
            MCTSNode next = current.getUCTNode(state);
            if (next == null) {
                //XXX if all follow on states explored so far are null, we are now a leaf node
                return current;
            }
            current = next;
        }
        return current;
    }

    /**
     * Select a new action for the expansion node.
     *
     * @param state   the game state to travel from
     * @param agentID the AgentID to use for action selection
     * @param node    the Node to use for expansion
     * @return the next action to be added to the tree from this state.
     */
    protected Action selectActionForExpand(GameState state, MCTSNode node, int agentID) {
        Collection<Action> legalActions = node.getLegalMoves(state, agentID);
        assert !legalActions.isEmpty() : "no legal moves from this state";

        if (legalActions.isEmpty()) return null;
        Iterator<Action> actionItr = legalActions.iterator();

        int selected = random.nextInt(legalActions.size());
        Action curr = actionItr.next();
        for (int i = 0; i < selected; i++) {
            curr = actionItr.next();
        }

        return curr;
    }

    protected MCTSNode expand(MCTSNode parent, GameState state) {
        int nextAgentID = (parent.getAgent() + 1) % state.getPlayerCount();
        Action action = selectActionForExpand(state, parent, nextAgentID);
        // It is possible it wasn't allowed
        if (action == null) return parent;
        //XXX we may expand a node which we already visited? :S
        MCTSNode child = new MCTSNode(parent, nextAgentID, action, Utils.generateAllActions(nextAgentID, state.getPlayerCount()));
        parent.addChild(child);
        return child;
    }

    protected int rollout(GameState state, final int agentID) {
        int playerID = agentID;
        int moves = 0;

        while (!state.isGameOver() && moves < rolloutDepth) {
            Collection<Action> legalActions = Utils.generateActions(playerID, state);
            assert !legalActions.isEmpty() : "no legal actions in rollout";
            Action action = legalActions.iterator().next();
            action.apply(playerID, state);
            playerID = (playerID + 1) % state.getPlayerCount();
            moves++;
        }
        return state.getScore();
    }

    public void setPrintDebug(boolean printDebug){
        this.printDebug = printDebug;
    }

    public String toString() {
        return "MCTS";
    }

    private static void printCard(Map.Entry<Integer, List<Card>> entry) {
        System.err.println("\t" + entry.getKey() + ":" + entry.getValue());
    }

}
