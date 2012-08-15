package com.msxt.client.swing.model;

import java.awt.Component;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.URL;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.msxt.client.model.Examination;

public class Question {
    
    public enum State { UNFINISH, FINISHED }
    public enum Type { SINGLE_CHOICE, MULTI_CHOICE, FILL }
    
    private Examination.Question gerQuestion;
    private String shortDescription; // used for tooltips
    private String answer;
    private Icon icon;
        
    private Component component;
    private State state;
    
    private PropertyChangeSupport pcs;
    
    public Question( Examination.Question gerQuestion ) {
    	this.gerQuestion = gerQuestion;
    	state = State.UNFINISH;
        pcs = new PropertyChangeSupport(this);
        
        String iconPath = null;
        if( gerQuestion.getType().equals( "Single Choice" ) ) {
        	iconPath = "/images/single_choice.gif";
        } else if( gerQuestion.getType().equals( "Multiple Choice" ) ) {
        	iconPath = "/images/multiple_choice.gif";
        }
        
        if( iconPath!=null )
        	icon = getIconFromPath(iconPath);
        
        shortDescription = gerQuestion.getName() + " " + gerQuestion.getScore() + "分";
    }
    
    public String getName() {
        return gerQuestion.getName();
    }
    
    public Icon getIcon() {
        return icon;
    }
 
    private Icon getIconFromPath(String path) {
        Icon icon = null;
        URL imageURL = this.getClass().getResource(path);
        if (imageURL != null) {
            icon = new ImageIcon(imageURL);
        }
        return icon;
    }
    
    public String getShortDescription() {
        return shortDescription;
    }
    
    void setDemoComponent(Component component) {
        this.component = component;
    }
    
    public Component getDemoComponent() {
        return component;    
    } 
    
    public State getState() {
        return state;
    }
    
    public Examination.Question getOriginalQuestion(){
    	return gerQuestion;
    }
    
    protected void setState(State state) {
        State oldState = this.state;
        this.state = state;
        pcs.firePropertyChange("state", oldState, state);
    }
    
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        pcs.addPropertyChangeListener(pcl);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        pcs.removePropertyChangeListener(pcl);
    }
}
