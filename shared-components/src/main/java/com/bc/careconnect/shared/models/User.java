package com.bc.careconnect.shared.models;

import java.time.LocalDateTime;
import java.util.Objects;

import static java.util.Objects.hash;

/**
 * User entity representing a student in the CareConnect system
 * This class is used by both web and desktop applications to ensure
 * consistent user data representation across platforms
 */
public class User {
    // Primary identification
    private String
            studentNumber,
            name,
            surname,
            email,
            phone,
            password; // Stores hashed password, never plain text

    // User management fields
    private UserRole role;
    private boolean isActive;
    private LocalDateTime
            createdAt,
            lastLogin;

    /**
     * Default constructor - initializes user with default values
     */
    public User() {
        this.role = UserRole.STUDENT;
        this.isActive = true;
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Constructor with essential registration fields
     * @param studentNumber
     * @param name
     * @param surname
     * @param email
     * @param phone
     * @param password
     */
    public User(String studentNumber, String name, String surname, String email, String phone, String password) {
        this(); // Call default constructor for default values
        this.studentNumber = studentNumber;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    // ================== GETTERS AND SETTERS ==================

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    // ================== UTILITY METHODS ==================

    /**
     * Returns full name combining first and last name
     * Used in both web dashboard and desktop headers
     * @return
     */
    public String getFullName() {
        if (name == null && surname == null)
            return "Unkown User";
        if(name == null)
            return surname;
        if (surname == null) {
            return name;
        }
        return name + " " + surname;
    }

    /**
     * Validates if user has all required fields for registration
     * Used by both web registration and any admin user creation
     * @return
     */
    public boolean isValidForRegistration() {
        return studentNumber != null && !studentNumber.trim().isEmpty() &&
                name != null && !name.trim().isEmpty() &&
                surname != null && !surname.trim().isEmpty() &&
                email != null && !email.trim().isEmpty() &&
                phone != null && !phone.trim().isEmpty() &&
                password != null && !password.trim().isEmpty();
    }

    /**
     * Returns a display-friendly user identifier
     * Combines name and student number for UI displays
     * @return
     */
    public String getDisplayName() {
        return getFullName() + " (" + studentNumber + ")";
    }

    /**
     * Checks if user account if is valid for login
     * @return
     */
    public boolean canLogin() {
        return isActive && studentNumber !=null && password != null;
    }

    // ================== OBJECT OVERRIDES ==================

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        User user = (User) o;
        return Objects.equals(studentNumber, user.studentNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentNumber);
    }

    @Override
    public String toString() {
        return "User{" +
                "studentNumber='" + studentNumber + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", isActive=" + isActive +
                ", createdAt=" + createdAt +
                '}';
    }
}
