package com.bc.desktop.models;

import java.time.LocalDateTime;
import java.util.Objects;

public class Appointment {
    public enum Status {
        SCHEDULED, COMPLETED, CANCELLED, RESCHEDULED
    }
    
    private Long id;
    private int studentNumber;
    private Long counselorId;
    private String counselorName;
    private LocalDateTime appointmentDateTime;
    private Status status;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public Appointment() {
        this.status = Status.SCHEDULED;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public Appointment(int studentNumber, Long counselorId, String counselorName, LocalDateTime appointmentDateTime) {
        this();
        this.studentNumber = studentNumber;
        this.counselorId = counselorId;
        this.counselorName = counselorName;
        this.appointmentDateTime = appointmentDateTime;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public int getStudentNumber() {
        return studentNumber;
    }
    
    public void setStudentNumber(int studentNumber) {
        this.studentNumber = studentNumber;
    }
    
    public Long getCounselorId() {
        return counselorId;
    }
    
    public void setCounselorId(Long counselorId) {
        this.counselorId = counselorId;
    }
    
    public String getCounselorName() {
        return counselorName;
    }
    
    public void setCounselorName(String counselorName) {
        this.counselorName = counselorName;
    }
    
    public LocalDateTime getAppointmentDateTime() {
        return appointmentDateTime;
    }
    
    public void setAppointmentDateTime(LocalDateTime appointmentDateTime) {
        this.appointmentDateTime = appointmentDateTime;
        this.updatedAt = LocalDateTime.now();
    }
    
    public Status getStatus() {
        return status;
    }
    
    public void setStatus(Status status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
        this.updatedAt = LocalDateTime.now();
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public boolean isUpcoming() {
        return appointmentDateTime.isAfter(LocalDateTime.now()) && 
               (status == Status.SCHEDULED || status == Status.RESCHEDULED);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Appointment that = (Appointment) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Appointment with " + counselorName + " on " + appointmentDateTime;
    }
}
