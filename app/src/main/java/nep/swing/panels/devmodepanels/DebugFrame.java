package nep.swing.panels.devmodepanels;

import nep.swing.panels.devmodepanels.devmodeduplicates.*;
import nep.swing.panels.devmodepanels.devmodeduplicates.rostertabpanelsdev.EncompassingTabDev;

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
    public DebugFrame(){
        setTitle("NEP - Fault Trace");
        setSize(593, 460);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);
        setResizable(false);
        setVisible(true);
        
        getContentPane().setBackground(new Color(238, 238, 238, 255));

        ImageIcon logo = new ImageIcon("Media/logo.png");
        setIconImage(logo.getImage());
        
        LoggingPanelDev loggingPanel = new LoggingPanelDev();
        add(loggingPanel.getLoggingPanel());
        
        ExamLocationPanelDev examLocationPanel = new ExamLocationPanelDev(loggingPanel);
        add(examLocationPanel.getExamLocationPanel());
        
        RosterAddedPanelDev rosterAddedPanel = new RosterAddedPanelDev(loggingPanel);
        add(rosterAddedPanel.getRosterBrowsingPanel());
        
        ExamAddedPanelDev examAddedPanel = new ExamAddedPanelDev(loggingPanel);
        add(examAddedPanel.getExamAddedPanel());
        
        CumulativeInfoPanelDev cumulativeInfoPanel = new CumulativeInfoPanelDev(examLocationPanel, examAddedPanel,
                rosterAddedPanel, loggingPanel);
        add(cumulativeInfoPanel.getCumulativeInfoPanel());
        
        EncompassingTabDev encompassingTab = new EncompassingTabDev(examLocationPanel, examAddedPanel, rosterAddedPanel,
                cumulativeInfoPanel, loggingPanel);
        add(encompassingTab.getEncompassingTab());

    }
}
