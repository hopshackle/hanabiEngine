package com.fossgalaxy.games.fireworks.human.ui.pretty;

import com.fossgalaxy.games.fireworks.human.ui.GameView;
import com.fossgalaxy.games.fireworks.state.CardColour;
import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.Hand;
import com.fossgalaxy.games.fireworks.state.actions.DiscardCard;
import com.fossgalaxy.games.fireworks.state.actions.PlayCard;
import com.fossgalaxy.games.fireworks.state.actions.TellColour;
import com.fossgalaxy.games.fireworks.state.actions.TellValue;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by webpigeon on 20/04/17.
 */
public class PrettyGameView extends GameView {
    private final GameState state;
    private final int playerID;
    private final HumanUIAgent player;
    private final Map<Integer, CardHinter> hinters;

    private JComponent myHand;


    public PrettyGameView(GameState state, int playerID, HumanUIAgent player) {
        super();

        this.hinters = new HashMap<>();
        this.state = state;
        this.playerID = playerID;
        this.player = player;

        this.setLayout(new FlowLayout());
        buildUI();
    }

    private Border hanabiBorder(String title) {
        return BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(title), BorderFactory.createEmptyBorder(5,5,5,5));
    }

    private void buildUI() {
        Box box = Box.createHorizontalBox();

        Box handBox = Box.createVerticalBox();
        for (int i=0; i<state.getPlayerCount(); i++) {
            if (i != playerID) {
                handBox.add(buildHand(state.getHand(i), i));
            }
        }

        handBox.add(Box.createVerticalStrut(50));

        myHand = buildHand(state.getHand(playerID), playerID);
        myHand.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK), myHand.getBorder()));
        handBox.add(myHand);

        box.add(handBox);

        Box middleBox = Box.createVerticalBox();

        Box table = Box.createVerticalBox();
        table.setBorder(hanabiBorder("table"));
        for (CardColour colour : CardColour.values()) {
            table.add(Box.createVerticalStrut(5));
            table.add(new TableCard(state, colour));
            table.add(Box.createVerticalStrut(5));
        }
        middleBox.add(table);
        middleBox.add(new DeckComponent(state));
        box.add(middleBox);

        Box right = Box.createVerticalBox();
        right.add(new InfoComponent(state));
        right.add(new LifeComponent(state));
        right.add(new DiscardComponent(state));
        box.add(right);

        add(box);
    }

    private JComponent buildHand(Hand hand, int player) {
        JComponent handView = Box.createHorizontalBox();
        handView.setBorder(hanabiBorder("player "+(player+1) ));

        final CardHinter hinter = new CardHinter(hand);

        for (int i=0; i<hand.getSize(); i++) {
            JComponent cardView = Box.createVerticalBox();

            CardComponent cardComp = new CardComponent(hand, i);
            hinter.setCard(i, cardComp);

            Box box = Box.createHorizontalBox();
            box.add(buildIndicators(hand, i, true));
            box.add(cardComp);
            box.add(buildIndicators(hand, i, false));

            cardView.add(box);

            Box buttons = Box.createHorizontalBox();

            if (player == playerID) {
                final int slot = i;
                JButton playBtn = new JButton("Play");
                playBtn.addActionListener(e -> forceMove(new PlayCard(slot)));
                playBtn.setToolTipText("Play this card");
                buttons.add(playBtn);

                JButton discardBtn = new JButton("Discard");
                discardBtn.addActionListener(e -> validateDiscard(new DiscardCard(slot)));
                discardBtn.setToolTipText("Discard this card");
                buttons.add(discardBtn);
            } else {
                final int slot = i;
                JButton playBtn = new JButton("Colour");
                playBtn.addActionListener(e -> validateTell(new TellColour(player, hand.getCard(slot).colour)));
                playBtn.setToolTipText("Point out this card's colour to this player");
                buttons.add(playBtn);


                playBtn.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        hinter.hoverColour(slot);
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        hinter.clearHover();
                    }
                });

                JButton discardBtn = new JButton("Value");
                discardBtn.addActionListener(e -> validateTell(new TellValue(player, hand.getCard(slot).value)));
                discardBtn.setToolTipText("Point out this card's value to this player");
                buttons.add(discardBtn);

                discardBtn.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        hinter.hoverValue(slot);
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        hinter.clearHover();
                    }
                });

            }
            cardView.add(buttons);

            handView.add(cardView);
            handView.add(Box.createHorizontalStrut(5));
        }

        hinters.put(player, hinter);

        return handView;
    }

    //TODO find out why is it never legal to play cards.
    public void forceMove(com.fossgalaxy.games.fireworks.state.actions.Action action) {
        player.setMove(action);
    }

    public void validateDiscard(com.fossgalaxy.games.fireworks.state.actions.Action action) {
        if (state.getInfomation() != state.getStartingInfomation()) {
            player.setMove(action);
        } else {
            JOptionPane.showMessageDialog(this, "You cannot discard with full information");
        }
    }

    public void validateTell(com.fossgalaxy.games.fireworks.state.actions.Action action) {
        if (action.isLegal(playerID, state)) {
            player.setMove(action);
        } else {
            JOptionPane.showMessageDialog(this, "You have no information tokens");
        }
    }


    public JComponent buildIndicators(Hand hand, int slot, boolean isColour) {
        return new IndicatorPanel(hand, slot, isColour);
    }


    @Override
    public void setState(GameState state, int id) {
        //this.state = state;
        //this.playerID = id;
    }

    Border borderMyTurn = BorderFactory.createLineBorder(Color.RED, 10);
    Border borderNotActive = BorderFactory.createLineBorder(Color.BLACK, 10);

    @Override
    public void setPlayerMoveRequest(boolean playerMoveRequest) {
        super.setPlayerMoveRequest(playerMoveRequest);

        if (playerMoveRequest) {
            myHand.setBorder(borderMyTurn);
        } else {
            myHand.setBorder(borderNotActive);
        }
    }
}
