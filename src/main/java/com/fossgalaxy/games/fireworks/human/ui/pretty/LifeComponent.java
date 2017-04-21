package com.fossgalaxy.games.fireworks.human.ui.pretty;

import com.fossgalaxy.games.fireworks.human.ui.GameView;
import com.fossgalaxy.games.fireworks.state.GameState;

import javax.sound.midi.MidiDevice;
import javax.swing.*;
import java.awt.*;

/**
 * Created by webpigeon on 21/04/17.
 */
public class LifeComponent extends InfoComponent {

    public LifeComponent(GameState state) {
        super(state);
    }

    @Override
    protected void paintComponent(Graphics g) {

        //super.paintComponent(g);

        int pad = 5;
        int radius = 120 / 4;
        int info = state.getLives();

        int tokenWidth = getWidth() / (radius + pad);
        int currCol = 0;
        int currRow = 0;

        Graphics2D g2 = (Graphics2D)g;
        g2.translate(10, 10);
        g2.setStroke(outline);

        for (int i=0; i<state.getStartingLives(); i++) {
            g2.setColor(GameView.TANGO_DARK);

            if (info > 0) {
                g2.fillOval(currCol * radius + (pad * currCol), currRow * radius + (pad * currRow), radius, radius);
            }

            g2.setColor(GameView.TANGO_DARK.darker().darker());
            g2.drawOval(currCol * radius + (pad * i), currRow * radius + (pad * currRow), radius, radius);
            info--;
            currCol++;

            if (currCol >= tokenWidth) {
                currCol = 0;
                currRow++;
            }

        }

    }
}
