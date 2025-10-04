package nep.swing.panels.devmodepanels.devmodeduplicates;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@SuppressWarnings("FieldMayBeFinal")
public class LoggingPanelDev{
    private JPanel mainPanel;
    private JPanel logListPanel;
    private int panelWidth;
    private int panelHeight;
    private ArrayList<JLabel> logEntries;
    
    public LoggingPanelDev(){
        panelWidth = 250;
        panelHeight = 245;
        logEntries = new ArrayList<>();
        
        mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBounds(300, 155, panelWidth, panelHeight);
        mainPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        
        logListPanel = new JPanel();
        logListPanel.setLayout(new BoxLayout(logListPanel, BoxLayout.Y_AXIS));
        logListPanel.setBackground(Color.WHITE);
        
        TitledBorder titledBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE),
                "System Log",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font("Arial", Font.BOLD, 16)
        );

        Border padding = BorderFactory.createEmptyBorder(8, 0, 5, 0);
        logListPanel.setBorder(BorderFactory.createCompoundBorder(titledBorder, padding));
        
        
        JScrollPane scrollPane = new JScrollPane(logListPanel);
        scrollPane.setBounds(5, 5, panelWidth - 10, panelHeight - 10);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        mainPanel.add(scrollPane);
        
        log("NEP Build-1.0.0-Alpha");
    }
    
    public JPanel getLoggingPanel(){
        return mainPanel;
    }
    
    public void log(String message){
        String timeStamp = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String fullMessage = "[" + timeStamp + "] " + message;
        
        JLabel logLabel = new JLabel(fullMessage);
        logLabel.setFont(new Font("Consolas", Font.BOLD, 12));
        logLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        logLabel.setPreferredSize(new Dimension(1000, 20));
        
        logListPanel.add(logLabel);
        logEntries.add(logLabel);
        
        logListPanel.revalidate();
        logListPanel.repaint();
    }
    
    public void clearLog(){
        logListPanel.removeAll();
        logEntries.clear();
        log("NEP Build-1.0.0-Alpha");
        logListPanel.revalidate();
        logListPanel.repaint();
    }
    
    public void undoLastLog(){
        if (!logEntries.isEmpty()) {
            JLabel last = logEntries.remove(logEntries.size() - 1);
            logListPanel.remove(last);
            logListPanel.revalidate();
            logListPanel.repaint();
        }
    }
}
