package com.bc.web.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database configuration and connection management for CareConnect application.
 * 
 * This class provides centralized database configuration with support for both
 * development (localhost) and production (containerized) environments. It uses
 * environment variables for production deployment and falls back to default
 * values for local development.
 * 
 * Environment Variables (Production):
 * - DB_HOST: Database host (e.g., "postgres")
 * - DB_PORT: Database port (e.g., "5432")
 * - DB_NAME: Database name (e.g., "careconnect")
 * - DB_USER: Database username (e.g., "postgres")
 * - DB_PASSWORD: Database password
 * 
 * @author CareConnect Development Team
 * @version 1.0
 * @since 1.0
 */
public class DatabaseConfig {
    
    private static final String DB_URL = System.getenv("DB_HOST") != null 
        ? "jdbc:postgresql://" + System.getenv("DB_HOST") + ":" + System.getenv("DB_PORT") + "/" + System.getenv("DB_NAME")
        : "jdbc:postgresql://localhost:5432/careconnect";
    private static final String DB_USERNAME = System.getenv("DB_USER") != null ? System.getenv("DB_USER") : "postgres";
    private static final String DB_PASSWORD = System.getenv("DB_PASSWORD") != null ? System.getenv("DB_PASSWORD") : "careconnect2024";
    
    private static final String DRIVER_CLASS = "org.postgresql.Driver";
    
    static {
        try {
            Class.forName(DRIVER_CLASS);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgreSQL JDBC Driver not found", e);
        }
    }
    
    /**
     * Establishes a connection to the PostgreSQL database.
     * 
     * This method creates a new database connection using the configured
     * connection parameters. It logs the connection attempt for debugging
     * purposes in development and production environments.
     * 
     * @return A Connection object to the database
     * @throws SQLException if a database access error occurs or the connection fails
     */
    public static Connection getConnection() throws SQLException {
        System.out.println("Attempting to connect to database: " + DB_URL);
        System.out.println("Database user: " + DB_USERNAME);
        return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
    }
    
    /**
     * Safely closes a database connection.
     * 
     * This method handles the proper cleanup of database connections,
     * ensuring that resources are freed and no connection leaks occur.
     * It gracefully handles null connections and logs any errors.
     * 
     * @param connection The Connection object to close (can be null)
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            }
        }
    }
    
    /**
     * Tests the database connection to verify connectivity.
     * 
     * This method is primarily used for health checks and application
     * startup verification. It creates a temporary connection and
     * immediately closes it to test connectivity without holding resources.
     * 
     * @return true if the connection is successful and not closed, false otherwise
     */
    public static boolean testConnection() {
        try (Connection connection = getConnection()) {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            return false;
        }
    }
}