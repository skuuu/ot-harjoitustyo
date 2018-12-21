
package saastopossuapp.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.*;
import java.sql.*;
import saastopossuapp.domain.UserAccount;

/**
 * Class is responsible for database access operations related to UserAccounts
 */
public class UserAccountDao implements UserAccountDaoInterface {
    private final Database db;
    
    public UserAccountDao(Database db) {
        this.db = db;
    }
    
    
    /**
     * Method finds UserAccount based on username
     * @param username - UserAccount's username
     * @return UserAccount or null, if UserAccount doesn't exist.
     * @throws java.sql.SQLException if fetching from database fails
     */
    @Override
    public UserAccount findOne(String username) throws SQLException {
        Connection con = db.getConnection();
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
    
    /**
     * Method finds all UserAccounts from database
     * @return list of UserAccounts
     * @throws java.sql.SQLException if fetching from database fails
     */
    @Override
    public List<UserAccount> findAll() throws SQLException {
        Connection con = db.getConnection();
        PreparedStatement stmt = con.prepareStatement("SELECT * FROM UserAccount");
        List<UserAccount> users = new ArrayList<>();

        ResultSet rs = stmt.executeQuery();
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
    
    /**
     * Method deletes UserAccount related to username
     * @param username - UserAccount's username
     * @throws java.sql.SQLException if deleting from database fails
     */
    @Override
    public void delete(String username) throws SQLException {
        Connection conn = db.getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM UserAccount WHERE username = (?)");

        stmt.setString(1, username);
        stmt.executeUpdate();
        stmt.close();
        conn.close(); 
    }
    
    /**
     * Method saves new UserAccount to database
     * @param userAccount - userAccount that will be saved
     * @return  saved UserAccount
     * @throws java.sql.SQLException if saving to database fails
     */
    @Override
    public UserAccount saveOrUpdate(UserAccount userAccount) throws SQLException {
        Connection conn = db.getConnection();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO UserAccount (username, userbudget) VALUES (?,?)");
        stmt.setString(1, userAccount.getUsername());
        stmt.setInt(2, userAccount.getUserBudget());
        
        stmt.executeUpdate();
        stmt.close();
        conn.close();
        return userAccount;
    }
    
    /**
     * Method updates UserAccount's budget
     * @param username - username related to UserAccount whose budget will be updated
     * @param budget - new budget in cents
     * @return  saved budget
     * @throws java.sql.SQLException if updating database fails
     */
    @Override
    public int updateBudget(String username, int budget) throws SQLException {
        Connection conn = db.getConnection();
  
        PreparedStatement stmt = conn.prepareStatement("UPDATE useraccount SET userbudget = (?) WHERE username = (?)");
        stmt.setInt(1, budget);
        stmt.setString(2, username);
        
        stmt.executeUpdate();
        stmt.close();
        conn.close();
        return budget;
    }
}
