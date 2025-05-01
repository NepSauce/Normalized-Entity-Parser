package nep.swing.panels.tabpanels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import nep.entityclass.RosterEntityDetails;
import nep.rosterconversion.PDFConversion;
import nep.swing.panels.DatePickerPanel;
import nep.swing.panels.ExamAddedPanel;
import nep.swing.panels.ExamLocationPanel;
import nep.swing.panels.RosterAddedPanel;
import nep.util.DisplayUIError;
import nep.util.RosterObjectSplitter;

@SuppressWarnings("FieldMayBeFinal")
public class PanelButtonPalette{
    private JButton submitAllRostersButton;
    private JButton clearAllRostersFromPanelButton;
    private JButton undoLastRosterFromPanelButton;
    private JPanel panelButtonPanel;
    private JPanel buttonContainerPanel;
    private static LinkedList<RosterEntityDetails> rosterEntityDetails = new LinkedList<>();

    public PanelButtonPalette(ExamLocationPanel examLocationPanel, ExamAddedPanel examAddedPanel,
                               DatePickerPanel datePickerPanel, RosterAddedPanel rosterAddedPanel){
        
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

        submitAllRostersButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                PDFConversion.deleteFilesInFolder();
                RosterObjectSplitter newSplitter = new RosterObjectSplitter(rosterEntityDetails, rosterEntityDetails.size());
                LinkedList<String> directoryList = newSplitter.getRosterDirectory();
                LinkedList<String> fileNameList = newSplitter.getRosterFileName();
                LinkedList<String> locationList = newSplitter.getRosterLocation();
                
                for (int i = 0; i < rosterEntityDetails.size(); i++){
                    PDFConversion.generateNormalizedObject(directoryList.get(i),fileNameList.get(i), locationList.get(i));
                }
                
                PDFConversion.generateCombinedObject();
                PDFConversion.emptyCombinedNormalizedObjectContents();
            }
        });

        clearAllRostersFromPanelButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                examAddedPanel.clearAllRostersFromPanel();
                rosterAddedPanel.clearRosterInfo();
            }
        });

        undoLastRosterFromPanelButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                examAddedPanel.undoLastRoster();
            }
        });

        buttonContainerPanel.add(submitAllRostersButton);
        buttonContainerPanel.add(clearAllRostersFromPanelButton);
        buttonContainerPanel.add(undoLastRosterFromPanelButton);
    }

    public JPanel getPanelButtonPanel(){
        return panelButtonPanel;
    }
}
