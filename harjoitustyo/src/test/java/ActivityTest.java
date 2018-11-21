

import java.sql.Date;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import saastopossuapp.domain.Activity;

public class ActivityTest {
    Activity a;
    
    @Before
    public void setUp() {
        a = new Activity(100);
    }

    @Test
    public void setAndGetActivityIdWorks() {
        a.setActivityId(1);
        assertEquals(1, a.getActivityId());
    }
    @Test
    public void setAndGetActivitysUserWorks() {
        a.setActivitysUser("tester");
        assertEquals("tester", a.getActivitysUser());
    }
    @Test
    public void setAndGetCentsWorks() {
        a.setCents(20);
        assertEquals(20, a.getCents());
    }
    @Test
    public void setAndGetDateWorks() {
        Date date = new Date (2018-10-10);
        a.setDate(date);
        assertEquals(date, a.getDate());
    }
    
}
