
package saastopossuapp.logic;

import java.sql.Date;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;


/**
 * Class is responsible for convertions related to date types and money.
 * 
 */
public class Converter {
    

    /**
     * Method converts date of type String to localDate
     * @param  date of type String
     * @return  date of type LocalDate
     */
    public LocalDate stringToLocalDate(String date) {  
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate localDate = LocalDate.parse(date, dtf); 
        return localDate;
    }
    
    /**
     * Method converts Date to String in format dd.MM.yyyy
     * @param   date   date of type Date
     * @return   date of type String formatted to dd.MM.yyyy
     */
    public String dateToString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return dateFormat.format(date);
    }
    
    /**
     * Method converts LocalDate to String in format dd.MM.yyyy
     * @param   localDate   date of type LocalDate
     * @return   date of type String formatted to dd.MM.yyyy
     */
    public String localDateToString(LocalDate localDate) {
        Date date = localDateToDate(localDate);
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return dateFormat.format(date);
    }
    
    /**
     * Method converts localDate to Date
     * @param   localDate   date of type LocalDate
     * @return   date of type Date
     */
    public Date localDateToDate(LocalDate localDate) {
        return java.sql.Date.valueOf(localDate);
    }
    
    /**
     * Method converts String of euros and cents to cents
     * @param   euros   amount of euros
     * @param   cents   amount of cents
     * @return   total amount in cents
     */
    public int toCents(String euros, String cents) {
        int euro = Integer.parseInt(euros) * 100;
        int total = (euro + Integer.parseInt(cents));
        return total;
    }
    
    /**
     * Method converts Integer of cents to euros
     * @param   cents   amount of cents
     * @return   total amount in euros
     */
    public double toEuros(int cents) {
        double euros =  (double) cents / (double) 100;
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
        otherSymbols.setDecimalSeparator('.');
        DecimalFormat formatter = new DecimalFormat("#0.00", otherSymbols);
        return Double.parseDouble(formatter.format(euros));
    }
}
