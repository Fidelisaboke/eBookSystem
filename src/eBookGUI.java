/*
This class is where the GUI of the program is designed, mainly focusing on the frame and its contents, as well as
various events that take place depending on the user's interaction with the GUI.
 */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.PrinterException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Objects;
import java.util.Vector;

public class eBookGUI extends JFrame implements ActionListener{
    //Components from the form are initialized here
    private static eBookGUI frame_instance = null;
    private JPanel containerPanel;
    private JPanel homePanel;
    private JPanel adminLoginPanel;
    private JPanel userLoginPanel;
    private JButton btnAdminLogin;
    private JButton btnUserLogin;
    private JTextField txtUserUsername;
    private JPasswordField pwdUser;
    private JPasswordField pwdAdmin;
    private JButton btnRegister;
    private JPanel userRegisterPanel;
    private JTextField txtRegisterUsername;
    private JPasswordField pwdRegister;
    private JButton btnRegisterConfirm;
    private JButton btnRegisterClear;
    private JButton btnRegisterBack;
    private JButton btnUserConfirm;
    private JButton btnUserClear;
    private JButton btnUserBack;
    private JPanel adminMenuPanel;
    private JPanel userMenuPanel;
    private JButton btnAdminConfirm;
    private JButton btnAdminBack;
    private JButton btnAvailableBooks;
    private JButton btnLoanedBooks;
    private JPanel manageCatalogPanel;
    private JButton btnModify;
    private JButton btnDelete;
    private JTable tblAvailableBooks;
    private JButton btnManageCatalogSearch;
    private JComboBox<Integer> listBookID;
    private JTextField txtBookName;
    private JTextField txtBookGenre;
    private JButton btnManageCatalogBack;
    private JButton btnInsert;
    private JSpinner txtQuantity;
    private JButton btnUserMenuBack;
    private JButton btnUserMenuCatalog;
    private JPanel catalogPanel;
    private JTable tblCatalog;
    private JTable tblSelectedBooks;
    private JButton btnCatalogBack;
    private JComboBox<String> listBookName;
    private JButton btnCatalogClear;
    private JButton btnCatalogCart;
    private JButton btnCatalogConfirm;
    private JSpinner txtCatalogQty;
    private JPanel confirmPanel;
    private JTextArea txtReceipt;
    private JButton btnConfirmBack;
    private JButton btnConfirmPrint;
    private JButton btnConfirmExit;

    //Regular expression that checks the username entered during registration
    String username_regex = "^[a-zA-Z0-9]{1,16}$";

    //Creating only one instance of the "DatabaseHandler" class to auto-connect to the DB and use existing functions:
    DatabaseHandler db = DatabaseHandler.getInstance();

    //The private constructor for creating the frame and adding the components of the frame:
    private eBookGUI(){
        //Opening the DB Connection:
        db.establishConnection();

        //CREATING THE FRAME
        //Setting the icon for the frame:
        ImageIcon icon = new ImageIcon("icon_book.png");
        this.setIconImage(icon.getImage());

        //Creating the program window:
        this.setVisible(true);
        this.setTitle("eBook System");
        this.setSize(580, 420);
        this.setResizable(false);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setLocationRelativeTo(null); //Centering the frame


        //Adding other panels to the containerPanel which is set to cardLayout:
        containerPanel.add("Home Panel", homePanel);
        containerPanel.add("Admin Login Panel", adminLoginPanel);
        containerPanel.add("User Login Panel", userLoginPanel);
        containerPanel.add("User Register Panel", userRegisterPanel);
        containerPanel.add("Admin Menu Panel", adminMenuPanel);
        containerPanel.add("User Menu Panel", userMenuPanel);
        containerPanel.add("Manage Catalog Panel", manageCatalogPanel);
        containerPanel.add("Catalog Panel", catalogPanel);
        containerPanel.add("Confirm Panel", confirmPanel);

        //Adding the containerPanel:
        this.add(containerPanel);

        //OTHER COMPONENTS:
        //Limiting the values of the spinner(s) in the program:
        SpinnerModel model = new SpinnerNumberModel(1, 1, 100, 1);
        txtQuantity.setModel(model);
        txtCatalogQty.setModel(model);

        //ActionListeners for the buttons:
        //Home Panel:
        btnAdminLogin.addActionListener(this);
        btnUserLogin.addActionListener(this);
        btnRegister.addActionListener(this);

        //Admin Login Panel:
        btnAdminBack.addActionListener(this);
        btnAdminConfirm.addActionListener(this);
        btnUserBack.addActionListener(this);

        //User Login Panel:
        btnUserClear.addActionListener(this);
        btnUserConfirm.addActionListener(this);

        //User Register Panel:
        btnRegisterBack.addActionListener(this);
        btnRegisterClear.addActionListener(this);
        btnRegisterConfirm.addActionListener(this);

        //Admin Menu Panel:
        btnAvailableBooks.addActionListener(this);
        btnLoanedBooks.addActionListener(this);

        //Manage Catalog Panel:
        btnManageCatalogBack.addActionListener(this);
        btnManageCatalogSearch.addActionListener(this);
        btnInsert.addActionListener(this);
        btnModify.addActionListener(this);
        btnDelete.addActionListener(this);

        //User Menu Panel:
        btnUserMenuBack.addActionListener(this);
        btnUserMenuCatalog.addActionListener(this);

        //Catalog Panel:
        btnCatalogBack.addActionListener(this);
        btnCatalogCart.addActionListener(this);
        btnCatalogClear.addActionListener(this);
        btnCatalogConfirm.addActionListener(this);

        //Confirm panel:
        btnConfirmBack.addActionListener(this);
        btnConfirmPrint.addActionListener(this);
        btnConfirmExit.addActionListener(this);

        //'Modelling' the tables:
        DefaultTableModel modelCatalog = createBooksTableModel();
        DefaultTableModel modelSelectedBooks = createSelectedBooksTableModel();

        //tblAvailableBooks:
        initializeTable(tblAvailableBooks, modelCatalog);
        initializeTable(tblCatalog, modelCatalog);
        initializeTable(tblSelectedBooks, modelSelectedBooks);

        //Display data onto the table:
        db.displayBooksTable(modelCatalog);

        //WindowListener for when the 'X' on the frame is clicked.
        //A dialog pops up when it's clicked, asking the user whether to close the program or not:
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                String[] Options ={"Yes", "No"};
                int OptionSelection = JOptionPane.showOptionDialog(null,
                        "Do you want to close the program?", "Confirm Exit",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null,
                        Options,Options[1]);
                if(OptionSelection==0)
                {
                    try{
                        db.closeConnection();
                        System.exit(0);
                    } catch (Exception ex){
                        JOptionPane.showMessageDialog(eBookGUI.this, "Error encountered in closing " +
                                "connection");
                        System.exit(0);
                    }
                    System.exit(0);
                }
            }
        });

    }

    // Creating the getInstance() method to create only one object of eBookGUI:
    public static void getInstance(){
        if (frame_instance == null){
            frame_instance = new eBookGUI();
        }
    }

    //Getting the layout of containerPanel, setting it to 'cl' for easier navigation.
    CardLayout cl = (CardLayout)(containerPanel.getLayout());

    //FUNCTIONS:
    //Getting the current system date and time:
    LocalDateTime current = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String currentDateTime = current.format(formatter);

    //Showing the panels:
    public void showHomePanel(){
        cl.show(containerPanel, "Home Panel");
    }
    public void showAdminLoginPanel(){
        cl.show(containerPanel, "Admin Login Panel");
    }
    public void showUserLoginPanel(){
        cl.show(containerPanel, "User Login Panel");
    }
    public void showUserRegisterPanel(){
        cl.show(containerPanel, "User Register Panel");
    }
    public void showAdminMenuPanel(){
        cl.show(containerPanel, "Admin Menu Panel");
    }
    public void showUserMenuPanel(){
        cl.show(containerPanel, "User Menu Panel");
    }
    public void showManageCatalogPanel(){
        cl.show(containerPanel, "Manage Catalog Panel");
    }
    public void showCatalogPanel() {
        cl.show(containerPanel, "Catalog Panel");
    }
    public void showConfirmPanel(){
        cl.show(containerPanel, "Confirm Panel");
    }



    //Clearing fields on panels:
    public void clearUserLoginFields(){
        txtUserUsername.setText("");
        pwdUser.setText("");
    }

    public void clearUserRegisterFields(){
        txtRegisterUsername.setText("");
        pwdRegister.setText("");
    }

    public void clearAdminPasswordField(){
        pwdAdmin.setText("");
    }
    public void clearManageCatalogFields(){
        txtBookName.setText("");
        txtBookGenre.setText("");
        txtQuantity.setValue(1);
    }

    //TABLE FUNCTIONS:
    //Initialize common properties:
    public void initializeTable(JTable table, DefaultTableModel tableModel){
        table.setModel(tableModel);
        table.setEnabled(false);//to make the table read-only
        table.getTableHeader().setReorderingAllowed(false);//To prevent reordering of columns
    }
    //Set the model for tableBooks:
    public DefaultTableModel createBooksTableModel(){
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Book_ID");
        tableModel.addColumn("Book_Name");
        tableModel.addColumn("Book_Genre");
        tableModel.addColumn("Quantity");

        return tableModel;
    }

    public DefaultTableModel createSelectedBooksTableModel(){
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Book_Name");
        tableModel.addColumn("Book_Genre");
        tableModel.addColumn("Quantity");

        return tableModel;
    }
    //Transfer a record from one table to another:
    //Add records from Available Books to Selected books
    public void addRecordToSelection() {
        String selectedBook = Objects.requireNonNull(listBookName.getSelectedItem()).toString();
        int rowCount = tblCatalog.getRowCount();

        for (int row = 0; row < rowCount; row++) {
            Object value = tblCatalog.getValueAt(row, 1);
            if (value != null && value.toString().equals(selectedBook)) {
                Object bookNameValue = tblCatalog.getValueAt(row, 1);
                Object bookGenreValue = tblCatalog.getValueAt(row, 2);

                int initialBookQty = (int) tblCatalog.getValueAt(row, 3);

                //Checking if the variables have a value other than null and if a book exists in the tblSelectedBooks:
                if (bookNameValue != null && bookGenreValue != null && isBookNotSelected()) {
                    Object bookQuantityValue = txtCatalogQty.getValue();
                    int selectedBookQty = (int) bookQuantityValue;
                    int remainingQty = initialBookQty - selectedBookQty;

                    if(remainingQty<0 ){
                        JOptionPane.showMessageDialog(eBookGUI.this, "Quantity selected exceeds " +
                                "quantity available. Please try again.");
                    } else{
                        Vector<Object> newRowData = new Vector<>(Arrays.asList(bookNameValue, bookGenreValue, bookQuantityValue));
                        addRowToTable(newRowData);
                    }
                } else{
                    JOptionPane.showMessageDialog(eBookGUI.this, "Book has already been selected.");
                }
                break;
            }
        }
    }


    //Add row to existing tblSelectedBooks
    public void addRowToTable(Vector<Object> rowData) {
        DefaultTableModel tableModel = (DefaultTableModel) tblSelectedBooks.getModel();
        tableModel.addRow(rowData);
    }

    //Clearing all entries of a table:
    public void clearTable(JTable table){
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        tableModel.setRowCount(0);
    }

    //Check if a book exists in tblSelectedBooks:
    public boolean isBookNotSelected(){
        String selectedBook = Objects.requireNonNull(listBookName.getSelectedItem()).toString();
        int rowCount = tblSelectedBooks.getRowCount();
        for (int row = 0; row < rowCount; row++) {
            Object value = tblSelectedBooks.getValueAt(row, 0);
            if(value.toString().equals(selectedBook)){
                return false;
            }
        }
        return true;
    }

    //Option dialog for exiting directly to the home page:
    public void exitToHomePage(){
        String[] Options ={"Yes", "No"};
        int OptionSelection = JOptionPane.showOptionDialog(null,
                "Do you want to go back to the home page?", "Confirm Exit",
                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null,
                Options,Options[1]);
        if(OptionSelection==0){
            showHomePanel();
        }
    }

    //Create the receipt (Add entries from a JTable to a JTextArea)
    //This function loops through a table and appends the value to the text area (row by column):
    public void buildReceipt(JTable table, JTextArea area){
        area.setText(currentDateTime+"\n");
        area.append("Book Name"+"\t"+"Book Genre"+"\t"+"Book Quantity"+"\n");
        for (int row = 0; row < table.getRowCount(); row++) {
            for (int col = 0; col < table.getColumnCount(); col++) {
                Object value = table.getValueAt(row, col);
                area.append(value.toString() + "\t");
            }
            area.append("\n");
        }
        area.append("\nTotal no. of books: "+table.getRowCount());
    }

    // A function that prints the contents of the selected books on a text area:

    public void printContent(JTextArea area){
        try {
            boolean printed = area.print();
            if (printed) {
                JOptionPane.showMessageDialog(eBookGUI.this, "Receipt printed successfully!");
            } else {
                JOptionPane.showMessageDialog(eBookGUI.this, "Printing has been cancelled.");
            }
        } catch (PrinterException ex) {
            JOptionPane.showMessageDialog(eBookGUI.this, "An error occured when printing");
        }
    }



    //actionPerformed for button clicked events
    //It is meant to cover all the buttons of the program:
    @Override
    public void actionPerformed(ActionEvent e) {


        //HOME PANEL:
        if (e.getSource()==btnAdminLogin) {
            showAdminLoginPanel();
        } else if (e.getSource()==btnUserLogin){
            showUserLoginPanel();
        } else if (e.getSource()== btnRegister){
            showUserRegisterPanel();
        }


        //ADMIN LOGIN PANEL:
        else if (e.getSource()==btnAdminBack){
            clearAdminPasswordField();
            showHomePanel();
        } else if (e.getSource()==btnAdminConfirm){

            String password = new String(pwdAdmin.getPassword());
            try {
                if(db.verifyAdminPassword(password)) {
                    JOptionPane.showMessageDialog(  eBookGUI.this  , "Login successful!");
                    clearAdminPasswordField();
                    showAdminMenuPanel();
                }
                else{
                    JOptionPane.showMessageDialog(eBookGUI.this, "Login failed. Check password and" +
                            " try again.", "Incorrect Password", JOptionPane.ERROR_MESSAGE);
                    clearAdminPasswordField();
                }
            } catch (SQLException|NullPointerException ex) {
                JOptionPane.showMessageDialog(eBookGUI.this, "Error encountered in verifying " +
                        "password.", "Verification Error", JOptionPane.ERROR_MESSAGE);
            }
        }


        //USER LOGIN PANEL:
        else if (e.getSource()==btnUserBack){
            clearUserLoginFields();
            showHomePanel();
        } else if (e.getSource()==btnUserClear){
            clearUserLoginFields();
        } else if (e.getSource()==btnUserConfirm){

            String username = txtUserUsername.getText();
            String password = new String(pwdUser.getPassword());

            try{
                if(db.verifyUser(username, password))
                {
                    JOptionPane.showMessageDialog(eBookGUI.this, "Login successful!", "Login success",
                            JOptionPane.INFORMATION_MESSAGE);
                    clearUserLoginFields();
                    showUserMenuPanel(); //Will be developed 'soon.'
                } else {
                    JOptionPane.showMessageDialog(eBookGUI.this, "Incorrect username or password.",
                            "Login Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NullPointerException | SQLException ex){
                JOptionPane.showMessageDialog(eBookGUI.this, "An error occurred when verifying details.",
                        "User Verification Error", JOptionPane.ERROR_MESSAGE);
            }

        }


        //USER REGISTER PANEL:
        else if (e.getSource()==btnRegisterBack){
            clearUserRegisterFields();
            showHomePanel();
        } else if (e.getSource()==btnRegisterClear){
            clearUserRegisterFields();
        } else if (e.getSource()==btnRegisterConfirm){
            String username = txtRegisterUsername.getText();
            String password = new String(pwdRegister.getPassword());

            if ((Objects.equals(username, "") || Objects.equals(password, ""))){
                JOptionPane.showMessageDialog(eBookGUI.this, "Username and password cannot be blank!");
            } else{
                if (username.matches(username_regex)) {
                    db.registerUser(username, password);
                    clearUserRegisterFields();
                    showHomePanel();
                } else{
                    JOptionPane.showMessageDialog(  eBookGUI.this, "Please ensure that the username has" +
                            " numbers and\nletters only, and is only up to 16 characters.");
                }
            }
        }


        //ADMIN MENU PANEL:
        else if(e.getSource()==btnAvailableBooks){
            showManageCatalogPanel();
            db.loadBookIDs(listBookID);
        } else if(e.getSource()==btnLoanedBooks){
            JOptionPane.showMessageDialog(eBookGUI.this, "Feature still in development!");
        }


        //MANAGE CATALOG PANEL:
        else if(e.getSource()==btnManageCatalogBack){
            clearManageCatalogFields();
            showHomePanel();
        }
        else if(e.getSource()==btnManageCatalogSearch){
            db.displayRecord(listBookID, txtBookName, txtBookGenre, txtQuantity);
        } else if(e.getSource()==btnInsert){

            try {
                String bookName = txtBookName.getText();
                String bookGenre = txtBookGenre.getText();
                int bookQuantity = (int) txtQuantity.getValue();
                if (bookName.equals("") || bookGenre.equals("")){
                    JOptionPane.showMessageDialog(eBookGUI.this, "The fields are blank!");
                } else{
                    db.addRecord(bookName, bookGenre, bookQuantity);
                    db.refreshTable(tblAvailableBooks);
                    db.loadBookIDs(listBookID);
                }
            } catch (Exception ex){
                JOptionPane.showMessageDialog(eBookGUI.this, "An error has been encountered.\n" +
                        "Please check your input and try again.", "Insert Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if(e.getSource()==btnModify){
            String bookName = txtBookName.getText();
            String bookGenre = txtBookGenre.getText();
            int bookQuantity = (int) txtQuantity.getValue();
            db.modifyRecord(listBookID, bookName, bookGenre, bookQuantity);
            db.refreshTable(tblAvailableBooks);
        } else if(e.getSource()==btnDelete) {
            db.deleteRecord(listBookID);
            db.refreshTable(tblAvailableBooks);
        }


        //USER MENU PANEL:
        else if(e.getSource()==btnUserMenuBack){
            showHomePanel();
        } else if(e.getSource()==btnUserMenuCatalog){
            showCatalogPanel();
            db.loadBookNames(listBookName);
        }


        //CATALOG PANEL:
        else if(e.getSource()==btnCatalogBack){
            showUserMenuPanel();
        } else if(e.getSource()==btnCatalogCart){
            addRecordToSelection();
        } else if(e.getSource()==btnCatalogClear){
            clearTable(tblSelectedBooks);
        } else if(e.getSource()==btnCatalogConfirm){
            if(tblSelectedBooks.getRowCount()>0){
                //txtReceipt.setText("Book Name"+"*****"+"Book Genre"+"*****"+"Book Quantity"+"\n");
                showConfirmPanel();
                buildReceipt(tblSelectedBooks, txtReceipt);
            } else{
                JOptionPane.showMessageDialog(eBookGUI.this, "Please make a selection.");
            }
        }


        //CONFIRM PANEL:
        else if(e.getSource()==btnConfirmBack){
            showCatalogPanel();
        } else if(e.getSource()==btnConfirmPrint){
            printContent(txtReceipt);
            System.out.println("Printing...");
        } else if(e.getSource()==btnConfirmExit){
            exitToHomePage();
            clearTable(tblSelectedBooks);
        }
    }




}
