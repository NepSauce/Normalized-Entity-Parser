package nep.swing.panels;

import javax.swing.JTabbedPane;

import nep.swing.panels.tabpanels.CommandButtonPanel;

public class SettingsTab {
    JTabbedPane tabbedPane;

    public SettingsTab(ExamLocationPanel examLocationPanel, ExamAddedPanel examAddedPanel, DatePickerPanel datePickerPanel, RosterAddedPanel rosterAddedPanel){
        tabbedPane = new JTabbedPane();
        CommandButtonPanel commandButtonPanel = new CommandButtonPanel(examLocationPanel, examAddedPanel, datePickerPanel, rosterAddedPanel);
        tabbedPane.addTab("Base", commandButtonPanel.getCommandButtonPanel());
        tabbedPane.setBounds(25, 230, 250, 85);

    }

    public JTabbedPane getSettingsTab(){
        return tabbedPane;
    }
}
