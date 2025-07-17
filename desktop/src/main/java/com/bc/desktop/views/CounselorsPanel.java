package com.bc.desktop.views;

import com.bc.desktop.controllers.CounselorController;
import com.bc.desktop.models.Counselor;
import com.bc.desktop.models.StudentUser;
import com.bc.desktop.services.DatabaseService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CounselorsPanel extends JPanel {
    private final CounselorController counselorController;
    private JTable counselorTable;
    private DefaultTableModel tableModel;
    private JButton addButton;
    private JButton updateButton;
    private JButton removeButton;
    private StudentUser currentUser;

    // Default constructor for MainFrame compatibility
    public CounselorsPanel() {
        this(new CounselorController(DatabaseService.getInstance()));
    }

    public CounselorsPanel(CounselorController counselorController) {
        this.counselorController = counselorController;
        this.currentUser = null; // Will be set via updateUserInfo
        setLayout(new BorderLayout());
        initializeComponents();
        loadCounselors();
    }

    private void initializeComponents() {
        // Header
        JLabel headerLabel = new JLabel("Counselor Management");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        headerPanel.add(headerLabel, BorderLayout.NORTH);
        add(headerPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Name", "Specialization", "Email", "Phone", "Available"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Non-editable table
            }
        };
        counselorTable = new JTable(tableModel);
        counselorTable.setFont(new Font("Arial", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(counselorTable);
        scrollPane.setBorder(BorderFactory.createEtchedBorder());
        add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        addButton = new JButton("Add Counselor");
        updateButton = new JButton("Update Counselor");
        removeButton = new JButton("Remove Counselor");

        addButton.setFont(new Font("Arial", Font.PLAIN, 12));
        updateButton.setFont(new Font("Arial", Font.PLAIN, 12));
        removeButton.setFont(new Font("Arial", Font.PLAIN, 12));

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(removeButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Initially disable buttons (until user is set)
        setButtonsEnabled(false);

        // Action listeners
        addButton.addActionListener(e -> showAddCounselorDialog());
        updateButton.addActionListener(e -> showUpdateCounselorDialog());
        removeButton.addActionListener(e -> removeCounselor());
    }

    private void setButtonsEnabled(boolean enabled) {
        addButton.setEnabled(enabled);
        updateButton.setEnabled(enabled);
        removeButton.setEnabled(enabled);
    }

    private void loadCounselors() {
        counselorController.getAllCounselors(
                counselors -> {
                    tableModel.setRowCount(0); // Clear table
                    for (Counselor counselor : counselors) {
                        tableModel.addRow(new Object[]{
                                counselor.getId(),
                                counselor.getName(),
                                counselor.getSpecialization(),
                                counselor.getEmail(),
                                counselor.getPhone(),
                                counselor.isAvailable() ? "Yes" : "No"
                        });
                    }
                },
                ex -> JOptionPane.showMessageDialog(this,
                        "Error loading counselors: " + ex.getMessage(),
                        "Database Error", JOptionPane.ERROR_MESSAGE)
        );
    }

    private void showAddCounselorDialog() {
        JTextField nameField = new JTextField(20);
        JTextField specializationField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JTextField phoneField = new JTextField(20);
        JCheckBox availableCheckBox = new JCheckBox("Available", true);

        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Specialization:"));
        panel.add(specializationField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);
        panel.add(new JLabel("Available:"));
        panel.add(availableCheckBox);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Counselor",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String specialization = specializationField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            boolean isAvailable = availableCheckBox.isSelected();

            // Input validation
            if (name.isEmpty() || specialization.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name, specialization, and email are required.",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                JOptionPane.showMessageDialog(this, "Invalid email format.",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!phone.isEmpty() && !phone.matches("^\\d{3}-\\d{3}-\\d{4}$")) {
                JOptionPane.showMessageDialog(this, "Phone must be in format XXX-XXX-XXXX or empty.",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Counselor counselor = new Counselor(name, specialization, email, phone);
            counselor.setAvailable(isAvailable);

            counselorController.addCounselor(counselor,
                    success -> {
                        if (success) {
                            loadCounselors();
                            JOptionPane.showMessageDialog(this, "Counselor added successfully.",
                                    "Success", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(this, "Failed to add counselor.",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    },
                    ex -> JOptionPane.showMessageDialog(this,
                            "Error adding counselor: " + ex.getMessage(),
                            "Database Error", JOptionPane.ERROR_MESSAGE)
            );
        }
    }

    private void showUpdateCounselorDialog() {
        int selectedRow = counselorTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a counselor to update.",
                    "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Long counselorId = (Long) tableModel.getValueAt(selectedRow, 0);
        String currentName = (String) tableModel.getValueAt(selectedRow, 1);
        String currentSpecialization = (String) tableModel.getValueAt(selectedRow, 2);
        String currentEmail = (String) tableModel.getValueAt(selectedRow, 3);
        String currentPhone = (String) tableModel.getValueAt(selectedRow, 4);
        boolean currentAvailable = "Yes".equals(tableModel.getValueAt(selectedRow, 5));

        JTextField nameField = new JTextField(currentName, 20);
        JTextField specializationField = new JTextField(currentSpecialization, 20);
        JTextField emailField = new JTextField(currentEmail, 20);
        JTextField phoneField = new JTextField(currentPhone, 20);
        JCheckBox availableCheckBox = new JCheckBox("Available", currentAvailable);

        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Specialization:"));
        panel.add(specializationField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);
        panel.add(new JLabel("Available:"));
        panel.add(availableCheckBox);

        int result = JOptionPane.showConfirmDialog(this, panel, "Update Counselor",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String specialization = specializationField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            boolean isAvailable = availableCheckBox.isSelected();

            // Input validation
            if (name.isEmpty() || specialization.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name, specialization, and email are required.",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                JOptionPane.showMessageDialog(this, "Invalid email format.",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!phone.isEmpty() && !phone.matches("^\\d{3}-\\d{3}-\\d{4}$")) {
                JOptionPane.showMessageDialog(this, "Phone must be in format XXX-XXX-XXXX or empty.",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Counselor counselor = new Counselor(name, specialization, email, phone);
            counselor.setId(counselorId);
            counselor.setAvailable(isAvailable);

            counselorController.updateCounselor(counselor,
                    success -> {
                        if (success) {
                            loadCounselors();
                            JOptionPane.showMessageDialog(this, "Counselor updated successfully.",
                                    "Success", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(this, "Failed to update counselor.",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    },
                    ex -> JOptionPane.showMessageDialog(this,
                            "Error updating counselor: " + ex.getMessage(),
                            "Database Error", JOptionPane.ERROR_MESSAGE)
            );
        }
    }

    private void removeCounselor() {
        int selectedRow = counselorTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a counselor to remove.",
                    "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Long counselorId = (Long) tableModel.getValueAt(selectedRow, 0);
        String counselorName = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to remove " + counselorName + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            counselorController.deleteCounselor(counselorId,
                    success -> {
                        if (success) {
                            loadCounselors();
                            JOptionPane.showMessageDialog(this, "Counselor removed successfully.",
                                    "Success", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(this,
                                    "Cannot remove counselor: They may have existing appointments.",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    },
                    ex -> JOptionPane.showMessageDialog(this,
                            "Error removing counselor: " + ex.getMessage(),
                            "Database Error", JOptionPane.ERROR_MESSAGE)
            );
        }
    }

    public void updateUserInfo(StudentUser studentUser) {
        this.currentUser = studentUser;
        setButtonsEnabled(studentUser != null && studentUser.isAdmin());
        loadCounselors(); // Refresh in case admin status affects visibility
    }
}