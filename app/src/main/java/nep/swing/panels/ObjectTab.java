package nep.swing.panels;

import javax.swing.*;
import java.awt.*;
import nep.swing.panels.objecttabpanels.CombinedButtonPalette;
import nep.swing.panels.objecttabpanels.RemovedButtonPalette;
import nep.swing.panels.objecttabpanels.GroupedButtonPalette;

public class ObjectTab {
    private JTabbedPane objectPane;
    
    public ObjectTab(ExamLocationPanel examLocationPanel, ExamAddedPanel examAddedPanel,
                     DatePickerPanel datePickerPanel, RosterAddedPanel rosterAddedPanel) {
        objectPane = new JTabbedPane();
        objectPane.setBounds(300, 240, 250, 80);
        
        JPanel combinedPanel = new CombinedButtonPalette().getCombinedButtonPanel();
        JPanel removedPanel = new RemovedButtonPalette().getRemovedButtonPanel();
        JPanel groupedPanel = new GroupedButtonPalette().getGroupedButtonPanel();
   
        addTooltips(combinedPanel);
        addTooltips(removedPanel);
        addTooltips(groupedPanel);
        
        objectPane.addTab("Combined", combinedPanel);
        objectPane.addTab("Removed", removedPanel);
        objectPane.addTab("Grouped", groupedPanel);
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
