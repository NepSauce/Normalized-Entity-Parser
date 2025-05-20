package nep.swing.panels.rostertabpanels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.LinkedList;

import javax.swing.*;
import javax.swing.border.BevelBorder;

import nep.entityclass.RosterEntityDetails;
import nep.rosterconversion.PDFConversion;
import nep.swing.panels.DatePickerPanel;
import nep.swing.panels.ExamAddedPanel;
import nep.swing.panels.ExamLocationPanel;
import nep.swing.panels.RosterAddedPanel;
import nep.swing.panels.removedobjectpanels.RemovedObjectPanel;
import nep.util.RosterObjectSplitter;

/**
 * The PanelButtonPalette class creates a panel with buttons for interacting with rosters.
 * It allows submitting, clearing, and undoing roster actions.
 */
@SuppressWarnings("FieldMayBeFinal")
public class PanelButtonPalette{
    private JPanel panelButtonPanel;
    private static LinkedList<RosterEntityDetails> rosterEntityDetails = new LinkedList<>();
    
    /**
     * Constructs the PanelButtonPalette, initializes the UI components, and sets up the buttons for submitting, clearing,
     * and undoing roster actions.
     *
     * @param examLocationPanel the panel responsible for managing exam locations.
     * @param examAddedPanel the panel that handles the addition of exams to the roster.
     * @param datePickerPanel the panel for selecting dates.
     * @param rosterAddedPanel the panel that manages the added rosters.
     */
    public PanelButtonPalette(ExamLocationPanel examLocationPanel, ExamAddedPanel examAddedPanel,
                              DatePickerPanel datePickerPanel, RosterAddedPanel rosterAddedPanel){
        
        rosterEntityDetails = SelectionButtonPalette.getRosterEntityList();
        
        panelButtonPanel = new JPanel();
        panelButtonPanel.setLayout(null);
        panelButtonPanel.setBackground(Color.WHITE);
        panelButtonPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        panelButtonPanel.setPreferredSize(new Dimension(100, 60));
        
        JPanel buttonContainerPanel = new JPanel();
        buttonContainerPanel.setLayout(new GridLayout(1, 3, 10, 0));
        buttonContainerPanel.setBackground(Color.WHITE);
        buttonContainerPanel.setBounds(10, 10 , 225, 35);
        
        panelButtonPanel.add(buttonContainerPanel);
        
        JButton submitAllRostersButton = new JButton("Submit");
        JButton clearAllRostersFromPanelButton = new JButton("Clear");
        JButton undoLastRosterFromPanelButton = new JButton("Undo");
        
        submitAllRostersButton.addActionListener((ActionEvent e) -> {
            PDFConversion.deleteRemovedObjectFile();
            
            RosterObjectSplitter newSplitter = new RosterObjectSplitter(rosterEntityDetails, rosterEntityDetails.size());
            LinkedList<String> directoryList = newSplitter.getRosterDirectory();
            LinkedList<String> fileNameList = newSplitter.getRosterFileName();
            LinkedList<String> locationList = newSplitter.getRosterLocation();
            
            if (!directoryList.isEmpty()){
                PDFConversion.deleteCombinedObjectFile();
                PDFConversion.emptyCombinedNormalizedObjectContents();
                
                for (int i = 0; i < rosterEntityDetails.size(); i++){
                    PDFConversion.generateNormalizedObject(directoryList.get(i), fileNameList.get(i), locationList.get(i));
                }
                PDFConversion.generateCombinedObject();
            }
        });
        
        clearAllRostersFromPanelButton.addActionListener((ActionEvent e) -> {
            examAddedPanel.clearAllRostersFromPanel();
            rosterAddedPanel.clearRosterInfo();
        });
        
        undoLastRosterFromPanelButton.addActionListener((ActionEvent e) -> {
            examAddedPanel.undoLastRoster();
        });
        
        buttonContainerPanel.add(submitAllRostersButton);
        buttonContainerPanel.add(clearAllRostersFromPanelButton);
        buttonContainerPanel.add(undoLastRosterFromPanelButton);
    }
    
    /**
     * Opens the RemovedObjectPanel in a new JFrame to manage and display removed objects.
     */
    private void openRemovedObjectWindow(){
        RemovedObjectPanel removedObjectPanel = new RemovedObjectPanel();
        JFrame frame = new JFrame("Removed Objects");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(650, 500);
        frame.setLocationRelativeTo(null);
        frame.setContentPane(removedObjectPanel);
        frame.setVisible(true);
    }
    
    /**
     * Returns the main panel containing the buttons for managing rosters.
     *
     * @return the JPanel containing the roster management buttons.
     */
    public JPanel getPanelButtonPanel(){
        return panelButtonPanel;
    }
}
