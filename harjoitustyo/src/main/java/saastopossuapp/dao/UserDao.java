
package saastopossuapp.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.*;
import java.sql.*;
import saastopossuapp.domain.*;

public class UserDao implements Dao<UserAccount, Integer, String> {
    private Database db;
    
    public UserDao(Database db) {
        this.db = db;
    }
    @Override
    public UserAccount findOne(String username) throws SQLException {
        Connection con = db.connect();
        PreparedStatement stmt = con.prepareStatement("SELECT * FROM useraccount WHERE username = ?");
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        UserAccount user = new UserAccount(rs.getString("username").trim());
        user.setUserBudget(rs.getInt("userBudget"));
        user.setUserId(rs.getInt("userId"));

        stmt.close();
        rs.close();
        con.close();
        return user;
        
    }
    @Override
    public List<UserAccount> findAll() throws SQLException {
        Connection con = db.connect();
        PreparedStatement stmt = con.prepareStatement("SELECT * FROM UserAccount");
        
        ResultSet rs = stmt.executeQuery();
        List<UserAccount> users = new ArrayList<>();

        while (rs.next()) {
            UserAccount user = new UserAccount(rs.getString("username"));
            user.setUserBudget(rs.getInt("userBudget"));
            user.setUserId(rs.getInt("userId"));
            users.add(user);
        }
        stmt.close();
        rs.close();
        con.close();
        return users;
        
    }
    @Override
    public void delete(String username) throws SQLException {
        Connection conn = db.connect();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM UserAccount"
                + " WHERE username = (?)");

        stmt.setString(1, username);
        stmt.executeUpdate();
        stmt.close();
        conn.close(); 
        
    }
    public UserAccount save(UserAccount user) throws SQLException {
        Connection conn = db.connect();
  
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO UserAccount"
                + " (username, userBudget, userId)"
                + " VALUES (?,?,?)");
        
        stmt.setString(1, user.getUsername().trim());
        stmt.setInt(2, user.getUserBudget());
        stmt.setInt(3, findAll().size() + 1);
        
        stmt.executeUpdate();
        stmt.close();
        return user;
    
    }
    public int updateBudget(String username, int budget) throws SQLException {
        Connection conn = db.connect();
  
        PreparedStatement stmt = conn.prepareStatement("UPDATE useraccount SET userbudget = (?) WHERE username = (?)");
        stmt.setInt(1, budget);
        stmt.setString(2, username);
        
        stmt.executeUpdate();
        stmt.close();
        return budget;
    }
}
