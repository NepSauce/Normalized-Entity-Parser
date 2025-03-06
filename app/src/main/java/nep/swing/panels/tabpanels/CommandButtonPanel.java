package nep.swing.panels.tabpanels;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import nep.swing.panels.DatePickerPanel;
import nep.swing.panels.ExamAddedPanel;
import nep.swing.panels.ExamLocationPanel;

@SuppressWarnings("FieldMayBeFinal")
public class CommandButtonPanel {
    private JButton addExamLocationButton;
    private JButton clearExamLocationButton;
    private JPanel commandButtonPanel;

    @SuppressWarnings("Convert2Lambda")
    public CommandButtonPanel(ExamLocationPanel examLocationPanel, ExamAddedPanel examAddedPanel, DatePickerPanel datePickerPanel){ 
        commandButtonPanel = new JPanel();
        commandButtonPanel.setLayout(new BoxLayout(commandButtonPanel, BoxLayout.X_AXIS));
        commandButtonPanel.setBackground(Color.WHITE);
        commandButtonPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        commandButtonPanel.setBounds(25, 225, 250, 50);

        addExamLocationButton = new JButton("Add Exam");
        addExamLocationButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                examAddedPanel.addRosterToPanel("");
            }
        });

        clearExamLocationButton = new JButton("Reset Panel");

        addExamLocationButton.setAlignmentX(JButton.LEFT_ALIGNMENT);
        clearExamLocationButton.setAlignmentX(JButton.LEFT_ALIGNMENT);

        commandButtonPanel.add(Box.createVerticalStrut(0));
        commandButtonPanel.add(addExamLocationButton);
        commandButtonPanel.add(Box.createVerticalStrut(0));
        commandButtonPanel.add(clearExamLocationButton);
        commandButtonPanel.add(Box.createVerticalStrut(0));
    }

    public JPanel getCommandButtonPanel(){
        return commandButtonPanel;
    }
    
}
