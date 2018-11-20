
package saastopossuapp.domain;

import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;
import saastopossuapp.UserInterface;
import saastopossuapp.dao.ActivityDao;
import saastopossuapp.dao.UserDao;


public class Logic {
    private UserDao userDao;
    private ActivityDao activityDao;
    private UserInterface userInterface;
    
    public Logic (UserDao userDao, ActivityDao activityDao){
        this.userDao = userDao;
        this.activityDao = activityDao;
    }
    
    public Boolean checkUsername(String username) throws SQLException{
        if (userDao.findOne(username.trim())==null){
            return false;
        }
        return true;
        
    }
    public BarChart <String, Number> createChart(LocalDate afterDatePicker, LocalDate beforeDatePicker, String expenseLabel, String passwordField) throws SQLException{
        Date after = convertToDate(afterDatePicker);
        Date before = convertToDate(beforeDatePicker);
                
        HashMap <Date, Integer> map = activityDao.findAllByDate(after, before, passwordField);
        System.out.println("chartmap: " + map);
        CategoryAxis xAkseli = new CategoryAxis();
        xAkseli.setLabel("date");
        
        NumberAxis yAkseli = new NumberAxis();
        yAkseli.setLabel("€");
        BarChart<String, Number> pylvaskaavio = new BarChart<>(xAkseli, yAkseli);

        pylvaskaavio.setTitle("Daily Expenses");
        pylvaskaavio.setLegendVisible(false);
                
        XYChart.Series data = new XYChart.Series();
        map.keySet().stream().sorted().forEach(pari -> {
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            String strDate = dateFormat.format(pari);
            data.setName(strDate); 
            data.getData().add(new XYChart.Data(strDate, toEuros(map.get(pari))));
        });
        
        pylvaskaavio.getData().add(data);
        return pylvaskaavio;
       
    }
    public Boolean addExpense(String euros, String cents, Date transactionDate, String passwordField){
        if (validateIntegerInput(euros, cents)){
            try {
                if (cents.equals("")){
                    cents = "00";
                }
                int expense = toCents(Integer.parseInt(euros), Integer.parseInt(cents));
                Activity activity = new Activity(expense);
                activity.setActivityId(activityDao.findAll().size()+1); 
                System.out.println("findall+1 : " + activityDao.findAll().size() + 1);
                activity.setActivitysUser(passwordField); 
                System.out.println("password: "+passwordField);
                activity.setDate(transactionDate);
                activityDao.save(activity);
                return true;
            } catch (SQLException ex) {
                Logger.getLogger(Logic.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
    public Boolean changeBudget(String euros, String cents, String passwordField){
        if (validateIntegerInput(euros, cents)){
            if (cents.equals("")){
                cents = "00";
            }
            String username = passwordField;
            int budget = toCents(Integer.parseInt(euros), Integer.parseInt(cents));
            try {
                userDao.updateBudget(username, budget);
                return true;
            }catch (SQLException ex) {
                Logger.getLogger(Logic.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
        
    }
    public int toCents(int euros, int cents){
        euros = euros*100;
        int total = (euros+cents);
        return total;
        
    }
    public double toEuros(int cents){
        double euros =  (double)cents/(double)100;
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
        otherSymbols.setDecimalSeparator('.');
        DecimalFormat formatter = new DecimalFormat("##.00", otherSymbols);
        return Double.parseDouble(formatter.format(euros));

    }
    public Date convertToDate(LocalDate datePicker) {
        return java.sql.Date.valueOf(datePicker);
        
    }
    //hakee aikavälin menot ja laskee osuuden budjetista
    public String getBudgetAnalysis(LocalDate afterDatePicker, LocalDate beforeDatePicker, String passwordField){            
        if (validateDate(afterDatePicker, beforeDatePicker)){
           //haetaan budjetti: 
            double budget = 0;
            try {
                budget = toEuros(userDao.findOne(passwordField).getUserBudget());
            } catch (SQLException ex) {
                Logger.getLogger(Logic.class.getName()).log(Level.SEVERE, null, ex);
            }

            //haetaan menot: 
            Date after = convertToDate(afterDatePicker);
            Date before = convertToDate(beforeDatePicker);

            HashMap <Date, Integer> map = new HashMap<>();
            try {
                map = activityDao.findAllByDate(after, before, passwordField);
            } catch (SQLException ex) {
                Logger.getLogger(Logic.class.getName()).log(Level.SEVERE, null, ex);
            }
            int totalExpenses = 0;
            for (Integer expense: map.values()){
                totalExpenses+=expense;
            }        

            //montako päivää aikavälillä on? 
            int days  = Math.abs((int)afterDatePicker.toEpochDay()-1 - (int)beforeDatePicker.toEpochDay());
            System.out.println("päiviä aikavälillä: " + days);

            double expensesFromBudget = ((double)totalExpenses/(double)days)/(double)budget;
            DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
            otherSymbols.setDecimalSeparator('.');
            DecimalFormat formatter = new DecimalFormat("##.00", otherSymbols); /////////////////////miten
            expensesFromBudget = Double.parseDouble(formatter.format(expensesFromBudget));

            double budgetForChosenPeriod = (double)days*(double)budget;
            budgetForChosenPeriod = Double.parseDouble(formatter.format(budgetForChosenPeriod));

            double difference = budget - ((totalExpenses/days));           /////////////////VÄÄRIN
            difference = Double.parseDouble(formatter.format(difference/100));

            StringBuilder sb = new StringBuilder();
            sb.append("Total expenses from shown period: ").append(toEuros(totalExpenses))
                    .append("\nYour daily budget: ").append(budget).append("€ (").append(difference).append ("€/day on shown period)")
                    .append("\nYour budget for shown period: ").append(budgetForChosenPeriod).append(", (spent on shown period: ")
                    .append (expensesFromBudget).append("%)");
            return sb.toString();
        }else{
            return "Check dates!";
        }
    }
    public Boolean createUser(TextField signInField){
        try {
            String username = signInField.getText();
            UserAccount user = new UserAccount(username);
            user.setUserId(userDao.findAll().size()+1);
            user.setUserBudget(1000);
            userDao.save(user);
        } catch (SQLException ex) {
            Logger.getLogger(Logic.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    
    private boolean validateIntegerInput(String euros, String cents){
        if (cents.equals("") && !euros.equals("")){
            cents = "00";
        }
        if (euros.matches("[0-9]*")&& !euros.equals("")
                && cents.matches("[0-9]*")
                && cents.length()==2
                && (Integer.parseInt(euros)+Integer.parseInt(cents)>0)){
            return true;
        }else{
            return false;
        }
    }
    public Boolean validateDate(LocalDate after, LocalDate before){
        if (convertToDate(after).before(convertToDate(before))|convertToDate(after).equals(convertToDate(before))){
           return true;
        }else{
           return false;
        }
   }
}
