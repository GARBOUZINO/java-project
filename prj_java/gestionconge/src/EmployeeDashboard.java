import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.sql.*;

public class EmployeeDashboard extends JFrame {
    private String username;

    public EmployeeDashboard(String username) {
        this.username = username;
        setTitle("Espace Employé");
        setSize(600, 400);
        initUI();
    }

    private void initUI() {
        JTabbedPane tabs = new JTabbedPane();
        
        // Onglet Nouvelle demande
        JPanel newRequestPanel = new JPanel(new GridLayout(4, 2));
        JTextField startDate = new JTextField();
        JTextField endDate = new JTextField();
        JButton submitBtn = new JButton("Soumettre");
        
        newRequestPanel.add(new JLabel("Date début (AAAA-MM-JJ):"));
        newRequestPanel.add(startDate);
        newRequestPanel.add(new JLabel("Date fin:"));
        newRequestPanel.add(endDate);
        newRequestPanel.add(submitBtn);
        
        submitBtn.addActionListener(e -> submitRequest(startDate.getText(), endDate.getText()));
        
        // Onglet Historique
        JPanel historyPanel = new JPanel();
        historyPanel.add(new JScrollPane(createRequestsTable()));
        
        tabs.addTab("Nouvelle demande", newRequestPanel);
        tabs.addTab("Historique", historyPanel);
        
        add(tabs);
    }

    private JTable createRequestsTable() {
        String[] columns = {"Date Début", "Date Fin", "Statut", "Commentaire"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        
        try (Connection conn = Database.getConnection();
             PreparedStatement pst = conn.prepareStatement(
                 "SELECT * FROM demandes_conge WHERE user_id = (SELECT id FROM utilisateurs WHERE username = ?)")) {
            
            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getDate("date_debut"),
                    rs.getDate("date_fin"),
                    rs.getString("statut"),
                    rs.getString("commentaire")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return new JTable(model);
    }

    private void submitRequest(String start, String end) {
        try (Connection conn = Database.getConnection();
             PreparedStatement pst = conn.prepareStatement(
                 "INSERT INTO demandes_conge (user_id, date_debut, date_fin) " +
                 "VALUES ((SELECT id FROM utilisateurs WHERE username = ?), ?, ?)")) {
            
            pst.setString(1, username);
            pst.setString(2, start);
            pst.setString(3, end);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Demande soumise!");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}