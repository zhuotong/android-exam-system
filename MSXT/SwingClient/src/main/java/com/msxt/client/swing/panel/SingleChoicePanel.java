package com.msxt.client.swing.panel;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.msxt.client.model.Examination;

public class SingleChoicePanel extends JPanel{
	private static final long serialVersionUID = -3380325920299172267L;
	
	public SingleChoicePanel(Examination.Question question){
		this.setLayout( new GridBagLayout() );
		
		GridBagConstraints gc1 = new GridBagConstraints();
		gc1.gridx = 0;
		gc1.gridy = 0;
		gc1.weighty = 2;
		
		GridBagConstraints gc2 = new GridBagConstraints();
		gc2.gridx = 1;
		gc2.gridy = 0;
		
		GridBagConstraints gc3 = new GridBagConstraints();
		gc3.gridx = 1;
		gc3.gridy = 1;
		
		JLabel index = new JLabel( question.getIndex() + ". " );
		index.setFont( index.getFont().deriveFont(Font.BOLD, 13) );
		
		add( index, gc1 );
		add( createContentPanel(question), gc2 );
		add( createChoiceItemPanel(question), gc3 );
	}
	
	private JPanel createContentPanel(Examination.Question question){
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout() );
		
		JLabel content = new JLabel();
		content.setText("<html>"+question.getContent().replaceAll("\n", "<br>")+"</html>" );
		panel.add( content, BorderLayout.CENTER );
		return panel;
	}
	
	private JPanel createChoiceItemPanel(Examination.Question question){
		JPanel panel = new JPanel();
		panel.setLayout( new GridBagLayout() );
		
		List<Examination.Choice> choices = question.getChoices();
		for( int i=0; i<choices.size(); i++ ) {
			Examination.Choice c = choices.get(i);
			GridBagConstraints gc1 = new GridBagConstraints();
			gc1.gridx = 0;
			gc1.gridy = i;
			gc1.weighty = 0;
			gc1.weighty = 0;
			
			GridBagConstraints gc2 = new GridBagConstraints();
			gc2.gridx = 1;
			gc2.gridy = i;
			gc2.weighty = 1.0;
			gc2.weighty = 1.0;
			
			JRadioButton l = new JRadioButton( c.getLabel() );
			JLabel item = new JLabel();
			item.setText( "<html>"+c.getContent().replaceAll("\n", "<br>")+"</html>" );
			panel.add( l, gc1 );
			panel.add( item, gc2 );
		}
		return panel;
	}
}
