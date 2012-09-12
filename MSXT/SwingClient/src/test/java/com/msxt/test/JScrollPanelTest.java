package com.msxt.test;

import java.awt.GraphicsDevice;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class JScrollPanelTest {
	public static void main(String[] args){
		final JFrame frame = new JFrame();
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.setTitle( "Panel Test" );
		frame.setSize( 300,  400 );
		
		JTextArea ta = new JTextArea();
		for(int i=1; i<300; i++)
			ta.setText( ta.getText() + i +'\n' );
		ta.setText( ta.getText() + 300 );
		
		final JScrollPane panel = new JScrollPane(ta);
		frame.getContentPane().add( panel );
		
		JMenuBar mb = new JMenuBar();
		JMenu tm = new JMenu("Test");
		JMenuItem mi = tm.add( "Scroll To Button" );
		mi.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(panel, "tset");
				JScrollBar sb = panel.getVerticalScrollBar();
				System.out.println( sb.getModel() );
				sb.setValues( 290, 0, 0, 300 );
				panel.repaint();
			}
		});
		mb.add( tm );
		frame.setJMenuBar( mb );
		
		//frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		//frame.setAlwaysOnTop(true);    //总在最前面
		//frame.setResizable(false);    //不能改变大小
		//frame.setUndecorated(true);    //不要边框
		GraphicsDevice sd = frame.getGraphicsConfiguration().getDevice();
		frame.setUndecorated(true);
		frame.setBounds( sd.getDefaultConfiguration().getBounds() );
		frame.validate();
		frame.setVisible( true );
        if ( sd.isFullScreenSupported() )
        	sd.setFullScreenWindow(frame);
	}
}
