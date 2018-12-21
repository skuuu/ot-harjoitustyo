package domainTests;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import saastopossuapp.domain.UserAccount;

/**
 * Class is responsible for testing class UserAccount
 */
public class UserAccountTest {
    UserAccount testUser;
    
     /**
     * Method creates setup for the tests. 
     * One UserAccount (testUser) will be created.
     */
    @Before
    public void setUp() {
        testUser = new UserAccount("tester");
    }
    
    @Test
    public void newUsers_Budget_Is1000() {
        assertEquals(1000, testUser.getUserBudget());
    }
    
    @Test
    public void newUsers_Username_IsCorrect() {
        assertEquals("tester", testUser.getUsername());
    }
   
    @Test
    public void setAndGetBudget_Works() {
        testUser.setUserBudget(10);
        assertEquals(10, testUser.getUserBudget());
    }
    
    @Test
    public void setAndGetUsername_Works() {
        testUser.setUsername("testertwo");
        assertEquals("testertwo", testUser.getUsername());
    }
    
    @Test
    public void setAndGetUserId_Works() {
        testUser.setUserId(10);
        assertEquals(10, testUser.getUserId());
    }      
}
