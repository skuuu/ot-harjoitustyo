
package saastopossuapp.dao;


import java.sql.*;


public class Database {

    private String databaseAddress;

    public Database() throws ClassNotFoundException {
        this.databaseAddress = "jdbc:sqlite:users.db";
    }

    public Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(databaseAddress);
        } catch (SQLException ex) {
            return null;
        }
    }
}