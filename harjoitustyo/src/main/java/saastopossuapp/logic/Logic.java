
package saastopossuapp.logic;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import saastopossuapp.dao.ActivityDaoInterface;
import saastopossuapp.dao.UserAccountDaoInterface;
import saastopossuapp.domain.Activity;
import saastopossuapp.domain.UserAccount;

/**
 * Class is responsible for application logic. 
 * When calculations are needed, this class calls class Analysis. 
 * When convertions are needed, this class calls class Converter.
 */
public class Logic {
    private UserAccountDaoInterface userAccountDao;
    private ActivityDaoInterface activityDao;
    private String username;
    private Analysis analysis;
    private Converter conv;
    
    
    public Logic(UserAccountDaoInterface userDao, ActivityDaoInterface activityDao) {
        this.userAccountDao = userDao;
        this.activityDao = activityDao;
        this.analysis = new Analysis(activityDao, userDao);
        this.conv = new Converter();
        
        
    }
    
    /**
     * Method checks that username exists in the database and that it's valid.
     * @param   username   Username on the login-field.
     * @return true, if username exists, else false
     * @throws java.sql.SQLException    if fetch from database non-successfull
     */
    public Boolean checkUsername(String username) throws SQLException {
        if (!validateStringInput(username) | userAccountDao.findOne(username.trim()) == null) {
            return false;
        }
        this.username = username;
        return true;
    }
    
    /**
     * Method creates list of series for barChart.
     * @param   fromDate   Date from activities are being fetch
     * @param   untilDate  Date until activities are being fetch
     * @return list of series
     * @throws java.sql.SQLException    if fetch from database non-successfull
     */
    public ArrayList<XYChart.Series> createSerie(LocalDate fromDate, LocalDate untilDate) throws SQLException {
        ArrayList<XYChart.Series> series = new ArrayList<>();
        Date after = conv.localDateToDate(fromDate);
        Date before = conv.localDateToDate(untilDate);
        HashMap<String, ArrayList<Activity>> categorized = activityDao.findAllByCategory(after, before, username);
        for (String strDate: categorized.keySet()) { 
            XYChart.Series series1 = new XYChart.Series();
            series1.setName(strDate);
            for (Activity a: categorized.get(strDate)) {
                series1.getData().add(new XYChart.Data(conv.dateToString(a.getDate()), conv.toEuros(a.getCents())));
            }
            series.add(series1);
        }
        return series;
 
    }
    public ObservableList<String> arrangedXAxisCategories(LocalDate fromDate, LocalDate untilDate) throws SQLException {
        HashMap<String, ArrayList<Activity>> categorized = activityDao.findAllByCategory(conv.localDateToDate(fromDate), conv.localDateToDate(untilDate), username);
        ObservableList<String> listOfStrDates = FXCollections.observableArrayList();
        for (String strDate: categorized.keySet()) { 
            for (Activity a: categorized.get(strDate)) {
                if (!listOfStrDates.contains(conv.dateToString(a.getDate()))) {
                    listOfStrDates.add(conv.dateToString(a.getDate()));
                }
            }
        }        
        Collections.sort(listOfStrDates);
        return listOfStrDates;
    }
    
    /**
     * Method adds expense to category by accessing activityDao
     * @param   newCategoryName   name for the new category defined by user
     * @param   category  name of the existing category chosen by user
     * @param   euros  amount of euros
     * @param   cents  amount of cents
     * @param   transactionDate  date when the expense was made defined by user
     * @return true, if expense is valid and saving to database was made.
     */
    public Boolean addExpense(String newCategoryName, String category, String euros, String cents, LocalDate transactionDate) {
        if (validateNumberInput(euros, correctCents(cents))) { 
            try {
                int expense = conv.toCents(euros, correctCents(cents));
                Activity activity = new Activity(username, expense, conv.localDateToDate(transactionDate), category);
                activity.setActivityId(activityDao.findAll().size() + 1); 
                if (!category.equals("create new")) {
                    activityDao.saveOrUpdate(activity, username);
                    return true;
                } else if (category.equals("create new") && validateStringInput(newCategoryName)) {
                    activity.setCategory(newCategoryName);
                    activityDao.saveOrUpdate(activity, username);
                    return true;
                }
            } catch (SQLException ex) {
                Logger.getLogger(Logic.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
    
    /**
     * Method changes user's budget 
     * @param   euros   amount in euros
     * @param   cents   amount in cents
     * @return true, if budget updating happened 
     */
    public Boolean changeBudget(String euros, String cents) {
        if (validateNumberInput(euros, correctCents(cents))) {
            int budget = conv.toCents(euros, correctCents(cents));
            try {
                userAccountDao.updateBudget(username, budget);
                return true;
            } catch (SQLException ex) {
                Logger.getLogger(Logic.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
    
    /**
     * Method crates String that is placed for totalExpenses -label in UserInterface by accessing Analysis-class
     * @param   fromDate   Date from the expenses are being fetch
     * @param   untilDate    Date until the expenses are being fetch
     * @return true, if budget updating happened 
     */
    public String getBudgetAnalysis(LocalDate fromDate, LocalDate untilDate) {   
        if (validateDate(fromDate, untilDate)) {
            try {
                analysis.setList(fromDate, untilDate, username);
                StringBuilder sb = new StringBuilder();
                sb.append("Total expenses in the chosen time period: ")
                        .append(conv.toEuros(analysis.sumOfExpensesByDate())).append("€")
                        .append(" (average ").append(analysis.countAverage()).append("€/day in the chosen time period)")
                        .append("\nYour daily budget: ").append(conv.toEuros(analysis.getBudget())).append("€")
                        .append("\nYour budget for chosen time period: ").append(analysis.countBudgetForChosenPeriod()).append("€, (spent in the chosen time period: ")
                        .append(analysis.countExpensePercentageFromBudget()).append("%)");
                return sb.toString();
            } catch (SQLException ex) {
                Logger.getLogger(Logic.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return "Check the dates!";
    }
    
    /**
     * Method creates String for expenseLabel in UserInterface by accessing Analysis-class
     * @param   dateString   date that is chosen by user by hovering over a bar in a chart
     * @return   String that describes total expenses on a chosen date
     */
    public String getExpenseLabelText(String dateString) {
        try {
            return "Total expenses on " + dateString + ": " + conv.toEuros(analysis.expensesInADay(dateString)) + " €";
        } catch (SQLException ex) {
            Logger.getLogger(Logic.class.getName()).log(Level.SEVERE, null, ex);
        }
        return " ";
    }
    
    /**
     * Method creates new user by accessing UserAccountDao-class
     * @param   newUsername  username that is chosen by user
     * @return   true, if username is valid and saving to database successfull
     */
    public Boolean createUser(String newUsername) {
        if (validateStringInput(newUsername)) {
            try {
                userAccountDao.saveOrUpdate(new UserAccount(newUsername));
                return true;
            } catch (SQLException ex) {
                Logger.getLogger(Logic.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
    
    /**
     * Method creates choices for ComboBox-element by fetching existing categories from chosen time period
     * @param   fromDate   date from categories are being fetch
     * @param   untilDate   date until categories are being fetch
     * @return  ObservableList of categories user has created
     * @throws java.sql.SQLException    if fetch from database non-successfull
     */
    public ObservableList<String> createChoices(LocalDate fromDate, LocalDate untilDate) throws SQLException {
        Date after = conv.localDateToDate(fromDate);
        Date before = conv.localDateToDate(untilDate);
        HashMap<String, ArrayList<Activity>> categorized = activityDao.findAllByCategory(after, before, username);
      
        ObservableList<String> items = FXCollections.observableArrayList();
        for (String category: categorized.keySet()) {
            items.add(category.trim());
        }
        items.add("create new");
        return items;
    
    }

    /**
     * Method corrects user input so that missing input in field cents is corrected to "00" and missing second number to "0".
     * @param   cents   amount of cents
     * @return   corrected cents value
     */
    public String correctCents(String cents) {
        if (cents.isEmpty()) {
            cents = "00";
        }
        if (cents.length() == 1) {
            cents = cents + "0";
        }
        return cents;
    }

    /**
     * Method validates that user's input in fields euros and cents contains only numbers, isn't null and is greater than 0 and cents length is max 2 numbers. 
     * @param   euros    input in field euros
     * @param   cents   input in field cents
     * @return  true if input is valid, else false
     */
    public boolean validateNumberInput(String euros, String cents) {
        if (euros.matches("[0-9]*") && !euros.equals("")
               && cents.matches("[0-9]*")
               && (cents.length() <= 2)
               && (Integer.parseInt(euros) + Integer.parseInt(cents) > 0)) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Method validates that chosen time period's ending day is after beginning day
     * @param   beginningDate     date from time period begins
     * @param    endDate     date when time period ends
     * @return   true    if time period is valid, else false
     */
    public  Boolean validateDate(LocalDate beginningDate, LocalDate endDate) {
        if (conv.localDateToDate(beginningDate).before(conv.localDateToDate(endDate)) | conv.localDateToDate(beginningDate).equals(conv.localDateToDate(endDate))) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Method validates String input so that it only contains letters (A-Z or a-z), numbers (0-9) and that it's length is 2-30 characters.
     * @param   input    input defined by user
     * @return   true     if input is valid, else false
     */
    public Boolean validateStringInput(String input) {
        return ((input != null) && input.matches("[A-Za-z0-9_]+") && input.length() >= 2 && input.length() <= 30);
    }
    public String setBarChartTitle(LocalDate fromDate, LocalDate untilDate) {
        return "Daily Expenses from " + conv.localDateToString(fromDate) + "-" + conv.localDateToString(untilDate);
    }

}