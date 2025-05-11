package nep.swing.panels.devmodepanels.devmodeduplicates;

import javax.swing.*;
import java.awt.*;

import nep.swing.panels.devmodepanels.devmodeduplicates.objecttabpanelsdev.GroupedButtonPaletteDev;
import nep.swing.panels.objecttabpanels.CombinedButtonPalette;
import nep.swing.panels.objecttabpanels.RemovedButtonPalette;
import nep.swing.panels.objecttabpanels.GroupedButtonPalette;

public class ObjectTabDev {
    private JTabbedPane objectPane;
    
    public ObjectTabDev(ExamLocationPanelDev examLocationPanel, ExamAddedPanelDev examAddedPanel,
                     RosterAddedPanelDev rosterAddedPanel, CumulativeInfoPanelDev cumulativeInfoPanel) {
        objectPane = new JTabbedPane();
        objectPane.setBounds(25, 400, 250, 80);
        
        JPanel combinedPanel = new CombinedButtonPalette().getCombinedButtonPanel();
        JPanel removedPanel = new RemovedButtonPalette().getRemovedButtonPanel();
        GroupedButtonPaletteDev groupedPanel = new GroupedButtonPaletteDev(examLocationPanel, examAddedPanel,
                                                                           rosterAddedPanel, cumulativeInfoPanel);
        objectPane.add(groupedPanel.getGroupedButtonPanel());
        
        addTooltips(combinedPanel);
        addTooltips(removedPanel);
        addTooltips(groupedPanel.getGroupedButtonPanel());
        
        objectPane.addTab("Combined", combinedPanel);
        objectPane.addTab("Removed", removedPanel);
        objectPane.addTab("Grouped", groupedPanel.getGroupedButtonPanel());
    }
    
    public JTabbedPane getObjectTab() {
        return objectPane;
    }
    
    private void addTooltips(Container container) {
        for (Component c : container.getComponents()) {
            if (c instanceof JButton btn) {
                btn.setToolTipText(btn.getText());
            } else if (c instanceof Container) {
                addTooltips((Container) c);
            }
        }
    }
}
