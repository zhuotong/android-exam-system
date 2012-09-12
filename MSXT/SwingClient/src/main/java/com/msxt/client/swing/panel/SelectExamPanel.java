package com.msxt.client.swing.panel;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.msxt.client.model.LoginSuccessResult;
import com.msxt.client.model.Examination;
import com.msxt.client.model.transfer.Message2ModelTransfer;
import com.msxt.client.server.ServerProxy;
import com.msxt.client.server.ServerProxy.Result;

/**
 *
 * @author felix
 */
public class SelectExamPanel extends JPanel {
	private static final long serialVersionUID = 7525308717614863623L;
	
	private LoginSuccessResult lsr;
	
	private JButton start;
    private JComboBox examCB;
    private javax.swing.JScrollPane jScrollPane1;
    private JTextArea desc;
	private Examination exam = null; 
    
    private JDialog dialog = null;
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
        examCB = new JComboBox();
        start = new JButton("开始");

        desc.setColumns(20);
        desc.setRows(5);
        jScrollPane1.setViewportView( desc );
        desc.setText( lsr.getExaminations().get(0).getDesc() );
        
        examCB.setModel( getCBModel() );
        
        start.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doStart();
			}
		});
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setAutoCreateGaps(true);
	    layout.setAutoCreateContainerGaps(true);
	    
	    SequentialGroup h1 = layout.createSequentialGroup();
	    h1.addComponent( examCB ).addComponent( start );
        
	    ParallelGroup h = layout.createParallelGroup( javax.swing.GroupLayout.Alignment.LEADING ); 
	    h.addGroup( h1 ).addComponent( jScrollPane1 );
	    
	    layout.setHorizontalGroup( h );
	    
	    ParallelGroup v1 = layout.createParallelGroup();
	    v1.addComponent( examCB ).addComponent( start );
	    
	    SequentialGroup v = layout.createSequentialGroup();
	    v.addGroup( v1 ).addComponent( jScrollPane1 );
	    
	    layout.setVerticalGroup( v );
	    
//        layout.setHorizontalGroup(
//            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addGroup(layout.createSequentialGroup()
//                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                    .addGroup(layout.createSequentialGroup()
//                        .addGap(59, 59, 59)
//                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
//                            .addComponent(jScrollPane1)
//                            .addComponent(examCB, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
//                    .addGroup(layout.createSequentialGroup()
//                        .addGap(150, 150, 150)
//                        .addComponent(start)))
//                .addContainerGap(79, Short.MAX_VALUE))
//        );
//        layout.setVerticalGroup(
//            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
//                .addContainerGap(53, Short.MAX_VALUE)
//                .addComponent(examCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
//                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
//                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
//                .addGap(18, 18, 18)
//                .addComponent(start)
//                .addGap(80, 80, 80))
//        );
    }
    
    public Examination selectExamination( Frame parent ) {
         // locate the owner frame
         Frame owner;
         if (parent instanceof Frame)
            owner = (Frame) parent;
         else 
            owner = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, parent);

         // if first time, or if owner has changed, make new dialog
         if ( dialog == null || dialog.getOwner() != owner ) {      
            dialog = new JDialog(owner, true);
            dialog.add(this);
            dialog.pack();
         }
         // set title and show dialog
         dialog.setTitle( "选择试卷" );
         dialog.setVisible(true);      
         while( true ) {
        	 if( exam==null ) 
        		 dialog.setVisible(true);
        	 else
        		 break;
         }
         return exam;
	}
        
    private ComboBoxModel getCBModel(){
    	DefaultComboBoxModel model = new DefaultComboBoxModel();
    	for( LoginSuccessResult.Examination le : lsr.getExaminations() )
    		model.addElement( le );
    	return model;
    }
    
    private void doStart(){
    	ServerProxy sp = ServerProxy.Factroy.getCurrrentInstance();
    	String examId = ((LoginSuccessResult.Examination)examCB.getSelectedItem()).getId(); 
    	Result result = sp.getExam( examId );
    	if( result.getStatus() == ServerProxy.STATUS.ERROR ) {
    		JOptionPane.showMessageDialog( this, result.getErrorMessage() );
    	} else {
    		try{
		    	DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		        ByteArrayInputStream is = new ByteArrayInputStream( result.getSuccessMessage().getBytes() );
		        Document doc = db.parse( is );
		        is.close();
		        Element root = doc.getDocumentElement();
		        if( root.getTagName().equals( "error" ) ) {
		        	String desc = root.getElementsByTagName("desc").item(0).getTextContent();
		        	JOptionPane.showMessageDialog(this, desc, "登录失败", JOptionPane.ERROR_MESSAGE);
		        } else {
		        	exam = Message2ModelTransfer.Factory.getInstance().parseExamination( root );
		        	dialog.setVisible( false );
		        }
    		} catch (Exception e) {
    			e.printStackTrace();
    			JOptionPane.showMessageDialog(this, "错误消息格式");
			}
    	}
    }
}