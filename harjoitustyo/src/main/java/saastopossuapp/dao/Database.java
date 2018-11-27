
package saastopossuapp.dao;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {
    private final String url = "jdbc:postgresql://localhost/users";
    private final String user = "tester";
    private final String password = "tester1";

    
    public Connection connect() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
}
