import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginForm extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;

    public LoginForm() {
        setTitle("Authentification");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridLayout(3, 2));
        
        panel.add(new JLabel("Nom d'utilisateur:"));
        txtUsername = new JTextField();
        panel.add(txtUsername);
        
        panel.add(new JLabel("Mot de passe:"));
        txtPassword = new JPasswordField();
        panel.add(txtPassword);
        
        JButton btnLogin = new JButton("Se connecter");
        btnLogin.addActionListener(e -> authenticate());
        panel.add(btnLogin);
        
        add(panel);
    }

    private void authenticate() {
        String query = "SELECT * FROM utilisateurs WHERE username = ? AND password = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement pst = conn.prepareStatement(query)) {
            
            pst.setString(1, txtUsername.getText());
            pst.setString(2, new String(txtPassword.getPassword()));
            
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                String role = rs.getString("role");
                openDashboard(role);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Identifiants incorrects!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void openDashboard(String role) {
        if (role.equals("admin")) {
            new AdminDashboard().setVisible(true);
        } else {
            new EmployeeDashboard(txtUsername.getText()).setVisible(true);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }
}