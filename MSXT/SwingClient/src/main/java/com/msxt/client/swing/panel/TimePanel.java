package com.msxt.client.swing.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * 
 * @author Felix Wu
 *
 */
public class TimePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private int time;
	private int usedTime = 0;
	private Timer timer;
	
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public int getUsedTime() {
		return usedTime;
	}
	public void setUsedTime(int usedTime) {
		this.usedTime = usedTime;
	}
	
	public TimePanel(final int time){
		setPreferredSize( new Dimension(25, 25) );
		setMaximumSize( new Dimension(25, 25) );
		this.time = time;	
		timer = new Timer(60000, new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				usedTime++;
				setToolTipText( "Total Time: " + time + "minutes; Used: "+ usedTime +" minutes" );
				repaint();
				if( usedTime==time )
					timer.stop();
			}
		});
		timer.start();
		setToolTipText( "Total Time: " + time + "minutes; Used: 0 minutes" );
	}
	
	public void paint(Graphics g1) { 
        Graphics2D g2 = (Graphics2D) g1;
        
        g2.setColor(Color.BLACK);
        Rectangle2D rect = new Rectangle2D.Double(0, 0, getWidth(), getHeight());
        Ellipse2D ellipse = new Ellipse2D.Double();
        ellipse.setFrame(rect);
        g2.draw(ellipse);
        
        g2.setColor(Color.green); 
        g2.fillArc(0, 0, getWidth(), getHeight(), 90, -(usedTime*360/time)); 
        
        g2.setColor( Color.RED );
        g2.drawString( String.valueOf(time), getWidth()/5, getHeight()/4*3);
    }
}
