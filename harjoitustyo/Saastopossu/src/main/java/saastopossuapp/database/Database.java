
package saastopossuapp.database;
import java.sql.*;

public class Database {
    private final String url = "jdbc:postgresql://localhost/users";
    private final String user = "postgres";
    private final String password = "postgres";

    private String databaseAddress;

    public Database(String databaseAddress) throws ClassNotFoundException {
        this.databaseAddress = databaseAddress;
    }
    
   public Connection connect() {
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
