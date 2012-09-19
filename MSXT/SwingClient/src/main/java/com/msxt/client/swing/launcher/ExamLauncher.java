package com.msxt.client.swing.launcher;

import com.msxt.client.model.Examination;
import com.msxt.client.server.ServerProxy;
import com.msxt.client.server.ServerProxy.Result;
import com.msxt.client.swing.component.QuestionButton;
import com.msxt.client.swing.model.ExamBuildContext;
import com.msxt.client.swing.model.Question;
import com.msxt.client.swing.panel.ExamPanel;
import com.msxt.client.swing.panel.LoginPanel;
import com.msxt.client.swing.panel.ProcessingNotePanel;
import com.msxt.client.swing.panel.QuestionSelectorPanel;
import com.msxt.client.swing.panel.RoundedPanel;
import com.msxt.client.swing.panel.SelectExamPanel;
import com.msxt.client.swing.utilities.RoundedBorder;
import com.msxt.client.swing.utilities.Utilities;

import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.View;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Window;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 *
 * @author Felix
 */
public class ExamLauncher extends SingleFrameApplication  {
    private static final Logger logger = Logger.getLogger(ExamLauncher.class.getName());
    
    private static final ServiceLoader<LookAndFeel> LOOK_AND_FEEL_LOADER = ServiceLoader.load(LookAndFeel.class); 
    
    public static String title;

    public static final String CONTROL_VERY_LIGHT_SHADOW_KEY = "controlVeryLightShadowColor";
    public static final String CONTROL_LIGHT_SHADOW_KEY = "controlLightShadowColor";
    public static final String CONTROL_MID_SHADOW_KEY = "controlMidShadowColor";
    public static final String CONTROL_VERY_DARK_SHADOW_KEY = "controlVeryDarkShadowColor";
    public static final String CONTROL_DARK_SHADOW_KEY = "controlDarkShadowColor";
    public static final String TITLE_GRADIENT_COLOR1_KEY = "titleGradientColor1";
    public static final String TITLE_GRADIENT_COLOR2_KEY = "titleGradientColor2";
    public static final String TITLE_FOREGROUND_KEY = "titleForegroundColor";
    public static final String CODE_HIGHLIGHT_KEY = "codeHighlightColor";
    public static final String TITLE_FONT_KEY = "titleFont";
    public static final String SUB_PANEL_BACKGROUND_KEY = "subPanelBackgroundColor";

    public static final int MAIN_FRAME_WIDTH = 880;
    public static final int MAIN_FRAME_HEIGHT = 640;
    public static final int DEMO_SELECTOR_WIDTH = 250;
    public static final int DEMO_PANEL_HEIGHT = 400;
    public static final int DEMO_PANEL_WIDTH = MAIN_FRAME_WIDTH - DEMO_SELECTOR_WIDTH;
    
    private static final Border EMPTY_BORDER = new EmptyBorder(0, 0, 0, 0);
    private static final Border PANEL_BORDER = new EmptyBorder(1, 3, 1, 1);
    
    static {
        // Property must be set *early* due to Apple Bug#3909714
        if (System.getProperty("os.name").equals("Mac OS X")) {
            System.setProperty("apple.laf.useScreenMenuBar", "true"); 
        }
        UIManager.put("swing.boldMetal", Boolean.FALSE);
    }
    
    public static void main(String[] args) {
        launch(ExamLauncher.class, args);
    }
    
    public static boolean runningOnMac() {
        return System.getProperty("os.name").equals("Mac OS X");
    } 
    
    public static boolean usingNimbus() {
        return UIManager.getLookAndFeel().getName().equals("Nimbus");
    }
    
    // GUI components
    private JPanel mainPanel;
    private QuestionSelectorPanel questionSelectorPanel;
    private JPanel examContainer;
    private ButtonGroup lookAndFeelRadioGroup;
    private SelectExamPanel selectExamPanel;
    private JFrame mainFrame;
    
    // Properties
    private String lookAndFeel;
    private ResourceMap resourceMap;
    private Examination currentExam;
    
    @Override
    protected void initialize(String args[]) {        
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ex) {
        	logger.log( Level.SEVERE, ex.getMessage(), ex);
        }
        resourceMap = getContext().getResourceMap();
        
        title = resourceMap.getString("mainFrame.title");
    }    
    
    @Override 
    protected void startup() {
        UIManager.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent event) {
                if (event.getPropertyName().equals("lookAndFeel")) {
                    configureDefaults();
                }
            }
        });
        
        configureDefaults();
        
        View view = getMainView();
        view.setMenuBar( createMenuBar() );

        mainFrame = getMainFrame();
        mainFrame.setIconImage( resourceMap.getImageIcon("Application.icon").getImage() );
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainFrame.setResizable(false);     //不能改变大小
        show( view );     
             
        LoginPanel lp = new LoginPanel();
        lp.showDialog( mainFrame );
        if( lp.isLoginSuccess() ) {
        	selectExamPanel = new SelectExamPanel( lp.getLoginSuccessResult() );
        	doSelectExam();
        } else {
        	System.exit( 0 );
        }
    } 
    
    private void doSelectExam(){
    	currentExam = selectExamPanel.selectExamination( mainFrame );
    	if( currentExam == null )
    		System.exit( 0 );
    	else
    		createMainPanel( currentExam );
    }
    
    private void configureDefaults() {
        
        // Color palette algorithm courtesy of Jasper Potts
        Color controlColor = UIManager.getColor("control");
        
        UIManager.put(CONTROL_VERY_LIGHT_SHADOW_KEY, Utilities.deriveColorHSB(controlColor, 0, 0, -0.02f));
        UIManager.put(CONTROL_LIGHT_SHADOW_KEY,      Utilities.deriveColorHSB(controlColor, 0, 0, -0.06f));
        UIManager.put(CONTROL_MID_SHADOW_KEY,        Utilities.deriveColorHSB(controlColor, 0, 0, -0.16f));
        UIManager.put(CONTROL_VERY_DARK_SHADOW_KEY,  Utilities.deriveColorHSB(controlColor, 0, 0, -0.5f));
        UIManager.put(CONTROL_DARK_SHADOW_KEY,       Utilities.deriveColorHSB(controlColor, 0, 0, -0.32f));
        
        // Calculate gradient colors for title panels
        Color titleColor = UIManager.getColor( usingNimbus()? "nimbusBase" : "activeCaption" );

        // Some LAFs (e.g. GTK) don't contain "activeCaption" 
        if (titleColor == null) {
            titleColor = controlColor;
        }
        float hsb[] = Color.RGBtoHSB( titleColor.getRed(), titleColor.getGreen(), titleColor.getBlue(), null);
        UIManager.put(TITLE_GRADIENT_COLOR1_KEY,  Color.getHSBColor(hsb[0]-.013f, .15f, .85f));
        UIManager.put(TITLE_GRADIENT_COLOR2_KEY,  Color.getHSBColor(hsb[0]-.005f, .24f, .80f));
        UIManager.put(TITLE_FOREGROUND_KEY,       Color.getHSBColor(hsb[0], .54f, .40f));
        // Calculate highlight color for code pane
        UIManager.put(CODE_HIGHLIGHT_KEY,         Color.getHSBColor(hsb[0]-.005f, .20f, .95f));
       
        Font labelFont = UIManager.getFont("Label.font");
        UIManager.put(TITLE_FONT_KEY, labelFont.deriveFont(Font.BOLD, labelFont.getSize()+0.5f));        
 
        Color panelColor = UIManager.getColor("Panel.background");
        UIManager.put(SUB_PANEL_BACKGROUND_KEY,   Utilities.deriveColorHSB(panelColor, 0, 0, -.06f));
    } 
    
    public void createMainPanel( Examination exam ) {
    	JScrollPane epsp = new JScrollPane();
    	epsp.getVerticalScrollBar().setUnitIncrement(5);
    	ExamBuildContext ebc = new ExamBuildContext( exam );
    	ebc.setExamScrollPane( epsp );
    	
    	mainPanel = new JPanel();
        mainPanel.setLayout( new BorderLayout() );
        
        // Create question selector panel on left
        questionSelectorPanel = new QuestionSelectorPanel( ebc );
        questionSelectorPanel.setPreferredSize(new Dimension(DEMO_SELECTOR_WIDTH, MAIN_FRAME_HEIGHT));
        mainPanel.add(questionSelectorPanel, BorderLayout.WEST);
        
        examContainer = new JPanel();
        examContainer.setLayout( new BorderLayout() );
        examContainer.setBorder( PANEL_BORDER );
        examContainer.setPreferredSize( new Dimension(DEMO_PANEL_WIDTH, DEMO_PANEL_HEIGHT) );
        epsp.setViewportView( new ExamPanel( ebc ) );
        examContainer.add( epsp, BorderLayout.CENTER );
        mainPanel.add(examContainer, BorderLayout.CENTER );
        
        getMainView().setComponent( mainPanel );
    }
    
    protected JMenuBar createMenuBar() {
    
        JMenuBar menubar = new JMenuBar();
        menubar.setName("menubar");
        
        // exam menu
        JMenu fileMenu = new JMenu();
        fileMenu.setName("exam");
        menubar.add(fileMenu);
        
        // Examination -> Submit
        JMenuItem submitItem = new JMenuItem();
        submitItem.setName("exam.submit");
        submitItem.setAction( getAction("exam.submit") );
        fileMenu.add(submitItem);
        
        // File -> Quit
        if ( !runningOnMac() ) {
            JMenuItem quitItem = new JMenuItem();
            quitItem.setName("quit");
            quitItem.setAction( getAction("quit") );
            fileMenu.add(quitItem);
        }
       
        // View menu
        JMenu viewMenu = new JMenu();
        viewMenu.setName("view");
        // View -> Look and Feel       
        viewMenu.add(createLookAndFeelMenu());
        menubar.add(viewMenu);

        return menubar;
    }
    
    protected JMenu createLookAndFeelMenu() {
        JMenu menu = new JMenu();
        menu.setName("lookAndFeel");
        
        // Look for toolkit look and feels first
        UIManager.LookAndFeelInfo lookAndFeelInfos[] = UIManager.getInstalledLookAndFeels();
        lookAndFeel = UIManager.getLookAndFeel().getClass().getName();
        lookAndFeelRadioGroup = new ButtonGroup();
        for(UIManager.LookAndFeelInfo lafInfo: lookAndFeelInfos) {
            menu.add(createLookAndFeelItem(lafInfo.getName(), lafInfo.getClassName()));
        }  
        // Now load any look and feels defined externally as service via java.util.ServiceLoader
        LOOK_AND_FEEL_LOADER.iterator();
        for (LookAndFeel laf : LOOK_AND_FEEL_LOADER) {           
            menu.add(createLookAndFeelItem(laf.getName(), laf.getClass().getName()));
        }
         
        return menu;
    }
    
    protected JRadioButtonMenuItem createLookAndFeelItem(String lafName, String lafClassName) {
        JRadioButtonMenuItem lafItem = new JRadioButtonMenuItem();

        lafItem.setSelected( lafClassName.equals(lookAndFeel) );
        lafItem.setHideActionText( true );
        lafItem.setAction( getAction("setLookAndFeel") );
        lafItem.setText( lafName );
        lafItem.setActionCommand( lafClassName );
        lookAndFeelRadioGroup.add( lafItem );
        
        return lafItem;
    }
    
    public void setLookAndFeel(String lookAndFeel) throws ClassNotFoundException,
        InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        
        String oldLookAndFeel = this.lookAndFeel;
        
		if ( !oldLookAndFeel.equals(lookAndFeel) ) {
            UIManager.setLookAndFeel(lookAndFeel);
            this.lookAndFeel = lookAndFeel;
            updateLookAndFeel();
            firePropertyChange("lookAndFeel", oldLookAndFeel, lookAndFeel);                     
		}
    }
    
    @Action 
    public void setLookAndFeel() {
        ButtonModel model = lookAndFeelRadioGroup.getSelection();
        String lookAndFeelName = model.getActionCommand();
        try {
            setLookAndFeel(lookAndFeelName);
        } catch (Exception ex) {
            displayErrorMessage(resourceMap.getString("error.unableToChangeLookAndFeel") + "to "+lookAndFeelName, ex);
        }
    }
    
    @Action(name="exam.submit")
    public void submit(){
    	Enumeration<AbstractButton> buttonEnum = questionSelectorPanel.getButtonGroup().getElements();
    	
    	while( buttonEnum.hasMoreElements() ) {
    		QuestionButton qb = (QuestionButton)buttonEnum.nextElement();
    		if( !qb.getQuestion().isFinished() ) {
    			int i = JOptionPane.showConfirmDialog( getMainFrame(), 
    					                       resourceMap.getString("submit.unfinished.note"),
    					                       resourceMap.getString("submit.confirm"),
    					                       JOptionPane.YES_NO_OPTION);
    			if( i == 1 )
    				return;
    			else
    				break;
    		}
    	}
    	doSubmit();
    }
    
    public String getLookAndFeel() {
        return lookAndFeel;
    }

    private void displayErrorMessage(String title, String message) {
        JPanel messagePanel = new JPanel( new BorderLayout() );       
        JLabel label = new JLabel(title);
        messagePanel.add( label );
       
        RoundedPanel panel = new RoundedPanel( new BorderLayout() );
        panel.setBorder(new RoundedBorder());
       
        JTextArea exceptionText = new JTextArea();
        exceptionText.setText("Cause of error:\n" + message);
        exceptionText.setBorder( new RoundedBorder() );
        exceptionText.setOpaque( false );
        exceptionText.setBackground( Utilities.deriveColorHSB(UIManager.getColor("Panel.background"), 0, 0, -.2f) );
        JScrollPane scrollpane = new JScrollPane( exceptionText );
        scrollpane.setBorder( EMPTY_BORDER );
        scrollpane.setPreferredSize( new Dimension(600, 240) );
        panel.add(scrollpane);
        
        messagePanel.add( panel, BorderLayout.SOUTH );            
        
        JOptionPane.showMessageDialog( getMainFrame(), messagePanel, resourceMap.getString("error.title"), JOptionPane.ERROR_MESSAGE ); 
    }
    
    // For displaying error messages to user
    private void displayErrorMessage(String title, Exception ex) {
        StringWriter writer = new StringWriter();
        ex.printStackTrace(new PrintWriter(writer));
        String msg = writer.getBuffer().toString();
        displayErrorMessage( title, msg );
    }
    
    private void updateLookAndFeel() {
        Window windows[] = Frame.getWindows();
        for(Window window : windows) {
            SwingUtilities.updateComponentTreeUI(window);
          //SwingUtilities.updateComponentTreeUI(examContainer);
        }
    }
    
    public void doSubmit(){
    	ProcessingNotePanel pnp = new ProcessingNotePanel( resourceMap.getString("submit.processing") );
    	pnp.showProcessing( getMainFrame() );
    	
    	Enumeration<AbstractButton> buttonEnum = questionSelectorPanel.getButtonGroup().getElements();
    	Map<String, String> answerMap = new HashMap<String, String>();
    	while( buttonEnum.hasMoreElements() ) {
    		QuestionButton qb = (QuestionButton)buttonEnum.nextElement();
    		Question q = qb.getQuestion();
    		if( q.isFinished() ) {
    			String id = qb.getQuestion().getOriginalQuestion().getId();
    			String answer = qb.getQuestion().getQuestionPanel().getAnswer();
    			answerMap.put(id, answer);
    		}
    	}
    	
    	ServerProxy sp = ServerProxy.Factroy.getCurrrentInstance();
    	Result r = sp.submitAnswer( currentExam.getId(), answerMap );
    	
    	pnp.cancelShowProcessing();
    	
    	if( r.isSuccess() ) {
    		try{
	    		DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		        ByteArrayInputStream is = new ByteArrayInputStream( r.getSuccessMessage().getBytes() );
		        Document doc = db.parse( is );
		        is.close();
		        Element root = doc.getDocumentElement();
		        String status = root.getElementsByTagName("status").item(0).getTextContent();
		        if( status.equals( "success" ) ) {
		        	String score = root.getElementsByTagName("score").item(0).getTextContent();
		        	JOptionPane.showMessageDialog( getMainFrame(), resourceMap.getString("submit.success", score) );
		        	doSelectExam();
		        } else {
		        	String desc = root.getElementsByTagName("desc").item(0).getTextContent();
		        	displayErrorMessage("Submit failed", desc );
		        }
    		} catch (Exception e){
    			displayErrorMessage("Process Response Failed", e );
    		}
    	} else {
    		displayErrorMessage("Submit failed", r.getErrorMessage() );
    	}
    }
    
    private javax.swing.Action getAction(String actionName) {
        return getContext().getActionMap().get(actionName);
    }
}