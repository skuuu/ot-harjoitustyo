
package saastopossuapp.dao;

import java.sql.*;
import java.util.List;
import saastopossuapp.domain.UserAccount;

/**
 * Class defines the Interface that UserAccountDao implements.
 */
public interface UserAccountDaoInterface {
    List<UserAccount> findAll() throws SQLException;
    void delete(String username) throws SQLException;
    UserAccount findOne(String username) throws SQLException;
    UserAccount saveOrUpdate(UserAccount userAccount) throws SQLException;
    int updateBudget(String username, int budget) throws SQLException;
}
