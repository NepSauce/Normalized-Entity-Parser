package nep.swing.panels.devmodepanels.devmodeduplicates;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class LoggingPanelDev {

    private static LoggingPanelDev instance; 

    private JPanel mainPanel;
    private JPanel logListPanel;
    private ArrayList<JLabel> logEntries;

    public LoggingPanelDev() {
        instance = this; 

        logEntries = new ArrayList<>();

        mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBounds(300, 155, 250, 245);
        mainPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

        logListPanel = new JPanel();
        logListPanel.setLayout(new BoxLayout(logListPanel, BoxLayout.Y_AXIS));
        logListPanel.setBackground(Color.WHITE);

        TitledBorder titledBorder = new TitledBorder(BorderFactory.createLineBorder(Color.WHITE), "System Log");
        titledBorder.setTitleFont(new Font("Arial", Font.BOLD, 16));
        logListPanel.setBorder(titledBorder);

        JScrollPane scrollPane = new JScrollPane(logListPanel);
        scrollPane.setBounds(5, 5, 240, 235);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // <-- disable horizontal scrolling
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane);

        // Initial log
        log(" NEP Build-1.1.0-Beta", false);
        log(" Maintained By:", false);
        log("  • Zawad Atif", false);
        log("  • Nafisah Nubah", false);
        log("=====================================", false);
        log("Nep Booting", true);
    }

    public static void logGlobal(String message, boolean timeStampEnabled) {
        if (instance != null) {
            instance.log(message, timeStampEnabled);
        }
    }

    public JPanel getLoggingPanel() {
        return mainPanel;
    }

    public void log(String message, boolean timeStampEnabled) {
        String fullMessage = timeStampEnabled
                ? "[" + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "] " + message
                : message;

        JLabel logLabel = new JLabel(fullMessage);
        logLabel.setFont(new Font("Consolas", Font.BOLD, 12));
        logLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        logLabel.setPreferredSize(new Dimension(1000, 20));

        logListPanel.add(logLabel);
        logEntries.add(logLabel);

        logListPanel.revalidate();
        logListPanel.repaint();
    }

    public void clearLog() {
        logListPanel.removeAll();
        logEntries.clear();
        log("NEP Build-1.1.0-Beta", true);
        logListPanel.revalidate();
        logListPanel.repaint();
    }

    public void undoLastLog() {
        if (!logEntries.isEmpty()) {
            JLabel last = logEntries.remove(logEntries.size() - 1);
            logListPanel.remove(last);
            logListPanel.revalidate();
            logListPanel.repaint();
        }
    }
}
