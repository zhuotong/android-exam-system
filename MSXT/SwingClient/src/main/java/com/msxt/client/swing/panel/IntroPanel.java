package com.msxt.client.swing.panel;

import com.msxt.client.swing.launcher.ExamLauncher;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.interpolation.PropertySetter;

/**
 *
 * @author Amy Fowler
 */
public class IntroPanel extends RoundedPanel {

	private static final long serialVersionUID = -6490600667239140131L;
	private JLabel introImage;
    private SlidingLabel introText;
    
    public IntroPanel() {
        setLayout(null);
        setOpaque(false);

        introImage = new JLabel(new ImageIcon(
        		ExamLauncher.class.getResource("resources/images/home_notext.png")));
        introImage.setVerticalAlignment(JLabel.TOP);
        
        introText = new SlidingLabel(new ImageIcon(
        		ExamLauncher.class.getResource("resources/images/home_text.png")));
        introText.setVisible(false);
        
        introImage.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent event) {
                slideTextIn();
            }
        });
        
        add(introText);
        add(introImage);
    }
    
    @Override
    public void doLayout() {
        Dimension size = getSize();
        Insets insets = getInsets();
        int w = size.width - insets.left - insets.right;
        
        Dimension prefSize = introImage.getPreferredSize();
        introImage.setBounds(0, 0, prefSize.width, prefSize.height);
        
        if (introText.isVisible()) {
            prefSize = introText.getPreferredSize();
            introText.setBounds(introText.getX(), introText.getY(),
                    prefSize.width, prefSize.height);
        }
        
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (!introText.isVisible()) {
            introText.setLocation(1000, 110);
            introText.setVisible(true);
            slideTextIn();
        }
    }
    
    public void slideTextIn() {
        Animator animator = new Animator(800, 
                new PropertySetter(introText, "x", getWidth(), 30));
        animator.setStartDelay(800);
        animator.setAcceleration(.2f);
        animator.setDeceleration(.5f);
        animator.start();
    }
    
    public void slideTextOut() {
        Animator animator = new Animator(600, 
                new PropertySetter(introText, "x", introText.getX(), -introText.getWidth()));
        animator.setStartDelay(10);
        animator.setAcceleration(.5f);
        animator.setDeceleration(.2f);
        animator.start();        
    }
    
    public class SlidingLabel extends JLabel {
        public SlidingLabel(Icon icon) {
            super(icon);
        }
        
        public void setX(int x) {
            setLocation(x, getY());
            revalidate();
        }
    }
}
