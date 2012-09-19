package com.msxt.client.swing.panel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import org.jdesktop.application.Application;

public class ProcessingNotePanel extends JPanel{

	private static final long serialVersionUID = 1L;
	
	private JLabel descLabel;
	private JProgressBar process;
	JDialog dialog;
	private boolean isCanceled = false;
	
	public ProcessingNotePanel(String desc){
		descLabel = new JLabel( desc );
		process = new JProgressBar();
		process.setIndeterminate( true );
		
		setPreferredSize( new Dimension(400, 50) );
	  //setBorder( new RoundedBorder() );
		setLayout( new BorderLayout() );
		add( descLabel, BorderLayout.NORTH );
		add( process, BorderLayout.CENTER );
	}
	
	public void showProcessing(Frame parent){
		// locate the owner frame
        Frame owner;
        if (parent instanceof Frame)
           owner = (Frame) parent;
        else 
           owner = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, parent);
        String title = Application.getInstance().getContext().getResourceMap().getString("processing"); 
        		
        
        dialog = new JDialog(owner, true);
      //dialog.setUndecorated( true );
        dialog.setTitle(title);
        dialog.add(this);
        dialog.pack();
        dialog.setLocationRelativeTo( null );
        
        new Thread(){
        	public void run(){
        		dialog.setVisible(true); 
        		while( !isCanceled ) {
        			dialog.setVisible( true );
        		}
        	}
        }.start();
	}
	
	public void cancelShowProcessing(){
		isCanceled = true;
		dialog.setVisible( false );
	}
}
