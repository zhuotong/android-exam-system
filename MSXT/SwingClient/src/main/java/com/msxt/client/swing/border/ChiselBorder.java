package com.msxt.client.swing.border;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.Border;

import com.msxt.client.swing.utilities.Utilities;

public class ChiselBorder implements Border {
    private Insets insets = new Insets(1, 0, 1, 0);
    
    public ChiselBorder() {            
    }
    
    public Insets getBorderInsets(Component c) {
        return insets;
    }
    public boolean isBorderOpaque() {
        return true;
    }
     public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Color color = c.getBackground();
        // render highlight at top
        g.setColor(Utilities.deriveColorHSB(color, 0, 0, .2f));
        g.drawLine(x, y, x + width, y);
        // render shadow on bottom
        g.setColor(Utilities.deriveColorHSB(color, 0, 0, -.2f));
        g.drawLine(x, y + height - 1, x + width, y + height - 1);
    }
}
