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

import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
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
	private ResourceMap resourceMap = Application.getInstance().getContext().getResourceMap();
	
	private LoginSuccessResult lsr;
	
	private JButton start;
    private JComboBox<LoginSuccessResult.Examination> examCB;
    private javax.swing.JScrollPane jScrollPane1;
    private JTextArea desc;
	private Examination currentExam = null; 
	
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
        examCB = new JComboBox<LoginSuccessResult.Examination>();
        start = new JButton( resourceMap.getString("exam.start") );

        desc.setColumns(50);
        desc.setRows(10);
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
	    h1.addComponent( examCB, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,  Short.MAX_VALUE).addComponent( start );
        
	    ParallelGroup h = layout.createParallelGroup( javax.swing.GroupLayout.Alignment.LEADING ); 
	    h.addGroup( h1 ).addComponent( jScrollPane1 );
	    
	    layout.setHorizontalGroup( h );
	    
	    ParallelGroup v1 = layout.createParallelGroup();
	    v1.addComponent( examCB ).addComponent( start );
	    
	    SequentialGroup v = layout.createSequentialGroup();
	    v.addGroup( v1 ).addComponent( jScrollPane1 );
	    
	    layout.setVerticalGroup( v );
    }
    
    public Examination selectExamination( Frame parent ) {
    	DefaultComboBoxModel<LoginSuccessResult.Examination> model = (DefaultComboBoxModel<LoginSuccessResult.Examination>)examCB.getModel();
    	
    	if( currentExam != null ) {
        	model.removeElement( model.getSelectedItem() );
        	currentExam = null;
        }
        
        if( model.getSize()==0 ) 
        	return null;
        
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
		dialog.setTitle( resourceMap.getString( "exam.select" ) );
		dialog.setLocationRelativeTo( null );
		dialog.setVisible(true);  
		while( true ) {
			if( currentExam==null ) 
				dialog.setVisible(true);
			else
				break;
		}
		return currentExam;
	}
    
    private ComboBoxModel<LoginSuccessResult.Examination> getCBModel(){
    	DefaultComboBoxModel<LoginSuccessResult.Examination> model = new DefaultComboBoxModel<LoginSuccessResult.Examination>();
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
		        	JOptionPane.showMessageDialog(this, desc, resourceMap.getString( "exam.getexam.failed" ), JOptionPane.ERROR_MESSAGE);
		        } else {
		        	currentExam = Message2ModelTransfer.Factory.getInstance().parseExamination( root );
		        	dialog.setVisible( false );
		        }
    		} catch (Exception e) {
    			e.printStackTrace();
    			JOptionPane.showMessageDialog(this, resourceMap.getString("error.badmessageformat") );
			}
    	}
    }
}