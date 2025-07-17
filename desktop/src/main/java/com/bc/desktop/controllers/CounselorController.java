package com.bc.desktop.controllers;

import com.bc.desktop.models.Counselor;
import com.bc.desktop.services.DatabaseService;
import com.bc.desktop.views.CounselorsPanel;
import javax.swing.*;
import java.util.List;
import java.util.stream.Collectors;

public class CounselorController {
    private CounselorsPanel counselorsPanel;
    private DatabaseService databaseService;
    private List<Counselor> allCounselors;
    
    public CounselorController(CounselorsPanel counselorsPanel, DatabaseService databaseService) {
        this.counselorsPanel = counselorsPanel;
        this.databaseService = databaseService;
        setupEventHandlers();
    }
    
    private void setupEventHandlers() {
        counselorsPanel.addRefreshButtonListener(e -> loadCounselors());
        counselorsPanel.addAddButtonListener(e -> showAddCounselorDialog());
        counselorsPanel.addEditButtonListener(e -> showEditCounselorDialog());
        counselorsPanel.addDeleteButtonListener(e -> deleteCounselor());
    }
    
    public void initialize() {
        loadCounselors();
    }
    
    public void initialize(boolean isAdmin) {
        counselorsPanel.setAdminMode(isAdmin);
        loadCounselors();
    }
    
    private void loadCounselors() {
        SwingWorker<List<Counselor>, Void> worker = new SwingWorker<List<Counselor>, Void>() {
            @Override
            protected List<Counselor> doInBackground() throws Exception {
                return databaseService.getAllCounselors();
            }
            
            @Override
            protected void done() {
                try {
                    allCounselors = get();
                    filterAndDisplayCounselors();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(
                        counselorsPanel,
                        "Error loading counselors: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        };
        
        worker.execute();
    }
    
    private void filterAndDisplayCounselors() {
        List<Counselor> filteredCounselors = allCounselors;
        
        String searchText = counselorsPanel.getSearchText();
        String specialization = counselorsPanel.getSelectedSpecialization();
        
        if (!searchText.isEmpty()) {
            filteredCounselors = filteredCounselors.stream()
                .filter(c -> c.getName().toLowerCase().contains(searchText.toLowerCase()) ||
                           c.getSpecialization().toLowerCase().contains(searchText.toLowerCase()))
                .collect(Collectors.toList());
        }
        
        if (specialization != null) {
            filteredCounselors = filteredCounselors.stream()
                .filter(c -> c.getSpecialization().equals(specialization))
                .collect(Collectors.toList());
        }
        
        counselorsPanel.updateTable(filteredCounselors);
    }
    
    private void showAddCounselorDialog() {
        CounselorDialog dialog = new CounselorDialog(
            (JFrame) SwingUtilities.getWindowAncestor(counselorsPanel),
            "Add Counselor",
            null
        );
        
        dialog.setVisible(true);
        
        if (dialog.isConfirmed()) {
            Counselor newCounselor = dialog.getCounselor();
            try {
                boolean success = databaseService.saveCounselor(newCounselor);
                
                if (success) {
                    JOptionPane.showMessageDialog(
                        counselorsPanel,
                        "Counselor added successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                    loadCounselors();
                } else {
                    JOptionPane.showMessageDialog(
                        counselorsPanel,
                        "Failed to add counselor. Please try again.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            } catch (RuntimeException e) {
                JOptionPane.showMessageDialog(
                    counselorsPanel,
                    "Error: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
    
    private void showEditCounselorDialog() {
        Counselor selected = counselorsPanel.getSelectedCounselor(allCounselors);
        if (selected == null) {
            JOptionPane.showMessageDialog(
                counselorsPanel,
                "Please select a counselor to edit",
                "No Selection",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        
        CounselorDialog dialog = new CounselorDialog(
            (JFrame) SwingUtilities.getWindowAncestor(counselorsPanel),
            "Edit Counselor",
            selected
        );
        
        dialog.setVisible(true);
        
        if (dialog.isConfirmed()) {
            Counselor updatedCounselor = dialog.getCounselor();
            boolean success = databaseService.updateCounselor(updatedCounselor);
            
            if (success) {
                JOptionPane.showMessageDialog(
                    counselorsPanel,
                    "Counselor updated successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
                loadCounselors();
            } else {
                JOptionPane.showMessageDialog(
                    counselorsPanel,
                    "Failed to update counselor. Please try again.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
    
    private void deleteCounselor() {
        Counselor selected = counselorsPanel.getSelectedCounselor(allCounselors);
        if (selected == null) {
            JOptionPane.showMessageDialog(
                counselorsPanel,
                "Please select a counselor to delete",
                "No Selection",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        
        int option = JOptionPane.showConfirmDialog(
            counselorsPanel,
            "Are you sure you want to delete " + selected.getName() + "?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (option == JOptionPane.YES_OPTION) {
            boolean success = databaseService.deleteCounselor(selected.getId());
            
            if (success) {
                JOptionPane.showMessageDialog(
                    counselorsPanel,
                    "Counselor deleted successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
                loadCounselors();
            } else {
                JOptionPane.showMessageDialog(
                    counselorsPanel,
                    "Failed to delete counselor. Counselor may have existing appointments.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
    
    public void cleanup() {
        counselorsPanel.clearSelection();
    }
    
    public List<Counselor> getAllCounselors() {
        return allCounselors;
    }
    
    private static class CounselorDialog extends JDialog {
        private JTextField nameField;
        private JComboBox<String> specializationCombo;
        private JTextField emailField;
        private JTextField phoneField;
        private JCheckBox availableCheckbox;
        private boolean confirmed = false;
        private Counselor counselor;
        
        public CounselorDialog(JFrame parent, String title, Counselor counselor) {
            super(parent, title, true);
            this.counselor = counselor;
            initializeComponents();
            layoutComponents();
            populateFields();
            pack();
            setLocationRelativeTo(parent);
        }
        
        private void initializeComponents() {
            nameField = new JTextField(20);
            specializationCombo = new JComboBox<>(new String[]{
                "General Counseling", "Academic Support", "Mental Health", 
                "Career Guidance", "Stress Management"
            });
            emailField = new JTextField(20);
            phoneField = new JTextField(15);
            availableCheckbox = new JCheckBox("Available", true);
        }
        
        private void layoutComponents() {
            setLayout(new java.awt.BorderLayout());
            
            JPanel formPanel = new JPanel(new java.awt.GridBagLayout());
            java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
            gbc.insets = new java.awt.Insets(5, 5, 5, 5);
            
            gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = java.awt.GridBagConstraints.EAST;
            formPanel.add(new JLabel("Name:"), gbc);
            gbc.gridx = 1; gbc.anchor = java.awt.GridBagConstraints.WEST;
            formPanel.add(nameField, gbc);
            
            gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = java.awt.GridBagConstraints.EAST;
            formPanel.add(new JLabel("Specialization:"), gbc);
            gbc.gridx = 1; gbc.anchor = java.awt.GridBagConstraints.WEST;
            formPanel.add(specializationCombo, gbc);
            
            gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = java.awt.GridBagConstraints.EAST;
            formPanel.add(new JLabel("Email:"), gbc);
            gbc.gridx = 1; gbc.anchor = java.awt.GridBagConstraints.WEST;
            formPanel.add(emailField, gbc);
            
            gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = java.awt.GridBagConstraints.EAST;
            formPanel.add(new JLabel("Phone:"), gbc);
            gbc.gridx = 1; gbc.anchor = java.awt.GridBagConstraints.WEST;
            formPanel.add(phoneField, gbc);
            
            gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.anchor = java.awt.GridBagConstraints.CENTER;
            formPanel.add(availableCheckbox, gbc);
            
            JPanel buttonPanel = new JPanel(new java.awt.FlowLayout());
            JButton saveButton = new JButton("Save");
            JButton cancelButton = new JButton("Cancel");
            
            saveButton.addActionListener(e -> {
                if (validateInput()) {
                    confirmed = true;
                    dispose();
                }
            });
            
            cancelButton.addActionListener(e -> dispose());
            
            buttonPanel.add(saveButton);
            buttonPanel.add(cancelButton);
            
            add(formPanel, java.awt.BorderLayout.CENTER);
            add(buttonPanel, java.awt.BorderLayout.SOUTH);
        }
        
        private void populateFields() {
            if (counselor != null) {
                nameField.setText(counselor.getName());
                specializationCombo.setSelectedItem(counselor.getSpecialization());
                emailField.setText(counselor.getEmail());
                phoneField.setText(counselor.getPhone());
                availableCheckbox.setSelected(counselor.isAvailable());
            }
        }
        
        public boolean isConfirmed() {
            return confirmed;
        }
        
        private boolean validateInput() {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name is required", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            if (name.length() < 2 || name.length() > 100) {
                JOptionPane.showMessageDialog(this, "Name must be between 2 and 100 characters", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            if (email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Email is required", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            if (!isValidEmail(email)) {
                JOptionPane.showMessageDialog(this, "Please enter a valid email address", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            if (!phone.isEmpty() && !isValidPhone(phone)) {
                JOptionPane.showMessageDialog(this, "Please enter a valid phone number (10 digits)", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            return true;
        }
        
        private boolean isValidEmail(String email) {
            return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
        }
        
        private boolean isValidPhone(String phone) {
            String cleanPhone = phone.replaceAll("[\\s\\-\\(\\)]", "");
            return cleanPhone.matches("^\\d{10}$");
        }
        
        public Counselor getCounselor() {
            if (counselor == null) {
                counselor = new Counselor();
            }
            
            counselor.setName(nameField.getText().trim());
            counselor.setSpecialization((String) specializationCombo.getSelectedItem());
            counselor.setEmail(emailField.getText().trim());
            counselor.setPhone(phoneField.getText().trim());
            counselor.setAvailable(availableCheckbox.isSelected());
            
            return counselor;
        }
    }
}