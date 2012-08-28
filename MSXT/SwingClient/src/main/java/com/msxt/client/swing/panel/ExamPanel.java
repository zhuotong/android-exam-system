package com.msxt.client.swing.panel;

import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.JPanel;

import com.msxt.client.model.Examination;
import com.mxst.client.swing.utilities.GBC;

public class ExamPanel extends JPanel{
	private static final long serialVersionUID = 2469894635490125068L;
	
	public ExamPanel(Examination e){
		setLayout( new GridBagLayout() );
		List<Examination.Catalog> ecs = e.getCatalogs();
		for( int i=0; i<ecs.size(); i++ ) {
			Examination.Catalog ec = ecs.get(i);		
			JPanel qp = new CatalogPanel( ec );
			add( qp, new GBC(0,i).setWeight(100, 100).setFill(10).setFill( GBC.HORIZONTAL ) );
		}
	}
}
