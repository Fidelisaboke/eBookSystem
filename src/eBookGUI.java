import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

public class eBookGUI extends JFrame implements ActionListener{
    //Components from the form are initialized here
    private JPanel containerPanel;
    private JPanel homePanel;
    private JPanel adminLoginPanel;
    private JPanel userLoginPanel;
    private JButton btnAdminLogin;
    private JButton btnUserLogin;
    private JTextField txtUserUsername;
    private JTextField pwdUser;
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


    //Instantiate the "connectDB" class to auto-connect to the DB and use existing functions:
    connectDB db = new connectDB();
    public eBookGUI(){
        //CREATING THE FRAME
        //Setting the icon for the frame:
        ImageIcon icon = new ImageIcon("icon_book.png");
        this.setIconImage(icon.getImage());

        //Creating the program window:
        this.setVisible(true);
        this.setTitle("eBook System");
        this.setSize(640, 360);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);


        //Adding other panels to the containerPanel which is set to cardLayout:
        containerPanel.add("Home Panel", homePanel);
        containerPanel.add("Admin Login Panel", adminLoginPanel);
        containerPanel.add("User Login Panel", userLoginPanel);
        containerPanel.add("User Register Panel", userRegisterPanel);
        containerPanel.add("Admin Menu Panel", adminMenuPanel);
        containerPanel.add("User Menu Panel", userMenuPanel);

        //Adding the containerPanel:
        this.add(containerPanel);

        //ActionListeners for the buttons:
        btnAdminLogin.addActionListener(this);
        btnUserLogin.addActionListener(this);
        btnRegister.addActionListener(this);
        btnAdminBack.addActionListener(this);
        btnAdminConfirm.addActionListener(this);
        btnUserBack.addActionListener(this);
        btnUserClear.addActionListener(this);
        btnUserConfirm.addActionListener(this);
        btnRegisterBack.addActionListener(this);
        btnRegisterClear.addActionListener(this);
        btnRegisterConfirm.addActionListener(this);


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
            showHomePanel();
        } else if (e.getSource()==btnUserClear){
            clearUserLoginFields();
        } else if (e.getSource()==btnUserConfirm){

            //if details are true:
            showUserMenuPanel();
        }

        //USER REGISTER PANEL:
        else if (e.getSource()==btnRegisterBack){
            clearUserRegisterFields();
            showHomePanel();
        } else if (e.getSource()==btnRegisterClear){
            clearUserRegisterFields();
        } else if (e.getSource()==btnRegisterConfirm){

            //if details are true:
            clearUserRegisterFields();
            showHomePanel();
        }
    }




}
