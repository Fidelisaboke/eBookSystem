import javax.swing.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
public class connectDB {
    private Connection connection;
    static final String USERNAME = "root";
    static final String PASSWORD = "";
    static final String DB_URL = "jdbc:mysql://localhost:3306/dbBooks";
    static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";

    //Establish connection with the program's database:
    public connectDB(){
        try{
            Class.forName(DB_DRIVER);
            connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
        } catch (SQLException | ClassNotFoundException e){
            JOptionPane.showMessageDialog(null, "Error loading database", "Database Loading Error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(connectDB.class.getName()).log(Level.SEVERE, null, e);
        }

    }

}
