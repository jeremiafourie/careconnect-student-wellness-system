package com.bc.desktop.views;

import com.bc.desktop.models.StudentUser;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainFrame extends JFrame {
    private JTabbedPane tabbedPane;
    private LoginPanel loginPanel;
    private HomePanel homePanel;
    private CounselorsPanel counselorsPanel;
    private AppointmentsPanel appointmentsPanel;
    private FeedbackPanel feedbackPanel;
    private JMenuBar menuBar;
    private JMenu userMenu;
    private JMenuItem logoutMenuItem;
    private JLabel statusLabel;
    
    private boolean isLoggedIn = false;
    
    public MainFrame() {
        initializeComponents();
        layoutComponents();
        setupWindow();
        showLoginView();
    }
    
    private void initializeComponents() {
        tabbedPane = new JTabbedPane();
        
        loginPanel = new LoginPanel();
        homePanel = new HomePanel();
        counselorsPanel = new CounselorsPanel();
        appointmentsPanel = new AppointmentsPanel();
        feedbackPanel = new FeedbackPanel();
        
        setupMenuBar();
        setupStatusBar();
    }
    
    private void setupMenuBar() {
        menuBar = new JMenuBar();
        
        userMenu = new JMenu("User");
        logoutMenuItem = new JMenuItem("Logout");
        userMenu.add(logoutMenuItem);
        
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutMenuItem = new JMenuItem("About");
        aboutMenuItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutMenuItem);
        
        menuBar.add(userMenu);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
        userMenu.setVisible(false);
    }
    
    private void setupStatusBar() {
        statusLabel = new JLabel("Ready");
        statusLabel.setBorder(BorderFactory.createEtchedBorder());
    }
    
    private void layoutComponents() {
        setLayout(new BorderLayout());
        
        tabbedPane.addTab("Home", homePanel);
        tabbedPane.addTab("Counselors", counselorsPanel);
        tabbedPane.addTab("Appointments", appointmentsPanel);
        tabbedPane.addTab("Feedback", feedbackPanel);
        
        add(tabbedPane, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);
    }
    
    private void setupWindow() {
        setTitle("BC Student Wellness Management System");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleWindowClose();
            }
        });
        
        // Use default look and feel
    }
    
    public void showLoginView() {
        getContentPane().removeAll();
        
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.add(loginPanel);
        
        add(centerPanel, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);
        
        isLoggedIn = false;
        userMenu.setVisible(false);
        setTitle("BC Student Wellness Management System - Login");
        
        revalidate();
        repaint();
    }
    
    public void showMainView(StudentUser studentUser) {
        getContentPane().removeAll();
        
        add(tabbedPane, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);
        
        homePanel.updateUserInfo(studentUser);
        
        isLoggedIn = true;
        userMenu.setVisible(true);
        setTitle("BC Student Wellness Management System - " + studentUser.getFullName());
        
        revalidate();
        repaint();
    }
    
    private void handleWindowClose() {
        if (isLoggedIn) {
            int option = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to exit? You will be logged out.",
                "Confirm Exit",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            if (option == JOptionPane.YES_OPTION) {
                cleanup();
                System.exit(0);
            }
        } else {
            cleanup();
            System.exit(0);
        }
    }
    
    private void cleanup() {
        homePanel.cleanup();
    }
    
    private void showAboutDialog() {
        String message = """
            BC Student Wellness Management System
            Version 1.0
            
            Developed for Belgium Campus
            PRG3781 Project 2025
            
            This application helps students manage their wellness
            appointments, view counselor information, and provide
            feedback on services.
            
            For support, contact the IT Help Desk.
            """;
        
        JOptionPane.showMessageDialog(
            this,
            message,
            "About",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    public void setStatusText(String text) {
        statusLabel.setText(" " + text);
    }
    
    public void addLogoutListener(java.awt.event.ActionListener listener) {
        logoutMenuItem.addActionListener(listener);
    }
    
    public LoginPanel getLoginPanel() {
        return loginPanel;
    }
    
    public HomePanel getHomePanel() {
        return homePanel;
    }
    
    public CounselorsPanel getCounselorsPanel() {
        return counselorsPanel;
    }
    
    public AppointmentsPanel getAppointmentsPanel() {
        return appointmentsPanel;
    }
    
    public FeedbackPanel getFeedbackPanel() {
        return feedbackPanel;
    }
    
    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }
}