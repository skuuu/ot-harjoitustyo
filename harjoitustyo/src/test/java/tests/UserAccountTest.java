package tests;



import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import saastopossuapp.domain.UserAccount;


public class UserAccountTest {
    UserAccount testUser;
    
    @Before
    public void setUp() {
        testUser = new UserAccount("tester");
    }
    
    @Test
    public void newUsersBudgetIs1000() {
        assertEquals(1000, testUser.getUserBudget());
    }
    
    @Test
    public void newUsersUsernameIsCorrect() {
        assertEquals("tester", testUser.getUsername());
    }
   
    @Test
    public void setAndGetBudgetWorks() {
        testUser.setUserBudget(10);
        assertEquals(10, testUser.getUserBudget());
    }
    
    @Test
    public void setAndGetUsernameWorks() {
        testUser.setUsername("testertwo");
        assertEquals("testertwo", testUser.getUsername());

    }
    @Test
    public void setAndGetUserIdWorks() {
        testUser.setUserId(10);
        assertEquals(10, testUser.getUserId());
    }      
}
