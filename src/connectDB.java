import javax.swing.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
public class connectDB {
    private Connection connection;
    private PreparedStatement pst;
    private ResultSet rs;
    static final String USERNAME = "root";
    static final String PASSWORD = "";
    static final String DB_URL = "jdbc:mysql://localhost:3306/db_books";
    static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";

    //Establish connection with the program's database:
    public connectDB(){
        try{
            Class.forName(DB_DRIVER);
            connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
        } catch (SQLException | ClassNotFoundException e){
            JOptionPane.showMessageDialog(null, "Error loading database.", "Database Loading " +
                    "Error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(connectDB.class.getName()).log(Level.SEVERE, null, e);
        }

    }

    //Verifying the admin password entered and returning 'true' or 'false' depending on what is entered:
    public boolean verifyAdminPassword(String password) throws SQLException {
        String sql = "SELECT * FROM tbl_admin WHERE admin_password = ?";
        pst = connection.prepareStatement(sql);
        pst.setString(1, password);
        ResultSet result = pst.executeQuery();
        return result.next();
    }

}
