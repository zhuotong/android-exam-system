package com.msxt.client.swing.panel;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.msxt.client.model.Examination;
import com.msxt.client.swing.utilities.GBC;

public class SingleChoicePanel extends QuestionPanel{
	private static final long serialVersionUID = -3380325920299172267L;
	
	private boolean unFinish;
	private ButtonGroup bg;
	
	public SingleChoicePanel(Examination.Question question){
		unFinish = true;
		bg = new ButtonGroup();
		
		this.setLayout( new GridBagLayout() );
				
		JLabel index = new JLabel( question.getIndex() + ". " );
		index.setFont( index.getFont().deriveFont(Font.BOLD, 13) );
		
		add( index, new GBC(0, 0, 1, 2).setAnchor( GBC.NORTHEAST ) );
		add( createContentPanel(question), new GBC(1, 0).setWeight(100, 100).setInsets(1).setAnchor( GBC.WEST ).setFill( GBC.HORIZONTAL ) );
		add( createChoiceItemPanel(question), new GBC(1, 1).setWeight(100, 100).setInsets(1).setAnchor( GBC.WEST ).setFill( GBC.HORIZONTAL ) );
	}
	
	private JPanel createContentPanel(Examination.Question question){
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout() );
		
		JLabel content = new JLabel();
		content.setText("<html>"+question.getContent().replace("\n", "<br>").replace(" ", "&nbsp;")+"</html>" );
		panel.add( content, BorderLayout.CENTER );
		return panel;
	}
	
	private JPanel createChoiceItemPanel(Examination.Question question){
		JPanel panel = new JPanel();
		panel.setLayout( new GridBagLayout() );
		
		List<Examination.Choice> choices = question.getChoices();
		for( int i=0; i<choices.size(); i++ ) {
			Examination.Choice c = choices.get(i);			
			JRadioButton l = new JRadioButton( c.getLabel() + ". " );
			l.setActionCommand( c.getLabel() );
			l.addActionListener( new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if( unFinish ) {
						fireFinished();
						unFinish = false;
					}
					
				}
			});
			bg.add( l );
			
			JLabel item = new JLabel();
			item.setText( "<html>"+c.getContent().replaceAll("\n", "<br>")+"</html>" );

			panel.add( l, new GBC(0, i).setAnchor( GBC.NORTHEAST ) );
			panel.add( item, new GBC(1, i).setAnchor( GBC.WEST ).setWeight(100,  100).setInsets(1, 5, 0, 0).setFill( GBC.HORIZONTAL ) );
		}
		return panel;
	}

	@Override
	public String getAnswer() {
		return bg.getSelection().getActionCommand();
	}
}
