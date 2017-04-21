package com.fossgalaxy.games.fireworks.human.ui.pretty;

import com.fossgalaxy.games.fireworks.human.ui.GameView;
import com.fossgalaxy.games.fireworks.state.Card;
import com.fossgalaxy.games.fireworks.state.CardColour;
import com.fossgalaxy.games.fireworks.state.Hand;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;

/**
 * Created by webpigeon on 20/04/17.
 */
public class CardComponent extends JComponent {
    private final Hand hand;
    private final int slot;
    private Stroke outline = new BasicStroke(5);
    private boolean hover;

    public CardComponent(Hand hand, int slot) {
        setPreferredSize(new Dimension(60, 90));

        this.hand = hand;
        this.slot = slot;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        CardColour cardColour = null;
        Color javaColour = GameView.TANGO_DARK;
        String cardVal = "?";

        Card card = hand.getCard(slot);
        if (card != null) {
            cardColour = card.colour;
            javaColour = GameView.getColor(cardColour);
            cardVal = Integer.toString(card.value);
            setToolTipText(card.toString());
        }

        g.setColor(javaColour);
        g.fillRoundRect(10, 10, getWidth()-20, getHeight()-20, 20, 20);

        if (hover) {
            g.setColor(javaColour.brighter().brighter());
        } else {
            g.setColor(javaColour.darker().darker());
        }
        g2.setStroke(outline);
        g.drawRoundRect(10, 10, getWidth()-20, getHeight()-20, 20, 20);

        //draw the numbers
        FontMetrics metrics = g.getFontMetrics();
        int w = metrics.stringWidth(cardVal)/2;

        g.setColor(Color.WHITE);
        g.drawString(cardVal, getWidth()/2 - w, getHeight()/2);
    }

    public void setHover(boolean hover) {
        this.hover = hover;
        repaint();
    }
}
