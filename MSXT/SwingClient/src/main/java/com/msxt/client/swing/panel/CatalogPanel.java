package com.msxt.client.swing.panel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.JPanel;

import com.msxt.client.model.Examination;

public class CatalogPanel extends JPanel{

	private static final long serialVersionUID = -5876976956518188963L;
	
	public CatalogPanel(Examination.Catalog ec){
		this.setLayout( new GridBagLayout() );
		
		List<Examination.Question> eqs = ec.getQuestions();
		for( int i=0; i<eqs.size(); i++ ) {
			Examination.Question eq = eqs.get(i);
			GridBagConstraints gc2 = new GridBagConstraints();
			gc2.gridx = 0;
			gc2.gridy = i;
			gc2.weighty = 1.0;
			gc2.weighty = 1.0;
			
			JPanel qp = new SingleChoicePanel( eq );
			add( qp, gc2 );
		}
	}
}
