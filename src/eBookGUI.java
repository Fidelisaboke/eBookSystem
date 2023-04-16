import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

    public eBookGUI(){
        //CREATING THE FRAME
        //Setting the icon for the frame:
        ImageIcon icon = new ImageIcon("icon_book.png");
        this.setIconImage(icon.getImage());

        //Size and title:
        this.setVisible(true);
        this.setTitle("eBook System");
        this.setSize(640, 360);


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
            showHomePanel();
        } else if (e.getSource()==btnAdminConfirm){

            //if password is true:
            showAdminMenuPanel();
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
            showHomePanel();
        } else if (e.getSource()==btnRegisterClear){
            clearUserRegisterFields();
        } else if (e.getSource()==btnRegisterConfirm){

            //if details are true:
            showHomePanel();
        }
    }
}
