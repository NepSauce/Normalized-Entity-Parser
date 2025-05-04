package nep.swing.panels.objecttabpanels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class RemovedButtonPalette {
    private JPanel panel;
    
    public RemovedButtonPalette() {
        panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createBevelBorder(1));
        panel.setPreferredSize(new Dimension(100, 60));
        
        JPanel buttonContainer = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonContainer.setBounds(10, 10, 225, 35);
        buttonContainer.setBackground(Color.WHITE);
        
        JButton modify = new JButton("Edit Entry");
        JButton delete = new JButton("Delete");
        
        delete.addActionListener((ActionEvent e) -> {
            int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete from Removed?",
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                System.out.println("Deleted from Removed");
            }
        });
        
        buttonContainer.add(modify);
        buttonContainer.add(delete);
        panel.add(buttonContainer);
    }
    
    public JPanel getPanel() {
        return panel;
    }
}