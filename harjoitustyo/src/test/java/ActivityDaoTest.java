
import java.sql.Date;
import java.sql.SQLException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import saastopossuapp.dao.ActivityDao;
import saastopossuapp.dao.UserDao;
import saastopossuapp.dao.Database;
import saastopossuapp.domain.Activity;
import saastopossuapp.domain.UserAccount;

public class ActivityDaoTest {
    private Database db;
    private ActivityDao ad;
    private Activity testActivity;
    private UserAccount testUser;
    private UserDao ud;
    
   
    @Before
    public void setUp() throws SQLException, ClassNotFoundException {
        this.db = new Database();
        this.ad = new ActivityDao(db);
        
        this.ud = new UserDao(db);
        this.testUser = new UserAccount("tester");
        testUser.setUserId(ud.findAll().size()+1);
        testUser.setUserBudget(100);
        ud.save(testUser);
        
        this.testActivity = new Activity(10);
        testActivity.setActivityId(ad.findAll().size()+1);
        testActivity.setActivitysUser("tester");
        testActivity.setCategory("category");
        Date date = java.sql.Date.valueOf("1992-03-31");
        testActivity.setDate(date);
        
    }
    @After
    public void tearDown() throws SQLException {
        ad.delete("tester");
        ud.delete("tester");
    }
    
    @Test
    public void saveWorks() throws SQLException {
        assertEquals(testActivity.getActivityId(), ad.saveOrUpdate(testActivity, "tester").getActivityId());  
    }

    @Test
    public void findOneworksWhenActivityIdExists() throws SQLException {
        ad.saveOrUpdate(testActivity, "tester");
        assertEquals(testActivity.getActivityId(), ad.findOne(testActivity.getActivityId()).getActivityId());

    }
    @Test
    public void findOneworksWhenActivityIdDoesntExist() throws SQLException {
        assertEquals(null, ad.findOne(ad.findAll().size()+30));
    }
    @Test
    public void findAllWorks() throws SQLException {
        int koko = ad.findAll().size();
        ad.saveOrUpdate(testActivity, "tester");
        assertEquals(koko+1, ad.findAll().size()); 
    }
    @Test
    public void findAllByUserWorks() throws SQLException {
        int koko = ad.findAllByUser("tester").size();
        ad.saveOrUpdate(testActivity, "tester");
        assertEquals(koko+1, ad.findAllByUser("tester").size()); 
    }
    @Test
    public void findAllByDateWorks() throws SQLException {
        Date after = java.sql.Date.valueOf("1992-03-31");
        Date before = java.sql.Date.valueOf("1992-04-01");
        ad.saveOrUpdate(testActivity, "tester");
        assertEquals(1, ad.findAllByDate(after, before, "tester").size()); 
    }
    @Test
    public void findExpensesByDateWorksWhenDatesAreValid() throws SQLException {
        ad.saveOrUpdate(testActivity, "tester");
        int cents = ad.findExpensesByDate("31.03.1992", "tester");
        assertEquals(10, cents); 
    }
    @Test //2018-11-25 -muotoon
    public void convertStringToDateWorks() throws SQLException {
        String str = "31.03.1992";
        Date date = java.sql.Date.valueOf("1992-03-31");
        assertEquals(date, ad.convertStringToDate(str)); 
    }
    @Test
    public void addExpenseToSameDayDoesntAddNewExpense() throws SQLException {
        int sizeBefore = ad.findAllByUser("tester").size();
        ad.saveOrUpdate(testActivity, "tester");
        ad.saveOrUpdate(testActivity, "tester");
        assertEquals(sizeBefore+1, ad.findAllByUser("tester").size());
    }
    @Test
    public void findAllByCategoryReturnsEmptyMapIfNoActivities() throws SQLException {
        Date after = java.sql.Date.valueOf("1990-03-30");
        Date before = java.sql.Date.valueOf("1990-03-31");
        assertEquals(0, ad.findAllByCategory(after, before, "tester").size());
    }
}
