package com.bc.careconnect.shared.models;

/**
 * Enumeration representing different user roles in CareConnect system
 * Provides type safety and consistent role management across applications
 */
public enum UserRole {
    STUDENT("student"),
    COUNSELOR("counselor"),
    ADMIN("admin");

    private final String value;

    /**
     * Constructor for enum values
     * @param value ["student", "counselor", "admin"]
     */
    UserRole(String value) {
        this.value = value;
    }

    /**
     * Get the string value of the role
     * Used for database storage and display
     * @return vlaue
     */
    public String getValue() {
        return value;
    }

    /**
     * Convert string value back to enum
     * Used when reading from database or form inputs
     * @param value
     * @return
     */
    public static UserRole fromString(String value) {
        if (value==null) {
            return STUDENT; // Default role
        }

        for (UserRole role : UserRole.values()) {
            if (role.value.equalsIgnoreCase(value.trim())) {
                return role;
            }
        }
        return STUDENT; // Default if no match is found
    }

    /**
     * Check if role has administrative privileges
     * @return
     */
    public boolean isAdmin() {
        return this == ADMIN;
    }

    /**
     * Check if role is counselor
     * @return
     */
    public boolean isCounselor() {
        return this == COUNSELOR || this == ADMIN;
    }

    /**
     * Check if role is student
     * @return
     */
    public boolean isStudent() {
        return this == STUDENT;
    }

    /**
     * Get display name for UI
     * @return
     */
    public String getDisplayName() {
        return value.substring(0, 1).toUpperCase() + value.substring(1).toLowerCase();
    }

    @Override
    public String toString() {
        return value;
    }
}
