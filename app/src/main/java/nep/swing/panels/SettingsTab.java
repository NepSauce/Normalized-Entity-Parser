package nep.swing.panels;

import javax.swing.*;
import java.awt.Component;
import java.awt.Container;

import nep.swing.panels.rostertabpanels.DevModeButtonPalette;
import nep.swing.panels.rostertabpanels.PanelButtonPalette;
import nep.swing.panels.rostertabpanels.SelectionButtonPalette;

public class SettingsTab {
    JTabbedPane tabbedPane;
    
    public SettingsTab(ExamLocationPanel examLocationPanel, ExamAddedPanel examAddedPanel,
                       DatePickerPanel datePickerPanel, RosterAddedPanel rosterAddedPanel){
        tabbedPane = new JTabbedPane();
        tabbedPane.setBounds(25, 240, 250, 80);
        
        // Selection Tab
        SelectionButtonPalette commandButtonPanel = new SelectionButtonPalette(examLocationPanel,
                examAddedPanel, datePickerPanel, rosterAddedPanel);
        tabbedPane.addTab("Selection", commandButtonPanel.getSelectionButtonPanel());
        addTooltips(commandButtonPanel.getSelectionButtonPanel());
        
        // Panel Tab
        PanelButtonPalette panelButtonPalette = new PanelButtonPalette(examLocationPanel,
                examAddedPanel, datePickerPanel, rosterAddedPanel);
        tabbedPane.addTab("Panel", panelButtonPalette.getPanelButtonPanel());
        addTooltips(panelButtonPalette.getPanelButtonPanel());
        
        // DevMode Tab
        DevModeButtonPalette devModeButtonPalette = new DevModeButtonPalette();
        tabbedPane.addTab("DevMode", devModeButtonPalette.getDevModePanel());
        addTooltips(devModeButtonPalette.getDevModePanel());
    }
    
    public JTabbedPane getSettingsTab(){
        return tabbedPane;
    }
    
    // Method to add tooltips to all buttons inside a container, recursively checking all subcomponents
    private void addTooltips(Component container) {
        if (container instanceof JPanel) {
            for (Component c : ((JPanel) container).getComponents()) {
                if (c instanceof JButton btn) {
                    btn.setToolTipText(btn.getText()); // Tooltip will show the button's text
                } else if (c instanceof Container) {
                    addTooltips((Container) c); // Recursively check sub-containers
                }
            }
        }
    }
}
