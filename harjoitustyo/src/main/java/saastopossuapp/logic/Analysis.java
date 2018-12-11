
package saastopossuapp.logic;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import saastopossuapp.dao.ActivityDaoInterface;
import saastopossuapp.dao.UserAccountDaoInterface;

/**
 * Class is responsible for budget calculations and is called from class Logic only.
 */
public class Analysis {
    private final ActivityDaoInterface activityDao;
    private final UserAccountDaoInterface userAccountDao;
    private final Converter conv;
    private DescriptiveStatistics stats = new DescriptiveStatistics();
    private String username;
    private LocalDate from;
    private LocalDate to;
    private Integer days;
    private Integer budget;
    
    
    
    public Analysis(ActivityDaoInterface activityDao, UserAccountDaoInterface userAccountDao) {
        this.conv = new Converter();
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
    
    /**
     * Method counts how much (percentage) user's expenses are from the budget in a chosen time period
     * @return  percentage from budget
     */
    public double countExpensePercentageFromBudget() {
        return formatDecimals(stats.getSum() / (budget * days) * 100);
    }
    
    /**
     * Method counts how much (percentage) user's expenses are from the budget in a chosen time period
     * @return  percentage from budget in euros
     */
    public double countBudgetForChosenPeriod() {
        return formatDecimals((this.days * this.budget) / 100.0);
    }
    
    /**
     * Method counts what is the average expense in a day in a chosen time period
     * @return  average in euros
     */
    public double countAverage() {
        if (stats.getN() > 0) {
            return formatDecimals(stats.getMean() / 100.0); 
        }
        return 0;
    }
    
    /**
     * Method counts what is the average expense in a day in a chosen time period
     * @return  average in euros
     */
    public int sumOfExpensesByDate() {  
        return (int) stats.getSum();

    }
    
    /**
     * Method counts the expenses on a chosen day from all categories
     * @param   strDate    date of type String
     * @return  sum of expenses in a chosen day
     * @throws java.sql.SQLException fetch from database non-successfull
     */
    public int expensesInADay(String strDate) throws SQLException {   //muokattu
        int inADay = 0;
        for (Integer cents: activityDao.findExpensesByDate(conv.stringToLocalDate(strDate), conv.stringToLocalDate(strDate), username)) {
            inADay += cents;
        }
        activityDao.findExpensesByDate(conv.stringToLocalDate(strDate), conv.stringToLocalDate(strDate), username);
        
        return inADay;
    }
    
    /**
     * Method formats decimals to match "#0.00"
     * @param   formatThis double to format
     * @return  formatted Double
     */
    public double formatDecimals(double formatThis) {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
        otherSymbols.setDecimalSeparator('.');
        DecimalFormat formatter = new DecimalFormat("#0.00", otherSymbols); 
        return Double.parseDouble(formatter.format(formatThis));
        
    }
    
    /**
     * Method returns budget
     * @return  budget
     */
    public int getBudget() {
        return this.budget;
    }
    
}

