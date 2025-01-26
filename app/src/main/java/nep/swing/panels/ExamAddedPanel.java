package nep.swing.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class ExamAddedPanel {
    private JPanel selectedPanel;
    private JPanel examListPanel;
    private int selectedPanelWidth, selectedPanelHeight;
    private int currentXPosition = 25;
    private ArrayList<String> addedRosterArray;

    public ExamAddedPanel() {
        selectedPanelHeight = 175;
        selectedPanelWidth = 250;

        selectedPanel = new JPanel();
        selectedPanel.setLayout(null);
        selectedPanel.setBackground(Color.WHITE);
        selectedPanel.setBounds(300, 35, selectedPanelWidth, selectedPanelHeight + 65); // Increased height to fit the button
        selectedPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

        examListPanel = new JPanel();
        examListPanel.setLayout(null);
        examListPanel.setBounds(5, 5, selectedPanelWidth - 10, selectedPanelHeight);
        examListPanel.setBackground(Color.WHITE);

        Border lineBorder = BorderFactory.createLineBorder(new Color(80, 80, 80), 0);

        TitledBorder titledBorder = new TitledBorder(lineBorder, "Selected Exams");
        titledBorder.setTitleFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
        examListPanel.setBorder(titledBorder);

        selectedPanel.add(examListPanel);

        // Add button to trigger adding new horizontal panels
        JButton addButton = new JButton("Add Exam");
        addButton.setBounds(50, selectedPanelHeight + 5, 150, 30); // Adjusted position to fit within the new height
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addHorizontalPanel();
            }
        });
        
        selectedPanel.add(addButton);
    }

    public JPanel getExamAddedPanel() {
        return selectedPanel;
    }

    public JPanel getExamListPanel() {
        return examListPanel;
    }

    private void addHorizontalPanel() {
        JPanel newPanel = new JPanel();
        newPanel.setBounds(5, currentXPosition, selectedPanelWidth - 15, 25); // New panel next to previous one
        newPanel.setBackground(new Color(100, 100, 100)); // Example background color

        // Add a title to the new panel (optional)
        // Add the new panel to the main panel
        examListPanel.add(newPanel);

        // Update X position for the next panel
        currentXPosition += 30; // Adjust this value based on the size of the panels
        selectedPanel.revalidate(); // Revalidate the panel to update the layout
        selectedPanel.repaint(); // Repaint to show the new panel
    }
}
