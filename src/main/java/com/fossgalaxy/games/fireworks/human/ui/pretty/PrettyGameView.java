package com.fossgalaxy.games.fireworks.human.ui.pretty;

import com.fossgalaxy.games.fireworks.human.ui.GameView;
import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.Hand;
import com.fossgalaxy.games.fireworks.state.actions.DiscardCard;
import com.fossgalaxy.games.fireworks.state.actions.PlayCard;
import com.fossgalaxy.games.fireworks.state.actions.TellColour;
import com.fossgalaxy.games.fireworks.state.actions.TellValue;

import javax.swing.*;
import java.awt.*;

/**
 * Created by webpigeon on 20/04/17.
 */
public class PrettyGameView extends GameView {
    private final GameState state;
    private final int playerID;
    private final HumanUIAgent player;

    public PrettyGameView(GameState state, int playerID, HumanUIAgent player) {
        super();

        this.state = state;
        this.playerID = playerID;
        this.player = player;

        this.setLayout(new FlowLayout());
        buildUI();
    }

    private void buildUI() {
        Box box = Box.createHorizontalBox();

        Box handBox = Box.createVerticalBox();
        for (int i=0; i<state.getPlayerCount(); i++) {
            if (i != playerID) {
                handBox.add(buildHand(state.getHand(i), i));
            }
        }

        JComponent myHand = buildHand(state.getHand(playerID), playerID);
        myHand.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK), myHand.getBorder()));
        handBox.add(myHand);

        box.add(handBox);

        add(box);
    }

    private JComponent buildHand(Hand hand, int player) {
        JComponent handView = Box.createHorizontalBox();
        handView.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(5,5,5,5), "player "+(player+1) ));

        for (int i=0; i<hand.getSize(); i++) {
            JComponent cardView = Box.createVerticalBox();

            Box box = Box.createHorizontalBox();
            box.add(buildIndicators(hand, i, true));
            box.add(buildCard(hand, i));
            box.add(buildIndicators(hand, i, false));

            cardView.add(box);

            Box buttons = Box.createHorizontalBox();

            if (player == playerID) {
                final int slot = i;
                JButton playBtn = new JButton("Play");
                playBtn.addActionListener(e -> forceMove(new PlayCard(slot)));
                buttons.add(playBtn);

                JButton discardBtn = new JButton("Discard");
                discardBtn.addActionListener(e -> forceMove(new DiscardCard(slot)));
                buttons.add(discardBtn);
            } else {
                final int slot = i;
                JButton playBtn = new JButton("Colour");
                playBtn.addActionListener(e -> validateTell(new TellColour(player, hand.getCard(slot).colour)));
                buttons.add(playBtn);

                JButton discardBtn = new JButton("Value");
                discardBtn.addActionListener(e -> validateTell(new TellValue(player, hand.getCard(slot).value)));
                buttons.add(discardBtn);
            }
            cardView.add(buttons);

            handView.add(cardView);
        }

        return handView;
    }

    //TODO find out why is it never legal to play cards.
    public void forceMove(com.fossgalaxy.games.fireworks.state.actions.Action action) {
        player.setMove(action);
    }

    public void validateTell(com.fossgalaxy.games.fireworks.state.actions.Action action) {
        if (action.isLegal(playerID, state)) {
            player.setMove(action);
        } else {
            JOptionPane.showMessageDialog(this, "that move is not legal");
        }
    }

    public JComponent buildCard(Hand hand, int slot) {
        return new CardComponent(hand, slot);
    }

    public JComponent buildIndicators(Hand hand, int slot, boolean isColour) {
        return new IndicatorPanel(hand, slot, isColour);
    }


    @Override
    public void setState(GameState state, int id) {
        //this.state = state;
        //this.playerID = id;
    }
}
