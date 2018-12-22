package daoTests;

import java.io.IOException;
import java.sql.SQLException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import saastopossuapp.dao.UserAccountDao;
import saastopossuapp.dao.Database;
import saastopossuapp.domain.UserAccount;

/**
 * Class is responsible for testing class UserAccountDao
 */
public class UserAccountDaoTest {
    private Database db;
    private static UserAccountDao ud;
    private UserAccount testUser;
    
    /**
     * Method creates setup for the tests. 
     * One UserAccount (testUser) will be added to testdatabase.
     * @throws java.sql.SQLException
     * @throws java.lang.ClassNotFoundException
     */
    @Before
    public void setUp() throws ClassNotFoundException, SQLException, IOException {
        db = new Database();
        db.changeDatabase("jdbc:sqlite:testdatabase.db");
        db.init();
        ud = new UserAccountDao(db);
        testUser = new UserAccount("tester");
        testUser.setUserId(ud.findAll().size()+1);
        
        ud.saveOrUpdate(testUser);
    }
    
    @After
    public void tearDown() throws SQLException {
        ud.delete("tester");
    }
    
    @Test
    public void save_Works() throws SQLException {
        ud.delete("tester");
        assertEquals(testUser, ud.saveOrUpdate(testUser)); 
    }

    @Test
    public void findOne_Works_IfUsernameExists() throws SQLException {
        assertEquals(testUser.getUsername(), ud.findOne("tester").getUsername());
    }
    @Test
    public void findOne_ReturnsNull_IfUsernameDoesntExist() throws SQLException {
        assertEquals(null, ud.findOne("notex1212"));
    }
    @Test
    public void findAll_ReturnsCorrectSizedList() throws SQLException {
        assertEquals(1, ud.findAll().size()); 
    }
    @Test
    public void updateBudget_UpdatesBudget() throws SQLException {
        ud.updateBudget("tester", 20);
        assertEquals(20, ud.findOne("tester").getUserBudget()); 
    }
}
