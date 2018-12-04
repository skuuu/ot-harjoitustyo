package tests;



import java.sql.Date;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import saastopossuapp.domain.Activity;

public class ActivityTest {
    Activity a;
    Activity b;
    
    @Before
    public void setUp() {
        a = new Activity("tester", 100, new Date (2018-10-10), "category");
        b = new Activity("tester", 100, new Date (2018-10-11), "category2");
    }

    @Test
    public void setAndGetActivityIdWorks() {
        a.setActivityId(2);
        assertEquals(2, a.getActivityId());
    }
    @Test
    public void setAndGetActivitysUserWorks() {
        a.setActivitysUser("tester2");
        assertEquals("tester2", a.getActivitysUser());
    }
    @Test
    public void setAndGetCentsWorks() {
        a.setCents(20);
        assertEquals(20, a.getCents());
    }
    @Test
    public void setAndGetDateWorks() {
        Date date = new Date (2018-11-10);
        a.setDate(date);
        assertEquals(date, a.getDate());
    }
    @Test
    public void compareToReturns1IfActivitysDateIsEarlier() {
        assertEquals(1, a.compareTo(b));
    }
    @Test
    public void compareToReturnsMinus1IfActivitysDateIsLater() {
        assertEquals(-1, b.compareTo(a));
    }
    
}
