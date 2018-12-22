package domainTests;

import java.sql.Date;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import saastopossuapp.domain.Activity;

/**
 * Class is responsible for testing class Activity
 */
public class ActivityTest {
    Activity activity1;
    Activity activity2;
    
     /**
     * Method creates setup for the tests. 
     * Two Activities (activity1, activity2) will be created.
     */
    @Before
    public void setUp() {
        activity1 = new Activity("tester", 100, new Date (2018-10-10), "category", "description");
        activity2 = new Activity("tester", 100, new Date (2018-10-11), "category2", "description");
    }

    @Test
    public void setAndGetActivityId_Works() {
        activity1.setActivityId(2);
        assertEquals(2, activity1.getActivityId());
    }
    @Test
    public void setAndGetActivitysUser_Works() {
        activity1.setActivitysUser("tester2");
        assertEquals("tester2", activity1.getActivitysUser());
    }
    
    @Test
    public void setAndGetDescription_Works() {
    activity1.setDescription("description2");
    assertEquals("description2", activity1.getDescription());
    }
    
    @Test
    public void setAndGetCents_Works() {
        activity1.setCents(20);
        assertEquals(20, activity1.getCents());
    }
    
    @Test
    public void setAndGetDate_Works() {
        Date date = new Date (2018-11-10);
        activity1.setDate(date);
        assertEquals(date, activity1.getDate());
    }
    
    @Test
    public void compareTo_Returns1_IfActivitysDateIsEarlier() {
        assertEquals(1, activity1.compareTo(activity2));
    }
    
    @Test
    public void compareTo_ReturnsMinus1_IfActivitysDateIsLater() {
        assertEquals(-1, activity2.compareTo(activity1));
    }
    
    @Test
    public void equals_returnsTrueValue_whenSameActivities() {
    Activity x = new Activity("tester", 100, new Date (2018-10-10), "category", "description");
    Activity y = new Activity("tester", 100, new Date (2018-10-10), "category", "description");
    Assert.assertTrue(x.equals(y) && y.equals(x));
    }
    
    @Test
    public void hashCode_returnsTrueValue_whenSameActivities() {
    Activity x = new Activity("tester", 100, new Date (2018-10-10), "category", "description");
    Activity y = new Activity("tester", 100, new Date (2018-10-10), "category", "description");
    Assert.assertTrue(x.hashCode() == y.hashCode());
    }
    
    @Test
    public void toString_returnsCorrectString() {
    assertEquals("1.0€", activity1.toString());
    }
}
