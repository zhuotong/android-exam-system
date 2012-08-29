package com.msxt.test;

import javax.swing.JFrame;

import com.msxt.client.swing.panel.IntroPanel;

public class PanelTest {
	public static void main(String[] args){
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.setTitle( "Panel Test" );
		frame.add( new IntroPanel() );
		frame.setVisible( true );
	}
}
