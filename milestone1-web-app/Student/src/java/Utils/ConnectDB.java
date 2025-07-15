/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author mosam
 */
public class ConnectDB {
    private static final String URL = "jdbc:postgresql://localhost:5432/BelgiumCampus"; // Replace if needed
    private static final String USER = "postgres"; // Your PostgreSQL username
    private static final String PASSWORD = "mosa"; // Your PostgreSQL password

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver"); 
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
