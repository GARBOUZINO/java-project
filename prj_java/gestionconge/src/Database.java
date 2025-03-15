import java.sql.*;
public class Database {
    private static final String URL = "jdbc:mysql://localhost/phpmyadmin";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
    public static void main(String[] args) {
        try {
            Connection conn = Database.getConnection();
            System.out.println("Connexion réussie!");
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
