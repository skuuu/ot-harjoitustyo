
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javafx.scene.chart.XYChart.Series;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import saastopossuapp.dao.ActivityDao;
import saastopossuapp.dao.UserDao;
import saastopossuapp.dao.Database;
import saastopossuapp.domain.Activity;
import saastopossuapp.domain.UserAccount;
import saastopossuapp.logic.Logic;

public class LogicTest {
    private Logic logic;
    private Database db;
    private ActivityDao ad;
    private Activity testActivity;
    private Activity testActivity2;
    private UserAccount testUser;
    private UserDao ud;
    private Date activitysDate;
    private Date activitysDate2;
    private LocalDate ldActivitysDate;
    private LocalDate ldBefore;
    private LocalDate ldAfter;
    
 
    @Before
    public void setUp() throws SQLException {  
        this.db = new Database();
        this.ad = new ActivityDao(db);
     
        this.ud = new UserDao(db);
        this.logic = new Logic(ud, ad);
        this.testUser = new UserAccount("tester");
        this.testUser.setUserId(ud.findAll().size()+1);
        this.testUser.setUserBudget(100);
        ud.save(testUser);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.ldBefore = LocalDate.parse("2018-11-05", formatter);
        this.ldActivitysDate = LocalDate.parse("2018-11-02", formatter);
        this.ldAfter = LocalDate.parse("2018-11-01", formatter);
        
        activitysDate = java.sql.Date.valueOf("2018-11-02");
        this.testActivity = new Activity(10);
        testActivity.setActivityId(ad.findAll().size()+1);
        testActivity.setActivitysUser("tester");
        testActivity.setCategory("category");
        testActivity.setDate(activitysDate);
        ad.saveOrUpdate(testActivity, "tester");
        
        this.activitysDate2 = java.sql.Date.valueOf("2018-11-03");
        this.testActivity2 = new Activity(10);
        testActivity2.setActivityId(ad.findAll().size()+1);
        testActivity2.setActivitysUser("tester");
        testActivity2.setCategory("category2");
        testActivity2.setDate(activitysDate2);
        
    }
    @After
    public void tearDown() throws SQLException {
        ad.delete("tester");
        ud.delete("tester");        
       
    }
    @Test
    public void addExpenseWithDifferentDateAndSameCategoryCreatesNewActivity() throws SQLException {
        int sizeBefore = ad.findAllByUser("tester").size();
        logic.addExpense(null, "category", "20", "20", activitysDate2, "tester");
        assertEquals(sizeBefore+1, ad.findAllByUser("tester").size());
    }
    @Test
    public void addExpenseWithSameDateAndNewCategoryCreatesNewActivity() throws SQLException {
        int sizeBefore = ad.findAllByUser("tester").size();
        logic.addExpense("test", "new category", "20", "20", activitysDate2, "tester");
        assertEquals(sizeBefore+1, ad.findAllByUser("tester").size());
    }
    
    @Test
    public void toCentsWorks(){
        assertEquals(1010, logic.toCents(10, 10));
    }

    @Test
    public void changeBudgetDoesntWorkWhenInvalidValues(){
        assertEquals(false, logic.changeBudget("00", "00", "tester"));
        
    }
    @Test
    public void changeBudgetWorksWhenValidValues() throws SQLException{
        logic.changeBudget("1", "00", "tester");
        assertEquals(100, ud.findOne("tester").getUserBudget());
        
    }
    @Test
    public void toEurosWorks(){
        assertEquals(1, logic.toEuros(100), 0.00001);
        
    }
    @Test
    public void convertToDateWorks(){
        assertEquals(activitysDate, logic.convertToDate(ldActivitysDate));
        
    }
    @Test
    public void getBudgetAnalysisInvalidDatesReturnsErrorMessage(){
        assertEquals("Check the dates!", logic.getBudgetAnalysis(ldBefore, ldAfter, "tester"));
    }
    @Test
    public void getBudgetAnalysisValidDatesReturnsCorrectString(){ //5päivää
        StringBuilder sb = new StringBuilder();
            sb.append("Total expenses in the chosen time period: 0.1€ (average 0.02€/day in the chosen time period)")
                    .append("\nYour daily budget: 1.0€")
                    .append("\nYour budget for chosen time period: 5.0€, (spent int the chosen time period: ")
                    .append ("2.0%)");
       
        assertEquals(sb.toString(), logic.getBudgetAnalysis(ldAfter, ldBefore, "tester"));
    }
    @Test
    public void createUser_createsUserIfValidUsername() throws SQLException{
        int users = ud.findAll().size();
        logic.createUser("la123la");
        assertEquals(users+1, ud.findAll().size());
        ud.delete("la123la");

    }
    @Test
    public void createUser_dontCreateUserIfInvalidUsername() throws SQLException{
        int users = ud.findAll().size();
        logic.createUser("la123l111111111111111111111111a");
        assertEquals(users, ud.findAll().size());
        ud.delete("la123l111111111111111111111111a");
    }
    @Test
    public void validateIntegerInputReturnsFalseIfInvalid(){
        assertEquals(false, logic.validateIntegerInput("", ""));
    }
    @Test
    public void validateIntegerInputReturnsTrueIfCentsFieldEmpty(){
        assertEquals(true, logic.validateIntegerInput("10", ""));
    }
    @Test
    public void createChoices_includesAllCategories() throws SQLException{
        logic.createChoices(ldAfter, ldActivitysDate, "tester" );
        assertEquals(2, logic.createChoices(ldAfter, ldActivitysDate, "tester" ).size());
    }
    @Test
    public void doubleToStringWorks(){
        assertEquals("11.00", logic.doubleToString(10.9999));
    }
    @Test
    public void countExpensesFromBudgetWorks() throws SQLException{
        assertEquals(1.0, logic.countExpensesFromBudget(20, 2, 10), 0.01);
    }
    @Test
    public void countBudgetForChosenPeriodWorks() throws SQLException{
        assertEquals(100.0, logic.countBudgetForChosenPeriod(10, 10.0), 0.01);
    }
    @Test
    public void countAverageWorks() throws SQLException{
        assertEquals(0.01, logic.countAverage(10.0, 10), 0.01);
    }
    @Test
    public void checkUsernameReturnsFalseIfNotFound() throws SQLException{
        assertEquals(false, logic.checkUsername("blaa123"));
    }
    @Test
    public void checkUsernameReturnsTrueIfFound() throws SQLException{
        assertEquals(true, logic.checkUsername("tester"));
    }
    @Test
    public void validateDateReturnsTrueIfValidDates() throws SQLException{
        assertEquals(true, logic.validateDate(ldAfter, ldBefore));
    }
    @Test
    public void createSerie() throws SQLException{
        assertEquals(1, logic.createSerie(ldAfter, ldBefore, "tester").size());
    }
    @Test
    public void arrangeXAxisReturnsCorrectlyArrangedList() throws SQLException{
        ad.saveOrUpdate(testActivity2, "tester");
        ArrayList <Series> series = logic.createSerie(ldAfter, ldBefore, "tester");
        assertEquals("02.11.2018", logic.arrangeXAxis(series, ldAfter, ldBefore, "tester").get(0));
    }
}
