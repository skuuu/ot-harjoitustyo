package logicTests;


import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import saastopossuapp.dao.ActivityDao;
import saastopossuapp.dao.UserAccountDao;
import saastopossuapp.dao.Database;
import saastopossuapp.domain.Activity;
import saastopossuapp.domain.UserAccount;
import saastopossuapp.logic.Analysis;
import saastopossuapp.logic.Converter;
import saastopossuapp.logic.Logic;

public class LogicTest {
    private Logic logic;
    private Database db;
    private ActivityDao ad;
    private Activity testActivity;
    private Activity testActivity2;
    private UserAccount testUser;
    private UserAccountDao ud;
    private Date activitysDate;
    private Date activitysDate2;
    private LocalDate ldActivitysDate;
    private LocalDate ldActivitysDate2;
    private LocalDate ldBefore;
    private LocalDate ldAfter;
    private Analysis analysis;
    private Converter conv;
    
    @Before
    public void setUp() throws SQLException, ClassNotFoundException { 
        this.db = new Database();
        this.ad = new ActivityDao(db);     
        this.ud = new UserAccountDao(db);
        this.logic = new Logic(ud, ad);
        this.conv = new Converter();
        
        String username = "tester";
        this.testUser = new UserAccount("tester");
        this.testUser.setUserId(ud.findAll().size()+1);
        this.testUser.setUserBudget(100);
        ud.saveOrUpdate(testUser);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.ldBefore = LocalDate.parse("2018-11-05", formatter);
        this.ldActivitysDate = LocalDate.parse("2018-11-02", formatter);
        this.ldAfter = LocalDate.parse("2018-11-01", formatter);
        
        activitysDate = java.sql.Date.valueOf("2018-11-02");
        this.testActivity = new Activity("tester", 10, activitysDate, "category");
        testActivity.setActivityId((ad.findAll().size()+1));
        ad.saveOrUpdate(testActivity, "tester");
        
        this.activitysDate2 = java.sql.Date.valueOf("2018-11-03");
        this.ldActivitysDate2 = LocalDate.parse("2018-11-03", formatter);
        this.testActivity2 = new Activity("tester", 10, activitysDate2, "category2");
        testActivity2.setActivityId(ad.findAll().size()+1);
        logic.checkUsername("tester");
        
    }
    @After
    public void tearDown() throws SQLException {
        ad.delete("tester");
        ud.delete("tester");        
    }
   
    @Test
    public void addExpenseWithDifferentDateAndSameCategoryCreatesNewActivity() throws SQLException {
        int sizeBefore = ad.findAllByUser("tester").size();
        logic.addExpense(null, "category", "20", "20", ldActivitysDate2);
        assertEquals(sizeBefore+1, ad.findAllByUser("tester").size());
     
        
    }
    @Test
    public void addExpenseWithSameDateAndNewCategoryCreatesNewActivity() throws SQLException {
        int sizeBefore = ad.findAllByUser("tester").size();
        logic.addExpense("test", "create new", "20", "20", ldActivitysDate2);
        assertEquals(sizeBefore+1, ad.findAllByUser("tester").size());
       
    }
    @Test
    public void addExpenseWithNonValidAmountReturnsFalse() throws SQLException {
        assertEquals(false, logic.addExpense("test", "create new", "", "", ldActivitysDate2));
       
    }
    @Test
    public void getExpenseLabelTextReturnsString(){
        String dateString = "02.11.2018";
        assertEquals("Total expenses on 02.11.2018: 0.0 €", logic.getExpenseLabelText(dateString));
    }
    
    

    @Test
    public void changeBudgetDoesntWorkWhenInvalidValues(){
        assertEquals(false, logic.changeBudget("00", "00"));
        
    }
    @Test
    public void changeBudgetWorksWhenValidValues() throws SQLException{
        logic.changeBudget("1", "00");
        assertEquals(100, ud.findOne("tester").getUserBudget());
   
        
    
    }
    @Test
    public void getBudgetAnalysisInvalidDatesReturnsErrorMessage() throws SQLException{
        assertEquals("Check the dates!", logic.getBudgetAnalysis(ldBefore, ldAfter));
      
    }
    @Test
    public void getBudgetAnalysisValidDatesReturnsCorrectString() throws SQLException{ //5päivää
        StringBuilder sb = new StringBuilder();
            sb.append("Total expenses in the chosen time period: 0.1€ (average 0.1€/day in the chosen time period)")
                    .append("\nYour daily budget: 1.0€")
                    .append("\nYour budget for chosen time period: 5.0€, (spent in the chosen time period: ")
                    .append ("2.0%)");
       
        assertEquals(sb.toString(), logic.getBudgetAnalysis(ldAfter, ldBefore));
     
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
        assertEquals(false, logic.createUser("abcdefghijkl111111111111111111111111111111111111111111"));
        ud.delete("abcdefghijkl111111111111111111111111111111111111111111");
      
    }
    @Test
    public void createUser_dontCreateUserIfUsernameExists() throws SQLException{
        assertEquals(false, logic.createUser("tester"));
      
    }
    @Test
    public void validateNumberInputReturnsFalseIfInvalid() throws SQLException{
        assertEquals(false, logic.validateNumberInput("", ""));
     
    }
    @Test
    public void validateNumberInputReturnsTrueIfCentsFieldTooLong() throws SQLException{
        assertEquals(false, logic.validateNumberInput("10", "111111"));
       
    }
    @Test
    public void createChoices_includesAllCategories() throws SQLException{
        logic.createChoices(ldAfter, ldActivitysDate );
        assertEquals(2, logic.createChoices(ldAfter, ldActivitysDate ).size());
        
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
        assertEquals(1, logic.createSerie(ldAfter, ldBefore).size());
   
    }
   
    @Test
    public void correctCents_Returns00WhenEmptyString() throws SQLException{
        assertEquals("00", logic.correctCents(""));
    }
    @Test
    public void correctCents_Returns10WhenSingleNumber() throws SQLException{
        assertEquals("10", logic.correctCents("1"));
    }
    @Test
    public void validateStringInputReturnsFalseIfNull() throws SQLException{
        assertEquals(false, logic.validateStringInput(null));
    }
    @Test
    public void validateStringInput_ReturnsFalseIfNotNumbers() throws SQLException{
        assertEquals(false, logic.validateStringInput("1=1"));
    }
    @Test
    public void validateStringInput_ReturnsTrueIfvalidInput() throws SQLException{
        assertEquals(true, logic.validateStringInput("10"));
    }
    @Test
    public void arrangedXAxisCategories_ReturnsArrangedObservableList() throws SQLException {
        ad.saveOrUpdate(testActivity2, "tester");
        assertEquals("02.11.2018",  logic.arrangedXAxisCategories(ldAfter, ldBefore).get(0));
    }
    @Test
    public void setBarChartTitle_returnsCorrectString() throws SQLException {
        assertEquals("Daily Expenses from 01.11.2018-05.11.2018",  logic.setBarChartTitle(ldAfter, ldBefore));
    }
}
