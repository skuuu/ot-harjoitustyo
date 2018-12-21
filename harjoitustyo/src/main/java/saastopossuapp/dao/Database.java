
package saastopossuapp.dao;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class is responsible for database connection
 */
public class Database {
    private String databaseAddress;

    public Database() throws ClassNotFoundException, SQLException {
        this.databaseAddress = "jdbc:sqlite:saastopossuDatabase.db";
        this.init();
    }
    

    public void changeDatabase(String databaseAddress) {
        this.databaseAddress = databaseAddress;
    }
    
    /**
     * Method creates connection to SQL database
     * @return  connection
     * @throws java.sql.SQLException if database connection fails
     */ 
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(databaseAddress);
    }
    
    /**
     * Method initializes the Database class
     * @throws java.sql.SQLException if database connection fails
     */ 
    public void init() throws SQLException {
        List<String> commands = this.sqliteCommands();

        Connection conn = getConnection();
        Statement st = conn.createStatement();
        for (String com : commands) {
            st.executeUpdate(com);
        }
    }
    
    /**
     * Method creates SQL-commands for creating tables id database doesn't exist yet
     * @return list of commands
     */ 
    private List<String> sqliteCommands() {
        ArrayList<String> list = new ArrayList<>();

        list.add("CREATE TABLE IF NOT EXISTS useraccount ("
                + "username varchar, "
                + "userbudget integer, "
                + "userid integer, "
                + "CONSTRAINT userid PRIMARY KEY (userid), "
                + "CONSTRAINT username UNIQUE (username));");
        
        list.add("CREATE TABLE IF NOT EXISTS activity ("
                + "activityid integer NOT NULL, "
                + "cents integer, "
                + "date date NOT NULL, "
                + "activitysuser varchar, "
                + "category varchar, "
                + "description varchar, "
                + "CONSTRAINT activityid PRIMARY KEY (activityid), "
                + "CONSTRAINT activitysuser FOREIGN KEY (activitysuser) REFERENCES useraccount (username));");
                
        return list;
    }
}