package tests;



import java.sql.SQLException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import saastopossuapp.dao.UserAccountDao;
import saastopossuapp.dao.Database;
import saastopossuapp.domain.UserAccount;


public class UserAccountDaoTest {
    private Database db;
    private static UserAccountDao ud;
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
        db = new Database();
        ud = new UserAccountDao(db);
        testUser = new UserAccount("tester");
        testUser.setUserId(ud.findAll().size()+1);
    }
    
    @After
    public void tearDown() throws SQLException {
        ud.delete("tester");
    }
    
    @Test
    public void saveWorks() throws SQLException {
        assertEquals(testUser, ud.saveOrUpdate(testUser)); 
    }

    @Test
    public void findOneworksWhenUsernameExists() throws SQLException {
        ud.saveOrUpdate(testUser);
        assertEquals(testUser.getUsername(), ud.findOne("tester").getUsername());
    }
    @Test
    public void findOneworksWhenUsernameDoesntExist() throws SQLException {
        ud.saveOrUpdate(testUser);
        assertEquals(null, ud.findOne("notex1212"));
    }
    @Test
    public void findAllWorks() throws SQLException {
        int koko = ud.findAll().size();
        ud.saveOrUpdate(testUser);
        assertEquals(koko+1, ud.findAll().size()); 
    }
    @Test
    public void updateBudgetWorks() throws SQLException {
        ud.saveOrUpdate(testUser);
        ud.updateBudget("tester", 20);
        assertEquals(20, ud.findOne("tester").getUserBudget()); 
    }
}
