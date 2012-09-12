package com.msxt.test;

import java.awt.*;
import javax.swing.*;

public class TestGroupLayout extends JFrame{
   
	private static final long serialVersionUID = -5999544049795081064L;
	
	public TestGroupLayout() {
	    super("Find");
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    //String laf = UIManager.getCrossPlatformLookAndFeelClassName();
	    String laf = UIManager.getSystemLookAndFeelClassName();
	    try {
	         UIManager.setLookAndFeel(laf);
	    } catch (UnsupportedLookAndFeelException exc) {
	    	System.err.println("Warning: UnsupportedLookAndFeel: " + laf);
	    } catch (Exception exc) {
	    	System.err.println("Error loading " + laf + ": " + exc);
	    }
	    
	    JLabel label1 = new JLabel("Find What:");
	    JTextField textField1 = new JTextField();
	    JCheckBox caseCheckBox = new JCheckBox("Match Case");
	    JCheckBox wholeCheckBox = new JCheckBox("Whole Words");
	    JCheckBox wrapCheckBox = new JCheckBox("Warp Around");
	    JCheckBox backCheckBox = new JCheckBox("Search Backwards");
	    JButton findButton = new JButton("Find");
	    JButton cancelButton = new JButton("Cancel");
	   
	    Container c = getContentPane();
	    GroupLayout layout = new GroupLayout(c);
	    c.setLayout(layout);
	   
	    //自动设定组件、组之间的间隙
	    layout.setAutoCreateGaps(true);
	    layout.setAutoCreateContainerGaps(true);
	
	    //LEADING -- 左对齐    BASELINE -- 底部对齐  CENTER -- 中心对齐
	    GroupLayout.ParallelGroup hpg2a = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
	    hpg2a.addComponent(caseCheckBox);
	    hpg2a.addComponent(wholeCheckBox);
	    
	    GroupLayout.ParallelGroup hpg2b = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
	    hpg2b.addComponent(wrapCheckBox);
	    hpg2b.addComponent(backCheckBox);
	
	    GroupLayout.SequentialGroup hpg2H = layout.createSequentialGroup();
	    hpg2H.addGroup(hpg2a).addGroup(hpg2b);
	  
	    GroupLayout.ParallelGroup hpg2 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
	    hpg2.addComponent(textField1);
	    hpg2.addGroup(hpg2H);
	  
	    GroupLayout.ParallelGroup hpg3 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
	    hpg3.addComponent(findButton);
	    hpg3.addComponent(cancelButton);
	
	    //水平
	    layout.setHorizontalGroup( layout.createSequentialGroup()
	                               .addComponent(label1)
	                               .addGroup(hpg2)
	                               .addGroup(hpg3));   
	   
	    //设定两个Button在水平方向一样宽
	    layout.linkSize( SwingConstants.HORIZONTAL, new Component[] { findButton, cancelButton } );
	    //layout.linkSize(SwingConstants.HORIZONTAL,new Component[] { caseCheckBox, wholeCheckBox, wrapCheckBox, backCheckBox});
	
	    GroupLayout.ParallelGroup vpg1 = layout.createParallelGroup(GroupLayout.Alignment.BASELINE);
	    vpg1.addComponent(label1);
	    vpg1.addComponent(textField1);
	    vpg1.addComponent(findButton);
	   
	    GroupLayout.ParallelGroup vpg2 = layout.createParallelGroup(GroupLayout.Alignment.CENTER);
	    vpg2.addComponent(caseCheckBox);
	    vpg2.addComponent(wrapCheckBox);
	    vpg2.addComponent(cancelButton);
	
	    GroupLayout.ParallelGroup vpg3 = layout.createParallelGroup(GroupLayout.Alignment.BASELINE);
	    vpg3.addComponent(wholeCheckBox);
	    vpg3.addComponent(backCheckBox);
	
	    //垂直
	   layout.setVerticalGroup( layout.createSequentialGroup()
			   					.addGroup(vpg1)
			   					.addGroup(vpg2)
			   					.addGroup(vpg3) );
	    
	    setLocation(200,200);
	    pack();
	    setVisible(true);
  }
	
  public static void main(String[] args){
	  new TestGroupLayout();
 }
}