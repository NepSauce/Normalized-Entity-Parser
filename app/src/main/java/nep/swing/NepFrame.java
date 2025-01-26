package nep.swing;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import nep.swing.panels.DatePickerPanel;
import nep.swing.panels.ExamAddedPanel;
import nep.swing.panels.ExamLocationPanel;

public class NepFrame extends JFrame{
    /**
     * Default Constructor for NepFrame
     * EXIT_ON_CLOSE
     */
    public NepFrame(){
        setTitle("NEP");
        setSize(600, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
        getContentPane().setBackground(new Color(238,238,238,255));
        
        ImageIcon logo = new ImageIcon("Media/logo.png");
        setIconImage(logo.getImage());

        setLayout(new BorderLayout());

        ExamLocationPanel examLocationPanel = new ExamLocationPanel();
        add(examLocationPanel.getExamLocationPanel());

        ExamAddedPanel examAddedPanel = new ExamAddedPanel();
        add(examAddedPanel.getExamAddedPanel());

        DatePickerPanel datePickerPanel = new DatePickerPanel();
        add(datePickerPanel.getDatePickerPanel());

        setLayout(null);
    }
}
