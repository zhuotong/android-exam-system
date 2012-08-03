package com.msxt.client.swing.panel;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.msxt.client.model.LoginSuccessResult;

/**
 *
 * @author felix
 */
public class SelectExamPanel extends JPanel {
	private static final long serialVersionUID = 7525308717614863623L;
	
	private LoginSuccessResult lsr;
	
	private JButton start;
    private JComboBox<LoginSuccessResult.Examination> examCB;
    private javax.swing.JScrollPane jScrollPane1;
    private JTextArea desc;
	    
    /**
     * Creates new form SelectExam
     */
    public SelectExamPanel(LoginSuccessResult lsr) {
    	this.lsr = lsr;
        initComponents();
    }

    private void initComponents() {
        jScrollPane1 = new JScrollPane();
        desc = new JTextArea();
        examCB = new JComboBox<LoginSuccessResult.Examination>();
        start = new JButton("开始");

        desc.setColumns(20);
        desc.setRows(5);
        jScrollPane1.setViewportView( desc );
        
        examCB.setModel( getCBModel() );

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1)
                            .addComponent(examCB, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(150, 150, 150)
                        .addComponent(start)))
                .addContainerGap(79, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(53, Short.MAX_VALUE)
                .addComponent(examCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(start)
                .addGap(80, 80, 80))
        );
    }
    
    private ComboBoxModel<LoginSuccessResult.Examination> getCBModel(){
    	DefaultComboBoxModel<LoginSuccessResult.Examination> model = new DefaultComboBoxModel<LoginSuccessResult.Examination>();
    	for( LoginSuccessResult.Examination le : lsr.getExaminations() )
    		model.addElement( le );
    	return model;
    }
}