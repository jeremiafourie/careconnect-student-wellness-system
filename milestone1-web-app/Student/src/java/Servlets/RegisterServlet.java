package Servlets;

import Utils.ConnectDB;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "RegisterServlet", urlPatterns = {"/RegisterServlet"})
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get form data
        String name = request.getParameter("studentName");
        String surname = request.getParameter("studentSurname");
        String email = request.getParameter("emailAddress");
        String course = request.getParameter("course");
        String password = request.getParameter("createPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            request.setAttribute("message", "Passwords do not match.");
            request.getRequestDispatcher("/Views/register.jsp").forward(request, response);
            return;
        }

        // Try connecting and inserting
        try (Connection conn = ConnectDB.getConnection()) {

            // 1. Check if email already exists
            String checkSql = "SELECT 1 FROM users WHERE email = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, email);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                request.setAttribute("message", " Email is already registered.");
                request.getRequestDispatcher("/Views/register.jsp").forward(request, response);
                return;
            }

            // 2. Insert new user
            String insertSql = "INSERT INTO users (name, surname, email, course, password) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertSql);
            insertStmt.setString(1, name);
            insertStmt.setString(2, surname);
            insertStmt.setString(3, email);
            insertStmt.setString(4, course);
            insertStmt.setString(5, password); // Plain text for now

            int rowsInserted = insertStmt.executeUpdate();

            if (rowsInserted > 0) {
                request.setAttribute("message", "Registration successful!");
            } else {
                request.setAttribute("message", " Registration failed. Please try again.");
            }

            request.getRequestDispatcher("/Views/register.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("message", "❌ Database error: " + e.getMessage());
            request.getRequestDispatcher("/Views/register.jsp").forward(request, response);
        }
    }
}


