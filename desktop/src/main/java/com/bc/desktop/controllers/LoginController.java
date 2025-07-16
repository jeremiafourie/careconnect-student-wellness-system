package com.bc.desktop.controllers;

import com.bc.desktop.models.StudentUser;
import com.bc.desktop.services.AuthenticationService;
import com.bc.desktop.views.LoginPanel;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

public class LoginController {
    private LoginPanel loginPanel;
    private AuthenticationService authService;
    private Consumer<StudentUser> loginSuccessListener;
    
    public LoginController(LoginPanel loginPanel, AuthenticationService authService) {
        this.loginPanel = loginPanel;
        this.authService = authService;
        setupEventHandlers();
    }
    
    private void setupEventHandlers() {
        loginPanel.addLoginListener(this::handleLogin);
    }
    
    private void handleLogin(ActionEvent e) {
        String studentNumber = loginPanel.getStudentNumber();
        String password = loginPanel.getPassword();
        
        if (!validateInput(studentNumber, password)) {
            return;
        }
        
        loginPanel.setLoginEnabled(false);
        loginPanel.showMessage("Authenticating...", false);
        
        SwingWorker<StudentUser, Void> loginWorker = new SwingWorker<StudentUser, Void>() {
            @Override
            protected StudentUser doInBackground() throws Exception {
                return authService.authenticate(studentNumber, password);
            }
            
            @Override
            protected void done() {
                try {
                    StudentUser user = get();
                    handleLoginSuccess(user);
                } catch (Exception ex) {
                    handleLoginFailure(ex);
                } finally {
                    loginPanel.setLoginEnabled(true);
                }
            }
        };
        
        loginWorker.execute();
    }
    
    private boolean validateInput(String studentNumber, String password) {
        if (studentNumber.isEmpty()) {
            loginPanel.showMessage("Please enter your student number", true);
            return false;
        }
        
        if (password.isEmpty()) {
            loginPanel.showMessage("Please enter your password", true);
            return false;
        }
        
        if (!authService.validateStudentNumber(studentNumber)) {
            loginPanel.showMessage("Student number must be 6 digits", true);
            return false;
        }
        
        if (!authService.validatePassword(password)) {
            loginPanel.showMessage("Password must be at least 6 characters", true);
            return false;
        }
        
        return true;
    }
    
    private void handleLoginSuccess(StudentUser user) {
        loginPanel.clearFields();
        loginPanel.showMessage("Login successful!", false);
        
        if (loginSuccessListener != null) {
            loginSuccessListener.accept(user);
        }
    }
    
    private void handleLoginFailure(Exception ex) {
        String message = "Login failed";
        
        if (ex instanceof AuthenticationService.AuthenticationException) {
            message = ex.getMessage();
        } else if (ex.getCause() instanceof AuthenticationService.AuthenticationException) {
            message = ex.getCause().getMessage();
        } else {
            System.err.println("Login error: " + ex.getMessage());
            ex.printStackTrace();
        }
        
        loginPanel.showMessage(message, true);
    }
    
    public void setLoginSuccessListener(Consumer<StudentUser> listener) {
        this.loginSuccessListener = listener;
    }
}