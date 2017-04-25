package com.fossgalaxy.games.fireworks.human.ui.pretty;

import com.fossgalaxy.games.fireworks.GameStats;
import com.fossgalaxy.games.fireworks.human.ui.GameView;
import com.fossgalaxy.games.fireworks.state.Card;
import com.fossgalaxy.games.fireworks.state.CardColour;
import com.fossgalaxy.games.fireworks.state.GameState;

import javax.swing.*;
import java.awt.*;

/**
 * Created by webpigeon on 21/04/17.
 */
public class TableCard extends JComponent {
    private GameState state;
    private CardColour cardColour;
    private Stroke outline = new BasicStroke(5);

    public TableCard(GameState state, CardColour colour){
        this.setPreferredSize(new Dimension(90, 135));
        this.setMinimumSize(getPreferredSize());
        this.setMaximumSize(getPreferredSize());
        this.state = state;
        this.cardColour = colour;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setStroke(outline);

        Color javaColour = GameView.getColor(cardColour);
        String cardVal = Integer.toString(state.getTableValue(cardColour));

        g.setColor(javaColour);
        g.fillRoundRect(10, 10, getWidth()-20, getHeight()-20, 20, 20);

        g.setColor(javaColour.darker().darker());
        g.drawRoundRect(10, 10, getWidth()-20, getHeight()-20, 20, 20);

        //draw the numbers
        FontMetrics metrics = g.getFontMetrics();
        int w = metrics.stringWidth(cardVal)/2;

        g.setColor(GameView.textColors.get(javaColour));
        g.drawString(cardVal, getWidth()/2 - w, getHeight()/2);
    }

}
