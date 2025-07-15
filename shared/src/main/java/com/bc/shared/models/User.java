package com.bc.shared.models;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * User entity representing a student in the CareConnect system.
 * 
 * This class models the core user information including authentication details,
 * contact information, and audit timestamps. It corresponds to the 'users' table
 * in the PostgreSQL database.
 * 
 * @author CareConnect Development Team
 * @version 1.0
 * @since 1.0
 */
public class User {
    
    /** Unique identifier for the user (database primary key) */
    private Long id;
    
    /** Student number as 6-digit integer (e.g., 123456) */
    private int studentNumber;
    
    /** Student's first name */
    private String name;
    
    /** Student's surname/last name */
    private String surname;
    
    /** Student's email address (unique) */
    private String email;
    
    /** Student's phone number (optional) */
    private String phone;
    
    /** BCrypt hashed password for authentication */
    private String passwordHash;
    
    /** Timestamp when the user account was created */
    private LocalDateTime createdAt;
    
    /** Timestamp when the user account was last updated */
    private LocalDateTime updatedAt;
    
    /**
     * Default constructor for JPA/Hibernate and serialization.
     */
    public User() {
    }
    
    /**
     * Constructor for creating a new user with all required fields.
     * Sets createdAt and updatedAt to current timestamp.
     * 
     * @param studentNumber Student number as 6-digit integer
     * @param name Student's first name
     * @param surname Student's surname
     * @param email Student's email address
     * @param phone Student's phone number (can be null)
     * @param passwordHash BCrypt hashed password
     */
    public User(int studentNumber, String name, String surname, String email, String phone, String passwordHash) {
        this.studentNumber = studentNumber;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phone = phone;
        this.passwordHash = passwordHash;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
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
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getSurname() {
        return surname;
    }
    
    public void setSurname(String surname) {
        this.surname = surname;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getPasswordHash() {
        return passwordHash;
    }
    
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
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
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
               Objects.equals(studentNumber, user.studentNumber) &&
               Objects.equals(email, user.email);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, studentNumber, email);
    }
    
    @Override
    public String toString() {
        return "User{" +
               "id=" + id +
               ", studentNumber='" + studentNumber + '\'' +
               ", name='" + name + '\'' +
               ", surname='" + surname + '\'' +
               ", email='" + email + '\'' +
               ", phone='" + phone + '\'' +
               ", createdAt=" + createdAt +
               ", updatedAt=" + updatedAt +
               '}';
    }
}