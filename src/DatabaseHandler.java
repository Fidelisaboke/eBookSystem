/*
This Java class handles database operations:
1. Connection establishment
2. Connection termination
3. CRUD Operations
 */

import javax.swing.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
public class DatabaseHandler {
    private Connection connection;
    private PreparedStatement pst;
    private ResultSet rs;
    static final String USERNAME = "root";
    static final String PASSWORD = "";
    static final String DB_URL = "jdbc:mysql://localhost:3306/db_books";
    static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";

    //Establish connection with the program's database:
    public void establishConnection(){
        try{
            Class.forName(DB_DRIVER);
            connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            JOptionPane.showMessageDialog(null, "Connection with database established.");
        } catch (SQLException | ClassNotFoundException e){
            JOptionPane.showMessageDialog(null, "Error loading database.", "Database Loading " +
                    "Error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    //Close the connection:
    public void closeConnection(){
        try {
            connection.close();
            JOptionPane.showMessageDialog(null, "Database connection closed.");
        } catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Error: Failed to close database connection.",
                    "Close Connection Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    //Verifying the admin password entered:
    public boolean verifyAdminPassword(String password) throws SQLException {
        String sql = "SELECT * FROM tbl_admin WHERE admin_password = ?";
        pst = connection.prepareStatement(sql);
        pst.setString(1, password);
        rs = pst.executeQuery();
        return rs.next();
    }

    //Verifying the user's details:
    public boolean verifyUser(String username, String password) throws SQLException{
        String sql = "SELECT * FROM tbl_user WHERE username=? AND password=?";
        pst = connection.prepareStatement(sql);
        pst.setString(1, username);
        pst.setString(2, password);
        rs = pst.executeQuery();
        return rs.next();
    }

    //Register the user into the database:
    public void registerUser(String username, String password){
        try{
            String sql = "INSERT INTO tbl_user VALUES(?,?)";
            pst = connection.prepareStatement(sql);
            pst.setString(1, username);
            pst.setString(2, password);
            int a = pst.executeUpdate();

            if(a==1){
                JOptionPane.showMessageDialog(null, "User Registered.");
            } else{
                JOptionPane.showMessageDialog(null, "User not registered. Try again.");
            }
        } catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Error: Failed to register user. Maybe the " +
                            "username already exists?",
                    "Registration Error", JOptionPane.ERROR_MESSAGE);
        }

    }

}
