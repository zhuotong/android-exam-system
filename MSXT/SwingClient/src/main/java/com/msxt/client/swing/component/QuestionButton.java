package com.msxt.client.swing.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import com.msxt.client.swing.border.QuestionButtonBorder;
import com.msxt.client.swing.model.Question;

public class QuestionButton extends JToggleButton implements PropertyChangeListener {
	private static final long serialVersionUID = -7757335147981397019L;
	
	private static final Border buttonBorder = new CompoundBorder(new QuestionButtonBorder(), new EmptyBorder(0, 18, 0, 0)); 
	private Color finishedForeground = new Color(245, 20, 80);
	private Color unFinishedForeground = new Color(100, 100, 150);
	private final ActionListener demoActionListener = new DemoActionListener();
	
	private Question question;
    private JScrollPane examScrollPane;
	
    public QuestionButton(Question q) {
        this.question = q;
        String demoName = q.getName();
        setText(demoName);
        setIcon(question.getIcon());
        setIconTextGap(10);
        setHorizontalTextPosition(JToggleButton.TRAILING);
        setHorizontalAlignment(JToggleButton.LEADING);
        setOpaque(false);
        setBorder(buttonBorder);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setToolTipText(q.getShortDescription());
        addActionListener(demoActionListener);
    }
    
    @Override
    public void updateUI() {
        super.updateUI();
        // some look and feels replace our border, so take it back
        setBorder(buttonBorder);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        if (isSelected()) {
            setBackground(UIManager.getColor("Tree.selectionBackground"));
            g.setColor(UIManager.getColor("Tree.selectionBackground"));
            Dimension size = getSize();
            g.fillRect(0, 0, size.width, size.height); 
            setForeground(UIManager.getColor("Tree.selectionForeground"));
        } else {
            setBackground(UIManager.getColor("ToggleButton.background"));
            Color foreground = UIManager.getColor("ToggleButton.foreground");
            switch(question.getState()) {
                case FINISHED: {
                	foreground = unFinishedForeground;
                    break;
                }
                case UNFINISH: {
                	foreground = finishedForeground;
                    
                }
            }
            setForeground(foreground);
        }
        super.paintComponent(g);
    }
    
    public Question getQuestion() {
        return question;
    }
    
    private class DemoActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            QuestionButton button = (QuestionButton)event.getSource();
            int posit = button.getQuestion().getQuestionPanel().getVerticalStartPosition();
            examScrollPane.getVerticalScrollBar().setValue(posit);
        }
    }

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String newValue = (String)evt.getNewValue();
		Question.State state = Question.State.valueOf( newValue );
		if( state != question.getState() ) {
			question.setState(state);
			repaint();
		}
	}

	public void setExamScrollPane(JScrollPane examScrollPane) {
		this.examScrollPane = examScrollPane;
	}
}