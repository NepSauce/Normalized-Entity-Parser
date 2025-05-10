package nep.swing.panels.devmodepanels.devmodeduplicates;

import nep.swing.panels.devmodepanels.devmodeduplicates.rostertabpanelsdev.PanelButtonPaletteDev;
import nep.swing.panels.devmodepanels.devmodeduplicates.rostertabpanelsdev.SelectionButtonPaletteDev;

import javax.swing.*;
import java.awt.*;

public class SettingsTabDev {
    JTabbedPane tabbedPane;
    
    public SettingsTabDev(ExamLocationPanelDev examLocationPanel, ExamAddedPanelDev examAddedPanel,
                          RosterAddedPanelDev rosterAddedPanel){
        tabbedPane = new JTabbedPane();
        tabbedPane.setBounds(25, 300, 250, 80);

        SelectionButtonPaletteDev commandButtonPanel = new SelectionButtonPaletteDev(examLocationPanel,
                examAddedPanel, rosterAddedPanel);
        tabbedPane.addTab("Selection", commandButtonPanel.getSelectionButtonPanel());
        addTooltips(commandButtonPanel.getSelectionButtonPanel());

        PanelButtonPaletteDev panelButtonPalette = new PanelButtonPaletteDev(examLocationPanel,
                examAddedPanel, rosterAddedPanel);
        tabbedPane.addTab("Panel", panelButtonPalette.getPanelButtonPanel());
        addTooltips(panelButtonPalette.getPanelButtonPanel());
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
