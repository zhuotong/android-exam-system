package com.msxt.client.swing.launcher;

import javax.swing.JFrame;

import com.msxt.client.model.LoginSuccessResult;
import com.msxt.client.swing.panel.LoginPanel;
import com.msxt.client.swing.panel.SelectExamPanel;

/**
 * @author felix
 */
public class DesktopLauncher {
	public static JFrame LOGIN_FRAME;
	public static JFrame SELECT_EXAM_FRAME;
	
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    	createLoginFrame();
    }
    
    public static void createLoginFrame(){
    	LOGIN_FRAME = new JFrame();
    	LOGIN_FRAME.getContentPane().add( new LoginPanel() );
    	LOGIN_FRAME.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
    	LOGIN_FRAME.setBounds( 400, 300, 400, 300); 
    	LOGIN_FRAME.setVisible(true);
    }
    
    public static void createSelectExamFram( LoginSuccessResult lsr ){
    	LOGIN_FRAME.setVisible( false );
    	SELECT_EXAM_FRAME = new JFrame();
    	SELECT_EXAM_FRAME.getContentPane().add( new SelectExamPanel(lsr) );
    	SELECT_EXAM_FRAME.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
    	SELECT_EXAM_FRAME.setBounds( 400, 300, 400, 300); 
    	SELECT_EXAM_FRAME.setVisible(true);
    }
}
