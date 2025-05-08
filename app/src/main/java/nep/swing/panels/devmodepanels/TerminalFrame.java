package nep.swing.panels.devmodepanels;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TerminalFrame extends JFrame {
    private JTextPane terminalPane;
    private JTextField inputField;
    private JPanel inputPanel;
    
    private final String prompt = "nep@NormalizedEntityParser[BuildVer-1.0.Alpha] | (Mode-SysAcc) <> ";
    
    public TerminalFrame() {
        setTitle("NEPTer - Terminal");
        setSize(750, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());
        
        // Terminal output area using JTextPane for rich text (e.g., bold)
        terminalPane = new JTextPane();
        terminalPane.setEditable(false);
        terminalPane.setBackground(Color.BLACK);
        terminalPane.setForeground(Color.WHITE);
        terminalPane.setFont(new Font("Monospaced", Font.PLAIN, 14));
        
        JScrollPane scrollPane = new JScrollPane(terminalPane);
        add(scrollPane, BorderLayout.CENTER);
        
        // Input panel at bottom
        inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(Color.BLACK);
        inputPanel.setPreferredSize(new Dimension(700, 30));
        
        JLabel promptLabel = new JLabel(prompt);
        promptLabel.setFont(new Font("Monospaced", Font.BOLD, 14));
        promptLabel.setForeground(Color.WHITE);
        inputPanel.add(promptLabel, BorderLayout.WEST);
        
        inputField = new JTextField();
        inputField.setFont(new Font("Monospaced", Font.PLAIN, 14));
        inputField.setBackground(Color.BLACK);
        inputField.setForeground(Color.WHITE);
        inputField.setCaretColor(Color.WHITE);
        
        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String command = inputField.getText();
                    inputField.setText("");
                    appendStyledCommand(prompt, command);
                    executeCommand(command);
                }
            }
        });
        
        inputPanel.add(inputField, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);
        
        setVisible(true);
        
        appendStartupMessage();
    }
    
    // Append prompt (bold) + command (plain) to terminalPane
    private void appendStyledCommand(String prompt, String command) {
        StyledDocument doc = terminalPane.getStyledDocument();
        
        Style bold = terminalPane.addStyle("Bold", null);
        StyleConstants.setBold(bold, true);
        StyleConstants.setForeground(bold, Color.WHITE);
        
        Style normal = terminalPane.addStyle("Normal", null);
        StyleConstants.setBold(normal, false);
        StyleConstants.setForeground(normal, Color.WHITE);
        
        try {
            doc.insertString(doc.getLength(), "\n" + prompt, bold);
            doc.insertString(doc.getLength(), command + "\n", normal);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
    
    private void appendStartupMessage() {
        StyledDocument doc = terminalPane.getStyledDocument();
        
        Style header = terminalPane.addStyle("Header", null);
        StyleConstants.setForeground(header, new Color(255, 191, 0));
        StyleConstants.setFontSize(header, 13);
        StyleConstants.setFontFamily(header, "Monospaced");
        
        try {
            doc.insertString(doc.getLength(), "Normalized Entity Parser [BuildVer-1.0.Alpha]\n", header);
            doc.insertString(doc.getLength(), "Authors: Zawad Atif & Nafisah Nubah.\n\n", header);
            doc.insertString(doc.getLength(), "DevMode Enables Access to Developer Tools For Opening, Modifying, " +
                    "and Managing Internal NEP Files and Features.\n", header);
            
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
    
    private void executeCommand(String command) {
        StyledDocument doc = terminalPane.getStyledDocument();
        
        try {
            if (command.equalsIgnoreCase("exit")) {
                doc.insertString(doc.getLength(), "Exiting terminal...\n", null);
                dispose();
            } else if (command.equalsIgnoreCase("clear")) {
                terminalPane.setText("");
            } else {
                doc.insertString(doc.getLength(), "Error: Command Not Recognized: Enter '@nep help' For a List of Commands." + "\n", null);
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}
