package com.bc.desktop.views;

import com.bc.desktop.models.Appointment;
import com.bc.desktop.models.Counselor;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AppointmentsPanel extends JPanel {
    private JTable appointmentsTable;
    private DefaultTableModel tableModel;
    private JButton bookButton;
    private JButton rescheduleButton;
    private JButton cancelButton;
    private JButton refreshButton;
    private JComboBox<String> statusFilter;
    private JCheckBox upcomingOnlyCheckbox;
    
    private final String[] columnNames = {"ID", "Counselor", "Date & Time", "Status", "Notes"};
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
    
    public AppointmentsPanel() {
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
        
        appointmentsTable = new JTable(tableModel);
        appointmentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        appointmentsTable.getTableHeader().setReorderingAllowed(false);
        
        appointmentsTable.getColumnModel().getColumn(0).setMaxWidth(50);
        appointmentsTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        appointmentsTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        appointmentsTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        appointmentsTable.getColumnModel().getColumn(4).setPreferredWidth(200);
        
        bookButton = new JButton("Book Appointment");
        rescheduleButton = new JButton("Reschedule");
        cancelButton = new JButton("Cancel");
        refreshButton = new JButton("Refresh");
        
        statusFilter = new JComboBox<>(new String[]{
            "All Status", "SCHEDULED", "COMPLETED", "CANCELLED", "RESCHEDULED"
        });
        
        upcomingOnlyCheckbox = new JCheckBox("Upcoming Only", true);
        
        rescheduleButton.setEnabled(false);
        cancelButton.setEnabled(false);
        
        appointmentsTable.getSelectionModel().addListSelectionListener(e -> {
            boolean hasSelection = appointmentsTable.getSelectedRow() != -1;
            rescheduleButton.setEnabled(hasSelection);
            cancelButton.setEnabled(hasSelection);
        });
    }
    
    private void layoutComponents() {
        setLayout(new BorderLayout());
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Filter:"));
        filterPanel.add(statusFilter);
        filterPanel.add(upcomingOnlyCheckbox);
        filterPanel.add(refreshButton);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(bookButton);
        buttonPanel.add(rescheduleButton);
        buttonPanel.add(cancelButton);
        
        topPanel.add(filterPanel, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);
        
        JScrollPane scrollPane = new JScrollPane(appointmentsTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Your Appointments"));
        
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    public void updateTable(List<Appointment> appointments) {
        tableModel.setRowCount(0);
        for (Appointment appointment : appointments) {
            Object[] row = {
                appointment.getId(),
                appointment.getCounselorName(),
                appointment.getAppointmentDateTime().format(dateFormatter),
                appointment.getStatus().toString(),
                appointment.getNotes() != null ? appointment.getNotes() : ""
            };
            tableModel.addRow(row);
        }
    }
    
    public Appointment getSelectedAppointment(List<Appointment> appointments) {
        int selectedRow = appointmentsTable.getSelectedRow();
        if (selectedRow != -1) {
            Long id = (Long) tableModel.getValueAt(selectedRow, 0);
            return appointments.stream()
                    .filter(a -> a.getId().equals(id))
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }
    
    public void addBookButtonListener(ActionListener listener) {
        bookButton.addActionListener(listener);
    }
    
    public void addRescheduleButtonListener(ActionListener listener) {
        rescheduleButton.addActionListener(listener);
    }
    
    public void addCancelButtonListener(ActionListener listener) {
        cancelButton.addActionListener(listener);
    }
    
    public void addRefreshButtonListener(ActionListener listener) {
        refreshButton.addActionListener(listener);
    }
    
    public String getSelectedStatus() {
        String selected = (String) statusFilter.getSelectedItem();
        return "All Status".equals(selected) ? null : selected;
    }
    
    public boolean isUpcomingOnlySelected() {
        return upcomingOnlyCheckbox.isSelected();
    }
    
    public void clearSelection() {
        appointmentsTable.clearSelection();
    }
    
    public void showBookingDialog(List<Counselor> counselors, ActionListener bookingListener) {
        BookingDialog dialog = new BookingDialog((Frame) SwingUtilities.getWindowAncestor(this), counselors);
        dialog.addBookingListener(bookingListener);
        dialog.setVisible(true);
    }
    
    private static class BookingDialog extends JDialog {
        private JComboBox<Counselor> counselorCombo;
        private JSpinner dateSpinner;
        private JSpinner timeSpinner;
        private JTextArea notesArea;
        private JButton bookButton;
        private JButton cancelButton;
        
        public BookingDialog(Frame parent, List<Counselor> counselors) {
            super(parent, "Book Appointment", true);
            initializeComponents(counselors);
            layoutComponents();
            pack();
            setLocationRelativeTo(parent);
        }
        
        private void initializeComponents(List<Counselor> counselors) {
            counselorCombo = new JComboBox<>();
            for (Counselor counselor : counselors) {
                if (counselor.isAvailable()) {
                    counselorCombo.addItem(counselor);
                }
            }
            
            dateSpinner = new JSpinner(new SpinnerDateModel());
            JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "MMM dd, yyyy");
            dateSpinner.setEditor(dateEditor);
            
            timeSpinner = new JSpinner(new SpinnerDateModel());
            JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
            timeSpinner.setEditor(timeEditor);
            
            notesArea = new JTextArea(4, 30);
            notesArea.setLineWrap(true);
            notesArea.setWrapStyleWord(true);
            
            bookButton = new JButton("Book");
            cancelButton = new JButton("Cancel");
            
            cancelButton.addActionListener(e -> dispose());
        }
        
        private void layoutComponents() {
            setLayout(new BorderLayout());
            
            JPanel formPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            
            gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
            formPanel.add(new JLabel("Counselor:"), gbc);
            gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
            formPanel.add(counselorCombo, gbc);
            
            gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
            formPanel.add(new JLabel("Date:"), gbc);
            gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
            formPanel.add(dateSpinner, gbc);
            
            gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST;
            formPanel.add(new JLabel("Time:"), gbc);
            gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
            formPanel.add(timeSpinner, gbc);
            
            gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.NORTHEAST;
            formPanel.add(new JLabel("Notes:"), gbc);
            gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
            formPanel.add(new JScrollPane(notesArea), gbc);
            
            JPanel buttonPanel = new JPanel(new FlowLayout());
            buttonPanel.add(bookButton);
            buttonPanel.add(cancelButton);
            
            add(formPanel, BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);
        }
        
        public void addBookingListener(ActionListener listener) {
            bookButton.addActionListener(e -> {
                listener.actionPerformed(e);
                dispose();
            });
        }
        
        public Counselor getSelectedCounselor() {
            return (Counselor) counselorCombo.getSelectedItem();
        }
        
        public String getNotes() {
            return notesArea.getText().trim();
        }
    }
}