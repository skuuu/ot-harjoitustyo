
package saastopossuapp.logic;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import saastopossuapp.dao.ActivityDao;
import saastopossuapp.dao.UserAccountDao;


public class Analysis {
    ActivityDao activityDao;
    UserAccountDao userAccountDao;
    DescriptiveStatistics stats = new DescriptiveStatistics();
    String username;
    LocalDate from;
    LocalDate to;
    Integer days;
    Integer budget;
    
    
    public Analysis(ActivityDao activityDao, UserAccountDao userAccountDao) {
        this.activityDao = activityDao;
        this.userAccountDao = userAccountDao;
        this.stats = new DescriptiveStatistics();
        this.username = "o";
        this.from = null;
        this.to = null;
        this.days = 0;
        this.budget = 0;
        
        
    } 
    public void setList(LocalDate from, LocalDate to, String username) throws SQLException {
        stats.clear();
        for (Integer i: activityDao.findExpensesByDate(from, to, username)) {
            stats.addValue(i);
        }
        this.username = username;
        this.from = from;
        this.to = to;
        
        this.days = Math.abs((int) from.toEpochDay() - 1 - (int) to.toEpochDay());
        try {
            this.budget = userAccountDao.findOne(username).getUserBudget();
        } catch (SQLException ex) {
            Logger.getLogger(Analysis.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public double countExpensesFromBudget() {
        return formatDecimals(stats.getSum() / (budget * days) * 100);
    }
    
    public double countBudgetForChosenPeriod() {
        return formatDecimals((this.days * this.budget) / 100.0);
    }
    
    public double countAverage() {
        if (stats.getN() > 0) {
            return formatDecimals(stats.getMean() / 100.0); 
        }
        return 0;
        

    }
    public int sumOfExpensesByDate() {  
        return (int) stats.getSum();

    }
    public int expensesInADay(String date) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate localDate = LocalDate.parse(date, dtf);   
        int inADay = 0;
        try {
            for (Integer cents: activityDao.findExpensesByDate(localDate, localDate, username)) {
                inADay += cents;
            }
            activityDao.findExpensesByDate(localDate, localDate, username);
        } catch (SQLException ex) {
            Logger.getLogger(Analysis.class.getName()).log(Level.SEVERE, null, ex);
        }
        return inADay;
    }
     
    public double formatDecimals(double formatThis) {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
        otherSymbols.setDecimalSeparator('.');
        DecimalFormat formatter = new DecimalFormat("#0.00", otherSymbols); 
        return Double.parseDouble(formatter.format(formatThis));
        
    }
    
    public int getBudget() {
        return this.budget;
    }
    
}

