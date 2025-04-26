package nep.swing.panels.tabpanels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import nep.swing.panels.DatePickerPanel;
import nep.swing.panels.ExamAddedPanel;
import nep.swing.panels.ExamLocationPanel;
import nep.swing.panels.RosterAddedPanel;

@SuppressWarnings("FieldMayBeFinal")
public class PanelButtonPalette{
    private JButton submitAllRostersButton;
    private JButton clearAllRostersFromPanelButton;
    private JButton undoLastRosterFromPanelButton;
    private JPanel panelButtonPanel;
    private JPanel panelButtonBorderPanel;

    public PanelButtonPalette(ExamLocationPanel examLocationPanel, ExamAddedPanel examAddedPanel, 
        DatePickerPanel datePickerPanel, RosterAddedPanel rosterAddedPanel){

        panelButtonPanel = new JPanel();
        panelButtonPanel.setLayout(null);
        panelButtonPanel.setBackground(Color.WHITE);
        panelButtonPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        panelButtonPanel.setBounds(25, 240, 250, 40);
        
        panelButtonBorderPanel = new JPanel();
        panelButtonBorderPanel.setLayout(new GridLayout(1, 2, 5 ,20));
        panelButtonBorderPanel.setBackground(Color.WHITE);
        panelButtonBorderPanel.setBounds(5, 5, 220, 30);

        panelButtonPanel.add(panelButtonBorderPanel);

        submitAllRostersButton = new JButton("Submit");
        clearAllRostersFromPanelButton = new JButton("Clear");
        undoLastRosterFromPanelButton = new JButton("Undo");

        Dimension buttonSize = new Dimension(80, 50);
        submitAllRostersButton.setPreferredSize(buttonSize);
        clearAllRostersFromPanelButton.setPreferredSize(buttonSize);
        undoLastRosterFromPanelButton.setPreferredSize(buttonSize);

        submitAllRostersButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                // Submit all rosters
            }
        });

        clearAllRostersFromPanelButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                // Clear all rosters
            }
        });

        undoLastRosterFromPanelButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                // Undo last roster submission
            }
        });

        panelButtonBorderPanel.add(submitAllRostersButton);
        panelButtonBorderPanel.add(clearAllRostersFromPanelButton);
        panelButtonBorderPanel.add(undoLastRosterFromPanelButton);
    }

    public JPanel getPanelButtonPanel(){
        return panelButtonPanel;
    }
}
