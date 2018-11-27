
package saastopossuapp.logic;

import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import saastopossuapp.dao.ActivityDao;
import saastopossuapp.dao.UserDao;
import saastopossuapp.domain.Activity;
import saastopossuapp.domain.UserAccount;


public class Logic {
    private UserDao userDao;
    private ActivityDao activityDao;
    
    public Logic(UserDao userDao, ActivityDao activityDao) {
        this.userDao = userDao;
        this.activityDao = activityDao;
    }
    
     /**
     * Method checks if username exists in the database.
     *
     * @param   username   Username on the login-field.
     * 
     * @return true, if username exists
     */
    public Boolean checkUsername(String username) throws SQLException {
        if (userDao.findOne(username.trim()) == null) {
            return false;
        }
        return true;
    }
    
    /**
     * Method creates list of series for barChart.
     *
     * @param   afterDatePicker   Date from activities are being fetch
     * @param   beforeDatePicker  Date until activities are being fetch
     * 
     * @return list of series
     */
    public ArrayList<XYChart.Series> createSerie(LocalDate afterDatePicker, LocalDate beforeDatePicker, String passwordField) throws SQLException {
        ArrayList<XYChart.Series> series = new ArrayList<>();
        Date after = convertToDate(afterDatePicker);
        Date before = convertToDate(beforeDatePicker);
        HashMap<String, ArrayList<Activity>> categorized = activityDao.findAllByCategory(after, before, passwordField);
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        
        for (String category: categorized.keySet()) { 
            XYChart.Series series1 = new XYChart.Series();
            series1.setName(category);
            for (Activity a: categorized.get(category)) {
                String strDate = dateFormat.format(a.getDate());
                series1.getData().add(new XYChart.Data(strDate, toEuros(a.getCents())));
            }
            series.add(series1);
        }
        return series;
    }
    
    public ObservableList<String> arrangeXAxis(ArrayList<XYChart.Series> list, LocalDate afterDatePicker, LocalDate beforeDatePicker, String passwordField) throws SQLException {     
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date after = convertToDate(afterDatePicker);
        Date before = convertToDate(beforeDatePicker);
        HashMap<String, ArrayList<Activity>> categorized = activityDao.findAllByCategory(after, before, passwordField);
        
        ObservableList<String> arranged = FXCollections.observableArrayList();
        
        for (String category: categorized.keySet()) { 
            for (Activity a: categorized.get(category)) {
                String strDate = dateFormat.format(a.getDate());
                if (!arranged.contains(strDate)) {
                    arranged.add(strDate);
                }
            }
        }
        return arranged.sorted();
    }
    
    public Boolean addExpense(String newCategoryField, String category, String euros, String cents, Date transactionDate, String passwordField) {
        if (validateIntegerInput(euros, cents)) {
            try {
                if (cents.equals("")) {
                    cents = "00";
                }
                int expense = toCents(Integer.parseInt(euros), Integer.parseInt(cents));
                Activity activity = new Activity(expense);
                activity.setActivityId(activityDao.findAll().size() + 1); 
                activity.setActivitysUser(passwordField); 
                activity.setDate(transactionDate);
                
                if (category.equals("create new")) {
                    activity.setCategory(newCategoryField);
                } else {
                    activity.setCategory(category);
                }
                activityDao.saveOrUpdate(activity, passwordField);
                return true;
            } catch (SQLException ex) {
                Logger.getLogger(Logic.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
        return false;
    }
    
    public Boolean changeBudget(String euros, String cents, String passwordField) {
        if (validateIntegerInput(euros, cents)) {
            if (cents.equals("")) {
                cents = "00";
            }
            int budget = toCents(Integer.parseInt(euros), Integer.parseInt(cents));
            try {
                userDao.updateBudget(passwordField, budget);
                return true;
            } catch (SQLException ex) {
                Logger.getLogger(Logic.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
    
    public int toCents(int euros, int cents) {
        euros = euros * 100;
        int total = (euros + cents);
        return total;
    }
    
    public double toEuros(int cents) {
        double euros =  (double) cents / (double) 100;
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
        otherSymbols.setDecimalSeparator('.');
        DecimalFormat formatter = new DecimalFormat("#0.00", otherSymbols);
        return Double.parseDouble(formatter.format(euros));
    }
    
    public Date convertToDate(LocalDate datePicker) {
        return java.sql.Date.valueOf(datePicker);
    }
    
    public String getBudgetAnalysis(LocalDate afterDatePicker, LocalDate beforeDatePicker, String passwordField) {            
        if (validateDate(afterDatePicker, beforeDatePicker)) {
            double budget = 0;
            try {
                budget = toEuros(userDao.findOne(passwordField).getUserBudget());
            } catch (SQLException ex) {
                Logger.getLogger(Logic.class.getName()).log(Level.SEVERE, null, ex);
            }

            Date after = convertToDate(afterDatePicker);
            Date before = convertToDate(beforeDatePicker);

            HashMap<Date, Integer> map = new HashMap<>();
            try {
                map = activityDao.findAllByDate(after, before, passwordField);
            } catch (SQLException ex) {
                Logger.getLogger(Logic.class.getName()).log(Level.SEVERE, null, ex);
            }
            int totalExpenses = 0;
            for (Integer expense: map.values()) {
                totalExpenses += expense;
            }        
            
            int days  = Math.abs((int) afterDatePicker.toEpochDay() - 1 - (int) beforeDatePicker.toEpochDay());
            double expensesFromBudget = countExpensesFromBudget(totalExpenses, days, budget);
            double budgetForChosenPeriod = countBudgetForChosenPeriod(days, budget);
            double difference = countAverage(totalExpenses, days);

            StringBuilder sb = new StringBuilder();
            sb.append("Total expenses in the chosen time period: ").append(toEuros(totalExpenses)).append("€").append(" (average ").append(difference).append("€/day in the chosen time period)")
                    .append("\nYour daily budget: ").append(budget).append("€")
                    .append("\nYour budget for chosen time period: ").append(budgetForChosenPeriod).append("€, (spent int the chosen time period: ")
                    .append(expensesFromBudget).append("%)");
            return sb.toString();
        } else {
            return "Check the dates!";
        }
    }
    
    public Boolean createUser(String signInField) {
        try {
            String username = signInField;
            UserAccount user = new UserAccount(username);
            user.setUserId(userDao.findAll().size() + 1);
            userDao.save(user);
        } catch (SQLException ex) {
            Logger.getLogger(Logic.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    
    public boolean validateIntegerInput(String euros, String cents) {
        if (cents.equals("") && !euros.equals("")) {
            cents = "00";
        }
        if (euros.matches("[0-9]*") && !euros.equals("")
               && cents.matches("[0-9]*")
               && cents.length() == 2
               && (Integer.parseInt(euros) + Integer.parseInt(cents) > 0)) {
            return true;
        } else {
            return false;
        }
    }
    
    public  Boolean validateDate(LocalDate after, LocalDate before) {
        if (convertToDate(after).before(convertToDate(before)) | convertToDate(after).equals(convertToDate(before))) {
            return true;
        } else {
            return false;
        }
    }
    
    public ObservableList<String> createChoices(LocalDate afterDatePicker, LocalDate beforeDatePicker, String passwordField) throws SQLException {
        Date after = convertToDate(afterDatePicker);
        Date before = convertToDate(beforeDatePicker);
        HashMap<String, ArrayList<Activity>> categorized = activityDao.findAllByCategory(after, before, passwordField);
      
        ObservableList<String> items = FXCollections.observableArrayList();
        for (String category: categorized.keySet()) {
            items.add(category.trim());
        }
        items.add("create new");
        return items;
    }
    
    public String doubleToString(double formatThis) {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
        otherSymbols.setDecimalSeparator('.');
        DecimalFormat formatter = new DecimalFormat("#0.00", otherSymbols); 
        return formatter.format(formatThis);
    }
    
    public double countExpensesFromBudget(int totalExpenses, int days, double budget) {
        double expensesFromBudget = ((double) totalExpenses / (double) days) / (double) budget;
        return Double.parseDouble(doubleToString(expensesFromBudget));
    }
    
    public double countBudgetForChosenPeriod(int days, double budget) {
        double budgetForChosenPeriod = (double) days * (double) budget;
        return Double.parseDouble(doubleToString(budgetForChosenPeriod));
    }
    
    public double countAverage(double totalExpenses, int days) {
        double difference = (totalExpenses / days);
        return Double.parseDouble(doubleToString(difference / 100.0));
    }
}