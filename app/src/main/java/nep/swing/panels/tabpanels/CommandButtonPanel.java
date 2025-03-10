package nep.swing.panels.tabpanels;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import nep.entityclass.RosterEntityDetails;
import nep.swing.panels.DatePickerPanel;
import nep.swing.panels.ExamAddedPanel;
import nep.swing.panels.ExamLocationPanel;
import nep.swing.panels.RosterAddedPanel;
import nep.util.FieldValidator;

@SuppressWarnings("FieldMayBeFinal")
public class CommandButtonPanel {
    private JButton addExamLocationButton;
    private JButton clearExamLocationButton;
    private JPanel commandButtonPanel;
    private DatePickerPanel datePickerPanel;
    private ExamLocationPanel examLocationPanel;
    private RosterAddedPanel rosterAddedPanel;
    private LinkedList<RosterEntityDetails> rosterObjectEntityList;

    @SuppressWarnings("Convert2Lambda")
    public CommandButtonPanel(ExamLocationPanel examLocationPanel, ExamAddedPanel examAddedPanel, DatePickerPanel datePickerPanel, RosterAddedPanel rosterAddedPanel){ 
        this.rosterObjectEntityList = new LinkedList<>();
        this.datePickerPanel = datePickerPanel;
        this.examLocationPanel = examLocationPanel;
        this.rosterAddedPanel = rosterAddedPanel;

        commandButtonPanel = new JPanel();
        commandButtonPanel.setLayout(new BoxLayout(commandButtonPanel, BoxLayout.X_AXIS));
        commandButtonPanel.setBackground(Color.WHITE);
        commandButtonPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        commandButtonPanel.setBounds(25, 225, 250, 50);

        addExamLocationButton = new JButton("Add Exam");
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
                System.out.println(errorMessage);
        
                if (errorMessage == null){  
                    String rosterDetails = location + " " + day + "-" + month + "-" + year;
                    examAddedPanel.addRosterToPanel(rosterDetails);
                } 
                else{
                    // UI Level Warning 
                }
            }
        });        

        clearExamLocationButton = new JButton("Reset Panel");
        clearExamLocationButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                
            }
        });

        addExamLocationButton.setAlignmentX(JButton.LEFT_ALIGNMENT);
        clearExamLocationButton.setAlignmentX(JButton.LEFT_ALIGNMENT);

        commandButtonPanel.add(Box.createVerticalStrut(0));
        commandButtonPanel.add(addExamLocationButton);
        commandButtonPanel.add(Box.createVerticalStrut(0));
        commandButtonPanel.add(clearExamLocationButton);
        commandButtonPanel.add(Box.createVerticalStrut(0));
    }

    public void saveRosterObject(String location, int day, String month, int year){
        RosterEntityDetails newRosterEntity = new RosterEntityDetails(location, day, month, year);
        rosterObjectEntityList.add(newRosterEntity);
        System.out.println("Roster Detail Added.");
    }

    public JPanel getCommandButtonPanel(){
        return commandButtonPanel;
    }
    
}
