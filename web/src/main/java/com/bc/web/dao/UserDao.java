package com.bc.web.dao;

import com.bc.shared.models.User;
import com.bc.shared.utils.PasswordUtils;
import com.bc.web.config.DatabaseConfig;

import java.sql.*;
import java.time.LocalDateTime;

import java.util.Optional;

public class UserDao {
    
    public boolean createUser(User user) {
        String sql = "INSERT INTO users (student_number, name, surname, email, phone, password_hash) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            statement.setInt(1, user.getStudentNumber());
            statement.setString(2, user.getName());
            statement.setString(3, user.getSurname());
            statement.setString(4, user.getEmail());
            statement.setString(5, user.getPhone());
            statement.setString(6, user.getPasswordHash());
            
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setId(generatedKeys.getLong(1));
                        return true;
                    }
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error creating user: " + e.getMessage());
        }
        
        return false;
    }
    
    public Optional<User> findByStudentNumber(int studentNumber) {
        String sql = "SELECT id, student_number, name, surname, email, phone, password_hash, created_at, updated_at FROM users WHERE student_number = ?";
        
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, studentNumber);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToUser(resultSet));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding user by student number: " + e.getMessage());
        }
        
        return Optional.empty();
    }
    
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT id, student_number, name, surname, email, phone, password_hash, created_at, updated_at FROM users WHERE email = ?";
        
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, email);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToUser(resultSet));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding user by email: " + e.getMessage());
            // TODO
        }
        
        return Optional.empty();
    }
    
    public Optional<User> authenticateUser(String identifier, String password) {
        Optional<User> userOpt = Optional.empty();
        
        // Try to parse as student number first
        try {
            int studentNumber = Integer.parseInt(identifier);
            userOpt = findByStudentNumber(studentNumber);
        } catch (NumberFormatException e) {
            // Not a number, try as email
        }
        
        if (userOpt.isEmpty()) {
            userOpt = findByEmail(identifier);
        }
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (PasswordUtils.verifyPassword(password, user.getPasswordHash())) {
                return Optional.of(user);
            }
        }
        
        return Optional.empty();
    }
    
    public boolean isStudentNumberExists(int studentNumber) {
        return findByStudentNumber(studentNumber).isPresent();
    }
    
    public boolean isEmailExists(String email) {
        return findByEmail(email).isPresent();
    }
    
    public boolean updateUser(User user) {
        String sql = "UPDATE users SET name = ?, surname = ?, email = ?, phone = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, user.getName());
            statement.setString(2, user.getSurname());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPhone());
            statement.setLong(5, user.getId());
            
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
        }
        
        return false;
    }
    
    public boolean updatePassword(Long userId, String newPasswordHash) {
        String sql = "UPDATE users SET password_hash = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, newPasswordHash);
            statement.setLong(2, userId);
            
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating password: " + e.getMessage());
        }
        
        return false;
    }
    
    private User mapResultSetToUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setStudentNumber(resultSet.getInt("student_number"));
        user.setName(resultSet.getString("name"));
        user.setSurname(resultSet.getString("surname"));
        user.setEmail(resultSet.getString("email"));
        user.setPhone(resultSet.getString("phone"));
        user.setPasswordHash(resultSet.getString("password_hash"));
        
        Timestamp createdAt = resultSet.getTimestamp("created_at");
        if (createdAt != null) {
            user.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = resultSet.getTimestamp("updated_at");
        if (updatedAt != null) {
            user.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return user;
    }
    
    /**
     * Retrieves a user by their ID.
     * 
     * @param userId The user ID to search for
     * @return Optional containing the User if found, empty otherwise
     */
    public Optional<User> getUserById(Long userId) {
        String sql = "SELECT id, student_number, name, surname, email, phone, password_hash, created_at, updated_at FROM users WHERE id = ?";
        
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setLong(1, userId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToUser(resultSet));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding user by ID: " + e.getMessage());
        }
        
        return Optional.empty();
    }
}