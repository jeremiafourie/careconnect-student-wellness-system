package com.bc.shared.utils;

import java.util.regex.Pattern;

/**
 * Utility class providing validation methods for user input in the CareConnect system.
 * 
 * This class contains static methods for validating various user inputs including
 * email addresses, phone numbers, student numbers, passwords, and names. It uses
 * regular expressions and business rules to ensure data integrity and security.
 * 
 * All validation methods are designed to be null-safe and handle edge cases
 * gracefully. They follow BC (British Columbia) educational institution
 * standards for student number formats.
 * 
 * @author CareConnect Development Team
 * @version 1.0
 * @since 1.0
 */
public class ValidationUtils {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^\\d{10}$"
    );
    
    private static final Pattern STUDENT_NUMBER_PATTERN = Pattern.compile(
        "^\\d{6}$"
    );
    
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_PASSWORD_LENGTH = 100;
    
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }
    
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return true;
        }
        String cleanPhone = phone.replaceAll("[\\s\\-\\(\\)]", "");
        return PHONE_PATTERN.matcher(cleanPhone).matches();
    }
    
    public static boolean isValidStudentNumber(String studentNumber) {
        if (studentNumber == null || studentNumber.trim().isEmpty()) {
            return false;
        }
        return STUDENT_NUMBER_PATTERN.matcher(studentNumber.trim()).matches();
    }
    
    public static boolean isValidStudentNumber(int studentNumber) {
        return studentNumber >= 100000 && studentNumber <= 999999;
    }
    
    public static boolean isValidPassword(String password) {
        if (password == null) {
            return false;
        }
        
        int length = password.length();
        if (length < MIN_PASSWORD_LENGTH || length > MAX_PASSWORD_LENGTH) {
            return false;
        }
        
        boolean hasUppercase = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLowercase = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        boolean hasSpecial = password.chars().anyMatch(c -> 
            !Character.isLetterOrDigit(c) && !Character.isWhitespace(c)
        );
        
        return hasUppercase && hasLowercase && hasDigit && hasSpecial;
    }
    
    public static boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        
        String trimmed = name.trim();
        return trimmed.length() >= 2 && trimmed.length() <= 50 && 
               trimmed.matches("^[A-Za-z\\s'-]+$");
    }
    
    public static boolean passwordsMatch(String password, String confirmPassword) {
        if (password == null || confirmPassword == null) {
            return false;
        }
        return password.equals(confirmPassword);
    }
    
    public static String getEmailValidationMessage() {
        return "Please enter a valid email address";
    }
    
    public static String getPhoneValidationMessage() {
        return "Please enter a valid phone number (10 digits, e.g., 0652774945)";
    }
    
    public static String getStudentNumberValidationMessage() {
        return "Student number must be exactly 6 digits (e.g., 123456)";
    }
    
    public static String getPasswordValidationMessage() {
        return "Password must be 8-100 characters long and contain at least one uppercase letter, one lowercase letter, one digit, and one special character";
    }
    
    public static String getNameValidationMessage() {
        return "Name must be 2-50 characters long and contain only letters, spaces, hyphens, and apostrophes";
    }
    
    public static String getPasswordMismatchMessage() {
        return "Passwords do not match";
    }
}