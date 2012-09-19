package com.msxt.client.swing.model;

import java.net.URL;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.msxt.client.model.Examination;
import com.msxt.client.swing.panel.QuestionPanel;

public class Question {
    
    public enum State { UNFINISH, FINISHED }
    public enum Type { SINGLE_CHOICE, MULTI_CHOICE, FILL }
    
    private Examination.Question gerQuestion;
    private String shortDescription; // used for tooltips
    private Icon icon;
        
    private QuestionPanel questionPanel;
    private State state;
    
    public Question( Examination.Question gerQuestion ) {
    	this.gerQuestion = gerQuestion;
    	state = State.UNFINISH;
        
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
    
    public boolean isFinished(){
    	return state == State.FINISHED;
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
    
	public QuestionPanel getQuestionPanel() {
		return questionPanel;
	}

	public void setQuestionPanel(QuestionPanel questionPanel) {
		this.questionPanel = questionPanel;
	}

	public State getState() {
        return state;
    }
    
    public Examination.Question getOriginalQuestion(){
    	return gerQuestion;
    }
    
    public void setState(State state) {
        this.state = state;
    }
}
