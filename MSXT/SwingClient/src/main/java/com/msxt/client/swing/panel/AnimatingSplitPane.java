package com.msxt.client.swing.panel;

import javax.swing.JSplitPane;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.interpolation.PropertySetter;

/**
 *
 * @author Amy Fowler
 */
public class AnimatingSplitPane extends JSplitPane {
    private boolean firstExpanded = false;
    
    private int lastDividerLocation = -1;
    
    public AnimatingSplitPane(int orientation) {
        super(orientation);
        setOneTouchExpandable(false);
    }
    
    public void setExpanded(boolean expanded) {
        if (expanded != firstExpanded) {
            
            if (!firstExpanded) {
                lastDividerLocation = getDividerLocation();
            }
            
            this.firstExpanded = expanded;

            Animator animator = new Animator(500, new PropertySetter(this, "dividerLocation",
                   getDividerLocation(), (expanded? getHeight() : lastDividerLocation)));
            
            animator.setStartDelay(10);
            animator.setAcceleration(.2f);
            animator.setDeceleration(.3f);
            animator.start();            
        }
    }
    
    // workaround for bug where the animator blows up without it;
    // possibly a bug in reflection when method is in super class (?)
    public void setDividerLocation(int dividerLocation) {
        super.setDividerLocation(dividerLocation);
    }
}
