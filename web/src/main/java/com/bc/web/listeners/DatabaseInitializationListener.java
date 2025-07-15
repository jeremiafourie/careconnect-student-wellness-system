package com.bc.web.listeners;

import com.bc.web.config.DatabaseConfig;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;

@WebListener
public class DatabaseInitializationListener implements ServletContextListener {
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Initializing database schema...");
        
        try {
            // Check if users table exists
            if (!tableExists("users")) {
                System.out.println("Users table not found. Creating database schema...");
                createDatabaseSchema();
                System.out.println("Database schema created successfully.");
            } else {
                System.out.println("Users table already exists. Schema is up to date.");
            }
            
        } catch (Exception e) {
            System.err.println("Failed to initialize database schema: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    
    private boolean tableExists(String tableName) {
        try (Connection conn = DatabaseConfig.getConnection()) {
            var stmt = conn.prepareStatement(
                "SELECT EXISTS (SELECT FROM information_schema.tables WHERE table_name = ?)"
            );
            stmt.setString(1, tableName);
            var rs = stmt.executeQuery();
            return rs.next() && rs.getBoolean(1);
        } catch (Exception e) {
            System.err.println("Error checking table existence: " + e.getMessage());
            return false;
        }
    }
    
    private void createDatabaseSchema() throws Exception {
        // Read schema from classpath
        InputStream schemaStream = getClass().getClassLoader().getResourceAsStream("database_schema.sql");
        if (schemaStream == null) {
            throw new RuntimeException("database_schema.sql not found in classpath");
        }
        
        StringBuilder schemaBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(schemaStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Skip comments and empty lines
                if (!line.trim().startsWith("--") && !line.trim().isEmpty()) {
                    schemaBuilder.append(line).append("\n");
                }
            }
        }
        
        String schema = schemaBuilder.toString();
        
        // Execute schema
        try (Connection conn = DatabaseConfig.getConnection()) {
            // Split by semicolon and execute each statement
            String[] statements = schema.split(";");
            for (String sql : statements) {
                sql = sql.trim();
                if (!sql.isEmpty()) {
                    try (Statement stmt = conn.createStatement()) {
                        stmt.execute(sql);
                    }
                }
            }
        }
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Application context destroyed");
    }
}