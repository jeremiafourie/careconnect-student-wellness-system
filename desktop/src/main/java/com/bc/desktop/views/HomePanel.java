package com.bc.desktop.views;

import com.bc.desktop.models.StudentUser;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HomePanel extends JPanel {
    private JLabel welcomeLabel;
    private JLabel studentNumberLabel;
    private JLabel emailLabel;
    private JLabel currentTimeLabel;
    private JTextArea announcementsArea;
    private Timer timeUpdateTimer;
    
    public HomePanel() {
        initializeComponents();
        layoutComponents();
        startTimeUpdater();
    }
    
    private void initializeComponents() {
        welcomeLabel = new JLabel("Welcome to CareConnect");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        studentNumberLabel = new JLabel("Student Number: ");
        emailLabel = new JLabel("Email: ");
        currentTimeLabel = new JLabel();
        
        announcementsArea = new JTextArea(10, 40);
        announcementsArea.setEditable(false);
        announcementsArea.setFont(new Font("Arial", Font.PLAIN, 12));
        announcementsArea.setText(
            "Welcome to BC Student Wellness Management System!\n\n" +
            "Quick Tips:\n" +
            "• Use the Counselors tab to view available counselors\n" +
            "• Book appointments in the Appointments tab\n" +
            "• Share your feedback in the Feedback tab\n" +
            "• All your data is securely stored and managed\n\n" +
            "For technical support, contact IT Help Desk.\n" +
            "For wellness emergencies, contact campus security immediately."
        );
        announcementsArea.setCaretPosition(0);
    }
    
    private void layoutComponents() {
        setLayout(new BorderLayout());
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        headerPanel.add(welcomeLabel, BorderLayout.NORTH);
        
        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        infoPanel.add(studentNumberLabel);
        infoPanel.add(emailLabel);
        infoPanel.add(currentTimeLabel);
        
        headerPanel.add(infoPanel, BorderLayout.CENTER);
        
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        
        JLabel announcementsLabel = new JLabel("Announcements & Information");
        announcementsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        contentPanel.add(announcementsLabel, BorderLayout.NORTH);
        
        JScrollPane scrollPane = new JScrollPane(announcementsArea);
        scrollPane.setBorder(BorderFactory.createEtchedBorder());
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }
    
    private void startTimeUpdater() {
        timeUpdateTimer = new Timer(1000, e -> updateTime());
        timeUpdateTimer.start();
        updateTime();
    }
    
    private void updateTime() {
        LocalDateTime now = LocalDateTime.now();
        String timeString = now.format(DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy - HH:mm:ss"));
        currentTimeLabel.setText("Current Time: " + timeString);
    }
    
    public void updateUserInfo(StudentUser studentUser) {
        if (studentUser != null && studentUser.isLoggedIn()) {
            String roleText = studentUser.isAdmin() ? " (Administrator)" : "";
            welcomeLabel.setText("Welcome, " + studentUser.getFullName() + roleText + "!");
            studentNumberLabel.setText("Student Number: " + studentUser.getStudentNumber());
            emailLabel.setText("Email: " + studentUser.getEmail());
            
            if (studentUser.isAdmin()) {
                updateAdminAnnouncements();
            }
        } else {
            welcomeLabel.setText("Welcome to CareConnect");
            studentNumberLabel.setText("Student Number: ");
            emailLabel.setText("Email: ");
        }
    }
    
    private void updateAdminAnnouncements() {
        announcementsArea.setText(
            "Welcome to BC Student Wellness Management System - Administrator View!\n\n" +
            "Administrator Capabilities:\n" +
            "• Full CRUD operations on counselor records\n" +
            "• View and manage counselor information\n" +
            "• Monitor system-wide appointments and feedback\n" +
            "• Access to system statistics and reports\n\n" +
            "Quick Tips for Administrators:\n" +
            "• Use the Counselors tab to add, edit, or remove counselors\n" +
            "• Admin controls are only visible to administrator accounts\n" +
            "• Student accounts have limited access (view-only)\n" +
            "• All data is securely stored in JavaDB\n\n" +
            "Security Notice:\n" +
            "Administrator access is granted to Student ID 000000 only.\n" +
            "Please use administrative privileges responsibly."
        );
        announcementsArea.setCaretPosition(0);
    }
    
    public void cleanup() {
        if (timeUpdateTimer != null) {
            timeUpdateTimer.stop();
        }
    }
}