package com.msxt.client.swing.panel;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.msxt.client.model.Examination;
import com.msxt.client.swing.model.ExamBuildContext;
import com.msxt.client.swing.utilities.GBC;

public class MultiChoicePanel extends QuestionPanel{
	private static final long serialVersionUID = -3380325920299172267L;
	
	private boolean lastFinishStatus;
	private List<JCheckBox> bg;
	
	public MultiChoicePanel(Examination.Question question, ExamBuildContext ebc){
		lastFinishStatus = false;
		bg = new ArrayList<JCheckBox>();
		setOpaque( false );
		this.setLayout( new GridBagLayout() );
				
		JLabel index = new JLabel( question.getIndex() + "). " );
		index.setFont( ebc.getQuestionTitleFont() );
		
		add( index, new GBC(0, 0, 1, 2).setAnchor( GBC.NORTHEAST ) );
		add( createContentPanel(question, ebc), new GBC(1, 0).setWeight(100, 100).setInsets(1).setAnchor( GBC.WEST ).setFill( GBC.HORIZONTAL ) );
		add( createChoiceItemPanel(question, ebc), new GBC(1, 1).setWeight(100, 100).setInsets(1).setAnchor( GBC.WEST ).setFill( GBC.HORIZONTAL ) );
	}
	
	private JPanel createContentPanel(Examination.Question question, ExamBuildContext ebc){
		JPanel panel = new JPanel();
		panel.setOpaque( false );
		panel.setLayout( new BorderLayout() );
		
		JLabel content = new JLabel();
		content.setFont( ebc.getQuestionFont() );
		content.setText("<html>"+question.getContent().replace("\n", "<br>").replace(" ", "&nbsp;")+"</html>" );
		panel.add( content, BorderLayout.CENTER );
		return panel;
	}
	
	private JPanel createChoiceItemPanel(Examination.Question question, ExamBuildContext ebc){
		JPanel panel = new JPanel();
		panel.setOpaque( false );
		panel.setLayout( new GridBagLayout() );
		
		List<Examination.Choice> choices = question.getChoices();
		for( int i=0; i<choices.size(); i++ ) {
			Examination.Choice c = choices.get(i);			
			JCheckBox cb = new JCheckBox( c.getLabel() + ". " );
			cb.setFont( ebc.getQuestionFont() );
			cb.setActionCommand( c.getActualLabel() );
			cb.addActionListener( new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					boolean finishStatus = false;
					for(JCheckBox cb : bg){
						if( cb.isSelected() ) {
							finishStatus = true;
							break;
						}
					}
					if( finishStatus != lastFinishStatus ) {
						if( finishStatus )
							fireFinished();
						else
							fireUnfinish();
						lastFinishStatus = finishStatus;
					}
				}
			});
			bg.add( cb );
			
			JLabel item = new JLabel();
			item.setFont( ebc.getQuestionFont() );
			item.setText( "<html>"+c.getContent().replaceAll("\n", "<br>")+"</html>" );

			panel.add( cb, new GBC(0, i).setAnchor( GBC.NORTHEAST ) );
			panel.add( item, new GBC(1, i).setAnchor( GBC.WEST ).setWeight(100,  100).setInsets(1, 5, 0, 0).setFill( GBC.HORIZONTAL ) );
		}
		return panel;
	}

	@Override
	public String getAnswer() {
		StringBuffer sb = new StringBuffer("");
		for(JCheckBox cb : bg){
			if( cb.isSelected() ) {
				sb.append( cb.getActionCommand() );
			}
		}
		return sb.toString();
	}
	
	@Override
	public void disableEdit() {
		for( JCheckBox cb : bg ) {
			cb.setEnabled( false );
		}
	}
}
