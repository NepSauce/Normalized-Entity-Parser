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
    
    private final String prompt = "nep@NormalizedEntityParser[Build-1.0.0-Alpha] | (Mode-SysAcc) >> ";
    
    public TerminalFrame(){
        setTitle("NEP - BlackLight Node");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());

        terminalPane = new JTextPane();
        terminalPane.setEditable(false);
        terminalPane.setBackground(Color.BLACK);
        terminalPane.setForeground(Color.WHITE);
        terminalPane.setFont(new Font("Monospaced", Font.PLAIN, 14));
        
        JScrollPane scrollPane = new JScrollPane(terminalPane);
        add(scrollPane, BorderLayout.CENTER);

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
        
        inputField.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if (e.getKeyCode() == KeyEvent.VK_ENTER){
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

    private void appendStyledCommand(String prompt, String command){
        StyledDocument doc = terminalPane.getStyledDocument();
        
        Style bold = terminalPane.addStyle("Bold", null);
        StyleConstants.setBold(bold, true);
        StyleConstants.setForeground(bold, Color.WHITE);
        
        Style normal = terminalPane.addStyle("Normal", null);
        StyleConstants.setBold(normal, false);
        StyleConstants.setForeground(normal, Color.WHITE);
        
        try{
            doc.insertString(doc.getLength(), "\n" + prompt, bold);
            doc.insertString(doc.getLength(), command + "\n", normal);
        }
        catch (BadLocationException e){
            e.printStackTrace();
        }
    }
    
    private void appendStartupMessage(){
        StyledDocument doc = terminalPane.getStyledDocument();
        
        Style header = terminalPane.addStyle("Header", null);
        StyleConstants.setForeground(header, new Color(255, 191, 0));
        StyleConstants.setFontSize(header, 13);
        StyleConstants.setFontFamily(header, "Monospaced");
        
        try{
            doc.insertString(doc.getLength(), "Normalized Entity Parser [Build-1.0.0-Alpha]\n", header);
            doc.insertString(doc.getLength(), "Authors: Zawad Atif & Nafisah Nubah.\n\n", header);
            doc.insertString(doc.getLength(), "DevMode Enables Access to Developer Tools For Opening, Modifying, " +
                    "and Managing Internal NEP Files and Features.\n", header);
            
        }
        catch (BadLocationException e){
            e.printStackTrace();
        }
    }
    
    private void executeCommand(String command){
        StyledDocument doc = terminalPane.getStyledDocument();
        
        Style response = terminalPane.addStyle("Response", null);
        StyleConstants.setForeground(response, new Color(255, 191, 0));
        StyleConstants.setFontFamily(response, "Monospaced");
        StyleConstants.setFontSize(response, 13);
        
        try {
            if (command.equalsIgnoreCase("@nep exit")){
                doc.insertString(doc.getLength(), "Exiting terminal...\n", response);
                dispose();
            }
            else if (command.equalsIgnoreCase("@nep clear")){
                terminalPane.setText("");
            }
            else if (command.equalsIgnoreCase("@nep help")){
                doc.insertString(doc.getLength(),
                        ">> Dev-Terminal Note: This interface is currently under active development.\n", response);
                doc.insertString(doc.getLength(),
                        ">> Available Commands:\n", response);
                doc.insertString(doc.getLength(),
                        "   - @nep help   : Displays this message.\n", response);
                doc.insertString(doc.getLength(),
                        "   - @nep clear       : Clears the terminal.\n", response);
                doc.insertString(doc.getLength(),
                        "   - @nep exit        : Closes the terminal.\n", response);
            }
            else{
                doc.insertString(doc.getLength(),
                        "Error: Command Not Recognized: Enter '@nep help' For a List of Commands.\n", response);
            }
        }
        catch (BadLocationException e){
            e.printStackTrace();
        }
    }
}
