package com.msxt.client.swing.panel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.JPanel;

public abstract class QuestionPanel extends JPanel{
	
	private static final long serialVersionUID = 7385952284010102827L;
	
	private PropertyChangeSupport pcs;
	
	public QuestionPanel(){
		pcs = new PropertyChangeSupport(this);
	}
	
	public void addStateChangeListener(PropertyChangeListener pcl) {
        pcs.addPropertyChangeListener(pcl);
    }
    
    public void removeStateChangeListener(PropertyChangeListener pcl) {
        pcs.removePropertyChangeListener(pcl);
    }
	
    public void fireFinished(){
        pcs.firePropertyChange("state", "UNFINISH", "FINISHED");
    }
    
    public void fireUnfinish(){
    	pcs.firePropertyChange("state", "FINISHED", "UNFINISH");
    }
    
	public abstract String getAnswer();
}
