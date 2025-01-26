package nep.swing.panels;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

@SuppressWarnings("FieldMayBeFinal")
public class DatePickerPanel{
    private int datePanelWidth;
    private int datePanelHeight;
    private JComboBox<String> dayComboBox;
    private JComboBox<String> monthComboBox;
    private JComboBox<String> yearComboBox;
    private JPanel datePickerPanel;

    public DatePickerPanel(){
        datePanelHeight = 175;
        datePanelWidth = 250;

        datePickerPanel = new JPanel();
        datePickerPanel.setLayout(new FlowLayout());
        datePickerPanel.setBackground(Color.WHITE);
        datePickerPanel.setBounds(25, 105, datePanelWidth, datePanelHeight - 105);
        datePickerPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

        Border bevelBorder = BorderFactory.createBevelBorder(BevelBorder.LOWERED);

        Border lineBorder = BorderFactory.createLineBorder(new Color(80, 80, 80), 0);
        TitledBorder titledBorder = new TitledBorder(lineBorder, "Select a Date");
        titledBorder.setTitleFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));

        Border compoundBorder = BorderFactory.createCompoundBorder(bevelBorder, titledBorder);

        datePickerPanel.setBorder(compoundBorder);
    }

    public JPanel getDatePickerPanel(){
        return datePickerPanel; 
    }
}
