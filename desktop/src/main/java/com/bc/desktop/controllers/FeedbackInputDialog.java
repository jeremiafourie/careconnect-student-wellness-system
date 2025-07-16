package com.bc.desktop.controllers;

import javax.swing.*;
import java.awt.*;

public class FeedbackInputDialog extends JDialog {
    private JSlider ratingSlider;
    private JComboBox<String> categoryCombo;
    private JTextArea commentsArea;
    private JButton submitButton;
    private JButton cancelButton;
    private JLabel ratingLabel;
    private boolean confirmed = false;
    
    public FeedbackInputDialog(Frame parent) {
        super(parent, "Submit Feedback", true);
        initializeComponents();
        layoutComponents();
        pack();
        setLocationRelativeTo(parent);
    }
    
    private void initializeComponents() {
        ratingSlider = new JSlider(1, 5, 3);
        ratingSlider.setMajorTickSpacing(1);
        ratingSlider.setPaintTicks(true);
        ratingSlider.setPaintLabels(true);
        ratingSlider.setSnapToTicks(true);
        
        ratingLabel = new JLabel("Rating: ★★★☆☆ (3)");
        ratingSlider.addChangeListener(e -> updateRatingLabel());
        
        categoryCombo = new JComboBox<>(new String[]{
            "General", "Counseling Service", "Appointment System", 
            "Staff", "Facilities", "Other"
        });
        
        commentsArea = new JTextArea(6, 40);
        commentsArea.setLineWrap(true);
        commentsArea.setWrapStyleWord(true);
        commentsArea.setBorder(BorderFactory.createEtchedBorder());
        
        submitButton = new JButton("Submit");
        cancelButton = new JButton("Cancel");
        
        submitButton.addActionListener(e -> {
            if (validateInput()) {
                confirmed = true;
                dispose();
            }
        });
        
        cancelButton.addActionListener(e -> dispose());
        
        // Allow Enter to submit
        getRootPane().setDefaultButton(submitButton);
    }
    
    private void layoutComponents() {
        setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(ratingLabel, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(ratingSlider, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(new JLabel("Category:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 2; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(categoryCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(new JLabel("Comments:"), gbc);
        
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0; gbc.weighty = 1.0;
        formPanel.add(new JScrollPane(commentsArea), gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);
        
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void updateRatingLabel() {
        int rating = ratingSlider.getValue();
        String stars = "★".repeat(rating) + "☆".repeat(5 - rating);
        ratingLabel.setText("Rating: " + stars + " (" + rating + ")");
    }
    
    private boolean validateInput() {
        String comments = commentsArea.getText().trim();
        if (comments.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Please enter your comments.",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE
            );
            commentsArea.requestFocus();
            return false;
        }
        
        if (comments.length() < 10) {
            JOptionPane.showMessageDialog(
                this,
                "Comments must be at least 10 characters long.",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE
            );
            commentsArea.requestFocus();
            return false;
        }
        
        return true;
    }
    
    public boolean isConfirmed() {
        return confirmed;
    }
    
    public int getRating() {
        return ratingSlider.getValue();
    }
    
    public String getCategory() {
        return (String) categoryCombo.getSelectedItem();
    }
    
    public String getComments() {
        return commentsArea.getText().trim();
    }
}