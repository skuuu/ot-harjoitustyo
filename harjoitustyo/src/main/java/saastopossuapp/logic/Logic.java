
package saastopossuapp.logic;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;
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
    private final UserAccountDaoInterface userAccountDao;
    private final ActivityDaoInterface activityDao;
    private Analysis analysis;
    private final Converter conv;
    private TreeMap<String, ArrayList<Activity>> expensesByCategory;
    public LocalDate after;
    public LocalDate before;
    private String username;
    private ArrayList<XYChart.Series> serieslist;
    
    public Logic(UserAccountDaoInterface userAccountDao, ActivityDaoInterface activityDao, LocalDate after, LocalDate before) {
        this.userAccountDao = userAccountDao;
        this.activityDao = activityDao;
        this.after = after;
        this.before = before; 
        this.conv = new Converter();
        this.analysis = null; 
    }
    
    
    /**
     * Method checks that username exists in the database and that it's valid.
     * @param username - Username on the login-field.
     * @return true, if username exists, else false
     */
    public Boolean checkUsername(String username) {
        this.username = username;
        try {
            if (validateUsernameInput(username) && userAccountDao.findOne(username.trim()) != null) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Logic.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    /**
     * Method creates list of series for barChart.
     * @param fromDate - Date from activities are being fetch
     * @param untilDate - Date until activities are being fetch
     * @return list of series
     */
    public ArrayList<XYChart.Series> createSerie(LocalDate fromDate, LocalDate untilDate) {
        ArrayList<XYChart.Series> series = new ArrayList();
        this.after = fromDate;
        this.before = untilDate;
        try {
            this.expensesByCategory = activityDao.findAllByCategory(conv.localDateToDate(fromDate), conv.localDateToDate(untilDate), username);
        } catch (SQLException ex) {
            Logger.getLogger(Logic.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (String category: expensesByCategory.keySet()) { 
            XYChart.Series series1 = new XYChart.Series();
            series1.setName(category);
            for (Activity a: expensesByCategory.get(category)) {
                series1.getData().add(new XYChart.Data(conv.dateToString(a.getDate()), conv.toEuros(a.getCents())));
            }
            series.add(series1);
        }
        this.serieslist = series;
        return series;
    }    

    /**
     * Method arranges barChart's XAxis categories (dates) to ascending order from oldest to newest date (left-to-right)
     * @return sorted ObservableList of dates of type String
     */
    public ObservableList<String> arrangedXAxisCategories() {
        ObservableList<String> listOfStrDates = FXCollections.observableArrayList();
        for (String strDate: expensesByCategory.keySet()) { 
            for (Activity a: expensesByCategory.get(strDate)) {
                if (!listOfStrDates.contains(conv.dateToString(a.getDate()))) {
                    listOfStrDates.add(conv.dateToString(a.getDate()));
                }
            }
        }        
        Collections.sort(listOfStrDates);
        return listOfStrDates;
    }
    
    /**
     * Method creates choices for ComboBox-element by fetching existing categories from chosen time period
     * @return ObservableList of categories user has created
     */
    public ObservableList<String> createChoices() {
        ObservableList<String> items = FXCollections.observableArrayList();
        for (String category: expensesByCategory.keySet()) {
            items.add(category.trim());
        }
        items.add("create new");
        return items;
    
    }
    
    /**
     * Method adds expense to category by accessing activityDao
     * @param newCategoryName - name for the new category defined by user
     * @param category - name of the existing category chosen by user
     * @param euros - amount of euros
     * @param cents - amount of cents
     * @param transactionDate  date when the expense was made defined by user
     * @param description description of the expense defined by the user
     * @return true, if expense is valid and saving to database was made.
     */
    public Boolean addExpense(String newCategoryName, String category, String euros, String cents, LocalDate transactionDate, String description) {
        if (validateNumberInput(euros, correctCents(cents)) && validateTextInput(description)) { 
            try {
                int expense = conv.toCents(euros, correctCents(cents));
                Activity activity = new Activity(username, expense, conv.localDateToDate(transactionDate), category, description);
                if (!category.equals("create new")) {
                    activityDao.save(activity);
                    return true;
                } else if (category.equals("create new") && validateNewCategory(newCategoryName)) {
                    activity.setCategory(newCategoryName);
                    activityDao.save(activity);
                    return true;
                }
            } catch (SQLException ex) {
                Logger.getLogger(Logic.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
        return false;
    }
    
     /**
     * Method validates category name. 
     * Category name must be unique, contain only letters a-z, A-Z and numbers 0-9
     * Categories are limited to 7 categories.
     * @param  newCategoryName - name for the new category defined by user.
     * @return true, if category name is valid, else false.
     */
    private boolean validateNewCategory(String newCategoryName) {
        return validateTextInput(newCategoryName) && createChoices().size() <= 7 && !createChoices().contains(newCategoryName);
    }
    
     /**
     * Method returns speific input error message when user tries to add expense that isn't valid.
     * @param newCategoryName - name for the new category defined by user
     * @param category - existing category chosen from ComboBox
     * @param euros - amount in euros
     * @param  cents - amount in cents
     * @param description - expense description
     * @return error message defining the input error
     */
    public String getInputErrorMessage(String newCategoryName, Object category, String euros, String cents, String description) {
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid input.");
        if (category == null) {
            sb.append("\nChoose category!");
        } else if (category.equals("create new")) {
            if (createChoices().size() > 7) {
                sb.append("\nExpense Categories are limited to 7 categories.");
            } else if (createChoices().contains(newCategoryName)) {
                sb.append("\nExpense Category must be unique.");
            } else if (!validateTextInput(newCategoryName)) {
                sb.append("\nExpense Category must be 3-20 characters:\nletters a-z, A-Z and/or numbers 0-9.");
            } else if (!validateTextInput(description)) {
                sb.append("\nExpense Description must be 3-20 characters:\nletters a-z, A-Z and/or numbers 0-9.");
            } else {
                sb.append("\nExpense Amount must be over 0€: \ncents max. 2 digits, euros min. 1 digit.");
            }
        } else {
            if (!validateTextInput(description)) {
                sb.append("\nExpense Description must be 3-20 characters:\nletters a-z, A-Z and/or numbers 0-9.");
            } else {
                sb.append("\nExpense Amount must be over 0€: \ncents max. 2 digits, euros min. 1 digit.");
            }
        }
        return sb.toString();
    }
    
    /**
     * Method changes user's budget 
     * @param euros - amount in euros
     * @param  cents - amount in cents
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
     * @return true, if budget updating happened 
     */
    public String getBudgetAnalysis() {  
        if (validateDate(after, before)) {
            try {
                analysis = new Analysis(activityDao, userAccountDao, after, before, username);
            } catch (SQLException ex) {
                Logger.getLogger(Logic.class.getName()).log(Level.SEVERE, null, ex);
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Total expenses in the chosen time period: ")
                    .append(analysis.sumOfExpensesByDate()).append("€")
                    .append(" (average ").append(analysis.countAverage()).append("€/day in the chosen time period)")
                    .append("\nYour daily budget: ").append(analysis.getBudget()).append("€")
                    .append("\nYour budget for chosen time period: ").append(analysis.countBudgetForChosenPeriod()).append("€, (spent in the chosen time period: ")
                    .append(analysis.countExpensePercentageFromBudget()).append("%)")
                    .append("\nYou have saved: ").append(analysis.countSavings()).append("€");
            return sb.toString();
        }
        return "";
    }
    
    /**
     * Method creates String for expenseLabel in UserInterface by accessing Analysis-class
     * @param dateString - date that is chosen by user by hovering over a bar in a chart
     * @return String that describes total expenses on a chosen date
     */
    public String getExpenseLabelText(String dateString) {
        String labelText = "not found";
        try {
            labelText = "Total expenses on " + dateString + ": " + analysis.expensesInADay(dateString) + " €";
        } catch (SQLException ex) {
            Logger.getLogger(Logic.class.getName()).log(Level.SEVERE, null, ex);
        }
        return labelText;
    }
    
    /**
     * Method returns list of Activities from the day and from the category
     * @param date - date of type String from Activities are being fetch
     * @param category - category for the Activities
     * @param username - current user's username
     * @return ArrayList of Activities
     */
    public ArrayList<Activity> getDailyExpensesLabel(String date, String category, String username) {
        Date d = conv.stringToDate(date);
        ArrayList<Activity> list = null;
        ArrayList<Activity> activityList = new ArrayList();
        try {
            list = activityDao.findAllByUsername(username);
        } catch (SQLException ex) {
            Logger.getLogger(Logic.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (Activity a: list) {
            if (a.getCategory().equals(category) && (a.getDate().after(d) | a.getDate().equals(d) && (a.getDate().before(d) | a.getDate().equals(d)))) {
                activityList.add(a);
            }
        }           
        return activityList;
    }
    
    /**
     * Method access ActivityDao to delete Activity
     * @param activity - Activity to be deleted
     * @return deleted Activity
     */
    public Activity deleteActivity(Activity activity) {
        try {
            activityDao.deleteOne(activity.getActivityId());
        } catch (SQLException ex) {
            Logger.getLogger(Logic.class.getName()).log(Level.SEVERE, null, ex);
        }
        return activity;
    }

    
    /**
     * Method returns title for the BarChart.
     * @param fromDate - date from expenses are being fetch
     * @param untilDate - date until expenses are being fetch
     * @return String title 
     */
    public String getBarChartTitle(LocalDate fromDate, LocalDate untilDate) {
        return "Daily Expenses from " + conv.localDateToString(fromDate) + "-" + conv.localDateToString(untilDate);
    }
    
    /**
     * Method creates new user by accessing UserAccountDao-class
     * @param newUsername - username that is chosen by user
     * @return true, if username is valid and saving to database successfull
     */
    public Boolean createUser(String newUsername) {
        try {
            if (validateUsernameInput(newUsername) && userAccountDao.findOne(newUsername) == null) {
                userAccountDao.saveOrUpdate(new UserAccount(newUsername));
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Logic.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    /**
     * Method corrects user input so that missing input in field cents is corrected to "00" and missing second number to "0".
     * @param cents - amount of cents
     * @return corrected cents value
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
     * @param euros - input in field euros
     * @param cents - input in field cents
     * @return true if input is valid, else false
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
     * @param beginningDate - date from time period begins
     * @param endDate - date when time period ends
     * @return true if time period is valid, else false
     */
    public Boolean validateDate(LocalDate beginningDate, LocalDate endDate) {
        if (conv.localDateToDate(beginningDate).before(conv.localDateToDate(endDate)) | conv.localDateToDate(beginningDate).equals(conv.localDateToDate(endDate))) {
            return true;
        }
        return false;
    }
    
    /**
     * Method validates String input so that it only contains letters (A-Z or a-z), numbers (0-9) and that it's length is 2-30 characters.
     * @param username input defined by user
     * @return true if input is valid, else false
     */
    public Boolean validateUsernameInput(String username) {
        return ((username != null) && username.matches("[A-Za-z0-9_]+") && username.length() >= 2 && username.length() <= 30);
    }
    
    /**
     * Method validates String input so that it only contains letters (A-Z or a-z), numbers (0-9) and that it's length is 2-30 characters. Also
     * accepts whitespaces. 
     * @param input input defined by user
     * @return true if input is valid, else false
     */
    public Boolean validateTextInput(String input) {
        return ((input != null) && input.matches("[A-Za-z0-9_ ]+") && input.length() > 0 && input.length() <= 30);
    }
}