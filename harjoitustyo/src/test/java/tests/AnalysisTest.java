package tests;



import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import saastopossuapp.dao.ActivityDao;
import saastopossuapp.dao.Database;
import saastopossuapp.dao.UserAccountDao;
import saastopossuapp.domain.Activity;
import saastopossuapp.domain.UserAccount;
import saastopossuapp.logic.Analysis;
import saastopossuapp.logic.Logic;


public class AnalysisTest {
    private Database db;
    private ActivityDao ad;
    private Activity testActivity;
    private Activity testActivity2;
    private UserAccount testUser;
    private UserAccountDao ud;
    private Date activitysDate;
    private Date activitysDate2;
    private LocalDate ldActivitysDate;
    private LocalDate ldBefore;
    private LocalDate ldAfter;
    private Analysis analysis;
    private DescriptiveStatistics stats;
    private String username;
    private LocalDate from;
    private LocalDate to;
    private Integer days;
    private Integer budget;
    
    public AnalysisTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() throws ClassNotFoundException, SQLException {
        this.db = new Database();
        this.ad = new ActivityDao(db);
        this.ud = new UserAccountDao(db);
        this.analysis = new Analysis (ad, ud);
        
        this.testUser = new UserAccount("tester");
        this.testUser.setUserId(ud.findAll().size()+1);
        this.testUser.setUserBudget(100);
        ud.saveOrUpdate(testUser);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.ldBefore = LocalDate.parse("2018-11-05", formatter);
        this.ldAfter = LocalDate.parse("2018-11-01", formatter);
        
        activitysDate = java.sql.Date.valueOf("2018-11-02");
        this.testActivity = new Activity("tester", 10, activitysDate, "category");
        testActivity.setActivityId(ad.findAll().size()+1);
        ad.saveOrUpdate(testActivity, "tester");
        
        this.activitysDate2 = java.sql.Date.valueOf("2018-11-03");
        this.testActivity2 = new Activity("tester", 10, activitysDate2, "category2");
        testActivity2.setActivityId(ad.findAll().size()+1);
        ad.saveOrUpdate(testActivity, "tester");
        
        
        this.stats = new DescriptiveStatistics();
        this.username = "tester";
        this.from = ldAfter;
        this.to = ldBefore;
        this.days = 0;
        this.budget = 0;
        analysis.setList(from, to, "tester");
 
    }
    @After
    public void tearDown() throws SQLException {
        ad.delete("tester");
        ud.delete("tester"); 
    }

    @Test
    public void countExpensesFromBudgetWorks() throws SQLException {
        assertEquals(4.0, analysis.countExpensesFromBudget(), 0.01);
    }
    @Test
    public void countBudgetForChosenPeriodWorks() throws SQLException {
        assertEquals(5.0, analysis.countBudgetForChosenPeriod(), 0.01);
    }
    @Test
    public void countAverageWithValidInput() throws SQLException {
        assertEquals(0.2, analysis.countAverage(), 0.01);
    }
    @Test
    public void countAverageReturnsZeroWhenNoActivities() throws SQLException {
        ad.delete("tester");
        analysis.setList(from, to, "tester");
        assertEquals(0.0, analysis.countAverage(), 0.01);
    }
    @Test
    public void expensesInADayWorks() throws SQLException {
        assertEquals(20.0, analysis.expensesInADay("02.11.2018"), 0.01);
    }
}
