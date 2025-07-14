/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import org.apache.derby.iapi.sql.PreparedStatement;

/**
 *
 * @author itumo
 */
public class DBConnection {
    private static final String DRiver = "org.derby.jdbc.EmbeddedDriver";
    private static final String JDBC_URL = "jdbc:derby:studentDB;create=true";
    
    Connection conn;
    
    public void connect() throws ClassNotFoundException{
    try{
        Class.forName(DRiver);
        this.conn = (Connection) DriverManager.getConnection(JDBC_URL);
        if(conn != null)
        {System.err.println("Connected to database");}
    }catch(SQLException ex)
    {
        ex.printStackTrace();
    }
    }
    public void createTable(){
        try{
            String query = "Create Table Student (StudentID VARCHAR(20),"
                    +"Name VARCHAR(20), Surname VARCHAR(20),"
                    + "Councelor VARCHAR(20), Date DATE, Time TIME)";
            conn.createStatement().execute(query);
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }
    
    public  void add(String id, String name, String surname,String counselor, String date, String time)
    {
        try{
            String query = "INSERT INTO Student VALUES('"+id+"','"+name+"','"+surname
                    +"','"+counselor+"','"+date+"','"+time+"')'";
                    conn.createStatement().execute(query);
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
    }
    public void delete()
    {
        String query = "DELETE FROM Student WHERE StudentID = ?";
        try(java.sql.PreparedStatement ps = conn.prepareStatement(query)){
            ps.setString(1, "id");
            int row = ps.executeUpdate();
            if(row >0){
                System.err.println("Student deleted");
            }    
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
    }
}
