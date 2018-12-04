
package saastopossuapp.logic;

import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import saastopossuapp.dao.ActivityDao;
import saastopossuapp.dao.UserAccountDao;
import saastopossuapp.domain.Activity;
import saastopossuapp.domain.UserAccount;


public class Logic {
    private UserAccountDao userAccountDao;
    private ActivityDao activityDao;
    private String username;
    private Analysis analysis;
    
    
    public Logic(UserAccountDao userDao, ActivityDao activityDao) {
        this.userAccountDao = userDao;
        this.activityDao = activityDao;
        this.analysis = new Analysis(activityDao, userDao);
        
        
    }
    
     /**
     * Method checks if username exists in the database.
     *
     * @param   username   Username on the login-field.
     * 
     * @return true, if username exists
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
     *
     * @param   afterDatePicker   Date from activities are being fetch
     * @param   beforeDatePicker  Date until activities are being fetch
     * 
     * @return list of series
     */
    public ArrayList<XYChart.Series> createSerie(LocalDate afterDatePicker, LocalDate beforeDatePicker) throws SQLException {
        ArrayList<XYChart.Series> series = new ArrayList<>();
        Date after = localDateToDate(afterDatePicker);
        Date before = localDateToDate(beforeDatePicker);
        HashMap<String, ArrayList<Activity>> categorized = activityDao.findAllByCategory(after, before, username);
        for (ArrayList<Activity> a : categorized.values()) {
            Collections.sort(a);
        }
        for (String category: categorized.keySet()) { 
            XYChart.Series series1 = new XYChart.Series();
            series1.setName(category);
            for (Activity a: categorized.get(category)) {
                series1.getData().add(new XYChart.Data(dateToString(a.getDate()), toEuros(a.getCents())));
            }
            series.add(series1);
        }
        return series;
    }
    
    public Boolean addExpense(String newCategoryField, String category, String euros, String cents, Date transactionDate) {
        if (validateNumberInput(euros, correctCents(cents))) { 
            try {
                int expense = toCents(euros, correctCents(cents));
                Activity activity = new Activity(username, expense, transactionDate, category);
                activity.setActivityId(activityDao.findAll().size() + 1); 
                if (!category.equals("create new")) {
                    activityDao.saveOrUpdate(activity, username);
                    return true;
                } else if (category.equals("create new") && validateStringInput(newCategoryField)) {
                    activity.setCategory(newCategoryField);
                    activityDao.saveOrUpdate(activity, username);
                    return true;
                }
            } catch (SQLException ex) {
                Logger.getLogger(Logic.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
    
    public Boolean changeBudget(String euros, String cents) {
        if (validateNumberInput(euros, correctCents(cents))) {
            int budget = toCents(euros, correctCents(cents));
            try {
                userAccountDao.updateBudget(username, budget);
                return true;
            } catch (SQLException ex) {
                Logger.getLogger(Logic.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
    
    public String getBudgetAnalysis(LocalDate afterDatePicker, LocalDate beforeDatePicker) {        //Pilko pienemmiksi      
        if (validateDate(afterDatePicker, beforeDatePicker)) {
            try {
                analysis.setList(afterDatePicker, beforeDatePicker, username);
                StringBuilder sb = new StringBuilder();
                sb.append("Total expenses in the chosen time period: ")
                        .append(toEuros(analysis.sumOfExpensesByDate())).append("€")
                        .append(" (average ").append(analysis.countAverage()).append("€/day in the chosen time period)")
                        .append("\nYour daily budget: ").append(toEuros(analysis.getBudget())).append("€")
                        .append("\nYour budget for chosen time period: ").append(analysis.countBudgetForChosenPeriod()).append("€, (spent int the chosen time period: ")
                        .append(analysis.countExpensesFromBudget()).append("%)");
                return sb.toString();
            } catch (SQLException ex) {
                Logger.getLogger(Logic.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return "Check the dates!";
    }
    public String getExpenseLabelText(String dateString) {
        return "Total expenses on " + dateString + ": " + toEuros(analysis.expensesInADay(dateString)) + " €";
    }
    
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
    
    public ObservableList<String> createChoices(LocalDate afterDatePicker, LocalDate beforeDatePicker) throws SQLException {
        Date after = localDateToDate(afterDatePicker);
        Date before = localDateToDate(beforeDatePicker);
        HashMap<String, ArrayList<Activity>> categorized = activityDao.findAllByCategory(after, before, username);
      
        ObservableList<String> items = FXCollections.observableArrayList();
        for (String category: categorized.keySet()) {
            items.add(category.trim());
        }
        items.add("create new");
        return items;
    
    }
    
    public int toCents(String euros, String cents) {
        int euro = Integer.parseInt(euros) * 100;
        int total = (euro + Integer.parseInt(cents));
        return total;
    }
    
    public double toEuros(int cents) {
        double euros =  (double) cents / (double) 100;
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
        otherSymbols.setDecimalSeparator('.');
        DecimalFormat formatter = new DecimalFormat("#0.00", otherSymbols);
        return Double.parseDouble(formatter.format(euros));
    }
    
    public String correctCents(String cents) {
        if (cents.isEmpty()) {
            cents = "00";
        }
        if (cents.length() == 1) {
            cents = cents + "0";
        }
        return cents;
    }
    
    public Date localDateToDate(LocalDate datePicker) {
        return java.sql.Date.valueOf(datePicker);
    }
    
    public String localDateToString(LocalDate localDate) {
        Date date = localDateToDate(localDate);
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return dateFormat.format(date);
    }
    
    public String dateToString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return dateFormat.format(date);
    }
    
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
    
    public  Boolean validateDate(LocalDate after, LocalDate before) {
        if (localDateToDate(after).before(localDateToDate(before)) | localDateToDate(after).equals(localDateToDate(before))) {
            return true;
        } else {
            return false;
        }
    }
    
    public Boolean validateStringInput(String input) {
        return ((input != null) && input.matches("[A-Za-z0-9_]+") && input.length() >= 1 && input.length() <= 20);
    }
    
}