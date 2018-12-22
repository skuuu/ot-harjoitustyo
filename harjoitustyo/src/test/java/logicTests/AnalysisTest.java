package logicTests;


import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import saastopossuapp.dao.ActivityDao;
import saastopossuapp.dao.Database;
import saastopossuapp.dao.UserAccountDao;
import saastopossuapp.domain.Activity;
import saastopossuapp.domain.UserAccount;
import saastopossuapp.logic.Analysis;

/**
 * Class is responsible for testing class Analysis
 */
public class AnalysisTest {
    private Database db;
    private ActivityDao ad;
    private Activity testActivity1;
    private Activity testActivity2;
    private UserAccount testUser;
    private UserAccountDao ud;
    private Date activitysDate1;
    private Date activitysDate2;
    private LocalDate untilDate;
    private LocalDate fromDate;
    private Analysis analysis;
    private String username;
    
    
    /**
     * Method creates setup for the tests. 
     * One UserAccount (testUser) and two activities (testActivity1, testActivity2) will be added to testdatabase.
     * @throws java.sql.SQLException
     * @throws java.lang.ClassNotFoundException
     */
    @Before
    public void setUp() throws ClassNotFoundException, SQLException, IOException {
        this.db = new Database(); 
        db.changeDatabase("jdbc:sqlite:testdatabase.db");
        db.init();
        this.username = "tester";
        this.db = new Database();
        this.ad = new ActivityDao(db, username);
        this.ud = new UserAccountDao(db);
        this.ad = new ActivityDao(db, username);
        
        this.testUser = new UserAccount(username);
        this.testUser.setUserBudget(100);
        ud.saveOrUpdate(testUser);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.fromDate = LocalDate.parse("2018-11-01", formatter);
        this.untilDate = LocalDate.parse("2018-11-05", formatter);
        
        activitysDate1 = java.sql.Date.valueOf("2018-11-02");
        this.testActivity1 = new Activity(username, 10, activitysDate1, "category1", "description");
        ad.save(testActivity1);
        
        activitysDate2 = java.sql.Date.valueOf("2018-11-03");
        this.testActivity2 = new Activity(username, 10, activitysDate2, "category2", "description");
        ad.save(testActivity2);
        
        this.analysis = new Analysis (ad, ud, fromDate, untilDate, username);
    }
    
    @After
    public void tearDown() throws SQLException {
        ad.deleteAll("tester");
        ud.delete("tester"); 
    }

    @Test
    public void countExpensePercentageFromBudget_ReturnValueCorrect() throws SQLException {
        assertEquals(4.0, analysis.countExpensePercentageFromBudget(), 0.01);
    }
    
    @Test
    public void countBudgetForChosenPeriod_ReturnValueCorrect() throws SQLException {
        assertEquals(5.0, analysis.countBudgetForChosenPeriod(), 0.01);
    }
    
    @Test
    public void countAverage_ReturnValueCorrect_IfValidInput() throws SQLException {
        assertEquals(0.1, analysis.countAverage(), 0.01);
    }
    
    @Test
    public void countAverage_ReturnsZero_IfNoActivities() throws SQLException {
        analysis = new Analysis(ad, ud, LocalDate.now(), LocalDate.now(), username);
        assertEquals(0.0, analysis.countAverage(), 0.01);
    }
    
    @Test
    public void expensesInADay_ReturnValueCorrect() throws SQLException {
        assertEquals(0.1, analysis.expensesInADay("02.11.2018"), 0.01);
    }
    
    @Test
    public void countSavings_ReturnsZero_IfExpensesOverBudget() throws SQLException {
        this.testActivity1.setCents(1000);
        ad.save(testActivity1);
        analysis = new Analysis (ad, ud, fromDate, untilDate, username);
        assertEquals(0.0, analysis.countSavings(), 0.01);
    }
    
    @Test
    public void countSavings_ReturnsCorrectAmountOfSavings_IfExpensesUnderBudget() throws SQLException {
        assertEquals(4.8, analysis.countSavings(), 0.01);
    }
}
