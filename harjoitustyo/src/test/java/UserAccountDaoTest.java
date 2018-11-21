

import java.sql.SQLException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import saastopossuapp.dao.UserDao;
import saastopossuapp.database.Database;
import saastopossuapp.domain.UserAccount;


public class UserAccountDaoTest {
    private Database db;
    private static UserDao ud;
    private UserAccount testUser;
    
    public UserAccountDaoTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() throws SQLException {
        ud.delete("tester");
    }
    
    @Before
    public void setUp() throws ClassNotFoundException, SQLException {
        db = new Database("testdatabase.db");
        ud = new UserDao(db);
        testUser = new UserAccount("tester");
        testUser.setUserId(ud.findAll().size()+1);
    }
    
    @After
    public void tearDown() throws SQLException {
        ud.delete("tester");
    }
    
    @Test
    public void saveWorks() throws SQLException {
        assertEquals(testUser, ud.save(testUser)); 
    }

    @Test
    public void findOneworksWhenUsernameExists() throws SQLException {
        ud.save(testUser);
        System.out.println(testUser.getUsername());
        assertEquals(testUser.getUsername(), ud.findOne("tester").getUsername());
    }
    @Test
    public void findOneworksWhenUsernameDoesntExist() throws SQLException {
        ud.save(testUser);
        assertEquals(null, ud.findOne("notex1212"));
    }
    @Test
    public void findAllWorks() throws SQLException {
        int koko = ud.findAll().size();
        ud.save(testUser);
        assertEquals(koko+1, ud.findAll().size()); 
    }
    @Test
    public void updateBudgetWorks() throws SQLException {
        ud.save(testUser);
        ud.updateBudget("tester", 20);
        assertEquals(20, ud.findOne("tester").getUserBudget()); 
    }
}
