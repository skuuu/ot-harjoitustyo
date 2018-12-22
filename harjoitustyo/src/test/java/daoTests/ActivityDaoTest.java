package daoTests;

import java.io.IOException;
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

/**
 * Class is responsible for testing class ActivityDao
 */
public class ActivityDaoTest {
    private Database db;
    private ActivityDao ad;
    private Activity testActivity1;
    private Activity testActivity2;
    private UserAccount testUser;
    private UserAccountDao ud;
    private String username;
    private Date activitysDate1;
    private Date dateBeforeActivitysDate;
   
    /**
     * Method creates setup for the tests. 
     * One UserAccount (testUser) and one activity (testActivity1) will be added to testdatabase.
     * Additional Activity (testActivity2) will be added separately in tests if needed.
     * @throws java.sql.SQLException
     * @throws java.lang.ClassNotFoundException
     */
    @Before
    public void setUp() throws SQLException, ClassNotFoundException, IOException {
        this.db = new Database();
        db.changeDatabase("jdbc:sqlite:testdatabase.db");
        db.init();
        this.username = "tester";
        this.ad = new ActivityDao(db, username);
        
        this.ud = new UserAccountDao(db);
        this.testUser = new UserAccount(username);
        testUser.setUserBudget(100);
        ud.saveOrUpdate(testUser);
        
        activitysDate1 = java.sql.Date.valueOf("1992-03-31");
        this.testActivity1 = new Activity(username, 10, activitysDate1, "category", "description1");
        testActivity1.setActivityId(1);
        ad.save(testActivity1);
        
        dateBeforeActivitysDate = java.sql.Date.valueOf("1992-03-30");
        this.testActivity2 = new Activity(username, 20, dateBeforeActivitysDate, "category", "description2");
        
    }
    @After
    public void tearDown() throws SQLException {
        ad.deleteAll(username);
        ud.delete(username);
    }
    
    @Test
    public void save_Works() throws SQLException {
        assertEquals(testActivity1.getActivityId(), ad.save(testActivity1).getActivityId());  
    }

    @Test
    public void findOne_ReturnsCorrectActivity_IfActivityIdExists() throws SQLException {
        assertEquals(testActivity1, ad.findOne(testActivity1.getActivityId()));
    }
    
    @Test
    public void findOneReturnsNull_IfActivityIdDoesntExist() throws SQLException {
        assertEquals(null, ad.findOne(ad.findAll().size()+30));
    }
    
    @Test
    public void findAll_ReturnsCorrectSizedList() throws SQLException {
        assertEquals(1, ad.findAll().size()); 
    }
    
    @Test
    public void findAllByUser_ReturnsCorrectSizedList() throws SQLException {
        assertEquals(1, ad.findAllByUsername(username).size()); 
    }
    
    @Test
    public void findExpensesForTheChosenTimePeriod_ReturnsTotalExpenses() throws SQLException {
        int expenses = 0;
        for (Integer cents: ad.findExpensesForTheChosenTimePeriod(dateBeforeActivitysDate, activitysDate1)) {
            expenses += cents;
        }
        assertEquals(testActivity1.getCents(), expenses); 
    }
    
    @Test
    public void findExpensesForTheChosenTimePeriod_ReturnsTotalExpenses_IfOnlyOneDay() throws SQLException {
        int expenses = 0;
        for (Integer cents: ad.findExpensesForTheChosenTimePeriod(activitysDate1, activitysDate1)){
            expenses += cents;
        }
        assertEquals(10, expenses); 
    }
    
    @Test
    public void findAllByCategory_ReturnsEmptyMap_IfNoActivities() throws SQLException {
        assertEquals(0, ad.findAllByCategory(dateBeforeActivitysDate, dateBeforeActivitysDate, username).size());
    }
    
    @Test
    public void findAllByCategory_ReturnsMapThatDoesntHaveSameCategoryTwice_IfDifferentDates() throws SQLException {
        ad.save(testActivity2);
        assertEquals(1, ad.findAllByCategory(dateBeforeActivitysDate, activitysDate1, username).size());
    }
    
    @Test
    public void findAllByCategory_ReturnsMapThatHasActivitiesFromRightTimePeriod() throws SQLException {
        assertEquals(0, ad.findAllByCategory(dateBeforeActivitysDate, dateBeforeActivitysDate, username).size());
    }
    
    @Test
    public void deleteOne_deletesActivity() throws SQLException {
        ad.deleteOne(1);
        assertEquals(0, ad.findAllByCategory(activitysDate1, activitysDate1, username).size());
    }
    
    @Test
    public void getDailyActivities_DoesntAddDoubleActivityIfAlreadyExists() throws SQLException {
        ad.save(testActivity1);
        assertEquals(1, ad.getDailyActivities(activitysDate1, activitysDate1, username).size());
    }
    
    @Test
    public void findAll_ReturnsEmptyList_ifNoActivities() throws SQLException {
        ad.deleteAll(username);
        assertEquals(0, ad.findAll().size());
    }
    
    @Test
    public void findAllByCategory_ReturnsEmptyMap_ifNoActivities() throws SQLException {
        ad.deleteAll(username);
        assertEquals(0, ad.findAllByCategory(activitysDate1, activitysDate1, username).size());
    }    
}
