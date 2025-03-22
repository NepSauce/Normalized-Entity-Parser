package nep.swing.panels;

import javax.swing.JTabbedPane;

import nep.swing.panels.tabpanels.PanelButtonPalette;
import nep.swing.panels.tabpanels.SelectionButtonPalette;

public class SettingsTab {
    JTabbedPane tabbedPane;

    public SettingsTab(ExamLocationPanel examLocationPanel, ExamAddedPanel examAddedPanel, 
        DatePickerPanel datePickerPanel, RosterAddedPanel rosterAddedPanel){
        tabbedPane = new JTabbedPane();
        tabbedPane.setBounds(25, 240, 250, 70);

        SelectionButtonPalette commandButtonPanel = new SelectionButtonPalette(examLocationPanel, 
            examAddedPanel, datePickerPanel, rosterAddedPanel);
        tabbedPane.addTab("Selection", commandButtonPanel.getSelectionButtonPanel());

        PanelButtonPalette panelButtonPalette = new PanelButtonPalette(examLocationPanel, 
            examAddedPanel, datePickerPanel, rosterAddedPanel);
        tabbedPane.addTab("Panel", panelButtonPalette.getPanelButtonPanel());
    }

    public JTabbedPane getSettingsTab(){
        return tabbedPane;
    }
}
