
package saastopossuapp.dao;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Class is responsible for database connection
 */
public class Database {
    private String databaseAddress;
    private String database;

    public Database() throws ClassNotFoundException, SQLException, FileNotFoundException, IOException {
        this.databaseAddress = "jdbc:sqlite:saastopossu.db";
        this.init();
    }
    
    /**
     * Method changes database address
     * @param databaseAddress- new database address
     */ 
    public void changeDatabase(String databaseAddress) {
        this.databaseAddress = databaseAddress;
    }
    
    /**
     * Method creates connection to SQL database
     * @return  connection to database
     * @throws java.sql.SQLException if database connection fails
     */ 
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(databaseAddress);
    }
    
    /**
     * Method initializes the Database class
     * @throws java.sql.SQLException if database connection fails
     * @throws java.io.IOException if problems with configuration
     */ 
    public void init() throws SQLException, IOException {
        Properties props = new Properties();
        try {
            props.load(new FileInputStream("saastopossu.conf"));
        } catch (FileNotFoundException ex) {
        }
        
        database = props.getProperty("database");
        databaseAddress = "jdbc:sqlite:" + database;
        if (database.equals("")) {
            databaseAddress = "jdbc:sqlite:saastopossu.db";
        }
        
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