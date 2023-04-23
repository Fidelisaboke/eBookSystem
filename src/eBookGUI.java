/*
This class is where the GUI of the program is designed, mainly focusing on the frame and its contents, as well as
various events that take place depending on the user's interaction with the GUI.
 */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.Objects;

public class eBookGUI extends JFrame implements ActionListener{
    //Components from the form are initialized here
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
    private JComboBox<Integer> txtBookID;
    private JTextField txtBookName;
    private JTextField txtBookGenre;
    private JButton btnManageCatalogBack;
    private JButton btnInsert;
    private JSpinner txtQuantity;
    private JButton btnUserMenuBack;

    //Regular expression that checks the username entered during registration
    String username_regex = "^[a-zA-Z0-9]{1,16}$";

    //Instantiate the "DatabaseHandler" class to auto-connect to the DB and use existing functions:
    DatabaseHandler db = new DatabaseHandler();

    public eBookGUI(){
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
        //Adding the containerPanel:
        this.add(containerPanel);

        //OTHER COMPONENTS:
        //Limiting the values of the spinner(s) in the program:
        SpinnerModel model = new SpinnerNumberModel(1, 1, 100, 1);
        txtQuantity.setModel(model);

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

        //'Modelling' the tables:
        DefaultTableModel modelBooks = createBooksTableModel();

        //tblAvailableBooks:
        initializeTable(tblAvailableBooks, modelBooks);
        db.displayTable(modelBooks);//display data onto the table



        //WindowListener for when the 'X' on the frame is clicked:
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

    //Getting the layout of containerPanel, setting it to 'cl' for easier navigation.
    CardLayout cl = (CardLayout)(containerPanel.getLayout());

    //FUNCTIONS:
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
        txtQuantity.setValue(0);
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
            db.viewColumn(txtBookID);
        } else if(e.getSource()==btnLoanedBooks){
            JOptionPane.showMessageDialog(eBookGUI.this, "Feature still in development!");
        }


        //MANAGE CATALOG PANEL:
        else if(e.getSource()==btnManageCatalogBack){
            clearManageCatalogFields();
            showHomePanel();
        }
        else if(e.getSource()==btnManageCatalogSearch){
            db.displayRecord(txtBookID, txtBookName, txtBookGenre, txtQuantity);
        } else if(e.getSource()==btnInsert){

            try {
                String bookName = txtBookName.getText();
                String bookGenre = txtBookGenre.getText();
                int bookQuantity = (int) txtQuantity.getValue();
                db.addRecord(bookName, bookGenre, bookQuantity);
                db.refreshTable(tblAvailableBooks);
                db.viewColumn(txtBookID);
            } catch (Exception ex){
                JOptionPane.showMessageDialog(eBookGUI.this, "An error has been encountered.\n" +
                        "Please check your input and try again.", "Insert Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if(e.getSource()==btnModify){
            String bookName = txtBookName.getText();
            String bookGenre = txtBookGenre.getText();
            int bookQuantity = (int) txtQuantity.getValue();
            db.modifyRecord(txtBookID, bookName, bookGenre, bookQuantity);
            db.refreshTable(tblAvailableBooks);
        } else if(e.getSource()==btnDelete) {
            db.deleteRecord(txtBookID);
            db.refreshTable(tblAvailableBooks);
        }


        //USER MENU PANEL:
        else if(e.getSource()==btnUserMenuBack){
            showHomePanel();
        }
    }




}
