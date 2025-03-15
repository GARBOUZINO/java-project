import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class AdminDashboard extends JFrame {
    public AdminDashboard() {
        setTitle("Espace Administrateur");
        setSize(800, 600);
        initUI();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        JTable requestsTable = createRequestsTable();
        mainPanel.add(new JScrollPane(requestsTable), BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel();
        JButton refreshBtn = new JButton("Actualiser");
        refreshBtn.addActionListener(e -> refreshTable(requestsTable));
        
        buttonPanel.add(refreshBtn);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }

    private JTable createRequestsTable() {
        String[] columns = {"Employé", "Date Début", "Date Fin", "Statut", "Actions"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement()) {
            
            ResultSet rs = stmt.executeQuery(
                "SELECT u.username, d.* FROM demandes_conge d " +
                "JOIN utilisateurs u ON d.user_id = u.id");
            
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("username"),
                    rs.getDate("date_debut"),
                    rs.getDate("date_fin"),
                    rs.getString("statut"),
                    "Action"
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        JTable table = new JTable(model);
        // Ajouter des boutons d'action
        return table;
    }

    private void refreshTable(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement()) {
            
            ResultSet rs = stmt.executeQuery(
                "SELECT u.username, d.* FROM demandes_conge d " +
                "JOIN utilisateurs u ON d.user_id = u.id");
            
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("username"),
                    rs.getDate("date_debut"),
                    rs.getDate("date_fin"),
                    rs.getString("statut"),
                    "Action"
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}