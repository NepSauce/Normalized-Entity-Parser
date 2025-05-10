package nep.swing.panels.devmodepanels.devmodeduplicates;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class LogPanelDev {
    private JPanel mainPanel;
    private JTextArea logArea;
    
    public LogPanelDev() {
        mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBounds(25, 25, 500, 300);
        mainPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                "Log Viewer",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font("Arial", Font.BOLD, 14)
        ));
        
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        
        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBounds(10, 25, 480, 260);
        
        mainPanel.add(scrollPane);
        
        // Always load the only file from "logs/" directory
        File logDir = new File("logs/");
        File[] files = logDir.listFiles((dir, name) -> name.endsWith(".txt"));
        
        if (files != null && files.length == 1) {
            loadLogFile(files[0]);
        } else {
            logArea.setText("Log file not found or multiple files exist in the 'logs/' folder.");
        }
    }
    
    
    private void loadLogFile(File logFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
            logArea.setText(builder.toString());
        } catch (IOException e) {
            logArea.setText("Failed to load log file:\n" + e.getMessage());
        }
    }
    
    public JPanel getLogPanel() {
        return mainPanel;
    }
}
