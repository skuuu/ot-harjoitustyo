
import java.sql.Date;
import java.sql.SQLException;
import javafx.scene.control.PasswordField;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import saastopossuapp.dao.ActivityDao;
import saastopossuapp.dao.UserDao;
import saastopossuapp.database.Database;
import saastopossuapp.domain.Activity;
import saastopossuapp.domain.UserAccount;

public class ActivityDaoTest {
    private  static Database db;
    private  static ActivityDao ad;
    private  static Activity testActivity;
    private  static UserAccount testUser;
    private  static UserDao ud;
    
    public ActivityDaoTest() throws SQLException, ClassNotFoundException {
        
    }
    
    @BeforeClass
    public static void setUpClass() throws SQLException, ClassNotFoundException {
        db = new Database("testdatabase.db");
        ad = new ActivityDao(db);
        
        ud = new UserDao(db);
        testUser = new UserAccount("tester");
        testUser.setUserId(ud.findAll().size()+1);
        testUser.setUserBudget(100);
        ud.save(testUser);
        
        testActivity = new Activity(10);
        testActivity.setActivityId(ad.findAll().size()+1);
        testActivity.setActivitysUser("tester");
        Date date = new Date(31-03-1992);
        testActivity.setDate(date);
        
    }

    @AfterClass
    public static void tearDownClass() throws SQLException {
        ad.delete("tester");
        ud.delete("tester");
    }
    
    @Before
    public void setUp() throws ClassNotFoundException, SQLException {
        
    }
    @After
    public void tearDown() throws SQLException {
        
    }
    
    @Test
    public void saveWorks() throws SQLException {
        assertEquals(testActivity.getActivityId(), ad.save(testActivity).getActivityId());  
    }

    @Test
    public void findOneworksWhenActivityIdExists() throws SQLException {
        assertEquals(testActivity.getActivityId(), ad.findOne(testActivity.getActivityId()).getActivityId());
    }
    @Test
    public void findOneworksWhenActivityIdDoesntExist() throws SQLException {
        assertEquals(null, ad.findOne(ad.findAll().size()+30));
    }
    @Test
    public void findAllWorks() throws SQLException {
        int koko = ad.findAll().size();
        ad.save(testActivity);
        assertEquals(koko+1, ad.findAll().size()); 
    }
    @Test
    public void findAllByUserWorks() throws SQLException {
        int koko = ad.findAllByUser("tester").size();
        ad.save(testActivity);
        assertEquals(koko+1, ad.findAllByUser("tester").size()); 
    }
    @Test
    public void findAllByDateWorks() throws SQLException {
        Date after = new Date(1992-30-31);
        Date before = new Date(1992-01-04);
        ad.save(testActivity);
        assertEquals(1, ad.findAllByDate(after, before, "tester").size()); 
    }
    
}
