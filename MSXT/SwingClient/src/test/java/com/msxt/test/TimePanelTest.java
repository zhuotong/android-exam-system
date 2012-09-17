package com.msxt.test;

import javax.swing.JFrame;

import com.msxt.client.swing.panel.TimePanel;

public class TimePanelTest {
	public static void main( String[] args) {
		JFrame f = new JFrame();
		f.setSize( 100, 130 );
		f.setLocationRelativeTo( null );
		f.getContentPane().add( new TimePanel(20) );
		f.setVisible( true );
	}
}
