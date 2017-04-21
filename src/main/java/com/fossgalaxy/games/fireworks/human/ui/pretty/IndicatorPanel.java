package com.fossgalaxy.games.fireworks.human.ui.pretty;

import com.fossgalaxy.games.fireworks.human.ui.GameView;
import com.fossgalaxy.games.fireworks.state.CardColour;
import com.fossgalaxy.games.fireworks.state.Hand;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 * Created by webpigeon on 20/04/17.
 */
public class IndicatorPanel extends JComponent {
    private final Hand hand;
    private final int slot;
    private final boolean isColour;

    public IndicatorPanel(Hand hand, int slot, boolean isColour) {
        this.setPreferredSize(new Dimension(15, 90));
        this.hand = hand;
        this.slot = slot;
        this.isColour = isColour;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int height = getHeight()/5;

        if (isColour) {
            List<CardColour> possibleColours = Arrays.asList(hand.getPossibleColours(slot));
            CardColour[] legalColours = CardColour.values();
            for (int i = 0; i < legalColours.length; i++) {
                int y = i * height;

                g.setColor(Color.BLACK);
                g.fillRect(0, y, getWidth(), height);

                if (possibleColours.contains(legalColours[i])) {
                    g.setColor(GameView.getColor(legalColours[i]));
                    g.fillRect(0, y, getWidth(), height);
                }
                g.setColor(Color.BLACK);
                g.drawRect(0, y, getWidth(), height);
            }
        } else {
            int[] possible = hand.getPossibleValues(slot);
            boolean[] possibleB = new boolean[5];
            for (int possVal : possible) {
                possibleB[possVal-1] = true;
            }

            for (int i = 0; i < possibleB.length; i++) {
                int y = i * height;

                g.setColor(Color.BLACK);
                g.fillRect(0, y, getWidth(), height);

                FontMetrics metrics = g.getFontMetrics();

                if (possibleB[i]) {
                    int h = metrics.getHeight();
                    int w = metrics.stringWidth(Integer.toString(i+1));

                    g.setColor(Color.WHITE);
                    g.drawString(Integer.toString(i+1), w, y+h);
                }

                g.setColor(Color.BLACK);
                g.drawRect(0, y, getWidth(), height);
            }
        }


    }
}
