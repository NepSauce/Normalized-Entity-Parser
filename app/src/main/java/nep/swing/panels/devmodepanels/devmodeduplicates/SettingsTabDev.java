package nep.swing.panels.devmodepanels.devmodeduplicates;

import nep.swing.panels.DatePickerPanel;
import nep.swing.panels.ExamAddedPanel;
import nep.swing.panels.ExamLocationPanel;
import nep.swing.panels.RosterAddedPanel;
import nep.swing.panels.devmodepanels.devmodeduplicates.rostertabpanelsdev.DevModeButtonPaletteDev;
import nep.swing.panels.devmodepanels.devmodeduplicates.rostertabpanelsdev.PanelButtonPaletteDev;
import nep.swing.panels.devmodepanels.devmodeduplicates.rostertabpanelsdev.SelectionButtonPaletteDev;
import nep.swing.panels.rostertabpanels.DevModeButtonPalette;
import nep.swing.panels.rostertabpanels.PanelButtonPalette;
import nep.swing.panels.rostertabpanels.SelectionButtonPalette;

import javax.swing.*;
import java.awt.*;

public class SettingsTabDev {
    JTabbedPane tabbedPane;
    
    public SettingsTabDev(ExamLocationPanelDev examLocationPanel, ExamAddedPanelDev examAddedPanel,
                          DatePickerPanel datePickerPanel, RosterAddedPanelDev rosterAddedPanel){
        tabbedPane = new JTabbedPane();
        tabbedPane.setBounds(25, 230, 250, 80);

        SelectionButtonPaletteDev commandButtonPanel = new SelectionButtonPaletteDev(examLocationPanel,
                examAddedPanel, datePickerPanel, rosterAddedPanel);
        tabbedPane.addTab("Selection", commandButtonPanel.getSelectionButtonPanel());
        addTooltips(commandButtonPanel.getSelectionButtonPanel());

        PanelButtonPaletteDev panelButtonPalette = new PanelButtonPaletteDev(examLocationPanel,
                examAddedPanel, datePickerPanel, rosterAddedPanel);
        tabbedPane.addTab("Panel", panelButtonPalette.getPanelButtonPanel());
        addTooltips(panelButtonPalette.getPanelButtonPanel());

        DevModeButtonPaletteDev devModeButtonPalette = new DevModeButtonPaletteDev();
        tabbedPane.addTab("DevMode", devModeButtonPalette.getDevModePanel());
        addTooltips(devModeButtonPalette.getDevModePanel());
    }
    
    public JTabbedPane getSettingsTab(){
        return tabbedPane;
    }

    private void addTooltips(Component container) {
        if (container instanceof JPanel) {
            for (Component c : ((JPanel) container).getComponents()) {
                if (c instanceof JButton btn) {
                    btn.setToolTipText(btn.getText());
                } else if (c instanceof Container) {
                    addTooltips((Container) c);
                }
            }
        }
    }
}
