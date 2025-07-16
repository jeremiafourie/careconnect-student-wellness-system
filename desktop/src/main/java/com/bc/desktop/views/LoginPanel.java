package com.bc.desktop.views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginPanel extends JPanel {
    private JTextField studentNumberField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel messageLabel;
    
    public LoginPanel() {
        initializeComponents();
        layoutComponents();
    }
    
    private void initializeComponents() {
        studentNumberField = new JTextField(15);
        passwordField = new JPasswordField(15);
        loginButton = new JButton("Login");
        messageLabel = new JLabel(" ");
        messageLabel.setForeground(Color.RED);
        
        studentNumberField.setToolTipText("Enter your 6-digit student number");
        passwordField.setToolTipText("Enter your password");
    }
    
    private void layoutComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        setBorder(BorderFactory.createTitledBorder("Student Login"));
        
        gbc.insets = new Insets(10, 10, 10, 10);
        
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Student Number:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        add(studentNumberField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Password:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(passwordField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(loginButton, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(messageLabel, gbc);
    }
    
    public void addLoginListener(ActionListener listener) {
        loginButton.addActionListener(listener);
        studentNumberField.addActionListener(listener);
        passwordField.addActionListener(listener);
    }
    
    public String getStudentNumber() {
        return studentNumberField.getText().trim();
    }
    
    public String getPassword() {
        return new String(passwordField.getPassword());
    }
    
    public void clearFields() {
        studentNumberField.setText("");
        passwordField.setText("");
        messageLabel.setText(" ");
    }
    
    public void showMessage(String message, boolean isError) {
        messageLabel.setText(message);
        messageLabel.setForeground(isError ? Color.RED : Color.GREEN);
    }
    
    public void setLoginEnabled(boolean enabled) {
        loginButton.setEnabled(enabled);
        studentNumberField.setEnabled(enabled);
        passwordField.setEnabled(enabled);
    }
}