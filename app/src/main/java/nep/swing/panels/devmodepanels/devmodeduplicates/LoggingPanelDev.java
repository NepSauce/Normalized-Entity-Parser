package nep.swing.panels.devmodepanels.devmodeduplicates;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@SuppressWarnings("FieldMayBeFinal")
public class LoggingPanelDev {
    private JPanel mainPanel;
    private JPanel logListPanel;
    private int panelWidth;
    private int panelHeight;
    private int currentYPosition = 25;
    private ArrayList<JLabel> logEntries;
    
    public LoggingPanelDev() {
        panelWidth = 225;
        panelHeight = 245;
        logEntries = new ArrayList<>();
        
        mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBounds(300, 155, panelWidth, panelHeight);
        mainPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        
        logListPanel = new JPanel();
        logListPanel.setLayout(null);
        logListPanel.setBackground(Color.WHITE);
        logListPanel.setPreferredSize(new Dimension(panelWidth - 30, panelHeight));
        
        TitledBorder titledBorder = BorderFactory.createTitledBorder(
                BorderFactory.createEmptyBorder(),
                "System Log",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font("Arial", Font.BOLD, 16)
        );
        logListPanel.setBorder(titledBorder);
        
        JScrollPane scrollPane = new JScrollPane(logListPanel);
        scrollPane.setBounds(5, 5, panelWidth - 10, panelHeight - 10);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        mainPanel.add(scrollPane);
        
        log("NEP Build-1.0.0-Alpha");
    }
    
    public JPanel getLoggingPanel() {
        return mainPanel;
    }
    
    public void log(String message) {
        // Get current time in HH:mm:ss format
        String timeStamp = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        
        // Combine time with message
        String fullMessage = "[" + timeStamp + "] " + message;
        
        JLabel logLabel = new JLabel(fullMessage);
        logLabel.setFont(new Font("Consolas", Font.BOLD, 12));
        logLabel.setBounds(5, currentYPosition, panelWidth - 50, 20);
        logListPanel.add(logLabel);
        logEntries.add(logLabel);
        
        currentYPosition += 25;
        logListPanel.setPreferredSize(new Dimension(panelWidth - 30, currentYPosition + 30));
        logListPanel.revalidate();
        logListPanel.repaint();
    }
    
    public void clearLog() {
        for (JLabel logLabel : logEntries) {
            logListPanel.remove(logLabel);
        }
        logEntries.clear();
        currentYPosition = 25;
        logListPanel.setPreferredSize(new Dimension(panelWidth - 30, panelHeight));
        logListPanel.revalidate();
        logListPanel.repaint();
    }
    
    public void undoLastLog() {
        if (!logEntries.isEmpty()) {
            JLabel last = logEntries.remove(logEntries.size() - 1);
            logListPanel.remove(last);
            currentYPosition -= 25;
            logListPanel.revalidate();
            logListPanel.repaint();
        }
    }
}
