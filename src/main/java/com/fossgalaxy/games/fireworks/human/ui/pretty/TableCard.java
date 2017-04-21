package com.fossgalaxy.games.fireworks.human.ui.pretty;

import com.fossgalaxy.games.fireworks.GameStats;
import com.fossgalaxy.games.fireworks.human.ui.GameView;
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

    public TableCard(GameState state, CardColour colour){
        this.setPreferredSize(new Dimension(60, 90));
        this.state = state;
        this.cardColour = colour;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(GameView.getColor(cardColour));
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(GameView.getColor(cardColour).darker().darker());
        g.drawRect(0, 0, getWidth(), getHeight());

        //draw the numbers
        FontMetrics metrics = g.getFontMetrics();
        int h = metrics.getHeight();

        g.setColor(Color.WHITE);
        g.drawString(Integer.toString(state.getTableValue(cardColour)), 5, h);
    }
}
