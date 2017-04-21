package com.fossgalaxy.games.fireworks.human.ui.pretty;

import com.fossgalaxy.games.fireworks.human.ui.GameView;
import com.fossgalaxy.games.fireworks.state.GameState;

import javax.swing.*;
import java.awt.*;

/**
 * Created by webpigeon on 21/04/17.
 */
public class InfoComponent extends JComponent {
    protected final GameState state;
    protected final Stroke outline = new BasicStroke(2);

    public InfoComponent(GameState state) {
        this.setPreferredSize(new Dimension(35*8, 35));
        this.state = state;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int pad = 5;
        int radius = 30;
        int info = state.getInfomation();

        int tokenWidth = getWidth() / (radius + pad);
        int currCol = 0;
        int currRow = 0;

        Graphics2D g2 = (Graphics2D)g;
        g2.translate(10, 10);
        g2.setStroke(outline);

        for (int i=0; i<state.getStartingInfomation(); i++) {
                g2.setColor(GameView.TANGO_BLUE);

                if (info > 0) {
                    g2.fillOval(currCol * radius + (pad * currCol), currRow * radius + (pad * currRow), radius, radius);
                }

                g2.setColor(GameView.TANGO_BLUE.darker().darker());
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
