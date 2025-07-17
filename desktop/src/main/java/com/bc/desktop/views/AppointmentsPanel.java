package com.bc.desktop.views;

import com.bc.desktop.models.Appointment;
import com.bc.desktop.models.Counselor;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
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
    
    private String[] columnNames = {"ID", "Counselor", "Date & Time", "Status", "Notes"};
    private boolean adminMode = false;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' HH:mm");
    
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
            Object[] row;
            if (adminMode) {
                row = new Object[]{
                    appointment.getId(),
                    appointment.getStudentNumber(),
                    appointment.getCounselorName(),
                    appointment.getAppointmentDateTime().format(dateFormatter),
                    appointment.getStatus().toString(),
                    appointment.getNotes() != null ? appointment.getNotes() : ""
                };
            } else {
                row = new Object[]{
                    appointment.getId(),
                    appointment.getCounselorName(),
                    appointment.getAppointmentDateTime().format(dateFormatter),
                    appointment.getStatus().toString(),
                    appointment.getNotes() != null ? appointment.getNotes() : ""
                };
            }
            tableModel.addRow(row);
        }
    }
    
    public void setAdminMode(boolean adminMode) {
        this.adminMode = adminMode;
        if (adminMode) {
            columnNames = new String[]{"ID", "Student #", "Counselor", "Date & Time", "Status", "Notes"};
        } else {
            columnNames = new String[]{"ID", "Counselor", "Date & Time", "Status", "Notes"};
        }
        
        // Update table model with new columns
        tableModel.setColumnIdentifiers(columnNames);
        
        // Adjust column widths for admin mode
        if (adminMode) {
            appointmentsTable.getColumnModel().getColumn(0).setMaxWidth(50);
            appointmentsTable.getColumnModel().getColumn(1).setPreferredWidth(80);
            appointmentsTable.getColumnModel().getColumn(2).setPreferredWidth(150);
            appointmentsTable.getColumnModel().getColumn(3).setPreferredWidth(140);
            appointmentsTable.getColumnModel().getColumn(4).setPreferredWidth(100);
            appointmentsTable.getColumnModel().getColumn(5).setPreferredWidth(200);
        } else {
            appointmentsTable.getColumnModel().getColumn(0).setMaxWidth(50);
            appointmentsTable.getColumnModel().getColumn(1).setPreferredWidth(150);
            appointmentsTable.getColumnModel().getColumn(2).setPreferredWidth(140);
            appointmentsTable.getColumnModel().getColumn(3).setPreferredWidth(100);
            appointmentsTable.getColumnModel().getColumn(4).setPreferredWidth(250);
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
    
    public void showBookingDialog(List<Counselor> counselors, ActionListener bookingListener, com.bc.desktop.services.DatabaseService databaseService) {
        lastBookingDialog = new BookingDialog((Frame) SwingUtilities.getWindowAncestor(this), counselors, databaseService);
        lastBookingDialog.addBookingListener(bookingListener);
        lastBookingDialog.setVisible(true);
    }
    
    public BookingDialog getLastBookingDialog() {
        return lastBookingDialog;
    }
    
    private BookingDialog lastBookingDialog;
    
    public static class BookingDialog extends JDialog {
        private JComboBox<Counselor> counselorCombo;
        private JSpinner dateSpinner;
        private JComboBox<String> timeSlotCombo;
        private JTextArea notesArea;
        private JButton bookButton;
        private JButton cancelButton;
        private JButton refreshSlotsButton;
        private JLabel availabilityLabel;
        private List<Counselor> counselors;
        private boolean confirmed = false;
        private LocalDateTime selectedDateTime;
        private com.bc.desktop.services.DatabaseService databaseService;
        
        public BookingDialog(Frame parent, List<Counselor> counselors, com.bc.desktop.services.DatabaseService databaseService) {
            super(parent, "Book Appointment", true);
            this.counselors = counselors;
            this.databaseService = databaseService;
            initializeComponents();
            layoutComponents();
            setupEventHandlers();
            pack();
            setLocationRelativeTo(parent);
        }
        
        private void initializeComponents() {
            counselorCombo = new JComboBox<>();
            for (Counselor counselor : counselors) {
                if (counselor.isAvailable()) {
                    counselorCombo.addItem(counselor);
                }
            }
            
            // Date spinner - start from tomorrow
            Calendar tomorrow = Calendar.getInstance();
            tomorrow.add(Calendar.DAY_OF_MONTH, 1);
            dateSpinner = new JSpinner(new SpinnerDateModel(tomorrow.getTime(), tomorrow.getTime(), null, Calendar.DAY_OF_MONTH));
            JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "MMM dd, yyyy");
            dateSpinner.setEditor(dateEditor);
            
            timeSlotCombo = new JComboBox<>();
            timeSlotCombo.addItem("Select a time slot");
            
            notesArea = new JTextArea(4, 30);
            notesArea.setLineWrap(true);
            notesArea.setWrapStyleWord(true);
            
            bookButton = new JButton("Book Appointment");
            cancelButton = new JButton("Cancel");
            refreshSlotsButton = new JButton("Refresh Slots");
            availabilityLabel = new JLabel("Select counselor and date to view available slots");
            
            bookButton.setEnabled(false);
        }
        
        private void setupEventHandlers() {
            counselorCombo.addActionListener(e -> refreshAvailableSlots());
            dateSpinner.addChangeListener(e -> refreshAvailableSlots());
            refreshSlotsButton.addActionListener(e -> refreshAvailableSlots());
            
            timeSlotCombo.addActionListener(e -> {
                String selected = (String) timeSlotCombo.getSelectedItem();
                bookButton.setEnabled(selected != null && !selected.equals("Select a time slot"));
            });
            
            cancelButton.addActionListener(e -> dispose());
        }
        
        private void refreshAvailableSlots() {
            Counselor selectedCounselor = (Counselor) counselorCombo.getSelectedItem();
            if (selectedCounselor == null) {
                availabilityLabel.setText("Please select a counselor");
                return;
            }
            
            Date selectedDate = (Date) dateSpinner.getValue();
            LocalDate date = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            
            // Check if selected date is in the past
            if (date.isBefore(LocalDate.now())) {
                availabilityLabel.setText("Cannot book appointments in the past");
                timeSlotCombo.removeAllItems();
                timeSlotCombo.addItem("Select a time slot");
                bookButton.setEnabled(false);
                return;
            }
            
            // Check if selected date is a weekend
            if (date.getDayOfWeek().getValue() == 6 || date.getDayOfWeek().getValue() == 7) {
                availabilityLabel.setText("Counselors are not available on weekends");
                timeSlotCombo.removeAllItems();
                timeSlotCombo.addItem("Select a time slot");
                bookButton.setEnabled(false);
                return;
            }
            
            // Generate available time slots (9 AM to 5 PM)
            timeSlotCombo.removeAllItems();
            timeSlotCombo.addItem("Select a time slot");
            
            List<String> availableSlots = new ArrayList<>();
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            
            for (int hour = 9; hour <= 17; hour++) {
                LocalDateTime slotTime = date.atTime(hour, 0);
                
                // Check if slot is available (mock implementation - in real app would check database)
                if (isSlotAvailable(selectedCounselor.getId(), slotTime)) {
                    String slotDisplay = slotTime.format(timeFormatter);
                    availableSlots.add(slotDisplay);
                    timeSlotCombo.addItem(slotDisplay);
                }
            }
            
            if (availableSlots.isEmpty()) {
                availabilityLabel.setText("No available slots for this date");
                timeSlotCombo.addItem("No slots available");
            } else {
                // Show booked slots info
                List<com.bc.desktop.models.Appointment> bookedAppointments = null;
                if (databaseService != null) {
                    bookedAppointments = databaseService.getCounselorAppointments(selectedCounselor.getId(), date);
                }
                
                String statusText = availableSlots.size() + " slots available";
                if (bookedAppointments != null && !bookedAppointments.isEmpty()) {
                    statusText += " (" + bookedAppointments.size() + " booked)";
                }
                availabilityLabel.setText(statusText);
            }
            
            bookButton.setEnabled(false);
        }
        
        private boolean isSlotAvailable(Long counselorId, LocalDateTime slotTime) {
            if (databaseService != null) {
                return databaseService.isTimeSlotAvailable(counselorId, slotTime);
            }
            
            // Fallback: Mock implementation blocks lunch and 2 PM slots
            int hour = slotTime.getHour();
            return !(hour == 12 || hour == 14);
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
            formPanel.add(new JLabel("Available Times:"), gbc);
            gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
            JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            timePanel.add(timeSlotCombo);
            timePanel.add(refreshSlotsButton);
            formPanel.add(timePanel, gbc);
            
            gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
            formPanel.add(availabilityLabel, gbc);
            
            gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.NORTHEAST;
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
                if (validateBooking()) {
                    confirmed = true;
                    listener.actionPerformed(e);
                    dispose();
                }
            });
        }
        
        private boolean validateBooking() {
            Counselor selectedCounselor = (Counselor) counselorCombo.getSelectedItem();
            String selectedTime = (String) timeSlotCombo.getSelectedItem();
            
            if (selectedCounselor == null) {
                JOptionPane.showMessageDialog(this, "Please select a counselor", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            if (selectedTime == null || selectedTime.equals("Select a time slot")) {
                JOptionPane.showMessageDialog(this, "Please select a time slot", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            // Create the complete appointment date and time
            Date selectedDate = (Date) dateSpinner.getValue();
            LocalDate date = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalTime time = LocalTime.parse(selectedTime);
            selectedDateTime = date.atTime(time);
            
            return true;
        }
        
        public boolean isConfirmed() {
            return confirmed;
        }
        
        public Counselor getSelectedCounselor() {
            return (Counselor) counselorCombo.getSelectedItem();
        }
        
        public LocalDateTime getSelectedDateTime() {
            return selectedDateTime;
        }
        
        public String getNotes() {
            return notesArea.getText().trim();
        }
    }
}