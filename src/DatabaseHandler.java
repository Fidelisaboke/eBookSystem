/*
This Java class handles database operations:
1. Connection establishment
2. Connection termination
3. CRUD Operations
 */

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Objects;

public class DatabaseHandler {
    private Connection connection;
    private PreparedStatement pst;
    private ResultSet rs;
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/db_books";
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";

    private static DatabaseHandler instance = null;

    // Private constructor to prevent direct object instantiation:
    private DatabaseHandler(){
    }

    // Method for creating one instance of the DatabaseHandler:
    public static DatabaseHandler getInstance(){
        if (instance == null){
            instance = new DatabaseHandler();
        }
        return instance;
    }

    //Establish connection with the program's database:
    public void establishConnection(){
        try{
            Class.forName(DB_DRIVER);
            connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
        } catch (SQLException | ClassNotFoundException e){
            JOptionPane.showMessageDialog(null, "Error loading database. Please check if MySQL " +
                    "server is running", "Database Loading " +
                    "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }
    }

    //Close the connection:
    public void closeConnection(){
        try {
            connection.close();
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
        } catch (NullPointerException e){
            JOptionPane.showMessageDialog(null, "Error: The fields a blank. Please fill them in.",
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    //View book ids on a JComboBox:
    //Will be used to view primary keys (mostly)
    public void loadBookIDs(JComboBox<Integer> comboBox){
        try{
            String sql = "SELECT book_id FROM tbl_books";
            pst = connection.prepareStatement(sql);
            rs = pst.executeQuery();
            comboBox.removeAllItems();
            while(rs.next())
            {
                comboBox.addItem(rs.getInt(1));
            }
        } catch (SQLException e){
            JOptionPane.showMessageDialog(null, "An error has occurred when reading the database " +
                    "column.", "Column Load Error", JOptionPane.ERROR_MESSAGE);
        }

    }


    //View book names on a JComboBox:
    public void loadBookNames(JComboBox<String> comboBox){
        try{
            String sql = "SELECT book_name FROM tbl_books";
            pst = connection.prepareStatement(sql);
            rs = pst.executeQuery();
            comboBox.removeAllItems();
            while(rs.next())
            {
                comboBox.addItem(rs.getString(1));
            }
        } catch (SQLException e){
            JOptionPane.showMessageDialog(null, "An error has occurred when reading the database " +
                    "column.", "Column Load Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Read or view an entry in the books table by using the book_id:
    public void displayRecord(JComboBox<Integer> comboBox, JTextField txtName, JTextField txtGenre, JSpinner txtQuantity){
        try{
            String bookID = Objects.requireNonNull(comboBox.getSelectedItem()).toString();


            pst = connection.prepareStatement("SELECT * FROM tbl_books WHERE book_id=?");
            pst.setString(1, bookID);
            rs = pst.executeQuery();

            if(rs.next()){
                txtName.setText(rs.getString(2));
                txtGenre.setText(rs.getString(3));
                txtQuantity.setValue(rs.getObject(4));
            } else{
                JOptionPane.showMessageDialog(null, "No record found", "Record Missing", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e){
            JOptionPane.showMessageDialog(null, "An exception has occurred when trying to display " +
                    "record", "Display Record Error.", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Add a record to the books table:
    public void addRecord(String bookName, String bookGenre, int bookQuantity){
        try{
            pst = connection.prepareStatement("INSERT INTO tbl_books (book_name, book_genre, book_quantity) VALUES (?,?,?)");
            pst.setString(1, bookName);
            pst.setString(2, bookGenre);
            pst.setInt(3, bookQuantity);

            int k = pst.executeUpdate();

            if(k==1){
                JOptionPane.showMessageDialog(null, "Data has been inserted to the table.");
            } else{
                JOptionPane.showMessageDialog(null, "Error: Record has not been inserted.");
            }
        } catch (SQLException ex){
            JOptionPane.showMessageDialog(null, "Error: Record has not been inserted." ,
                    "Insert Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Change a record of the table:
    public void modifyRecord(JComboBox<Integer> comboBox, String bookName, String bookGenre, int bookQuantity){
        try{
            int bookID = Integer.parseInt(Objects.requireNonNull(comboBox.getSelectedItem()).toString());
            String sql = "UPDATE tbl_books SET book_name=?, book_genre=?, book_quantity=? WHERE book_id=?";
            pst = connection.prepareStatement(sql);

            pst.setString(1, bookName);
            pst.setString(2, bookGenre);
            pst.setInt(3, bookQuantity);
            pst.setInt(4, bookID);
            int k = pst.executeUpdate();
            if(k==1){
                JOptionPane.showMessageDialog(null, "Data has been changed successfully.",
                        "Modification Successful", JOptionPane.INFORMATION_MESSAGE);
            } else{
                JOptionPane.showMessageDialog(null, "Error: Record has not been changed.");
            }

        } catch (Exception ex){
            JOptionPane.showMessageDialog(null, "Error: Record has not been changed. Please try" +
                    " again." , "Insert Error", JOptionPane.ERROR_MESSAGE);

        }
    }

    //Delete a record:
    public void deleteRecord(JComboBox<Integer> comboBox){
        try {
            int bookID = Integer.parseInt(Objects.requireNonNull(comboBox.getSelectedItem()).toString());
            pst = connection.prepareStatement("DELETE FROM tbl_books WHERE book_id=?");
            pst.setInt(1, bookID);
            int a = pst.executeUpdate();

            if(a==1){
                JOptionPane.showMessageDialog(null, "Record has been deleted.");
            } else{
                JOptionPane.showMessageDialog(null, "Record has not been deleted.");
            }
        } catch (Exception ex){
            JOptionPane.showMessageDialog(null, "Error: Record has not been deleted. Please try" +
                    " again." , "Delete Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Display a table from the database onto a JTable
    public void displayBooksTable(DefaultTableModel tableModel){
        try {
            pst = connection.prepareStatement("SELECT * FROM tbl_books");
            rs = pst.executeQuery();
            while(rs.next()){
                int bookID = rs.getInt("book_id");
                String bookName = rs.getString("book_name");
                String bookGenre = rs.getString("book_genre");
                int bookQuantity = rs.getInt("book_quantity");
                Object[] row = {bookID, bookName, bookGenre, bookQuantity};
                tableModel.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: Failed to display the table. ",
                 "Table Display Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Refresh the table after any changes are made to the table's data:
    public void refreshTable(JTable table){
        DefaultTableModel tblModel = (DefaultTableModel) table.getModel();
        tblModel.setRowCount(0);
        displayBooksTable(tblModel);

    }

}
