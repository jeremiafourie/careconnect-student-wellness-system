package com.bc.web.servlets;

import com.bc.web.config.DatabaseConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

@WebServlet("/health")
public class HealthServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        
        out.println("CareConnect Health Check");
        out.println("========================");
        out.println("Time: " + new java.util.Date());
        out.println("Version: Session Management Fix v2");
        out.println("Build: " + System.currentTimeMillis());
        out.println();
        
        // Check database connectivity
        out.println("Database Connection Test:");
        try {
            boolean dbConnected = DatabaseConfig.testConnection();
            if (dbConnected) {
                out.println("✓ Database connection: SUCCESS");
                
                // Test actual query
                try (Connection conn = DatabaseConfig.getConnection()) {
                    // Check if users table exists
                    var tableCheckStmt = conn.prepareStatement(
                        "SELECT EXISTS (SELECT FROM information_schema.tables WHERE table_name = 'users')"
                    );
                    var tableCheckRs = tableCheckStmt.executeQuery();
                    if (tableCheckRs.next() && tableCheckRs.getBoolean(1)) {
                        out.println("✓ Users table exists");
                        
                        // Count users
                        var countStmt = conn.prepareStatement("SELECT COUNT(*) FROM users");
                        var countRs = countStmt.executeQuery();
                        if (countRs.next()) {
                            int userCount = countRs.getInt(1);
                            out.println("✓ Users table accessible: " + userCount + " users found");
                        }
                    } else {
                        out.println("✗ Users table does not exist - schema initialization failed");
                    }
                } catch (Exception e) {
                    out.println("✗ Users table error: " + e.getMessage());
                }
            } else {
                out.println("✗ Database connection: FAILED");
            }
        } catch (Exception e) {
            out.println("✗ Database connection error: " + e.getMessage());
            e.printStackTrace(out);
        }
        
        out.println();
        out.println("Environment Variables:");
        out.println("DB_HOST: " + System.getenv("DB_HOST"));
        out.println("DB_PORT: " + System.getenv("DB_PORT"));
        out.println("DB_NAME: " + System.getenv("DB_NAME"));
        out.println("DB_USER: " + System.getenv("DB_USER"));
        out.println("DB_PASSWORD: " + (System.getenv("DB_PASSWORD") != null ? "[SET]" : "[NOT SET]"));
    }
}