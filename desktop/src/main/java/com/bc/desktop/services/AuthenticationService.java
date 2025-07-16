package com.bc.desktop.services;

import com.bc.desktop.models.StudentUser;
import com.bc.shared.models.User;
import com.bc.shared.utils.PasswordUtils;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class AuthenticationService {
    private static final String BASE_URL = "https://careconnect.exequtech.com";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    
    public AuthenticationService() {
        this.httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }
    
    public StudentUser authenticate(String studentNumber, String password) throws AuthenticationException {
        try {
            int studentNum = Integer.parseInt(studentNumber);
            
            String loginData = String.format(
                "studentNumber=%d&password=%s", 
                studentNum, 
                java.net.URLEncoder.encode(password, "UTF-8")
            );
            
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/login"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(loginData))
                .build();
            
            HttpResponse<String> response = httpClient.send(
                request, 
                HttpResponse.BodyHandlers.ofString()
            );
            
            if (response.statusCode() == 200) {
                String responseBody = response.body();
                try {
                    @SuppressWarnings("unchecked")
                    java.util.Map<String, Object> result = objectMapper.readValue(responseBody, java.util.Map.class);
                    
                    Boolean success = (Boolean) result.get("success");
                    if (success != null && success) {
                        @SuppressWarnings("unchecked")
                        java.util.Map<String, Object> userData = (java.util.Map<String, Object>) result.get("user");
                        User user = parseUserFromMap(userData);
                        return new StudentUser(user);
                    } else {
                        String message = (String) result.get("message");
                        throw new AuthenticationException(message != null ? message : "Authentication failed");
                    }
                } catch (Exception e) {
                    if (e instanceof AuthenticationException) {
                        throw e;
                    }
                    throw new AuthenticationException("Failed to parse server response: " + e.getMessage());
                }
            } else if (response.statusCode() == 401) {
                try {
                    @SuppressWarnings("unchecked")
                    java.util.Map<String, Object> result = objectMapper.readValue(response.body(), java.util.Map.class);
                    String message = (String) result.get("message");
                    throw new AuthenticationException(message != null ? message : "Invalid credentials");
                } catch (Exception e) {
                    throw new AuthenticationException("Invalid student number or password");
                }
            } else {
                throw new AuthenticationException("Server error: " + response.statusCode());
            }
        } catch (NumberFormatException e) {
            throw new AuthenticationException("Invalid student number format");
        } catch (IOException | InterruptedException e) {
            throw new AuthenticationException("Connection error: " + e.getMessage());
        }
    }
    
    private User parseUserFromMap(java.util.Map<String, Object> userData) {
        User user = new User();
        
        if (userData.get("id") != null) {
            user.setId(((Number) userData.get("id")).longValue());
        }
        if (userData.get("studentNumber") != null) {
            user.setStudentNumber(((Number) userData.get("studentNumber")).intValue());
        }
        if (userData.get("name") != null) {
            user.setName((String) userData.get("name"));
        }
        if (userData.get("surname") != null) {
            user.setSurname((String) userData.get("surname"));
        }
        if (userData.get("email") != null) {
            user.setEmail((String) userData.get("email"));
        }
        if (userData.get("phone") != null) {
            user.setPhone((String) userData.get("phone"));
        }
        if (userData.get("createdAt") != null) {
            try {
                String createdAtStr = (String) userData.get("createdAt");
                user.setCreatedAt(java.time.LocalDateTime.parse(createdAtStr));
            } catch (Exception e) {
                user.setCreatedAt(java.time.LocalDateTime.now());
            }
        }
        if (userData.get("updatedAt") != null) {
            try {
                String updatedAtStr = (String) userData.get("updatedAt");
                user.setUpdatedAt(java.time.LocalDateTime.parse(updatedAtStr));
            } catch (Exception e) {
                user.setUpdatedAt(java.time.LocalDateTime.now());
            }
        }
        
        return user;
    }
    
    public boolean validateStudentNumber(String studentNumber) {
        if (studentNumber == null || studentNumber.trim().isEmpty()) {
            return false;
        }
        
        try {
            int num = Integer.parseInt(studentNumber.trim());
            // Allow 000000 (admin) or regular 6-digit student numbers (100000-999999)
            return num == 0 || (num >= 100000 && num <= 999999);
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    public boolean validatePassword(String password) {
        return password != null && password.length() >= 6;
    }
    
    public static class AuthenticationException extends Exception {
        public AuthenticationException(String message) {
            super(message);
        }
        
        public AuthenticationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}