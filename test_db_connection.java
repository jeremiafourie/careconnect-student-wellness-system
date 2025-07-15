import java.sql.*;

public class test_db_connection {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/careconnect";
        String username = "postgres";
        String password = "careconnect2024";
        
        try {
            // Load the PostgreSQL JDBC driver
            Class.forName("org.postgresql.Driver");
            
            // Establish connection
            Connection conn = DriverManager.getConnection(url, username, password);
            
            if (conn != null) {
                System.out.println("✅ Database connection successful!");
                
                // Test the users table
                String sql = "SELECT COUNT(*) FROM users";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                
                if (rs.next()) {
                    System.out.println("✅ Users table exists and is accessible");
                    System.out.println("   Current user count: " + rs.getInt(1));
                }
                
                // Test table structure
                sql = "SELECT column_name, data_type FROM information_schema.columns WHERE table_name = 'users' ORDER BY ordinal_position";
                rs = stmt.executeQuery(sql);
                
                System.out.println("✅ Users table structure:");
                while (rs.next()) {
                    System.out.println("   - " + rs.getString("column_name") + " (" + rs.getString("data_type") + ")");
                }
                
                conn.close();
                System.out.println("✅ Database connection closed successfully");
            } else {
                System.out.println("❌ Failed to connect to database");
            }
        } catch (Exception e) {
            System.out.println("❌ Database connection error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}