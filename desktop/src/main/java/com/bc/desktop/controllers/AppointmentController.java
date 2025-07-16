package com.bc.desktop.controllers;

import com.bc.desktop.models.Appointment;
import com.bc.desktop.models.Counselor;
import com.bc.desktop.models.StudentUser;
import com.bc.desktop.services.DatabaseService;
import com.bc.desktop.views.AppointmentsPanel;
import javax.swing.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


public class AppointmentController {
    private AppointmentsPanel appointmentsPanel;
    private DatabaseService databaseService;
    private StudentUser currentUser;
    private List<Appointment> allAppointments;
    private List<Counselor> availableCounselors;
    
    public AppointmentController(AppointmentsPanel appointmentsPanel, DatabaseService databaseService) {
        this.appointmentsPanel = appointmentsPanel;
        this.databaseService = databaseService;
        setupEventHandlers();
    }
    
    private void setupEventHandlers() {
        appointmentsPanel.addRefreshButtonListener(e -> loadAppointments());
        appointmentsPanel.addBookButtonListener(e -> showBookingDialog());
        appointmentsPanel.addRescheduleButtonListener(e -> rescheduleAppointment());
        appointmentsPanel.addCancelButtonListener(e -> cancelAppointment());
    }
    
    public void initialize(StudentUser user) {
        this.currentUser = user;
        loadCounselors();
        loadAppointments();
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
                    availableCounselors = get().stream()
                        .filter(Counselor::isAvailable)
                        .collect(Collectors.toList());
                } catch (Exception e) {
                    System.err.println("Error loading counselors: " + e.getMessage());
                    availableCounselors = List.of();
                }
            }
        };
        
        worker.execute();
    }
    
    private void loadAppointments() {
        if (currentUser == null) return;
        
        SwingWorker<List<Appointment>, Void> worker = new SwingWorker<List<Appointment>, Void>() {
            @Override
            protected List<Appointment> doInBackground() throws Exception {
                return databaseService.getAppointmentsByStudent(currentUser.getStudentNumber());
            }
            
            @Override
            protected void done() {
                try {
                    allAppointments = get();
                    filterAndDisplayAppointments();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(
                        appointmentsPanel,
                        "Error loading appointments: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        };
        
        worker.execute();
    }
    
    private void filterAndDisplayAppointments() {
        List<Appointment> filteredAppointments = allAppointments;
        
        String selectedStatus = appointmentsPanel.getSelectedStatus();
        boolean upcomingOnly = appointmentsPanel.isUpcomingOnlySelected();
        
        if (selectedStatus != null) {
            filteredAppointments = filteredAppointments.stream()
                .filter(a -> a.getStatus().toString().equals(selectedStatus))
                .collect(Collectors.toList());
        }
        
        if (upcomingOnly) {
            LocalDateTime now = LocalDateTime.now();
            filteredAppointments = filteredAppointments.stream()
                .filter(a -> a.getAppointmentDateTime().isAfter(now))
                .collect(Collectors.toList());
        }
        
        appointmentsPanel.updateTable(filteredAppointments);
    }
    
    private void showBookingDialog() {
        if (availableCounselors == null || availableCounselors.isEmpty()) {
            JOptionPane.showMessageDialog(
                appointmentsPanel,
                "No counselors available for booking",
                "No Counselors",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        
        appointmentsPanel.showBookingDialog(availableCounselors, e -> bookAppointment());
    }
    
    private void bookAppointment() {
        try {
            
            // Create a sample appointment for demonstration
            Counselor selectedCounselor = availableCounselors.get(0); // For demo purposes
            Appointment appointment = new Appointment(
                currentUser.getStudentNumber(),
                selectedCounselor.getId(),
                selectedCounselor.getName(),
                LocalDateTime.now().plusDays(1) // Tomorrow
            );
            appointment.setNotes("Booking from desktop application");
            
            boolean success = databaseService.saveAppointment(appointment);
            
            if (success) {
                JOptionPane.showMessageDialog(
                    appointmentsPanel,
                    "Appointment booked successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
                loadAppointments();
            } else {
                JOptionPane.showMessageDialog(
                    appointmentsPanel,
                    "Failed to book appointment. Please try again.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                appointmentsPanel,
                "Error booking appointment: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    private void rescheduleAppointment() {
        Appointment selected = appointmentsPanel.getSelectedAppointment(allAppointments);
        if (selected == null) {
            JOptionPane.showMessageDialog(
                appointmentsPanel,
                "Please select an appointment to reschedule",
                "No Selection",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        
        if (selected.getStatus() == Appointment.Status.COMPLETED || 
            selected.getStatus() == Appointment.Status.CANCELLED) {
            JOptionPane.showMessageDialog(
                appointmentsPanel,
                "Cannot reschedule a " + selected.getStatus().toString().toLowerCase() + " appointment",
                "Invalid Status",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        
        // For demo purposes, just update the status
        selected.setStatus(Appointment.Status.RESCHEDULED);
        selected.setNotes((selected.getNotes() != null ? selected.getNotes() + "; " : "") + 
                         "Rescheduled on " + LocalDateTime.now().toLocalDate());
        
        boolean success = databaseService.updateAppointment(selected);
        
        if (success) {
            JOptionPane.showMessageDialog(
                appointmentsPanel,
                "Appointment marked for rescheduling. Please contact the counselor to set a new time.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
            );
            loadAppointments();
        } else {
            JOptionPane.showMessageDialog(
                appointmentsPanel,
                "Failed to reschedule appointment. Please try again.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    private void cancelAppointment() {
        Appointment selected = appointmentsPanel.getSelectedAppointment(allAppointments);
        if (selected == null) {
            JOptionPane.showMessageDialog(
                appointmentsPanel,
                "Please select an appointment to cancel",
                "No Selection",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        
        if (selected.getStatus() == Appointment.Status.COMPLETED || 
            selected.getStatus() == Appointment.Status.CANCELLED) {
            JOptionPane.showMessageDialog(
                appointmentsPanel,
                "Cannot cancel a " + selected.getStatus().toString().toLowerCase() + " appointment",
                "Invalid Status",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        
        int option = JOptionPane.showConfirmDialog(
            appointmentsPanel,
            "Are you sure you want to cancel this appointment?",
            "Confirm Cancellation",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (option == JOptionPane.YES_OPTION) {
            selected.setStatus(Appointment.Status.CANCELLED);
            selected.setNotes((selected.getNotes() != null ? selected.getNotes() + "; " : "") + 
                             "Cancelled on " + LocalDateTime.now().toLocalDate());
            
            boolean success = databaseService.updateAppointment(selected);
            
            if (success) {
                JOptionPane.showMessageDialog(
                    appointmentsPanel,
                    "Appointment cancelled successfully",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
                loadAppointments();
            } else {
                JOptionPane.showMessageDialog(
                    appointmentsPanel,
                    "Failed to cancel appointment. Please try again.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
    
    public void cleanup() {
        appointmentsPanel.clearSelection();
        currentUser = null;
    }
}