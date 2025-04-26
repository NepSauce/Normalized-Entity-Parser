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
public class PanelButtonPalette {
    private JButton submitAllRostersButton;
    private JButton clearAllRostersFromPanelButton;
    private JButton undoLastRosterFromPanelButton;
    private JPanel panelButtonPanel;
    private JPanel buttonContainerPanel;

    public PanelButtonPalette(ExamLocationPanel examLocationPanel, ExamAddedPanel examAddedPanel,
                               DatePickerPanel datePickerPanel, RosterAddedPanel rosterAddedPanel) {

        panelButtonPanel = new JPanel();
        panelButtonPanel.setLayout(null); // 1 row, 3 columns, 10px horizontal gap
        panelButtonPanel.setBackground(Color.WHITE);
        panelButtonPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        panelButtonPanel.setPreferredSize(new Dimension(100, 60)); // Adjust to fit nicely

        buttonContainerPanel = new JPanel();
        buttonContainerPanel.setLayout(new GridLayout(1, 3, 10, 0));
        buttonContainerPanel.setBackground(Color.WHITE);
        buttonContainerPanel.setBounds(10, 10 , 225, 35);
        
        panelButtonPanel.add(buttonContainerPanel);

        submitAllRostersButton = new JButton("Submit");
        clearAllRostersFromPanelButton = new JButton("Clear");
        undoLastRosterFromPanelButton = new JButton("Undo");

        submitAllRostersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Submit all rosters
            }
        });

        clearAllRostersFromPanelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Clear all rosters
            }
        });

        undoLastRosterFromPanelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Undo last roster submission
            }
        });

        buttonContainerPanel.add(submitAllRostersButton);
        buttonContainerPanel.add(clearAllRostersFromPanelButton);
        buttonContainerPanel.add(undoLastRosterFromPanelButton);
    }

    public JPanel getPanelButtonPanel() {
        return panelButtonPanel;
    }
}
