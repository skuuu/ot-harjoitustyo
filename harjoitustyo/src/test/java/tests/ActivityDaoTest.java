package tests;


import java.sql.Date;
import java.sql.SQLException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import saastopossuapp.dao.ActivityDao;
import saastopossuapp.dao.UserAccountDao;
import saastopossuapp.dao.Database;
import saastopossuapp.domain.Activity;
import saastopossuapp.domain.UserAccount;
import saastopossuapp.logic.Logic;

public class ActivityDaoTest {
    private Database db;
    private ActivityDao ad;
    private Activity testActivity;
    private Activity testActivity2;
    private UserAccount testUser;
    private UserAccountDao ud;
    private Logic logic;
   
    @Before
    public void setUp() throws SQLException, ClassNotFoundException {
        this.db = new Database();
        this.ad = new ActivityDao(db);
        
        this.ud = new UserAccountDao(db);
        this.testUser = new UserAccount("tester");
        testUser.setUserId(ud.findAll().size()+1);
        testUser.setUserBudget(100);
        ud.saveOrUpdate(testUser);
        
        Date date = java.sql.Date.valueOf("1992-03-31");
        this.testActivity = new Activity("tester", 10, date, "category");
        testActivity.setActivityId(ad.findAll().size()+1);
        
        Date date2 = java.sql.Date.valueOf("1992-03-30");
        this.testActivity2 = new Activity("tester", 20, date2, "category");
        testActivity2.setActivityId(ad.findAll().size()+1);
        
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
    public void findSumOfExpensesByDateWorks() throws SQLException {
        Date after = java.sql.Date.valueOf("1992-03-31");
        Date before = java.sql.Date.valueOf("1992-04-01");
        ad.saveOrUpdate(testActivity, "tester");
        assertEquals(testActivity.getCents(), (int)ad.findSumOfExpensesByDate(after, before, "tester")); 
    }
    @Test
    public void findExpensesByDateWorksWhenDatesAreValid() throws SQLException {
        ad.saveOrUpdate(testActivity, "tester");
        Date date = java.sql.Date.valueOf("1992-03-31");
        int cents = ad.findSumOfExpensesByDate(date, date, "tester");
        assertEquals(10, cents); 
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
    @Test
    public void findAllByCategory_ReturnsMapThatDoesntHaveSameCategoryTwiceIfDifferentDate() throws SQLException {
        ad.saveOrUpdate(testActivity, "tester");
        ad.saveOrUpdate(testActivity2, "tester");
        Date after = java.sql.Date.valueOf("1992-03-30");
        Date before = java.sql.Date.valueOf("1992-03-31");
        assertEquals(1, ad.findAllByCategory(after, before, "tester").size());
    }
    @Test
    public void findAllByCategory_ReturnsMapThatHasActivitiesFromRightTimePeriod() throws SQLException {
        ad.saveOrUpdate(testActivity, "tester");
        Date after = java.sql.Date.valueOf("1990-03-30");
        Date before = java.sql.Date.valueOf("1990-03-31");
        assertEquals(0, ad.findAllByCategory(after, before, "tester").size());
    }
}
