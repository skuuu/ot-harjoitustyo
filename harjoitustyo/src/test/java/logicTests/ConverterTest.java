
package logicTests;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.junit.Test;
import static org.junit.Assert.*;
import saastopossuapp.logic.Converter;

/**
 * Class is responsible for testing class Converter
 */
public class ConverterTest {
    private final Converter conv;
    private final LocalDate ldActivitysDate;
    private final Date activitysDate;

    
    public ConverterTest() {
        this.conv = new Converter();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.ldActivitysDate = LocalDate.parse("2018-11-02", formatter);
        activitysDate = java.sql.Date.valueOf("2018-11-02");
    }

    @Test
    public void toCents_ReturnsCorrectValue(){
        assertEquals(1010, conv.toCents("10", "10"));
    }
    
    @Test
    public void toEuros_ReturnsCorrectValue() throws SQLException {
        assertEquals(1, conv.toEuros(100), 0.01);
    }
    
    @Test
    public void localDateToDate_ReturnsCorrectValue() throws SQLException {
        assertEquals(activitysDate, conv.localDateToDate(ldActivitysDate));
    }
    
    @Test
    public void localDateToString_ReturnsCorrectValue() throws SQLException {
        assertEquals("02.11.2018", conv.localDateToString(ldActivitysDate));
    }
    
    @Test
    public void dateToString_ReturnsCorrectValue() throws SQLException {
        assertEquals("02.11.2018", conv.dateToString(activitysDate));
    }
    
    @Test
    public void StringToLocalDate_ReturnsCorrectValue() throws SQLException {
        assertEquals(ldActivitysDate, conv.stringToLocalDate("02.11.2018"));
    }
}
