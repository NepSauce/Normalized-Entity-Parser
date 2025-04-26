package nep.swing.panels.tabpanels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private static LinkedList<RosterEntityDetails> rosterObjectEntityList = new LinkedList<>();

    @SuppressWarnings("Convert2Lambda")
    public SelectionButtonPalette(ExamLocationPanel examLocationPanel, ExamAddedPanel examAddedPanel, 
        DatePickerPanel datePickerPanel, RosterAddedPanel rosterAddedPanel){ 
        
        rosterObjectEntityList.clear();
        this.datePickerPanel = datePickerPanel;
        this.examLocationPanel = examLocationPanel;
        this.rosterAddedPanel = rosterAddedPanel;

        selectionButtonPanel = new JPanel();
        selectionButtonPanel.setLayout(null);
        selectionButtonPanel.setBackground(Color.WHITE);
        selectionButtonPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        selectionButtonPanel.setBounds(25, 240, 250, 40); 

        selectionButtonBorderPanel = new JPanel();
        selectionButtonBorderPanel.setLayout(new GridLayout(1, 2, 10 ,20));
        selectionButtonBorderPanel.setBackground(Color.WHITE);
        selectionButtonBorderPanel.setBounds(10 ,10 , 225, 35);

        selectionButtonPanel.add(selectionButtonBorderPanel);

        addExamLocationButton = new JButton("Add Exam");
        resetDateButton = new JButton("Reset Date");

        Dimension buttonSize = new Dimension(120, 40);
        addExamLocationButton.setPreferredSize(buttonSize);
        resetDateButton.setPreferredSize(buttonSize);

        addExamLocationButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                String fileName = rosterAddedPanel.getRosterFileName();
                String fileAbsolutePath = rosterAddedPanel.getAbsoluteFilePath();
                String location = examLocationPanel.getExamLocation();
                int day = datePickerPanel.getDayInt();
                String month = datePickerPanel.getMonthString();
                int year = datePickerPanel.getYearInt();
        
                FieldValidator newValidator = new FieldValidator(fileAbsolutePath, location, day, month, year);
                newValidator.generateErrorMessage(); 
                String errorMessage = newValidator.getErrorMessage();
                System.out.println(errorMessage);
        
                if (errorMessage == null){
                    saveRosterObject(fileName, fileAbsolutePath, location, day, month, year);
                    String rosterDetails = location + " " + day + "-" + month + "-" + year;
                    examAddedPanel.addRosterToPanel(rosterDetails);
                    for (int i = 0; i < rosterObjectEntityList.size(); i++){
                        System.out.println(rosterObjectEntityList.get(i).getLocation());
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

        // Add buttons to the panel
        selectionButtonBorderPanel.add(addExamLocationButton);
        selectionButtonBorderPanel.add(resetDateButton);
    }

    public void saveRosterObject(String fileName, String directory, String location, int day, String month, int year){
        RosterEntityDetails newRosterEntity = new RosterEntityDetails(fileName, directory, location, day, month, year);
        rosterObjectEntityList.add(newRosterEntity);
        System.out.println("Roster Detail Added.");
    }

    public JPanel getSelectionButtonPanel(){
        return selectionButtonPanel;
    }
    
    public static LinkedList<RosterEntityDetails> getRosterEntityList(){
        return rosterObjectEntityList;
    }
}
