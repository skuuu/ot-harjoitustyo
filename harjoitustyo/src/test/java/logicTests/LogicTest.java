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
import saastopossuapp.dao.UserAccountDao;
import saastopossuapp.dao.Database;
import saastopossuapp.domain.Activity;
import saastopossuapp.domain.UserAccount;
import saastopossuapp.logic.Logic;

/**
 * Class is responsible for testing class Logic
 */
public class LogicTest {
    private Logic logic;
    private Database db;
    private Activity testActivity1;
    private Activity testActivity2;
    private UserAccount testUser;
    private Date activitysDate2;
    private LocalDate ldActivitysDate2;
    private LocalDate ldActivitysDate1;
    private LocalDate before;
    private LocalDate after;
    private String username; 
    private UserAccountDao ud;
    private ActivityDao ad;

    /**
     * Method creates setup for the tests. 
     * One UserAccount (testUser) and one activity (testActivity1) will be added to testdatabase.
     * Additional Activity (testActivity2) will be added separately in tests if needed.
     * @throws java.sql.SQLException if connection to database fails
     * @throws java.lang.ClassNotFoundException if class not found 
     * @throws java.io.IOException if configuration problems
     */
    @Before
    public void setUp() throws SQLException, ClassNotFoundException, IOException { 
        this.username = "tester";
        this.db = new Database();
        db.changeDatabase("jdbc:sqlite:testdatabase.db");
        db.init();
        this.ad = new ActivityDao(db, username);
        this.ud = new UserAccountDao(db);    
        
        this.testUser = new UserAccount(username);
        this.testUser.setUserId(ud.findAll().size()+1);
        this.testUser.setUserBudget(100);
        ud.saveOrUpdate(testUser);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.before = LocalDate.parse("2018-11-05", formatter);
        this.after = LocalDate.parse("2018-11-01", formatter);
        
        this.testActivity1 = new Activity(username, 10, java.sql.Date.valueOf("2018-11-02"), "category1", "description");
        ad.save(testActivity1);
        
        this.activitysDate2 = java.sql.Date.valueOf("2018-11-03");
        this.ldActivitysDate1 = LocalDate.parse("2018-11-02", formatter);
        this.ldActivitysDate2 = LocalDate.parse("2018-11-03", formatter);
        
        this.testActivity2 = new Activity(username, 10, activitysDate2, "category2", "description");
        
        this.logic = new Logic(ud, ad, after, before);
        logic.checkUsername(username);
        logic.createSerie(after, before);
        logic.getBudgetAnalysis();
    }
    
    @After
    public void tearDown() throws SQLException {
        ad.deleteAll("tester");
        ud.delete("tester");  
        ud.delete("qpqpqp");
        ud.delete("abcdefghijkl111111111111111111111111111111111111111111");
    }
   
    @Test
    public void addExpense_WithDifferentDateAndSameCategory_CreatesNewActivity() throws SQLException {
        int sizeBefore = ad.findAllByUsername(username).size();
        logic.addExpense(null, "category", "20", "20", ldActivitysDate2, "description");
        assertEquals(sizeBefore+1, ad.findAllByUsername("tester").size());
    }
    
    @Test
    public void addExpense_WithNewCategoryAlreadyExisting_DoesntCreateDoubleCategory() throws SQLException {
        int categories = logic.createChoices().size();
        logic.addExpense("category1", "create new", "20", "20", ldActivitysDate2, "description");
        assertEquals(categories, logic.createChoices().size());
    }
    
    @Test
    public void addExpense_WithNonValidCategoryName_ReturnsFalse() throws SQLException {
        assertEquals(false, logic.addExpense("!!!", "create new", "20", "20", ldActivitysDate2, "description"));
    }
    
    @Test
    public void addExpense_WithValidCategoryName_ReturnsTrue() throws SQLException {
        assertEquals(true, logic.addExpense("categoryNew", "create new", "20", "20", ldActivitysDate2, "description"));
    }
    
    @Test
    public void addExpense_WhenSevenCategories_ReturnsFalse() throws SQLException {
        logic.addExpense("category2", "create new", "20", "20", ldActivitysDate2, "description");
        logic.addExpense("category3", "create new", "20", "20", ldActivitysDate2, "description");
        logic.addExpense("category4", "create new", "20", "20", ldActivitysDate2, "description");
        logic.addExpense("category5", "create new", "20", "20", ldActivitysDate2, "description");
        logic.addExpense("category6", "create new", "20", "20", ldActivitysDate2, "description");  
        logic.addExpense("category7", "create new", "20", "20", ldActivitysDate2, "description");  
        logic.createSerie(after, before);
        assertEquals(false, logic.addExpense("category8", "create new", "20", "20", ldActivitysDate2, "description"));
    }
    
    @Test
    public void addExpense_WithNonValidNewCategory_ReturnsFalse() throws SQLException {
        assertEquals(false, logic.addExpense("%%", "create new", "20", "20", ldActivitysDate2, "description"));
    }
    
    @Test
    public void addExpense_WithSameDateAndNewCategory_CreatesNewActivity() throws SQLException {
        int sizeBefore = ad.findAllByUsername(username).size();
        logic.addExpense("test", "create new", "20", "20", ldActivitysDate2, "description");
        assertEquals(sizeBefore+1, ad.findAllByUsername("tester").size());   
    }
    
    @Test
    public void addExpense_WithNonValidAmount_ReturnsFalse() throws SQLException {
        assertEquals(false, logic.addExpense("test", "create new", "", "", ldActivitysDate2, "description"));   
    }
    
    @Test
    public void getExpenseLabelText_ReturnsCorrectString() throws SQLException{
        assertEquals("Total expenses on 02.11.2018: 0.1 €", logic.getExpenseLabelText("02.11.2018"));
    }

    @Test
    public void changeBudget_DoesntChangeBudget_IfInvalidValues(){
        assertEquals(false, logic.changeBudget("00", "00"));    
    }
    
    @Test
    public void changeBudget_ChangesBudget_IfValidValues() throws SQLException{
        logic.changeBudget("1", "00");
        assertEquals(100, ud.findOne(username).getUserBudget());
    }
    
    @Test
    public void getBudgetAnalysis_ReturnsEmptyString_IfInvalidDates() throws SQLException {
        this.logic = new Logic(ud, ad, LocalDate.now(), before);
        assertEquals("", logic.getBudgetAnalysis());
    }
    
    @Test
    public void getBudgetAnalysis_ReturnsCorrectString_IfValidDates() throws SQLException {
        StringBuilder sb = new StringBuilder();
            sb.append("\nYour daily budget: 1.0€")
                    .append("\nYour budget for chosen time period: 5.0€ (spent in the chosen time period: ")
                    .append ("2.0%)")
                    .append("\nTotal expenses in the chosen time period: 0.1€ (average 0.1€/day in the chosen time period)")
                    .append("\nYou have saved: 4.9€");
        assertEquals(sb.toString(), logic.getBudgetAnalysis());
    }
    
    @Test
    public void createUser_CreatesUser_IfValidUsername() throws SQLException{
        int users = ud.findAll().size();
        logic.createUser("qpqpqp");
        assertEquals(users + 1, ud.findAll().size());
    }
    
    @Test
    public void createUser_DoesntCreateUser_IfInvalidUsername() throws SQLException{
        assertEquals(false, logic.createUser("abcdefghijkl111111111111111111111111111111111111111111"));
    }
    
    @Test
    public void createUser_DoesntCreateUser_IfUsernameExists() throws SQLException{
        assertEquals(false, logic.createUser(username));
    }
    
    @Test
    public void validateNumberInput_ReturnsFalse_IfEmptyInput() throws SQLException{
        assertEquals(false, logic.validateNumberInput("", ""));
    }
    
    @Test
    public void validateNumberInput_ReturnsTrue_IfCentsFieldTooLong() throws SQLException{
        assertEquals(false, logic.validateNumberInput("10", "111111"));
    }
    
    @Test
    public void validateNumberInput_ReturnsFalse_IfEurosFieldEmpty() throws SQLException{
        assertEquals(false, logic.validateNumberInput("", "10"));
    }
    
    @Test
    public void validateNumberInput_ReturnsFalse_IfEurosFieldNotNumber() throws SQLException{
        assertEquals(false, logic.validateNumberInput("!", ""));
    }
    
    @Test
    public void validateNumberInput_ReturnsFalse_IfCentsFieldNotNumber() throws SQLException{
        assertEquals(false, logic.validateNumberInput("10", "!"));
    }
    
    @Test
    public void createChoices_IncludesAllCategories() throws SQLException{
        assertEquals(2, logic.createChoices().size());
    }
    
    @Test
    public void checkUsername_ReturnsFalse_IfNotFound() throws SQLException{
        assertEquals(false, logic.checkUsername("blaa123"));
    }
    
    @Test
    public void checkUsername_ReturnsTrue_IfFound() throws SQLException{
        assertEquals(true, logic.checkUsername(username));
    }
    
    @Test
    public void checkUsername_ReturnsFalse_IfNonValidUsername() throws SQLException{
        assertEquals(false, logic.checkUsername("1"));
    }
    
    @Test
    public void validateDate_ReturnsTrue_IfValidDates() throws SQLException{
        assertEquals(true, logic.validateDate(after, before));
    }
    
    @Test
    public void createSerie_IncludesRightAmountOfActivities() throws SQLException{
        assertEquals(1, logic.createSerie(after, before).size());
    }
    
    @Test
    public void correctCents_Returns00_IfEmptyString() throws SQLException{
        assertEquals("00", logic.correctCents(""));
    }
    
    @Test
    public void validateStringInput_ReturnsFalse_IfNull() throws SQLException{
        assertEquals(false, logic.validateUsernameInput(null));
    }
    
    @Test
    public void validateStringInput_ReturnsFalse_IfNotNumbers() throws SQLException{
        assertEquals(false, logic.validateUsernameInput("1=1"));
    }
    
    @Test
    public void validateStringInput_ReturnsFalse_IfInputTooLong() throws SQLException{
        assertEquals(false, logic.validateUsernameInput("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"));
    }
    
    @Test
    public void validateStringInput_ReturnsFalse_IfInputTooShort() throws SQLException{
        assertEquals(false, logic.validateUsernameInput("a"));
    }
    
    @Test
    public void validateStringInput_ReturnsTrue_IfValidInput() throws SQLException{
        assertEquals(true, logic.validateUsernameInput("10"));
    }
    
    @Test
    public void validateTextInput_ReturnsTrue_IfValidInputWithWhiteSpaces() throws SQLException{
        assertEquals(true, logic.validateTextInput(" blaa blaa "));
    }
    
    @Test
    public void validateTextInput_ReturnsFalse_IfNull() throws SQLException {
        assertEquals(false, logic.validateTextInput(null));
    }
    
    @Test
    public void validateTextInput_ReturnsFalse_IfTooLong() throws SQLException {
        assertEquals(false, logic.validateTextInput("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"));
    }
    
    @Test
    public void validateTextInput_ReturnsFalse_IfNotNumbersOrAlphabets() throws SQLException{
        assertEquals(false, logic.validateUsernameInput("!"));
    }
    
    @Test
    public void validateTextInput_ReturnsTrue_IfValidInput() throws SQLException {
        assertEquals(true, logic.validateTextInput(" blaa blaa "));
    }
    
    @Test
    public void arrangedXAxisCategories_ReturnsArrangedObservableList() throws SQLException {
        ad.save(testActivity2);
        assertEquals("02.11.2018", logic.arrangedXAxisCategories().get(0));
    }
    
    @Test
    public void  arrangedXAxisCategories_DoesntAddDoubleDates() throws SQLException {
        logic.addExpense("category2", "create new", "20", "20", ldActivitysDate1, "description");
        logic.createSerie(after, before);
        assertEquals(1, logic.arrangedXAxisCategories().size());
    }
    
    @Test
    public void setBarChartTitle_ReturnsCorrectString() throws SQLException {
        assertEquals("Daily Expenses from 01.11.2018-05.11.2018",  logic.getBarChartTitle(after, before));
    }
    
    @Test //String newCategoryName, Object category, String euros, String cents
    public void getInputErrorMessage_ReturnsCorrectErrorMessage_IfNullValues() throws SQLException {
        assertEquals("Invalid input.\nChoose category!",  logic.getInputErrorMessage("", null, "10", "10", "description"));
    }
    
    @Test //String newCategoryName, Object category, String euros, String cents
    public void getInputErrorMessage_ReturnsCorrectErrorMessage_IfSevenCategories() throws SQLException {
        logic.addExpense("category2", "create new", "20", "20", ldActivitysDate2, "description");
        logic.addExpense("category3", "create new", "20", "20", ldActivitysDate2, "description");
        logic.addExpense("category4", "create new", "20", "20", ldActivitysDate2, "description");
        logic.addExpense("category5", "create new", "20", "20", ldActivitysDate2, "description");
        logic.addExpense("category6", "create new", "20", "20", ldActivitysDate2, "description");  
        logic.addExpense("category7", "create new", "20", "20", ldActivitysDate2, "description");  
        logic.createSerie(after, before);
        assertEquals("Invalid input.\nExpense Categories are limited to 7 categories.",  logic.getInputErrorMessage("category8", "create new", "10", "10", "description"));
    }
    
    @Test
    public void getInputErrorMessage_ReturnsCorrectErrorMessage_IfCategoryAlreadyExists() throws SQLException {
        assertEquals("Invalid input.\nExpense Category must be unique.",  logic.getInputErrorMessage("category1", "create new", "10", "10", "description"));
    }
    
    @Test
    public void getInputErrorMessage_ReturnsCorrectErrorMessage_IfCategoryNameNonValid() throws SQLException {
        assertEquals("Invalid input.\nExpense Category must be 3-20 characters:\nletters a-z, A-Z and/or numbers 0-9.",  logic.getInputErrorMessage("!!!!", "create new", "10", "10", "description"));
    }
    
    @Test
    public void getInputErrorMessage_ReturnsCorrectErrorMessage_IfExpenseNonValidAndCategoryNew() throws SQLException {
        assertEquals("Invalid input.\nExpense Amount must be over 0€: \ncents max. 2 digits, euros min. 1 digit.",  logic.getInputErrorMessage("categoryx", "create new", "", "", "description"));
    }
    
    @Test
    public void getInputErrorMessage_ReturnsCorrectErrorMessage_IfExpenseNonValidAndCategoryNotNew() throws SQLException {
        assertEquals("Invalid input.\nExpense Amount must be over 0€: \ncents max. 2 digits, euros min. 1 digit.",  logic.getInputErrorMessage(null, "category1", "", "", "description"));
    }
    
    @Test
    public void deleteActivity_Works() throws SQLException {
        logic.deleteActivity(testActivity1);
        assertEquals(null, ad.findOne(testActivity1.getActivityId()));
    }
    
    @Test
    public void getDailyExpensesLabel_ReturnsCorrectActivities_IfNoDoubleActivities() throws SQLException {
        assertEquals(1, logic.getDailyExpensesLabel("02.11.2018", "category1", username).size());
    }
    
    @Test
    public void getDailyExpensesLabel_ReturnsCorrectActivities_IfCategoriesDiffer() throws SQLException {
        logic.addExpense(null, "categoryx", "20", "20", ldActivitysDate2, "description");
        logic.addExpense(null, "categoryy", "20", "20", ldActivitysDate2, "description");
        logic.createSerie(after, before);
        assertEquals(1, logic.getDailyExpensesLabel("03.11.2018", "categoryx", username).size());
    }
}