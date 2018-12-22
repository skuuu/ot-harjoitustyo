
package saastopossuapp.logic;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.Locale;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import saastopossuapp.dao.ActivityDaoInterface;
import saastopossuapp.dao.UserAccountDaoInterface;

/**
 * Class is responsible for budget calculations and is called from class Logic only.
 */
public class Analysis {
    private final ActivityDaoInterface activityDao;
    private final Converter conv;
    private DescriptiveStatistics stats;
    private final Integer days;
    private Integer budget;
    
    
    public Analysis(ActivityDaoInterface activityDao, UserAccountDaoInterface userAccountDao, LocalDate fromDate, LocalDate untilDate, String username) throws SQLException {
        this.conv = new Converter();
        this.activityDao = activityDao;
        this.stats = new DescriptiveStatistics();
        stats.clear();
        this.budget = userAccountDao.findOne(username).getUserBudget();
        for (Integer expense: activityDao.findExpensesForTheChosenTimePeriod(conv.localDateToDate(fromDate), conv.localDateToDate(untilDate))) {
            stats.addValue(expense);
        }
        this.days = Math.abs((int) fromDate.toEpochDay() - 1 - (int) untilDate.toEpochDay());
    }
   
    /**
     * Method counts how much (percentage) user's expenses are from the budget in a chosen time period
     * @return percentage from budget formatted to #0.00
     */
    public double countExpensePercentageFromBudget() {
        return formatDecimals(stats.getSum() / (budget * days) * 100);
    }
    
    /**
     * Method counts how much (percentage) user's expenses are from the budget in a chosen time period
     * @return percentage from budget in euros formatted to #0.00
     */
    public double countBudgetForChosenPeriod() {
        return conv.toEuros(days * budget);
    }
    
    /**
     * Method counts what is the average expense in a day in a chosen time period
     * @return average expense in euros formatted to #0.00
     */
    public double countAverage() {
        if (this.stats.getN() > 0) {
            return conv.toEuros(stats.getMean()); 
        }
        return 0;
    }
    
    /**
     * Method counts what is the average expense in a day in a chosen time period
     * @return average in euros formatted to #0.00
     */
    public double sumOfExpensesByDate() {  
        return conv.toEuros(stats.getSum());

    }
    
    /**
     * Method counts the expenses on a chosen day from all categories
     * @param strDate - date of type String
     * @return sum of expenses in a chosen day formatted to #0.00
     * @throws java.sql.SQLException if fetch from database fails
     */
    public double expensesInADay(String strDate) throws SQLException { 
        double inADay = 0;
        for (Integer cents: activityDao.findExpensesForTheChosenTimePeriod(conv.stringToDate(strDate), conv.stringToDate(strDate))) {
            inADay += cents;
        }
        return conv.toEuros(inADay);
    }
    
    /**
     * Method formats decimals to match "#0.00"
     * @param formatThis - double to format
     * @return Double in formatted to #0.00
     */
    public double formatDecimals(double formatThis) {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
        otherSymbols.setDecimalSeparator('.');
        DecimalFormat formatter = new DecimalFormat("#0.00", otherSymbols); 
        return Double.parseDouble(formatter.format(formatThis));
    }
    
    /**
     * Method returns budget in euros
     * @return budget in euros formatted to #0.00
     */
    public double getBudget() {
        return conv.toEuros(budget);
    }
    
    /**
     * Method counts savings from budget for a chosen time period
     * @return savings in euros
     */
    public double countSavings() {
        if (countBudgetForChosenPeriod() - sumOfExpensesByDate() < 0) {
            return 0.0;
        }
        return formatDecimals(countBudgetForChosenPeriod() - sumOfExpensesByDate());
    }
}

