package nep.swing.panels.tabpanels;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import nep.swing.panels.DatePickerPanel;
import nep.swing.panels.ExamAddedPanel;
import nep.swing.panels.ExamLocationPanel;
import nep.swing.panels.RosterAddedPanel;

public class PanelButtonPalette {
    private JButton submitAllRostersButton;
    private JButton clearAllRostersFromPanel;
    private JButton undoLastRosterFromPanel;
    private JPanel panelButtonPanel;

    public PanelButtonPalette(ExamLocationPanel examLocationPanel, ExamAddedPanel examAddedPanel, 
        DatePickerPanel datePickerPanel, RosterAddedPanel rosterAddedPanel){
        panelButtonPanel = new JPanel();
        panelButtonPanel.setLayout(new  BoxLayout(panelButtonPanel, BoxLayout.X_AXIS));
        panelButtonPanel.setBackground(Color.WHITE);
        panelButtonPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        panelButtonPanel.setBounds(25, 225, 250, 50);

        submitAllRostersButton = new JButton("Submit");
        submitAllRostersButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                
            }
        });
    }

    public JPanel getPanelButtonPanel(){
        return panelButtonPanel;
    }
}
