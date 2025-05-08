package nep.swing.panels.devmodepanels;

import nep.swing.panels.DatePickerPanel;
import nep.swing.panels.SettingsTab;
import nep.swing.panels.devmodepanels.devmodeduplicates.*;

import javax.swing.*;
import java.awt.*;

/**
 * The DebugFrame class represents the frame that opens when the "RootNEP" button is clicked.
 * It follows a layout similar to the NepFrame with panels.
 */
public class DebugFrame extends JFrame {
    
    /**
     * Constructs the DebugFrame and initializes its UI components.
     */
    public DebugFrame() {
        setTitle("RootNEP Debug");
        setSize(800, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        
        getContentPane().setBackground(new Color(238, 238, 238, 255)); // Set background color

        ImageIcon logo = new ImageIcon("Media/logo.png");
        setIconImage(logo.getImage());
        
        ExamLocationPanelDev examLocationPanel = new ExamLocationPanelDev();
        add(examLocationPanel.getExamLocationPanel());
        
        RosterAddedPanelDev rosterAddedPanel = new RosterAddedPanelDev();
        add(rosterAddedPanel.getRosterBrowsingPanel());
        
        ExamAddedPanelDev examAddedPanel = new ExamAddedPanelDev();
        add(examAddedPanel.getExamAddedPanel());
        
        SettingsTabDev settingsTab = new SettingsTabDev(examLocationPanel, examAddedPanel,
                rosterAddedPanel);
        add(settingsTab.getSettingsTab());
        
        ObjectTabDev objectTab = new ObjectTabDev(examLocationPanel, examAddedPanel,
                rosterAddedPanel);
        add(objectTab.getObjectTab());
        
        setLayout(new BorderLayout());
    }
}
