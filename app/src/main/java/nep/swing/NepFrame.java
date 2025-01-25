package nep.swing;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import nep.swing.panels.ExamLocationPanel;

public class NepFrame extends JFrame{
    /**
     * Default Constructor for NepFrame
     * EXIT_ON_CLOSE
     */
    public NepFrame(){
        setTitle("NEP");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);

        ImageIcon logo = new ImageIcon("Media/logo.png");
        setIconImage(logo.getImage());

        setLayout(new BorderLayout());

        ExamLocationPanel examLocationPanel = new ExamLocationPanel();
        add(examLocationPanel.getExamLocationPanel());

        setLayout(null);
    }
}
