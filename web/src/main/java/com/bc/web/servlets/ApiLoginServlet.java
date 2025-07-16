package com.bc.web.servlets;

import com.bc.shared.dto.UserLoginDto;
import com.bc.shared.models.User;
import com.bc.shared.utils.ValidationUtils;
import com.bc.web.dao.UserDao;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@WebServlet("/api/login")
public class ApiLoginServlet extends HttpServlet {
    
    private UserDao userDao;
    private ObjectMapper objectMapper;
    
    @Override
    public void init() throws ServletException {
        userDao = new UserDao();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        // Enable CORS for desktop application
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // Parse request parameters
            String studentNumberStr = request.getParameter("studentNumber");
            String password = request.getParameter("password");
            
            // Validate input
            if (studentNumberStr == null || studentNumberStr.trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "Student number is required");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                objectMapper.writeValue(response.getWriter(), result);
                return;
            }
            
            if (password == null || password.trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "Password is required");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                objectMapper.writeValue(response.getWriter(), result);
                return;
            }
            
            // Validate student number format
            if (!ValidationUtils.isValidStudentNumber(studentNumberStr.trim())) {
                result.put("success", false);
                result.put("message", "Invalid student number format. Must be 6 digits.");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                objectMapper.writeValue(response.getWriter(), result);
                return;
            }
            
            // Authenticate user
            Optional<User> userOpt = userDao.authenticateUser(studentNumberStr.trim(), password);
            
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                
                result.put("success", true);
                result.put("message", "Login successful");
                result.put("user", createUserResponse(user));
                
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                result.put("success", false);
                result.put("message", "Invalid student number or password");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "An error occurred during authentication");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            
            // Log the error
            getServletContext().log("API Login error: " + e.getMessage(), e);
        }
        
        objectMapper.writeValue(response.getWriter(), result);
    }
    
    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Handle CORS preflight requests
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setStatus(HttpServletResponse.SC_OK);
    }
    
    private Map<String, Object> createUserResponse(User user) {
        Map<String, Object> userResponse = new HashMap<>();
        userResponse.put("id", user.getId());
        userResponse.put("studentNumber", user.getStudentNumber());
        userResponse.put("name", user.getName());
        userResponse.put("surname", user.getSurname());
        userResponse.put("email", user.getEmail());
        userResponse.put("phone", user.getPhone());
        userResponse.put("createdAt", user.getCreatedAt());
        userResponse.put("updatedAt", user.getUpdatedAt());
        // Explicitly exclude password hash from response
        return userResponse;
    }
}