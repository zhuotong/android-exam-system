package com.msxt.client.swing.launcher;

import com.msxt.client.model.Examination;
import com.msxt.client.model.LoginSuccessResult;
import com.msxt.client.model.Question;
import com.msxt.client.swing.panel.ExamPanel;
import com.msxt.client.swing.panel.LoginPanel;
import com.msxt.client.swing.panel.SelectExamPanel;
import com.mxst.client.swing.utilities.RoundedBorder;
import com.mxst.client.swing.utilities.RoundedPanel;
import com.mxst.client.swing.utilities.Utilities;
import com.sun.swingset3.IntroPanel;
import com.sun.swingset3.QuestionSelectorPanel;

import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.View;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.logging.Logger;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Felix
 */
public class ExamLauncher extends SingleFrameApplication  {
    static final Logger logger = Logger.getLogger(ExamLauncher.class.getName());
    
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
    public static final int DEMO_SELECTOR_WIDTH = 186;
    public static final int DEMO_PANEL_HEIGHT = 400;
    public static final int DEMO_PANEL_WIDTH = MAIN_FRAME_WIDTH - DEMO_SELECTOR_WIDTH;
    
    private static final Border EMPTY_BORDER = new EmptyBorder(0, 0, 0, 0);
    private static final Border PANEL_BORDER = new EmptyBorder(10, 10, 10, 10);
    
    static {
        // Property must be set *early* due to Apple Bug#3909714
        if (System.getProperty("os.name").equals("Mac OS X")) {
            System.setProperty("apple.laf.useScreenMenuBar", "true"); 
        }
        
        // temporary workaround for problem with Nimbus classname
        UIManager.LookAndFeelInfo lafInfo[] = UIManager.getInstalledLookAndFeels();
        for(int i = 0; i < lafInfo.length; i++) {
            if (lafInfo[i].getName().equals("Nimbus")) {
                lafInfo[i] = new UIManager.LookAndFeelInfo("Nimbus", "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
                break;
            }
        }
        UIManager.setInstalledLookAndFeels(lafInfo);
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

    // End of statics
    
    private ResourceMap resourceMap;
    
    // Application models
    private PropertyChangeListener demoPropertyChangeListener;
    private Map<String, JPanel> runningPanelCache;
    private Question currentQuestion;

    // GUI components
    private JPanel selectExamPanel;
    private JPanel mainPanel;
    private QuestionSelectorPanel questionSelectorPanel;
    private JPanel demoContainer;
    private JComponent currentDemoPanel;
    private JComponent demoPlaceholder;
    private ButtonGroup lookAndFeelRadioGroup;
    
    private JPopupMenu popup;
    
    // Properties
    private String lookAndFeel;
    
    @Override
    protected void initialize(String args[]) {        
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ex) {
        }
        resourceMap = getContext().getResourceMap();
        
        title = resourceMap.getString("mainFrame.title");
        runningPanelCache = new HashMap<String, JPanel>();
        demoPlaceholder = new IntroPanel();
    }    
    
    protected PropertyChangeListener getDemoPropertyChangeListener() {
        if (demoPropertyChangeListener == null) {
            demoPropertyChangeListener = new DemoPropertyChangeListener();
        }
        return demoPropertyChangeListener;
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
        view.setComponent( new LoginPanel() );
        view.setMenuBar( createMenuBar() );
        // application framework should handle this
        getMainFrame().setIconImage(resourceMap.getImageIcon("Application.icon").getImage());
        
        show( view );     
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
        Color titleColor = UIManager.getColor(usingNimbus()? "nimbusBase" : "activeCaption");

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
        UIManager.put(TITLE_FONT_KEY, labelFont.deriveFont(Font.BOLD, labelFont.getSize()+4f));        
 
        Color panelColor = UIManager.getColor("Panel.background");
        UIManager.put(SUB_PANEL_BACKGROUND_KEY,   Utilities.deriveColorHSB(panelColor, 0, 0, -.06f));
        
    } 
    
    public void createSelectExamPanel( LoginSuccessResult lsr ) {
    	selectExamPanel = new SelectExamPanel(lsr);
    	getMainView().setComponent( selectExamPanel );
    }
    
    public void createMainPanel( Examination exam ) {
        mainPanel = new JPanel();
        mainPanel.setLayout( new BorderLayout() );
       
        // Create demo selector panel on left
        questionSelectorPanel = new QuestionSelectorPanel( exam );
        questionSelectorPanel.setPreferredSize(new Dimension(DEMO_SELECTOR_WIDTH, MAIN_FRAME_HEIGHT));
        questionSelectorPanel.addPropertyChangeListener( new QuestionSelectionListener() );
        mainPanel.add(questionSelectorPanel, BorderLayout.WEST);
        
        demoContainer = new JPanel();
        demoContainer.setLayout(new BorderLayout());
        demoContainer.setBorder(PANEL_BORDER);
        demoContainer.setPreferredSize(new Dimension(DEMO_PANEL_WIDTH, DEMO_PANEL_HEIGHT));
        
        mainPanel.add( demoContainer, BorderLayout.CENTER );
        
//      currentDemoPanel = demoPlaceholder;
//      demoContainer.add(demoPlaceholder, BorderLayout.CENTER);
        JScrollPane epsp = new JScrollPane( new ExamPanel( exam ) );
        demoContainer.add( epsp, BorderLayout.CENTER );
        
        // Create shareable popup menu for demo actions
        popup = new JPopupMenu();
        
        getMainView().setComponent( mainPanel );
    }
    
    protected JMenuBar createMenuBar() {
    
        JMenuBar menubar = new JMenuBar();
        menubar.setName("menubar");
        
        // File menu
        JMenu fileMenu = new JMenu();
        fileMenu.setName("file");
        menubar.add(fileMenu);
        
        // File -> Quit
        if (!runningOnMac()) {
            JMenuItem quitItem = new JMenuItem();
            quitItem.setName("quit");
            quitItem.setAction(getAction("quit"));
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

        lafItem.setSelected(lafClassName.equals(lookAndFeel));
        lafItem.setHideActionText(true);
        lafItem.setAction(getAction("setLookAndFeel"));
        lafItem.setText(lafName);
        lafItem.setActionCommand(lafClassName);
        lookAndFeelRadioGroup.add(lafItem);
        
        return lafItem;
    }
    
    private javax.swing.Action getAction(String actionName) {
        return getContext().getActionMap().get(actionName);
    }
       
    // For displaying error messages to user
    protected void displayErrorMessage(String message, Exception ex) {
        JPanel messagePanel = new JPanel(new BorderLayout());       
        JLabel label = new JLabel(message);
        messagePanel.add(label);
        if (ex != null) {
            RoundedPanel panel = new RoundedPanel(new BorderLayout());
            panel.setBorder(new RoundedBorder());
            
            // remind(aim): provide way to allow user to see exception only if desired
            StringWriter writer = new StringWriter();
            ex.printStackTrace(new PrintWriter(writer));
            JTextArea exceptionText = new JTextArea();
            exceptionText.setText("Cause of error:\n" + writer.getBuffer().toString());
            exceptionText.setBorder( new RoundedBorder() );
            exceptionText.setOpaque( false );
            exceptionText.setBackground( Utilities.deriveColorHSB(UIManager.getColor("Panel.background"), 0, 0, -.2f) );
            JScrollPane scrollpane = new JScrollPane( exceptionText );
            scrollpane.setBorder( EMPTY_BORDER );
            scrollpane.setPreferredSize( new Dimension(600, 240) );
            panel.add(scrollpane);
            messagePanel.add(panel, BorderLayout.SOUTH);            
        }
        JOptionPane.showMessageDialog(getMainFrame(), messagePanel, 
                resourceMap.getString("error.title"),
                JOptionPane.ERROR_MESSAGE);
                
    }
   
    public void setCurrentQuestion(Question q) {
        if (currentQuestion == q) {
            return; // already there
        }
        Question oldCurrentDemo = currentQuestion;        
        currentQuestion = q;
        if (q != null) {
            JPanel demoPanel = runningPanelCache.get(q.getName());
            demoContainer.remove(currentDemoPanel);
            currentDemoPanel = demoPanel;
            demoContainer.add( currentDemoPanel, BorderLayout.CENTER) ;
            demoContainer.revalidate();
            demoContainer.repaint();
            getMainFrame().validate();
        }
          
        firePropertyChange("currentDemo", oldCurrentDemo, currentQuestion);
    }
   
    
    public Question getCurrentDemo() {
        return currentQuestion;
    }
    
    public void setLookAndFeel(String lookAndFeel) throws ClassNotFoundException,
        InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        
        String oldLookAndFeel = this.lookAndFeel;
        
	if (oldLookAndFeel != lookAndFeel) {
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
            displayErrorMessage(resourceMap.getString("error.unableToChangeLookAndFeel") +
                    "to "+lookAndFeelName, ex);
        }
    }
    
    public String getLookAndFeel() {
        return lookAndFeel;
    }

    private void updateLookAndFeel() {
        Window windows[] = Frame.getWindows();

        for(Window window : windows) {
            SwingUtilities.updateComponentTreeUI(window);
            for(JPanel demoPanel : runningPanelCache.values()) {
                SwingUtilities.updateComponentTreeUI(demoPanel);
            }
        }
    }

    // hook used to detect if any components in the demo have registered a
    // code snippet key for the their creation code inside the source
    private void registerPopups(Component component) {
        
        if (component instanceof Container) {
            Component children[] = ((Container)component).getComponents();
            for(Component child: children) {
                if (child instanceof JComponent) {
                    registerPopups(child);
                }
            }
        }
        if (component instanceof JComponent) {
            JComponent jcomponent = (JComponent)component;
            String snippetKey = (String)jcomponent.getClientProperty("snippetKey");
            if (snippetKey != null) {
                jcomponent.setComponentPopupMenu(popup);
            }
        }
    }    
    
    private class QuestionSelectionListener implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent event) {
            if (event.getPropertyName().equals("selectedDemo")) {
                setCurrentQuestion((Question)event.getNewValue());
            }
        }
    }
           
    
    // registered on Demo to detect when the demo component is instantiated.
    // we need this because when we embed the demo inside an HTML description pane,
    // we don't have control over the demo component's instantiation
    private class DemoPropertyChangeListener implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent e) {
            String propertyName = e.getPropertyName();
            if (propertyName.equals("demoComponent")) {
                Question demo = (Question)e.getSource();
                JComponent demoComponent = (JComponent)e.getNewValue();
                if (demoComponent != null) {
                    demoComponent.putClientProperty("swingset3.demo", demo);
                    demoComponent.addHierarchyListener(new DemoVisibilityListener());
                    registerPopups(demoComponent);
                }
            } 
        }
    }
    
    private class DemoVisibilityListener implements HierarchyListener {
        public void hierarchyChanged(HierarchyEvent event) {
            if ((event.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) > 0) {
                JComponent component = (JComponent)event.getComponent();
                final Question demo = (Question)component.getClientProperty("swingset3.demo");
                if (!component.isShowing()) {
                    //demo.stop();
                } else {
                    demoContainer.revalidate();
                    EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            //demo.start();
                        }
                    });
                }
            }            
        }        
    }    
}
