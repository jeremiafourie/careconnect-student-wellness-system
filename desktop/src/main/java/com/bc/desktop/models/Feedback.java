package com.bc.desktop.models;

import java.time.LocalDateTime;
import java.util.Objects;

public class Feedback {
    private Long id;
    private int studentNumber;
    private int rating;
    private String comments;
    private String category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public Feedback() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public Feedback(int studentNumber, int rating, String comments, String category) {
        this();
        this.studentNumber = studentNumber;
        this.rating = rating;
        this.comments = comments;
        this.category = category;
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
    
    public int getRating() {
        return rating;
    }
    
    public void setRating(int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        this.rating = rating;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getComments() {
        return comments;
    }
    
    public void setComments(String comments) {
        this.comments = comments;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
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
    
    public String getRatingStars() {
        return "★".repeat(rating) + "☆".repeat(5 - rating);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Feedback feedback = (Feedback) o;
        return Objects.equals(id, feedback.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Feedback: " + getRatingStars() + " - " + 
               (comments != null && comments.length() > 50 ? 
                comments.substring(0, 47) + "..." : comments);
    }
}
