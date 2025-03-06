package nep.swing.panels;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

@SuppressWarnings("FieldMayBeFinal")
public class ExamAddedPanel {
    private JPanel selectedPanel;
    private JPanel examListPanel;
    private int selectedPanelWidth;
    private int selectedPanelHeight;
    private int currentXPosition = 25;
    private ArrayList<String> addedRosterArray;

    @SuppressWarnings("Convert2Lambda")
    public ExamAddedPanel() {
        selectedPanelHeight = 125;
        selectedPanelWidth = 250;

        selectedPanel = new JPanel();
        selectedPanel.setLayout(null);
        selectedPanel.setBackground(Color.WHITE);
        selectedPanel.setBounds(300, 35, selectedPanelWidth, selectedPanelHeight + 65);
        selectedPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

        examListPanel = new JPanel();
        examListPanel.setLayout(null);
        examListPanel.setBounds(5, 5, selectedPanelWidth - 10, selectedPanelHeight + 55);
        examListPanel.setBackground(Color.WHITE);

        Border lineBorder = BorderFactory.createLineBorder(new Color(80, 80, 80), 0);

        TitledBorder titledBorder = new TitledBorder(lineBorder, "Selected Exams");
        titledBorder.setTitleFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
        examListPanel.setBorder(titledBorder);

        selectedPanel.add(examListPanel);

        JButton addButton = new JButton("Add Roster");
        addButton.setBounds(50, selectedPanelHeight - 50, 150, 30); 
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                addRosterToPanel("");
            }
        });
        
        selectedPanel.add(addButton);
    }

    public JPanel getExamAddedPanel(){
        return selectedPanel;
    }

    public JPanel getExamListPanel(){
        return examListPanel;
    }
    
    /**
     * 
     * @param rosterDetails
     */
    private void addRosterToPanel(String rosterDetails){
        JPanel newPanel = new JPanel();
        newPanel.setBounds(5, currentXPosition, selectedPanelWidth - 15, 25); 
        newPanel.setBackground(new Color(238,238,238,255)); 

        JLabel detailString = new JLabel("Balls");
        detailString.setBounds(0, 0, 0, 20);

        newPanel.add(detailString);
        examListPanel.add(newPanel);

        currentXPosition += 30; 
        selectedPanel.revalidate(); 
        selectedPanel.repaint(); 
    }
}
