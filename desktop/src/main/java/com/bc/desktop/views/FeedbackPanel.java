package com.bc.desktop.views;

import com.bc.desktop.models.Feedback;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FeedbackPanel extends JPanel {
    private JTable feedbackTable;
    private DefaultTableModel tableModel;
    private JButton submitButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton refreshButton;
    private JComboBox<String> categoryFilter;
    private JComboBox<Integer> ratingFilter;
    
    private final String[] columnNames = {"ID", "Rating", "Category", "Comments", "Date"};
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
    
    public FeedbackPanel() {
        initializeComponents();
        layoutComponents();
    }
    
    private void initializeComponents() {
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        feedbackTable = new JTable(tableModel);
        feedbackTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        feedbackTable.getTableHeader().setReorderingAllowed(false);
        
        feedbackTable.getColumnModel().getColumn(0).setMaxWidth(50);
        feedbackTable.getColumnModel().getColumn(1).setPreferredWidth(80);
        feedbackTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        feedbackTable.getColumnModel().getColumn(3).setPreferredWidth(300);
        feedbackTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        
        submitButton = new JButton("Submit Feedback");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");
        refreshButton = new JButton("Refresh");
        
        categoryFilter = new JComboBox<>(new String[]{
            "All Categories", "General", "Counseling Service", "Appointment System", 
            "Staff", "Facilities", "Other"
        });
        
        ratingFilter = new JComboBox<>(new Integer[]{
            null, 5, 4, 3, 2, 1
        });
        ratingFilter.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                    int index, boolean isSelected, boolean cellHasFocus) {
                if (value == null) {
                    value = "All Ratings";
                } else {
                    value = "★".repeat((Integer) value) + " (" + value + ")";
                }
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });
        
        editButton.setEnabled(false);
        deleteButton.setEnabled(false);
        
        feedbackTable.getSelectionModel().addListSelectionListener(e -> {
            boolean hasSelection = feedbackTable.getSelectedRow() != -1;
            editButton.setEnabled(hasSelection);
            deleteButton.setEnabled(hasSelection);
        });
    }
    
    private void layoutComponents() {
        setLayout(new BorderLayout());
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Category:"));
        filterPanel.add(categoryFilter);
        filterPanel.add(new JLabel("Rating:"));
        filterPanel.add(ratingFilter);
        filterPanel.add(refreshButton);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(submitButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        
        topPanel.add(filterPanel, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);
        
        JScrollPane scrollPane = new JScrollPane(feedbackTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Your Feedback History"));
        
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    public void updateTable(List<Feedback> feedbackList) {
        tableModel.setRowCount(0);
        for (Feedback feedback : feedbackList) {
            Object[] row = {
                feedback.getId(),
                feedback.getRatingStars() + " (" + feedback.getRating() + ")",
                feedback.getCategory(),
                truncateText(feedback.getComments(), 50),
                feedback.getCreatedAt().format(dateFormatter)
            };
            tableModel.addRow(row);
        }
    }
    
    private String truncateText(String text, int maxLength) {
        if (text == null) return "";
        return text.length() > maxLength ? text.substring(0, maxLength - 3) + "..." : text;
    }
    
    public Feedback getSelectedFeedback(List<Feedback> feedbackList) {
        int selectedRow = feedbackTable.getSelectedRow();
        if (selectedRow != -1) {
            Long id = (Long) tableModel.getValueAt(selectedRow, 0);
            return feedbackList.stream()
                    .filter(f -> f.getId().equals(id))
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }
    
    public void addSubmitButtonListener(ActionListener listener) {
        submitButton.addActionListener(listener);
    }
    
    public void addEditButtonListener(ActionListener listener) {
        editButton.addActionListener(listener);
    }
    
    public void addDeleteButtonListener(ActionListener listener) {
        deleteButton.addActionListener(listener);
    }
    
    public void addRefreshButtonListener(ActionListener listener) {
        refreshButton.addActionListener(listener);
    }
    
    public String getSelectedCategory() {
        String selected = (String) categoryFilter.getSelectedItem();
        return "All Categories".equals(selected) ? null : selected;
    }
    
    public Integer getSelectedRating() {
        return (Integer) ratingFilter.getSelectedItem();
    }
    
    public void clearSelection() {
        feedbackTable.clearSelection();
    }
    
    public void showFeedbackDialog(Feedback feedback, ActionListener submitListener) {
        FeedbackDialog dialog = new FeedbackDialog(
            (Frame) SwingUtilities.getWindowAncestor(this), feedback);
        dialog.addSubmitListener(submitListener);
        dialog.setVisible(true);
    }
    
    private static class FeedbackDialog extends JDialog {
        private JSlider ratingSlider;
        private JComboBox<String> categoryCombo;
        private JTextArea commentsArea;
        private JButton submitButton;
        private JButton cancelButton;
        private JLabel ratingLabel;
        private boolean confirmed = false;
        
        public FeedbackDialog(Frame parent, Feedback feedback) {
            super(parent, feedback == null ? "Submit Feedback" : "Edit Feedback", true);
            initializeComponents(feedback);
            layoutComponents();
            pack();
            setLocationRelativeTo(parent);
        }
        
        private void initializeComponents(Feedback feedback) {
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
            
            submitButton = new JButton(feedback == null ? "Submit" : "Update");
            cancelButton = new JButton("Cancel");
            
            if (feedback != null) {
                ratingSlider.setValue(feedback.getRating());
                categoryCombo.setSelectedItem(feedback.getCategory());
                commentsArea.setText(feedback.getComments());
                updateRatingLabel();
            }
            
            cancelButton.addActionListener(e -> dispose());
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
        
        public void addSubmitListener(ActionListener listener) {
            submitButton.addActionListener(e -> {
                if (validateInput()) {
                    confirmed = true;
                    listener.actionPerformed(e);
                    dispose();
                }
            });
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
}