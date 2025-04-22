package nep.swing.panels.tabpanels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import nep.entityclass.RosterEntityDetails;
import nep.swing.panels.DatePickerPanel;
import nep.swing.panels.ExamAddedPanel;
import nep.swing.panels.ExamLocationPanel;
import nep.swing.panels.RosterAddedPanel;
import nep.util.FieldValidator;

@SuppressWarnings({"FieldMayBeFinal", "unused"})
public class SelectionButtonPalette{
    private JButton addExamLocationButton;
    private JButton resetDateButton;
    private JPanel selectionButtonBorderPanel;
    private JPanel selectionButtonPanel;
    private DatePickerPanel datePickerPanel;
    private ExamLocationPanel examLocationPanel;
    private RosterAddedPanel rosterAddedPanel;
    private ArrayList<RosterEntityDetails> rosterObjectEntityList;

    @SuppressWarnings("Convert2Lambda")
    public SelectionButtonPalette(ExamLocationPanel examLocationPanel, ExamAddedPanel examAddedPanel, 
        DatePickerPanel datePickerPanel, RosterAddedPanel rosterAddedPanel){ 
        
        this.rosterObjectEntityList = new ArrayList<>();
        this.datePickerPanel = datePickerPanel;
        this.examLocationPanel = examLocationPanel;
        this.rosterAddedPanel = rosterAddedPanel;

        selectionButtonPanel = new JPanel();
        selectionButtonPanel.setLayout(null);
        selectionButtonPanel.setBackground(Color.WHITE);
        selectionButtonPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        selectionButtonPanel.setBounds(25, 240, 250, 40); 

        selectionButtonBorderPanel = new JPanel();
        selectionButtonBorderPanel.setLayout(new GridLayout(1, 2, 5 ,20));
        selectionButtonBorderPanel.setBackground(Color.WHITE);
        selectionButtonBorderPanel.setBounds(5 ,5 , 220, 30);

        selectionButtonPanel.add(selectionButtonBorderPanel);

        addExamLocationButton = new JButton("Add Exam");
        resetDateButton = new JButton("Reset Date");

        Dimension buttonSize = new Dimension(120, 50);
        addExamLocationButton.setPreferredSize(buttonSize);
        resetDateButton.setPreferredSize(buttonSize);

        addExamLocationButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                String fileAbsolutePath = rosterAddedPanel.getAbsoluteFilePath();
                String location = examLocationPanel.getExamLocation();
                int day = datePickerPanel.getDayInt();
                String month = datePickerPanel.getMonthString();
                int year = datePickerPanel.getYearInt();
        
                FieldValidator newValidator = new FieldValidator(fileAbsolutePath, location, day, month, year);
                newValidator.generateErrorMessage(); 
                String errorMessage = newValidator.getErrorMessage();
        
                if (errorMessage == null){  
                    if (!checkLocationInList(location)){
                        String rosterDetails = location + " " + day + "-" + month + "-" + year;
                        examAddedPanel.addRosterToPanel(rosterDetails);

                        RosterEntityDetails newRosterDetails = new RosterEntityDetails(fileAbsolutePath, location, day, month, year);
                        rosterObjectEntityList.add(newRosterDetails);

                        // Debugging Total Roster Added Print
                        // Remove
                        for (int i = 0; i < rosterObjectEntityList.size(); i++){
                            System.out.println(rosterObjectEntityList.get(i).toString());
                        }
                    }
                    else{
                        System.out.println("Roster Location Already Exists");
                    }
                } 
                else{
                    // UI Level Warning
                }
            }
        });

        resetDateButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                datePickerPanel.resetDate();
            }
        });

        selectionButtonBorderPanel.add(addExamLocationButton);
        selectionButtonBorderPanel.add(resetDateButton);
    }

    public void saveRosterObject(String location, int day, String month, int year){
        RosterEntityDetails newRosterEntity = new RosterEntityDetails(location, day, month, year);
        rosterObjectEntityList.add(newRosterEntity);
        System.out.println("Roster Detail Added.");
    }

    public JPanel getSelectionButtonPanel(){
        return selectionButtonPanel;
    }

    public boolean checkLocationInList(String newLocation){
        for (int i = 0; i < rosterObjectEntityList.size(); i++){
            if (rosterObjectEntityList.get(i).getLocation().equalsIgnoreCase(newLocation)){
                return true;
            }
        }
        return false;
    }
}
