package com.msxt.client.swing.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;

import com.msxt.client.swing.utilities.Utilities;

/**
 * Simple panel subclass which renders a 2-color vertical linear gradient
 * as the background.
 * @author Aim
 */
public class GradientPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private final Color[] colors = new Color[2];
    private Image image; 
    
    public GradientPanel(Color color1, Color color2) {
        super();
        setOpaque(false);  // unfortunately required to disable automatic bg painting
        setBackground(color1); // in case colors are derived from background
        colors[0] = color1;
        colors[1] = color2;
    }
    
    public void setGradientColor1(Color color) {
        changeGradientColor(0, color);
    }
    
    public void setGradientColor2(Color color) {
        changeGradientColor(1, color);
    }
    
    protected void changeGradientColor(int colorIndex, Color newColor) {
        Color oldColor = colors[colorIndex];
        colors[colorIndex] = newColor;
        if (!oldColor.equals(newColor)) {
            image = null;
            firePropertyChange("gradientColor"+colorIndex, oldColor, newColor);
        }
    }
    
    protected Image getGradientImage() {
        Dimension size = getSize();
        if (image == null ||
                image.getWidth(null) != size.width ||
                image.getHeight(null) != size.height) {

            image = Utilities.createGradientImage(size.width, size.height, 
                    colors[0], colors[1]);
        
        }
        return image;
    }
   
    @Override
    protected void paintComponent(Graphics g) {
        Image gradientImage = getGradientImage();
        g.drawImage(gradientImage, 0, 0, null);
        super.paintComponent(g);
    }

}
