
package logicTests;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.junit.Test;
import static org.junit.Assert.*;
import saastopossuapp.logic.Converter;


public class ConverterTest {
    private Converter conv;
    private LocalDate ldActivitysDate;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private Date activitysDate;

    
    public ConverterTest() {
        this.conv = new Converter();
        this.ldActivitysDate = LocalDate.parse("2018-11-02", formatter);
        activitysDate = java.sql.Date.valueOf("2018-11-02");

    }

    @Test
    public void toCentsWorks(){
        assertEquals(1010, conv.toCents("10", "10"));
    }
    @Test
    public void toEurosWorks() throws SQLException {
        assertEquals(1, conv.toEuros(100), 0.01);
    }
    @Test
    public void localDateToDateWorks() throws SQLException {
        assertEquals(activitysDate, conv.localDateToDate(ldActivitysDate));
    }
    @Test
    public void localDateToStringWorks() throws SQLException {
        assertEquals("02.11.2018", conv.localDateToString(ldActivitysDate));
    }
    @Test
    public void dateToStringWorks() throws SQLException {
        assertEquals("02.11.2018", conv.dateToString(activitysDate));
    }
    @Test
    public void StringToLocalDateWorks() throws SQLException {
        assertEquals(ldActivitysDate, conv.stringToLocalDate("02.11.2018"));
    }
}
