package com.bc.desktop.controllers;

import com.bc.desktop.models.StudentUser;
import com.bc.desktop.services.AuthenticationService;
import com.bc.desktop.services.DatabaseService;
import com.bc.desktop.views.MainFrame;
import javax.swing.*;

public class MainController {
    private MainFrame mainFrame;
    private AuthenticationService authService;
    private DatabaseService databaseService;
    private StudentUser currentUser;
    
    private LoginController loginController;
    private CounselorController counselorController;
    private AppointmentController appointmentController;
    private FeedbackController feedbackController;
    
    public MainController() {
        initializeServices();
    }
    
    private void initializeServices() {
        authService = new AuthenticationService();
        databaseService = DatabaseService.getInstance();
    }
    
    private void initializeView() {
        SwingUtilities.invokeLater(() -> {
            mainFrame = new MainFrame();
            initializeControllers();
            setupEventHandlers();
            mainFrame.setVisible(true);
        });
    }
    
    private void initializeControllers() {
        loginController = new LoginController(mainFrame.getLoginPanel(), authService);
//        counselorController = new CounselorController(mainFrame.getCounselorsPanel(), databaseService);
//        appointmentController = new AppointmentController(mainFrame.getAppointmentsPanel(), databaseService);
//        feedbackController = new FeedbackController(mainFrame.getFeedbackPanel(), databaseService);
    }
    
    private void setupEventHandlers() {
        loginController.setLoginSuccessListener(this::handleLoginSuccess);
        mainFrame.addLogoutListener(e -> handleLogout());
    }
    
    private void handleLoginSuccess(StudentUser user) {
        this.currentUser = user;
        mainFrame.showMainView(user);
        mainFrame.setStatusText("Logged in as " + user.getFullName());
        
//        counselorController.initialize(user.isAdmin());
//        appointmentController.initialize(user);
//        feedbackController.initialize(user);
    }
    
    private void handleLogout() {
        int option = JOptionPane.showConfirmDialog(
            mainFrame,
            "Are you sure you want to logout?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (option == JOptionPane.YES_OPTION) {
            if (currentUser != null) {
                currentUser.logout();
            }
            currentUser = null;
            
//            counselorController.cleanup();
//            appointmentController.cleanup();
//            feedbackController.cleanup();
            
            mainFrame.showLoginView();
            mainFrame.setStatusText("Logged out");
        }
    }
    
    public void start() {
        initializeView();
    }
    
    public MainFrame getMainFrame() {
        return mainFrame;
    }
    
    public StudentUser getCurrentUser() {
        return currentUser;
    }
}