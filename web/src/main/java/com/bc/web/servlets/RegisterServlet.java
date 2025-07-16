package com.bc.web.servlets;

import com.bc.shared.dto.UserRegistrationDto;
import com.bc.shared.models.User;
import com.bc.shared.utils.PasswordUtils;
import com.bc.shared.utils.ValidationUtils;
import com.bc.web.dao.EmailUtil;
import com.bc.web.dao.UserDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    
    private UserDao userDao;
    
    @Override
    public void init() throws ServletException {
        userDao = new UserDao();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setStudentNumber(request.getParameter("studentNumber"));
        registrationDto.setName(request.getParameter("name"));
        registrationDto.setSurname(request.getParameter("surname"));
        registrationDto.setEmail(request.getParameter("email"));
        registrationDto.setPhone(request.getParameter("phone"));
        registrationDto.setPassword(request.getParameter("password"));
        registrationDto.setConfirmPassword(request.getParameter("confirmPassword"));
        
        List<String> errors = validateRegistration(registrationDto);
        
        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);
            request.setAttribute("registrationDto", registrationDto);
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }
        
        try {
            String hashedPassword = PasswordUtils.hashPassword(registrationDto.getPassword());
            
            int studentNumber = Integer.parseInt(registrationDto.getStudentNumber().trim());
            
            User user = new User(
                studentNumber,
                registrationDto.getName().trim(),
                registrationDto.getSurname().trim(),
                registrationDto.getEmail().trim().toLowerCase(),
                registrationDto.getPhone() != null ? registrationDto.getPhone().trim() : null,
                hashedPassword
            );
            
            boolean success = userDao.createUser(user);
            
            if (success) {
                request.setAttribute("successMessage", "Registration successful! Please login.");
                //send confirmation email
                try {
                        EmailUtil.sendEmail(user.getEmail(), user.getName() + " " + user.getSurname());
                    } catch (Exception e) {
                        System.err.println("❌ Failed to send email: " + e.getMessage());
                        e.printStackTrace(); // show full trace
                        request.setAttribute("errorMessage", "Registered, but failed to send confirmation email.");
                    }

                
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            } else {
                errors.add("Registration failed. Please try again.");
                request.setAttribute("errors", errors);
                request.setAttribute("registrationDto", registrationDto);
                request.getRequestDispatcher("/register.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Registration error: " + e.getMessage());
            errors.add("An error occurred during registration. Please try again.");
            request.setAttribute("errors", errors);
            request.setAttribute("registrationDto", registrationDto);
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }
    
    private List<String> validateRegistration(UserRegistrationDto dto) {
        List<String> errors = new ArrayList<>();
        
        if (dto.getStudentNumber() == null || dto.getStudentNumber().trim().isEmpty()) {
            errors.add("Student number is required");
        } else if (!ValidationUtils.isValidStudentNumber(dto.getStudentNumber())) {
            errors.add(ValidationUtils.getStudentNumberValidationMessage());
        } else {
            try {
                int studentNumber = Integer.parseInt(dto.getStudentNumber().trim());
                if (userDao.isStudentNumberExists(studentNumber)) {
                    errors.add("Student number already exists");
                }
            } catch (NumberFormatException e) {
                errors.add("Student number must be a valid number");
            }
        }
        
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            errors.add("Name is required");
        } else if (!ValidationUtils.isValidName(dto.getName())) {
            errors.add(ValidationUtils.getNameValidationMessage());
        }
        
        if (dto.getSurname() == null || dto.getSurname().trim().isEmpty()) {
            errors.add("Surname is required");
        } else if (!ValidationUtils.isValidName(dto.getSurname())) {
            errors.add(ValidationUtils.getNameValidationMessage());
        }
        
        if (dto.getEmail() == null || dto.getEmail().trim().isEmpty()) {
            errors.add("Email is required");
        } else if (!ValidationUtils.isValidEmail(dto.getEmail())) {
            errors.add(ValidationUtils.getEmailValidationMessage());
        } else if (userDao.isEmailExists(dto.getEmail().trim().toLowerCase())) {
            errors.add("Email already exists");
        }
        
        if (dto.getPhone() != null && !dto.getPhone().trim().isEmpty() && 
            !ValidationUtils.isValidPhone(dto.getPhone())) {
            errors.add(ValidationUtils.getPhoneValidationMessage());
        }
        
        if (dto.getPassword() == null || dto.getPassword().isEmpty()) {
            errors.add("Password is required");
        } else if (!ValidationUtils.isValidPassword(dto.getPassword())) {
            errors.add(ValidationUtils.getPasswordValidationMessage());
        }
        
        if (dto.getConfirmPassword() == null || dto.getConfirmPassword().isEmpty()) {
            errors.add("Password confirmation is required");
        } else if (!ValidationUtils.passwordsMatch(dto.getPassword(), dto.getConfirmPassword())) {
            errors.add(ValidationUtils.getPasswordMismatchMessage());
        }
        
        return errors;
    }
}