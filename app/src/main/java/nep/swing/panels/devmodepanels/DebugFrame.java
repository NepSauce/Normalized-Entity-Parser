package nep.swing.panels.devmodepanels;

import nep.swing.panels.devmodepanels.devmodeduplicates.*;
import nep.swing.panels.devmodepanels.devmodeduplicates.rostertabpanelsdev.EncompassingTabDev;

import javax.swing.*;

import java.awt.*;

import nep.rosterconversion.PDFCleaner;
import nep.rosterconversion.PDFConversion;

/**
 * The DebugFrame class represents the frame that opens when the "RootNEP" button is clicked.
 * It follows a layout similar to the NepFrame with panels.
 */
public class DebugFrame extends JFrame {
    
    /**
     * Constructs the DebugFrame and initializes its UI components.
     */
    public DebugFrame(){
        setTitle("NEP - Fault Trace - Normalized Entity Parser (Build-1.1.0-Beta)");
        setSize(593, 465);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);
        setResizable(false);
        setVisible(true); // after this, getHeight() returns real value

        JLabel ownershipLabel = new JLabel("Owned by Zawad Atif and Nafisah Nubah", SwingConstants.CENTER);
        ownershipLabel.setBounds(0, getHeight() - 30, getWidth(), 20);
        ownershipLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        ownershipLabel.setForeground(Color.DARK_GRAY);
        add(ownershipLabel);
        revalidate();
        repaint();


        getContentPane().setBackground(new Color(238, 238, 238, 255));

        ImageIcon logo = new ImageIcon("Media/logo.png");
        
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
        
        
        PDFConversion.deleteCombinedObjectFile();
        LoggingPanelDev.logGlobal("- Combined Deleted", true);
        PDFConversion.deleteRemovedObjectFile();
        LoggingPanelDev.logGlobal("- Removed Deleted", true);
        LoggingPanelDev.logGlobal("NEP Awake and Ready", true);
        
    }
}
