package com.msxt.client.swing.panel;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.JPanel;

import com.msxt.client.model.Examination;
import com.msxt.client.swing.model.ExamBuildContext;
import com.msxt.client.swing.utilities.GBC;

public class ExamPanel extends JPanel{
	private static final long serialVersionUID = 2469894635490125068L;
	
	public ExamPanel(ExamBuildContext ebc){
		setLayout( new GridBagLayout() );
		setBackground( Color.WHITE );
		List<Examination.Catalog> ecs = ebc.getExam().getCatalogs();
		for( int i=0; i<ecs.size(); i++ ) {
			Examination.Catalog ec = ecs.get(i);		
			JPanel cp = new CatalogPanel( ec, ebc );
			cp.setOpaque( false );
			add( cp, new GBC(0,i).setWeight(100, 100).setFill(10).setFill( GBC.HORIZONTAL ) );
		}
	}
}
