package com.bc.desktop.services;

import com.bc.desktop.models.Appointment;
import com.bc.desktop.models.Counselor;
import com.bc.desktop.models.Feedback;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DatabaseService {
    private static final String JDBC_URL = "jdbc:derby:careconnect_db;create=true";
    private static DatabaseService instance;
    
    private DatabaseService() {
        // Set Derby system properties
        System.setProperty("derby.system.home", System.getProperty("user.dir"));
        System.setProperty("derby.stream.error.field", "java.lang.System.err");
        
        // Load Derby driver
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            System.out.println("Derby driver loaded successfully");
        } catch (ClassNotFoundException e) {
            System.err.println("Derby driver not found: " + e.getMessage());
            throw new RuntimeException("Derby driver not available", e);
        }
        initializeDatabase();
    }
    
    public static synchronized DatabaseService getInstance() {
        if (instance == null) {
            try {
                instance = new DatabaseService();
            } catch (Exception e) {
                System.err.println("Failed to create DatabaseService instance: " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Database initialization failed", e);
            }
        }
        return instance;
    }
    
    private void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(JDBC_URL)) {
            System.out.println("Database connection established successfully");
            createTables(conn);
            insertSampleData(conn);
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize database", e);
        }
    }
    
    private void createTables(Connection conn) throws SQLException {
        String createCounselorsTable = """
            CREATE TABLE counselors (
                id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                name VARCHAR(255) NOT NULL,
                specialization VARCHAR(255) NOT NULL,
                email VARCHAR(255) UNIQUE,
                phone VARCHAR(20),
                is_available BOOLEAN DEFAULT TRUE,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """;
        
        String createAppointmentsTable = """
            CREATE TABLE appointments (
                id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                student_number INT NOT NULL,
                counselor_id BIGINT NOT NULL,
                counselor_name VARCHAR(255) NOT NULL,
                appointment_datetime TIMESTAMP NOT NULL,
                status VARCHAR(20) DEFAULT 'SCHEDULED',
                notes CLOB,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (counselor_id) REFERENCES counselors(id)
            )
        """;
        
        String createFeedbackTable = """
            CREATE TABLE feedback (
                id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                student_number INT NOT NULL,
                rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
                comments CLOB,
                category VARCHAR(100),
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """;
        
        try (Statement stmt = conn.createStatement()) {
            try { stmt.executeUpdate(createCounselorsTable); } catch (SQLException e) {}
            try { stmt.executeUpdate(createAppointmentsTable); } catch (SQLException e) {}
            try { stmt.executeUpdate(createFeedbackTable); } catch (SQLException e) {}
        }
    }
    
    private void insertSampleData(Connection conn) throws SQLException {
        String checkCounselors = "SELECT COUNT(*) FROM counselors";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(checkCounselors)) {
            if (rs.next() && rs.getInt(1) == 0) {
                String insertCounselors = """
                    INSERT INTO counselors (name, specialization, email, phone) VALUES
                    ('Dr. Sarah Johnson', 'Mental Health', 'sarah.johnson@bc.edu', '011-123-4567'),
                    ('Dr. Michael Brown', 'Academic Support', 'michael.brown@bc.edu', '011-234-5678'),
                    ('Dr. Lisa Davis', 'Career Guidance', 'lisa.davis@bc.edu', '011-345-6789'),
                    ('Dr. Robert Wilson', 'Stress Management', 'robert.wilson@bc.edu', '011-456-7890'),
                    ('Dr. Emily Taylor', 'General Counseling', 'emily.taylor@bc.edu', '011-567-8901')
                """;
                stmt.executeUpdate(insertCounselors);
            }
        }
    }
    
    public List<Counselor> getAllCounselors() {
        List<Counselor> counselors = new ArrayList<>();
        String sql = "SELECT * FROM counselors ORDER BY name";
        
        try (Connection conn = DriverManager.getConnection(JDBC_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Counselor counselor = new Counselor();
                counselor.setId(rs.getLong("id"));
                counselor.setName(rs.getString("name"));
                counselor.setSpecialization(rs.getString("specialization"));
                counselor.setEmail(rs.getString("email"));
                counselor.setPhone(rs.getString("phone"));
                counselor.setAvailable(rs.getBoolean("is_available"));
                counselor.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                counselor.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                counselors.add(counselor);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching counselors: " + e.getMessage());
        }
        
        return counselors;
    }
    
    public List<Appointment> getAppointmentsByStudent(int studentNumber) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointments WHERE student_number = ? ORDER BY appointment_datetime DESC";
        
        try (Connection conn = DriverManager.getConnection(JDBC_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, studentNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Appointment appointment = new Appointment();
                    appointment.setId(rs.getLong("id"));
                    appointment.setStudentNumber(rs.getInt("student_number"));
                    appointment.setCounselorId(rs.getLong("counselor_id"));
                    appointment.setCounselorName(rs.getString("counselor_name"));
                    appointment.setAppointmentDateTime(rs.getTimestamp("appointment_datetime").toLocalDateTime());
                    appointment.setStatus(Appointment.Status.valueOf(rs.getString("status")));
                    appointment.setNotes(rs.getString("notes"));
                    appointment.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    appointment.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                    appointments.add(appointment);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching appointments: " + e.getMessage());
        }
        
        return appointments;
    }
    
    public boolean saveAppointment(Appointment appointment) {
        String sql = """
            INSERT INTO appointments (student_number, counselor_id, counselor_name, 
                                    appointment_datetime, status, notes) 
            VALUES (?, ?, ?, ?, ?, ?)
        """;
        
        try (Connection conn = DriverManager.getConnection(JDBC_URL);
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, appointment.getStudentNumber());
            stmt.setLong(2, appointment.getCounselorId());
            stmt.setString(3, appointment.getCounselorName());
            stmt.setTimestamp(4, Timestamp.valueOf(appointment.getAppointmentDateTime()));
            stmt.setString(5, appointment.getStatus().toString());
            stmt.setString(6, appointment.getNotes());
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        appointment.setId(generatedKeys.getLong(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error saving appointment: " + e.getMessage());
        }
        
        return false;
    }
    
    public boolean updateAppointment(Appointment appointment) {
        String sql = """
            UPDATE appointments 
            SET appointment_datetime = ?, status = ?, notes = ?, updated_at = CURRENT_TIMESTAMP 
            WHERE id = ?
        """;
        
        try (Connection conn = DriverManager.getConnection(JDBC_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setTimestamp(1, Timestamp.valueOf(appointment.getAppointmentDateTime()));
            stmt.setString(2, appointment.getStatus().toString());
            stmt.setString(3, appointment.getNotes());
            stmt.setLong(4, appointment.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating appointment: " + e.getMessage());
        }
        
        return false;
    }
    
    public List<Feedback> getFeedbackByStudent(int studentNumber) {
        List<Feedback> feedbackList = new ArrayList<>();
        String sql = "SELECT * FROM feedback WHERE student_number = ? ORDER BY created_at DESC";
        
        try (Connection conn = DriverManager.getConnection(JDBC_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, studentNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Feedback feedback = new Feedback();
                    feedback.setId(rs.getLong("id"));
                    feedback.setStudentNumber(rs.getInt("student_number"));
                    feedback.setRating(rs.getInt("rating"));
                    feedback.setComments(rs.getString("comments"));
                    feedback.setCategory(rs.getString("category"));
                    feedback.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    feedback.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                    feedbackList.add(feedback);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching feedback: " + e.getMessage());
        }
        
        return feedbackList;
    }
    
    public boolean saveFeedback(Feedback feedback) {
        String sql = "INSERT INTO feedback (student_number, rating, comments, category) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DriverManager.getConnection(JDBC_URL);
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, feedback.getStudentNumber());
            stmt.setInt(2, feedback.getRating());
            stmt.setString(3, feedback.getComments());
            stmt.setString(4, feedback.getCategory());
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        feedback.setId(generatedKeys.getLong(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error saving feedback: " + e.getMessage());
        }
        
        return false;
    }
    
    public boolean updateFeedback(Feedback feedback) {
        String sql = "UPDATE feedback SET rating = ?, comments = ?, category = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        
        try (Connection conn = DriverManager.getConnection(JDBC_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, feedback.getRating());
            stmt.setString(2, feedback.getComments());
            stmt.setString(3, feedback.getCategory());
            stmt.setLong(4, feedback.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating feedback: " + e.getMessage());
        }
        
        return false;
    }
    
    public boolean deleteFeedback(Long id) {
        String sql = "DELETE FROM feedback WHERE id = ?";
        
        try (Connection conn = DriverManager.getConnection(JDBC_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting feedback: " + e.getMessage());
        }
        
        return false;
    }
    
    // CRUD Operations for Counselors (Admin only)
    public boolean saveCounselor(Counselor counselor) {
        String sql = """
            INSERT INTO counselors (name, specialization, email, phone, is_available) 
            VALUES (?, ?, ?, ?, ?)
        """;
        
        try (Connection conn = DriverManager.getConnection(JDBC_URL);
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, counselor.getName());
            stmt.setString(2, counselor.getSpecialization());
            stmt.setString(3, counselor.getEmail());
            stmt.setString(4, counselor.getPhone());
            stmt.setBoolean(5, counselor.isAvailable());
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        counselor.setId(generatedKeys.getLong(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error saving counselor: " + e.getMessage());
            if (e.getMessage().contains("duplicate") || e.getMessage().contains("unique")) {
                throw new RuntimeException("Email address already exists");
            }
            throw new RuntimeException("Database error: " + e.getMessage());
        }
        
        return false;
    }
    
    public boolean updateCounselor(Counselor counselor) {
        String sql = """
            UPDATE counselors 
            SET name = ?, specialization = ?, email = ?, phone = ?, is_available = ?, updated_at = CURRENT_TIMESTAMP 
            WHERE id = ?
        """;
        
        try (Connection conn = DriverManager.getConnection(JDBC_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, counselor.getName());
            stmt.setString(2, counselor.getSpecialization());
            stmt.setString(3, counselor.getEmail());
            stmt.setString(4, counselor.getPhone());
            stmt.setBoolean(5, counselor.isAvailable());
            stmt.setLong(6, counselor.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating counselor: " + e.getMessage());
        }
        
        return false;
    }
    
    public boolean deleteCounselor(Long id) {
        // First check if counselor has any appointments
        String checkAppointments = "SELECT COUNT(*) FROM appointments WHERE counselor_id = ?";
        
        try (Connection conn = DriverManager.getConnection(JDBC_URL);
             PreparedStatement checkStmt = conn.prepareStatement(checkAppointments)) {
            
            checkStmt.setLong(1, id);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    System.err.println("Cannot delete counselor: has existing appointments");
                    return false;
                }
            }
            
            // If no appointments, proceed with deletion
            String sql = "DELETE FROM counselors WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, id);
                return stmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error deleting counselor: " + e.getMessage());
        }
        
        return false;
    }
    
    // Admin functions for viewing all data
    public List<Appointment> getAllAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointments ORDER BY appointment_datetime DESC";
        
        try (Connection conn = DriverManager.getConnection(JDBC_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Appointment appointment = new Appointment();
                appointment.setId(rs.getLong("id"));
                appointment.setStudentNumber(rs.getInt("student_number"));
                appointment.setCounselorId(rs.getLong("counselor_id"));
                appointment.setCounselorName(rs.getString("counselor_name"));
                appointment.setAppointmentDateTime(rs.getTimestamp("appointment_datetime").toLocalDateTime());
                appointment.setStatus(Appointment.Status.valueOf(rs.getString("status")));
                appointment.setNotes(rs.getString("notes"));
                appointment.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                appointment.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                appointments.add(appointment);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all appointments: " + e.getMessage());
        }
        
        return appointments;
    }
    
    public List<Feedback> getAllFeedback() {
        List<Feedback> feedbackList = new ArrayList<>();
        String sql = "SELECT * FROM feedback ORDER BY created_at DESC";
        
        try (Connection conn = DriverManager.getConnection(JDBC_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Feedback feedback = new Feedback();
                feedback.setId(rs.getLong("id"));
                feedback.setStudentNumber(rs.getInt("student_number"));
                feedback.setRating(rs.getInt("rating"));
                feedback.setComments(rs.getString("comments"));
                feedback.setCategory(rs.getString("category"));
                feedback.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                feedback.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                feedbackList.add(feedback);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all feedback: " + e.getMessage());
        }
        
        return feedbackList;
    }
    
    // Statistics for admin dashboard
    public int getTotalAppointments() {
        String sql = "SELECT COUNT(*) FROM appointments";
        try (Connection conn = DriverManager.getConnection(JDBC_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting appointment count: " + e.getMessage());
        }
        return 0;
    }
    
    public int getTotalCounselors() {
        String sql = "SELECT COUNT(*) FROM counselors";
        try (Connection conn = DriverManager.getConnection(JDBC_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting counselor count: " + e.getMessage());
        }
        return 0;
    }
    
    public int getTotalFeedback() {
        String sql = "SELECT COUNT(*) FROM feedback";
        try (Connection conn = DriverManager.getConnection(JDBC_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting feedback count: " + e.getMessage());
        }
        return 0;
    }
    
    public double getAverageFeedbackRating() {
        String sql = "SELECT AVG(CAST(rating AS DOUBLE)) FROM feedback";
        try (Connection conn = DriverManager.getConnection(JDBC_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting average rating: " + e.getMessage());
        }
        return 0.0;
    }
    
    // Test database connection
    public boolean testConnection() {
        try (Connection conn = DriverManager.getConnection(JDBC_URL)) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            return false;
        }
    }
    
    // Check if a counselor is available at a specific time slot
    public boolean isTimeSlotAvailable(Long counselorId, LocalDateTime appointmentTime) {
        String sql = "SELECT COUNT(*) FROM appointments WHERE counselor_id = ? AND appointment_datetime = ? AND status != 'CANCELLED'";
        
        try (Connection conn = DriverManager.getConnection(JDBC_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, counselorId);
            stmt.setTimestamp(2, Timestamp.valueOf(appointmentTime));
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0; // Available if count is 0
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking time slot availability: " + e.getMessage());
        }
        
        return false;
    }
    
    // Get available time slots for a counselor on a specific date
    public List<LocalDateTime> getAvailableTimeSlots(Long counselorId, java.time.LocalDate date) {
        List<LocalDateTime> availableSlots = new ArrayList<>();
        
        // Generate working hours slots (9 AM to 5 PM, every hour)
        for (int hour = 9; hour <= 17; hour++) {
            LocalDateTime slot = date.atTime(hour, 0);
            if (isTimeSlotAvailable(counselorId, slot)) {
                availableSlots.add(slot);
            }
        }
        
        return availableSlots;
    }
    
    // Get booked appointments for a counselor on a specific date
    public List<Appointment> getCounselorAppointments(Long counselorId, java.time.LocalDate date) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointments WHERE counselor_id = ? AND DATE(appointment_datetime) = ? AND status != 'CANCELLED' ORDER BY appointment_datetime";
        
        try (Connection conn = DriverManager.getConnection(JDBC_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, counselorId);
            stmt.setDate(2, java.sql.Date.valueOf(date));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Appointment appointment = new Appointment();
                    appointment.setId(rs.getLong("id"));
                    appointment.setStudentNumber(rs.getInt("student_number"));
                    appointment.setCounselorId(rs.getLong("counselor_id"));
                    appointment.setCounselorName(rs.getString("counselor_name"));
                    appointment.setAppointmentDateTime(rs.getTimestamp("appointment_datetime").toLocalDateTime());
                    appointment.setStatus(Appointment.Status.valueOf(rs.getString("status")));
                    appointment.setNotes(rs.getString("notes"));
                    appointment.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    appointment.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                    appointments.add(appointment);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting counselor appointments: " + e.getMessage());
        }
        
        return appointments;
    }
    
    // Shutdown database properly
    public void shutdown() {
        try {
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        } catch (SQLException e) {
            // Derby shutdown throws an exception even on successful shutdown
            if (e.getErrorCode() == 50000 && "XJ015".equals(e.getSQLState())) {
                System.out.println("Derby shutdown successfully");
            } else {
                System.err.println("Error during Derby shutdown: " + e.getMessage());
            }
        }
    }
}