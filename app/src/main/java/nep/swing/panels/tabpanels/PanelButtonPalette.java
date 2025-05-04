package nep.swing.panels.tabpanels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
import nep.util.DisplayUIError;
import nep.util.RosterObjectSplitter;

@SuppressWarnings("FieldMayBeFinal")
public class PanelButtonPalette {
    
    private JButton submitAllRostersButton;
    private JButton clearAllRostersFromPanelButton;
    private JButton undoLastRosterFromPanelButton;
    private JPanel panelButtonPanel;
    private JPanel buttonContainerPanel;
    private static LinkedList<RosterEntityDetails> rosterEntityDetails = new LinkedList<>();
    
    public PanelButtonPalette(ExamLocationPanel examLocationPanel, ExamAddedPanel examAddedPanel,
                              DatePickerPanel datePickerPanel, RosterAddedPanel rosterAddedPanel) {
        
        rosterEntityDetails = SelectionButtonPalette.getRosterEntityList();
        
        panelButtonPanel = new JPanel();
        panelButtonPanel.setLayout(null);
        panelButtonPanel.setBackground(Color.WHITE);
        panelButtonPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        panelButtonPanel.setPreferredSize(new Dimension(100, 60));
        
        buttonContainerPanel = new JPanel();
        buttonContainerPanel.setLayout(new GridLayout(1, 3, 10, 0));
        buttonContainerPanel.setBackground(Color.WHITE);
        buttonContainerPanel.setBounds(10, 10 , 225, 35);
        
        panelButtonPanel.add(buttonContainerPanel);
        
        submitAllRostersButton = new JButton("Submit");
        clearAllRostersFromPanelButton = new JButton("Clear");
        undoLastRosterFromPanelButton = new JButton("Undo");
        
        submitAllRostersButton.addActionListener((ActionEvent e) -> {
            String folderPath = "NormalizedEntityParser/RemovedObjects/";
            File folder = new File(folderPath);
            File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));
            
            if (files != null && files.length > 0) {
                File file = files[0];
                
                if (file.exists() && file.length() > 0) {
                    openRemovedObjectWindow();
                } else {
                    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                        String line = reader.readLine();
                        if (line == null || line.trim().isEmpty()) {
                            new DisplayUIError("No Removed Objects Generated", 201).displayNormalError();
                        } else {
                            openRemovedObjectWindow();
                        }
                    } catch (IOException ex) {
                        new DisplayUIError("Error Reading File: " + ex.getMessage(), 202).displayCriticalError();
                    }
                }
            } else {
                new DisplayUIError("No Valid .txt Files Found in The RemovedObjects Folder.", 203).displayNormalError();
            }
            
            RosterObjectSplitter newSplitter = new RosterObjectSplitter(rosterEntityDetails, rosterEntityDetails.size());
            LinkedList<String> directoryList = newSplitter.getRosterDirectory();
            LinkedList<String> fileNameList = newSplitter.getRosterFileName();
            LinkedList<String> locationList = newSplitter.getRosterLocation();
            
            if (!directoryList.isEmpty()) {
                PDFConversion.deleteCombinedObjectFile();
                PDFConversion.emptyCombinedNormalizedObjectContents();
                
                for (int i = 0; i < rosterEntityDetails.size(); i++) {
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
    
    private void openRemovedObjectWindow() {
        RemovedObjectPanel removedObjectPanel = new RemovedObjectPanel();
        JFrame frame = new JFrame("Removed Objects");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(650, 500);
        frame.setLocationRelativeTo(null);
        frame.setContentPane(removedObjectPanel);
        frame.setVisible(true);
    }
    
    public JPanel getPanelButtonPanel() {
        return panelButtonPanel;
    }
}
