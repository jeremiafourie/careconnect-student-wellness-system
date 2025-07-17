package com.bc.desktop.controllers;

import com.bc.desktop.models.Feedback;
import com.bc.desktop.models.StudentUser;
import com.bc.desktop.services.DatabaseService;
import com.bc.desktop.views.FeedbackPanel;
import javax.swing.*;
import java.util.List;
import java.util.stream.Collectors;

public class FeedbackController {
    private FeedbackPanel feedbackPanel;
    private DatabaseService databaseService;
    private StudentUser currentUser;
    private List<Feedback> allFeedback;
    
    public FeedbackController(FeedbackPanel feedbackPanel, DatabaseService databaseService) {
        this.feedbackPanel = feedbackPanel;
        this.databaseService = databaseService;
        setupEventHandlers();
    }
    
    private void setupEventHandlers() {
        feedbackPanel.addRefreshButtonListener(e -> loadFeedback());
        feedbackPanel.addSubmitButtonListener(e -> showSubmitFeedbackDialog());
        feedbackPanel.addEditButtonListener(e -> showEditFeedbackDialog());
        feedbackPanel.addDeleteButtonListener(e -> deleteFeedback());
    }
    
    public void initialize(StudentUser user) {
        this.currentUser = user;
        feedbackPanel.setAdminMode(user.isAdmin());
        loadFeedback();
    }
    
    private void loadFeedback() {
        if (currentUser == null) return;
        
        SwingWorker<List<Feedback>, Void> worker = new SwingWorker<List<Feedback>, Void>() {
            @Override
            protected List<Feedback> doInBackground() throws Exception {
                if (currentUser.isAdmin()) {
                    return databaseService.getAllFeedback();
                } else {
                    return databaseService.getFeedbackByStudent(currentUser.getStudentNumber());
                }
            }
            
            @Override
            protected void done() {
                try {
                    allFeedback = get();
                    filterAndDisplayFeedback();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(
                        feedbackPanel,
                        "Error loading feedback: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        };
        
        worker.execute();
    }
    
    private void filterAndDisplayFeedback() {
        List<Feedback> filteredFeedback = allFeedback;
        
        String selectedCategory = feedbackPanel.getSelectedCategory();
        Integer selectedRating = feedbackPanel.getSelectedRating();
        
        if (selectedCategory != null) {
            filteredFeedback = filteredFeedback.stream()
                .filter(f -> f.getCategory().equals(selectedCategory))
                .collect(Collectors.toList());
        }
        
        if (selectedRating != null) {
            filteredFeedback = filteredFeedback.stream()
                .filter(f -> f.getRating() == selectedRating)
                .collect(Collectors.toList());
        }
        
        feedbackPanel.updateTable(filteredFeedback);
    }
    
    private void showSubmitFeedbackDialog() {
        submitFeedback();
    }
    
    private void showEditFeedbackDialog() {
        Feedback selected = feedbackPanel.getSelectedFeedback(allFeedback);
        if (selected == null) {
            JOptionPane.showMessageDialog(
                feedbackPanel,
                "Please select feedback to edit",
                "No Selection",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        
        // Check if user can edit this feedback
        if (!currentUser.isAdmin() && selected.getStudentNumber() != currentUser.getStudentNumber()) {
            JOptionPane.showMessageDialog(
                feedbackPanel,
                "You can only edit your own feedback",
                "Permission Denied",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        
        feedbackPanel.showFeedbackDialog(selected, e -> updateFeedback(selected));
    }
    
    private void submitFeedback() {
        try {
            FeedbackInputDialog dialog = new FeedbackInputDialog(
                (java.awt.Frame) SwingUtilities.getWindowAncestor(feedbackPanel)
            );
            dialog.setVisible(true);
            
            if (dialog.isConfirmed()) {
                Feedback feedback = new Feedback(
                    currentUser.getStudentNumber(),
                    dialog.getRating(),
                    dialog.getComments(),
                    dialog.getCategory()
                );
                
                boolean success = databaseService.saveFeedback(feedback);
                
                if (success) {
                    JOptionPane.showMessageDialog(
                        feedbackPanel,
                        "Feedback submitted successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                    loadFeedback();
                } else {
                    JOptionPane.showMessageDialog(
                        feedbackPanel,
                        "Failed to submit feedback. Please try again.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                feedbackPanel,
                "Error submitting feedback: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            e.printStackTrace();
        }
    }
    
    private void updateFeedback(Feedback original) {
        try {
            // Simple update for demonstration
            original.setRating(5);
            original.setComments(original.getComments() + " [Updated from desktop]");
            
            boolean success = databaseService.updateFeedback(original);
            
            if (success) {
                JOptionPane.showMessageDialog(
                    feedbackPanel,
                    "Feedback updated successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
                loadFeedback();
            } else {
                JOptionPane.showMessageDialog(
                    feedbackPanel,
                    "Failed to update feedback. Please try again.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                feedbackPanel,
                "Error updating feedback: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    private void deleteFeedback() {
        Feedback selected = feedbackPanel.getSelectedFeedback(allFeedback);
        if (selected == null) {
            JOptionPane.showMessageDialog(
                feedbackPanel,
                "Please select feedback to delete",
                "No Selection",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        
        // Check if user can delete this feedback
        if (!currentUser.isAdmin() && selected.getStudentNumber() != currentUser.getStudentNumber()) {
            JOptionPane.showMessageDialog(
                feedbackPanel,
                "You can only delete your own feedback",
                "Permission Denied",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        
        int option = JOptionPane.showConfirmDialog(
            feedbackPanel,
            "Are you sure you want to delete this feedback?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (option == JOptionPane.YES_OPTION) {
            boolean success = databaseService.deleteFeedback(selected.getId());
            
            if (success) {
                JOptionPane.showMessageDialog(
                    feedbackPanel,
                    "Feedback deleted successfully",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
                loadFeedback();
            } else {
                JOptionPane.showMessageDialog(
                    feedbackPanel,
                    "Failed to delete feedback. Please try again.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
    
    public void cleanup() {
        feedbackPanel.clearSelection();
        currentUser = null;
    }
}