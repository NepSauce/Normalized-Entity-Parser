package nep.swing.panels.devmodepanels.devmodeduplicates.rostertabpanelsdev;

import nep.entityclass.RosterEntityDetails;
import nep.swing.panels.DatePickerPanel;
import nep.swing.panels.ExamAddedPanel;
import nep.swing.panels.ExamLocationPanel;
import nep.swing.panels.RosterAddedPanel;
import nep.swing.panels.devmodepanels.devmodeduplicates.ExamAddedPanelDev;
import nep.swing.panels.devmodepanels.devmodeduplicates.ExamLocationPanelDev;
import nep.swing.panels.devmodepanels.devmodeduplicates.RosterAddedPanelDev;
import nep.util.DisplayUIError;
import nep.util.FieldValidator;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

/**
 * The SelectionButtonPalette class creates a panel with buttons for selecting and managing exam locations and dates.
 * It handles the validation and saving of roster details.
 */
@SuppressWarnings({"FieldMayBeFinal", "unused"})
public class SelectionButtonPaletteDev {
    private JPanel selectionButtonPanel;
    private static LinkedList<RosterEntityDetails> rosterObjectEntityList = new LinkedList<>();
    
    /**
     * Constructs the SelectionButtonPalette, initializes the UI components, and sets up the buttons for adding exam locations,
     * resetting the date, and handling validation.
     *
     * @param examLocationPanel the panel responsible for managing exam locations.
     * @param examAddedPanel the panel that handles the addition of exams to the roster.
     * @param rosterAddedPanel the panel that manages the added rosters.
     */
    @SuppressWarnings("Convert2Lambda")
    public SelectionButtonPaletteDev(ExamLocationPanelDev examLocationPanel, ExamAddedPanelDev examAddedPanel,
                                     RosterAddedPanelDev rosterAddedPanel){
        
        rosterObjectEntityList.clear();
        
        selectionButtonPanel = new JPanel();
        selectionButtonPanel.setLayout(null);
        selectionButtonPanel.setBackground(Color.WHITE);
        selectionButtonPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        selectionButtonPanel.setBounds(25, 240, 250, 40);
        
        JPanel selectionButtonBorderPanel = new JPanel();
        selectionButtonBorderPanel.setLayout(new GridLayout(1, 2, 10 ,20));
        selectionButtonBorderPanel.setBackground(Color.WHITE);
        selectionButtonBorderPanel.setBounds(10 ,10 , 225, 35);

        selectionButtonPanel.add(selectionButtonBorderPanel);
        
        JButton addExamLocationButton = new JButton("Add Exam");
        JButton resetDateButton = new JButton("Reset Date");

        Dimension buttonSize = new Dimension(120, 40);
        addExamLocationButton.setPreferredSize(buttonSize);
        resetDateButton.setPreferredSize(buttonSize);

        addExamLocationButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                String fileName = rosterAddedPanel.getRosterFileName();
                String fileAbsolutePath = rosterAddedPanel.getAbsoluteFilePath();
                String location = examLocationPanel.getExamLocation();
                int day = 1;
                String month = "January";
                int year = 2000;
        
                FieldValidator newValidator = new FieldValidator(fileAbsolutePath, location, day, month, year);
                newValidator.generateErrorMessage(); 
                String errorMessage = newValidator.getErrorMessage();
                System.out.println(errorMessage);
        
                if (errorMessage == null){
                    saveRosterObject(fileName, fileAbsolutePath, location, day, month, year);
                    String rosterDetails = location + "-" + fileName;
                    examAddedPanel.addRosterToPanel(rosterDetails);
                    for (int i = 0; i < rosterObjectEntityList.size(); i++){
                        System.out.println(rosterObjectEntityList.get(i).getLocation());
                        // Debug Logging
                    }
                } 
                else{
                    DisplayUIError newUIError = new DisplayUIError(errorMessage, 1);
                    newUIError.displayNormalError();
                }
            }
        });
        
        selectionButtonBorderPanel.add(addExamLocationButton);
        selectionButtonBorderPanel.add(resetDateButton);
    }
    
    /**
     * Saves the roster details by creating a new RosterEntityDetails object and adding it to the roster list.
     *
     * @param fileName the name of the file containing the roster.
     * @param directory the directory path of the file.
     * @param location the location of the exam.
     * @param day the day of the exam.
     * @param month the month of the exam.
     * @param year the year of the exam.
     */
    public void saveRosterObject(String fileName, String directory, String location, int day, String month, int year){
        RosterEntityDetails newRosterEntity = new RosterEntityDetails(fileName, directory, location, day, month, year);
        rosterObjectEntityList.add(newRosterEntity);
        System.out.println("Roster Detail Added.");
    }
    
    /**
     * Returns the main panel containing the selection buttons for managing exam locations and dates.
     *
     * @return the JPanel containing the selection buttons.
     */
    public JPanel getSelectionButtonPanel(){
        return selectionButtonPanel;
    }
    
    /**
     * Returns the list of roster entity details.
     *
     * @return the LinkedList of RosterEntityDetails.
     */
    public static LinkedList<RosterEntityDetails> getRosterEntityList(){
        return rosterObjectEntityList;
    }
}
