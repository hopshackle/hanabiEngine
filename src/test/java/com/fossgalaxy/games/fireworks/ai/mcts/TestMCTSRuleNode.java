package com.fossgalaxy.games.fireworks.ai.mcts;

import com.fossgalaxy.games.fireworks.ai.rule.*;
import com.fossgalaxy.games.fireworks.state.*;
import com.fossgalaxy.games.fireworks.ai.rule.Rule;
import com.fossgalaxy.games.fireworks.ai.rule.simple.PlayIfCertain;
import com.fossgalaxy.games.fireworks.state.BasicState;
import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.actions.*;
import org.junit.*;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class TestMCTSRuleNode {

    MCTSRuleNode root;
    List<Rule> allRules;
    GameState state = new BasicState(4);

    @Before
    public void setup() {
        allRules = new ArrayList<>();
        allRules.add(new DiscardOldestFirst());
        allRules.add(new DiscardHighest());
        allRules.add(new TellAboutOnes());
        allRules.add(new TellAnyoneAboutUsefulCard());
        allRules.add(new PlayIfCertain());

        root = new MCTSRuleNode(allRules);

        state.init();
        state.setCardAt(1, 0, new Card(3, CardColour.WHITE));
        state.setCardAt(1, 1, new Card(1, CardColour.ORANGE));
        state.setCardAt(1, 2, new Card(5, CardColour.RED));
        state.setCardAt(1, 3, new Card(2, CardColour.WHITE));

        state.setCardAt(2, 0, new Card(3, CardColour.BLUE));
        state.setCardAt(2, 1, new Card(4, CardColour.WHITE));
        state.setCardAt(2, 2, new Card(2, CardColour.BLUE));
        state.setCardAt(2, 3, new Card(1, CardColour.GREEN));
    }

    @Test
    public void testSimpleInitialisation() {
        assertFalse(root.fullyExpanded(state, 0));
        List<Action> legalMoves = root.getAllLegalMoves(state, 0).collect(Collectors.toList());
        assertEquals(legalMoves.size(), 2);
        assertTrue(legalMoves.get(0).equals(new DiscardCard(0)));
        assertTrue(legalMoves.get(1).equals(new TellValue(1, 1)));
        assertEquals(root.getLegalUnexpandedMoves(state, 0).size(), 2);
        assertTrue(root.getUCTNode(state) == null);
    }

    @Test
    public void testSomeValidActionsTaken() {
        MCTSRuleNode newChild = new MCTSRuleNode(root, 0, new DiscardCard(0), allRules);
        root.addChild(newChild);
        newChild.backup(1.0);
        List<Action> legalMoves = root.getAllLegalMoves(state, 0).collect(Collectors.toList());
        assertEquals(legalMoves.size(), 2);
        assertTrue(legalMoves.get(0).equals(new DiscardCard(0)));
        assertTrue(legalMoves.get(1).equals(new TellValue(1, 1)));
        assertEquals(root.getLegalUnexpandedMoves(state, 0).size(), 1);
        assertTrue(root.getLegalUnexpandedMoves(state, 0).get(0).equals(new TellValue(1, 1)));
        assertTrue(root.getUCTNode(state).equals(newChild));

        newChild = new MCTSRuleNode(root, 0, new TellValue(1, 1), allRules);
        root.addChild(newChild);
        newChild.backup(2.0);
        legalMoves = root.getAllLegalMoves(state, 0).collect(Collectors.toList());
        assertEquals(legalMoves.size(), 2);
        assertEquals(root.getLegalUnexpandedMoves(state, 0).size(), 0);
        assertTrue(root.getUCTNode(state).equals(newChild));
    }

    @Test
    public void testSomeIllegalActionsTaken() {
        MCTSRuleNode newChild = new MCTSRuleNode(root, 0, new DiscardCard(2), allRules);
        // this is not an action that is possible from any of the rules
        root.addChild(newChild);
        newChild.backup(1.0);
        newChild = new MCTSRuleNode(root, 0, new TellColour(2, CardColour.WHITE), allRules);
        // ditto
        root.addChild(newChild);
        newChild.backup(1.0);

        List<Action> legalMoves = root.getAllLegalMoves(state, 0).collect(Collectors.toList());
        assertEquals(legalMoves.size(), 2);
        assertTrue(legalMoves.get(0).equals(new DiscardCard(0)));
        assertTrue(legalMoves.get(1).equals(new TellValue(1, 1)));
        assertEquals(root.getLegalUnexpandedMoves(state, 0).size(), 2);
        assertTrue(root.getUCTNode(state) == null);
    }

    @Test
    public void testRuleChoiceWithInformation() {
        state.setCardAt(0, 0, new Card(2, CardColour.GREEN));
        state.setCardAt(0, 1, new Card(1, CardColour.WHITE));
        (new TellValue(0, 1)).apply(3, state);
        (new TellColour(0, CardColour.WHITE)).apply(3, state);
        // this now enables two new moves - Discard 1 (as the highest known card)
        // Play 1 (as certainly valid)

        List<Action> legalMoves = root.getAllLegalMoves(state, 0).collect(Collectors.toList());
        assertEquals(legalMoves.size(), 4);
        assertTrue(legalMoves.get(0).equals(new DiscardCard(2)));       // this is now the oldest card
        assertTrue(legalMoves.get(1).equals(new DiscardCard(1)));
        assertTrue(legalMoves.get(2).equals(new TellValue(1, 1)));
        assertTrue(legalMoves.get(3).equals(new PlayCard(1)));
    }
}
