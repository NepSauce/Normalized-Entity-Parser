package nep.swing.panels;

import javax.swing.JTabbedPane;

import nep.swing.panels.tabpanels.CommandButtonPanel;

public class SettingsTab {
    JTabbedPane tabbedPane;

    public SettingsTab(ExamLocationPanel examLocationPanel, ExamAddedPanel examAddedPanel, DatePickerPanel datePickerPanel){
        tabbedPane = new JTabbedPane();
        CommandButtonPanel commandButtonPanel = new CommandButtonPanel(examLocationPanel, examAddedPanel, datePickerPanel);
        tabbedPane.addTab("Base", commandButtonPanel.getCommandButtonPanel());
        tabbedPane.setBounds(25, 190, 250, 85);

    }

    public JTabbedPane getSettingsTab(){
        return tabbedPane;
    }
}
