package com.msxt.client.swing.panel;

import com.msxt.client.model.Examination;
import com.msxt.client.model.Examination.Catalog;
import com.msxt.client.swing.border.ChiselBorder;
import com.msxt.client.swing.component.QuestionButton;
import com.msxt.client.swing.launcher.ExamLauncher;
import com.msxt.client.swing.model.ExamBuildContext;
import com.msxt.client.swing.model.Question;
import com.msxt.client.swing.model.Question.State;
import com.msxt.client.swing.utilities.ArrowIcon;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

/**
 *
 * @author Felix Wu
 */
public class QuestionSelectorPanel extends JPanel {    
	private static final long serialVersionUID = 1L;
	
	private static final Border chiselBorder = new ChiselBorder();
    private static final Border panelBorder = new CompoundBorder(chiselBorder, new EmptyBorder(6,8,6,0));
    private static final Border categoryBorder = new CompoundBorder(chiselBorder, new EmptyBorder(0,0,10,0));    
    
    private GradientPanel titlePanel;
    private JLabel examNameLabel;
    private JPanel viewPanel;
    private JScrollPane scrollPane;
    private TimePanel timePanel;
    // need to track components that have defaults customizations
    private final List<CollapsiblePanel> collapsePanels = new ArrayList<CollapsiblePanel>();
    private Icon expandedIcon;
    private Icon collapsedIcon;
   
    private ButtonGroup group;
    private int buttonHeight = 0;
    
    public QuestionSelectorPanel( ExamBuildContext ebc ) {
        super(new BorderLayout());
        
        group = new ButtonGroup();
        // create title area at top
        add( createTitleArea( ebc ), BorderLayout.NORTH );
        
        // create scrollable question panel at bottom
        JComponent selector = createQuestionSelector( ebc );
        scrollPane = new JScrollPane( selector );
        //scrollPane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_NEVER );
        add(scrollPane, BorderLayout.CENTER);
        
        applyDefaults();
    }
    
    protected JComponent createTitleArea(ExamBuildContext ebc) {
        
        titlePanel = new GradientPanel(
                UIManager.getColor(ExamLauncher.TITLE_GRADIENT_COLOR1_KEY),
                UIManager.getColor(ExamLauncher.TITLE_GRADIENT_COLOR2_KEY));
        titlePanel.setLayout( new BorderLayout() );
        titlePanel.setBorder( panelBorder );
        examNameLabel = new JLabel( ebc.getExam().getName() );
        examNameLabel.setOpaque(false);
        examNameLabel.setHorizontalAlignment( JLabel.LEADING );
        titlePanel.add( examNameLabel, BorderLayout.CENTER );
                
        // Add panel with view combobox
        JLabel viewLabel = new JLabel("View:");
        final JComboBox<String> viewComboBox = new JComboBox<String>();
        viewComboBox.addItem("全部");
        viewComboBox.addItem("完成");
        viewComboBox.addItem("未完成");
        viewComboBox.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				filterQuestionButton( viewComboBox.getSelectedIndex() );
			}
		});
        
        timePanel = new TimePanel( ebc.getExam().getTime() );
        
        viewPanel = new JPanel();
        viewPanel.setLayout( new BoxLayout(viewPanel, BoxLayout.X_AXIS) );
        viewPanel.setBorder( new CompoundBorder(chiselBorder, new EmptyBorder(12,8,12,8)) );
        viewPanel.add( timePanel );
        viewPanel.add( Box.createHorizontalStrut(12) );
        viewPanel.add( viewLabel );
        viewPanel.add( Box.createHorizontalStrut(6) );
        viewPanel.add( viewComboBox);

        JPanel titleAreaPanel = new JPanel(new BorderLayout());
        titleAreaPanel.add(titlePanel, BorderLayout.NORTH);
        titleAreaPanel.add(viewPanel, BorderLayout.CENTER);
        
        return titleAreaPanel;
    }
        
    protected JComponent createQuestionSelector( ExamBuildContext ebc ) {
    	List<Catalog> catalogs = ebc.getExam().getCatalogs();
    	
        JPanel selectorPanel = new JPanel();
        GridBagLayout gridbag = new GridBagLayout();
        selectorPanel.setLayout(gridbag);
        
        GridBagConstraints c = new GridBagConstraints(); 
        c.gridx = c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
               
        GridBagLayout categoryGridbag = null;
        GridBagConstraints cc = new GridBagConstraints();
        cc.gridx = cc.gridy = 0;
        cc.weightx = 1;
        cc.fill = GridBagConstraints.HORIZONTAL;
        for(Catalog catalog : catalogs) {
            // Create category collapsible panel
        	JPanel categoryPanel = new JPanel();
            categoryGridbag = new GridBagLayout();
            categoryPanel.setLayout(categoryGridbag);    
            
            CollapsiblePanel collapsePanel = new CollapsiblePanel( categoryPanel, catalog.getName(), catalog.getDesc() );
            collapsePanels.add( collapsePanel );
            collapsePanel.setBorder( categoryBorder );
            
            gridbag.addLayoutComponent( collapsePanel, c );
            selectorPanel.add( collapsePanel );
            c.gridy++;
            
            for( Examination.Question q : catalog.getQuestions() ) {
            	Question question = new Question(q);
	            QuestionButton button = new QuestionButton( question );
	            button.setExamScrollPane( ebc.getExamScrollPane() );
	            categoryGridbag.addLayoutComponent(button, cc);
	            cc.gridy++;
	            group.add(button);
	            categoryPanel.add( button );
	            if ( buttonHeight == 0 ) {
	                buttonHeight = button.getPreferredSize().height;
	            }
	            
	            ebc.setButton( q , button );
            }
        }
        // add empty component to take up any extra room on bottom
        JPanel trailer = new JPanel();
        c.weighty = 1.0;
        gridbag.addLayoutComponent(trailer, c);
        selectorPanel.add(trailer);
        
        applyDefaults();
        
        return selectorPanel;
    }
    
    public void updateUI() {
        super.updateUI();
        applyDefaults();
    }
        
    protected void applyDefaults() {
        
        expandedIcon  = new ArrowIcon(ArrowIcon.SOUTH, UIManager.getColor(ExamLauncher.TITLE_FOREGROUND_KEY));
        collapsedIcon = new ArrowIcon(ArrowIcon.EAST, UIManager.getColor(ExamLauncher.TITLE_FOREGROUND_KEY));
        
        setBorder( new MatteBorder(0,0,0,1, UIManager.getColor(ExamLauncher.CONTROL_MID_SHADOW_KEY)) );
        
        if (titlePanel != null) {
            titlePanel.setGradientColor1( UIManager.getColor(ExamLauncher.TITLE_GRADIENT_COLOR1_KEY) );
            titlePanel.setGradientColor2( UIManager.getColor(ExamLauncher.TITLE_GRADIENT_COLOR2_KEY) );
        }

        if (examNameLabel != null) {
           examNameLabel.setForeground( UIManager.getColor(ExamLauncher.TITLE_FOREGROUND_KEY) );
           examNameLabel.setFont( UIManager.getFont(ExamLauncher.TITLE_FONT_KEY) );
        }
        if (viewPanel != null) {
            viewPanel.setBackground(UIManager.getColor(ExamLauncher.SUB_PANEL_BACKGROUND_KEY));
        }        
        if (collapsePanels != null) {
            for (CollapsiblePanel collapsePanel : collapsePanels) {
                collapsePanel.setFont(
                        UIManager.getFont("CheckBox.font").deriveFont(Font.BOLD));
                collapsePanel.setForeground(UIManager.getColor(ExamLauncher.TITLE_FOREGROUND_KEY));
                collapsePanel.setExpandedIcon(expandedIcon);
                collapsePanel.setCollapsedIcon(collapsedIcon);
            }
        }
        revalidate();
    }

    private void filterQuestionButton( int type ){
    	Enumeration<AbstractButton> en = group.getElements();
    	while( en.hasMoreElements() ) {
    		QuestionButton qb = (QuestionButton)en.nextElement();
    		if( type==0 ) {//全部
    			qb.setVisible( true );
    		} else if( type==1 ) {//完成
    			if( qb.getQuestion().getState() == State.FINISHED ) {
    				qb.setVisible( true );
    			} else {
    				qb.setVisible( false );
    			}
    		} else if ( type==2 ) {//未完成
    			if( qb.getQuestion().getState() == State.UNFINISH ) {
    				qb.setVisible( true );
    			} else {
    				qb.setVisible( false );
    			}
    		}
    		
    		revalidate();
    	}
    }
}
