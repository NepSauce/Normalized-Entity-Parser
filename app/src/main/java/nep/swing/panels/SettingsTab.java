package nep.swing.panels;

import javax.swing.JTabbedPane;

public class SettingsTab {
    JTabbedPane tabbedPane;

    public SettingsTab(){
        tabbedPane = new JTabbedPane();
        CommandButtonPanel commandButtonPanel = new CommandButtonPanel();
        tabbedPane.addTab("Base", commandButtonPanel.getCommandButtonPanel());
        tabbedPane.setBounds(25, 190, 250, 85);

    }

    public JTabbedPane getSettingsTab(){
        return tabbedPane;
    }
}
