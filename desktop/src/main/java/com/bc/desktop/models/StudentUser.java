package com.bc.desktop.models;

import com.bc.shared.models.User;

public class StudentUser {
    private User user;
    private boolean isLoggedIn;
    private UserRole role;
    
    public StudentUser() {
        this.isLoggedIn = false;
        this.role = UserRole.STUDENT;
    }
    
    public StudentUser(User user) {
        this.user = user;
        this.isLoggedIn = true;
        // Admin user has student number 000000 (0)
        this.role = (user.getStudentNumber() == 0) ? UserRole.ADMIN : UserRole.STUDENT;
    }
    
    public StudentUser(User user, UserRole role) {
        this.user = user;
        this.isLoggedIn = true;
        this.role = role;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
        this.isLoggedIn = (user != null);
    }
    
    public boolean isLoggedIn() {
        return isLoggedIn && user != null;
    }
    
    public void logout() {
        this.user = null;
        this.isLoggedIn = false;
    }
    
    public String getFullName() {
        return user != null ? user.getName() + " " + user.getSurname() : "";
    }
    
    public int getStudentNumber() {
        return user != null ? user.getStudentNumber() : 0;
    }
    
    public String getEmail() {
        return user != null ? user.getEmail() : "";
    }
    
    public UserRole getRole() {
        return role;
    }
    
    public void setRole(UserRole role) {
        this.role = role;
    }
    
    public boolean isAdmin() {
        return role == UserRole.ADMIN;
    }
    
    public boolean isStudent() {
        return role == UserRole.STUDENT;
    }
}