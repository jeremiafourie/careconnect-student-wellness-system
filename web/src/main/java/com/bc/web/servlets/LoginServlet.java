package com.bc.web.servlets;

import com.bc.shared.dto.UserLoginDto;
import com.bc.shared.models.User;
import com.bc.shared.utils.ValidationUtils;
import com.bc.web.dao.UserDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    
    private UserDao userDao;
    
    @Override
    public void init() throws ServletException {
        userDao = new UserDao();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        UserLoginDto loginDto = new UserLoginDto();
        loginDto.setIdentifier(request.getParameter("identifier"));
        loginDto.setPassword(request.getParameter("password"));
        
        List<String> errors = validateLogin(loginDto);
        
        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);
            request.setAttribute("loginDto", loginDto);
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }
        
        try {
            Optional<User> userOpt = userDao.authenticateUser(
                loginDto.getIdentifier().trim(), 
                loginDto.getPassword()
            );
            
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                
                // Create user session
                HttpSession session = request.getSession(true);
                
                session.setAttribute("user", user);
                session.setAttribute("userId", user.getId());
                session.setAttribute("userName", user.getName() + " " + user.getSurname());
                session.setAttribute("userEmail", user.getEmail());
                session.setAttribute("studentNumber", user.getStudentNumber());
                
                // Set session timeout to 30 minutes
                session.setMaxInactiveInterval(30 * 60);
                
                response.sendRedirect(request.getContextPath() + "/dashboard");
            } else {
                errors.add("Invalid student number/email or password");
                request.setAttribute("errors", errors);
                request.setAttribute("loginDto", loginDto);
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            errors.add("An error occurred during login. Please try again.");
            request.setAttribute("errors", errors);
            request.setAttribute("loginDto", loginDto);
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
    
    private List<String> validateLogin(UserLoginDto dto) {
        List<String> errors = new ArrayList<>();
        
        if (dto.getIdentifier() == null || dto.getIdentifier().trim().isEmpty()) {
            errors.add("Student number or email is required");
        } else {
            String identifier = dto.getIdentifier().trim();
            if (!ValidationUtils.isValidStudentNumber(identifier) && 
                !ValidationUtils.isValidEmail(identifier)) {
                errors.add("Please enter a valid student number (6 digits) or email address");
            }
        }
        
        if (dto.getPassword() == null || dto.getPassword().isEmpty()) {
            errors.add("Password is required");
        }
        
        return errors;
    }
}