package com.msxt.client.swing.border;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.AbstractButton;
import javax.swing.border.Border;

import com.msxt.client.swing.utilities.Utilities;

public class QuestionButtonBorder implements Border {
    private Insets insets = new Insets(2, 1, 1, 1);
    
    public QuestionButtonBorder() {            
    }
    
    public Insets getBorderInsets(Component c) {
        return insets;
    }
    public boolean isBorderOpaque() {
        return true;
    }
     public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        AbstractButton b = (AbstractButton)c;
        if (b.isSelected()) {
            Color color = c.getBackground();
            g.setColor(Utilities.deriveColorHSB(color, 0, 0, -.20f));
            g.drawLine(x, y, x + width, y);
            g.setColor(Utilities.deriveColorHSB(color, 0, 0, -.10f));
            g.drawLine(x, y + 1, x + width, y + 1);
            g.drawLine(x, y + 2, x, y + height - 2);
            g.setColor(Utilities.deriveColorHSB(color, 0, 0, .24f));
            g.drawLine(x, y + height - 1, x + width, y + height-1);
        }
    }
}
