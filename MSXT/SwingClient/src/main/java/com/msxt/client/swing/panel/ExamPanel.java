package com.msxt.client.swing.panel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.JPanel;

import com.msxt.client.model.Examination;

public class ExamPanel extends JPanel{
	private static final long serialVersionUID = 2469894635490125068L;
	
	public ExamPanel(Examination e){
		this.setLayout( new GridBagLayout() );
		List<Examination.Catalog> ecs = e.getCatalogs();
		for( int i=0; i<ecs.size(); i++ ) {
			Examination.Catalog ec = ecs.get(i);
			GridBagConstraints gc2 = new GridBagConstraints();
			gc2.gridx = 0;
			gc2.gridy = i;
			gc2.weighty = 1.0;
			gc2.weighty = 1.0;
			
			JPanel qp = new CatalogPanel( ec );
			add( qp, gc2 );
		}
	}
}
