
package saastopossuapp.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import saastopossuapp.domain.Activity;


public class ActivityDao implements Dao<Activity, Integer, Integer> {
    private Database db;
    
    public ActivityDao(Database db) {
        this.db = db;
    }

    @Override
    public Activity findOne(Integer key) throws SQLException {
        Connection con = db.connect();
        PreparedStatement stmt = con.prepareStatement("SELECT * FROM Activity WHERE ActivityId=?");
        stmt.setInt(1, key);
        ResultSet rs = stmt.executeQuery();
        
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Activity activity = new Activity(rs.getInt("cents"));
        activity.setDate(rs.getDate("date"));
        activity.setActivitysUser(rs.getString("activitysUser"));
        activity.setActivityId(rs.getInt("activityId"));

        stmt.close();
        rs.close();
        con.close();
        return activity;
    }
    @Override
    public List<Activity> findAll() throws SQLException {
        Connection con = db.connect();
        PreparedStatement stmt = con.prepareStatement("SELECT * FROM Activity");
        
        ResultSet rs = stmt.executeQuery();
        List<Activity> activities = new ArrayList<>();

        while (rs.next()) {
            Activity activity = new Activity(rs.getInt("cents"));
            activity.setActivityId(rs.getInt("activityId"));
            activity.setDate(rs.getDate("date"));
            activity.setActivitysUser(rs.getString("activitysUser"));
            activity.setCategory("no category");
            activities.add(activity);
        }
        stmt.close();
        rs.close();
        con.close();
        return activities;
        
    }
    public ArrayList<Activity> findAllByUser(String username) throws SQLException {
        Connection con = db.connect();
        PreparedStatement stmt = con.prepareStatement("SELECT * FROM Activity WHERE activitysuser = (?)");
        stmt.setString(1, username);
        
        ResultSet rs = stmt.executeQuery();
        ArrayList<Activity> activities = new ArrayList<>();

        while (rs.next()) {
            Activity activity = new Activity(rs.getInt("cents"));
            activity.setActivityId(rs.getInt("activityId"));
            activity.setDate(rs.getDate("date"));
            activity.setActivitysUser(rs.getString("activitysuser"));
            activity.setCategory(rs.getString("category"));
            activities.add(activity);
        }
        stmt.close();
        rs.close();
        con.close();
        return activities;
        
        
    }
    public HashMap<Date, Integer> findAllByDate(Date after, Date before, String passwordField) throws SQLException {
        Connection con = db.connect();
        PreparedStatement stmt = con.prepareStatement("SELECT date AS date, SUM(cents) AS cents FROM activity WHERE date >= (?) AND date <= (?) AND activitysuser = (?) GROUP BY date");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate1 = dateFormat.format(after);
        String strDate2 = dateFormat.format(before);
        
        stmt.setDate(1, after);
        stmt.setDate(2, before);
        stmt.setString(3, passwordField);
        
        ResultSet rs = stmt.executeQuery();
        HashMap<Date, Integer> map = new HashMap<>();
        while (rs.next()) {
            map.put(rs.getDate("date"), rs.getInt("cents"));
        }
        stmt.close();
        rs.close();
        con.close();

        return map;
        
    }
    public Integer findExpensesByDate(String date, String passwordField) throws SQLException {
        Connection con = db.connect();
        PreparedStatement stmt = con.prepareStatement("SELECT SUM(cents) AS cents FROM activity WHERE date = (?) AND activitysuser = (?)");
        
        stmt.setDate(1, convertStringToDate(date));
        System.out.println("muunnettu päivä: " + convertStringToDate(date));
        stmt.setString(2, passwordField);
        
        ResultSet rs = stmt.executeQuery();
        int cents = 0; 
        while (rs.next()) {
            cents += rs.getInt("cents");
        }
        stmt.close();
        rs.close();
        con.close();
        System.out.println("cents: " + cents);
        return cents;
        
    }
    @Override
    public void delete(String username) throws SQLException {
        Connection conn = db.connect();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Activity"
                + " WHERE activitysuser = (?)");

        stmt.setString(1, username);
        stmt.executeUpdate();
        stmt.close();
        conn.close(); 

    }
    public Activity saveOrUpdate(Activity lisattava, String password) throws SQLException {
        Connection conn = db.connect();
        List<Activity> all = findAllByUser(password);
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getDate().equals(lisattava.getDate()) && all.get(i).getCategory().trim().equals(lisattava.getCategory().trim())) {
                return update(lisattava, all.get(i));
            }
        }
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Activity"
            + " (activityId, activitysUser, cents, date, category)"
            + " VALUES (?,?,?,?,?)");

        stmt.setInt(1, findAll().size() + 1);
        stmt.setString(2, lisattava.getActivitysUser());
        stmt.setInt(3, lisattava.getCents());
        stmt.setDate(4, lisattava.getDate()); 
        stmt.setString(5, lisattava.getCategory());
        stmt.executeUpdate();
        stmt.close();
        return lisattava;
        
    }
    public Activity update(Activity a, Activity korvattava) throws SQLException {
        Connection con = db.connect();
        PreparedStatement stmt = con.prepareStatement("UPDATE Activity SET"
                + " cents = ? WHERE activityid = ?");
        stmt.setInt(1, korvattava.getCents() + a.getCents());
        stmt.setInt(2, korvattava.getActivityId());

        stmt.executeUpdate();
        stmt.close();
        con.close();

        return korvattava;
        
    }
    public HashMap<String, ArrayList<Activity>> findAllByCategory(Date after, Date before, String password) throws SQLException {
        HashMap<String, ArrayList<Activity>> map = new HashMap<>();
        ArrayList<Activity> activities = findAllByUser(password);
                
        if (activities.isEmpty()) {
            System.out.println("tyhja findAllByCategory");
            return map;
        }
        for (Activity a: activities) {
            if ((a.getDate().after(after) | a.getDate().equals(after)) && (a.getDate().before(before) | a.getDate().equals(before))) {
                if (map.keySet().contains(a.getCategory())) { 
                    map.get(a.getCategory()).add(a);
                } else if (!map.keySet().contains(a.getCategory())) {
                    ArrayList<Activity> list = new ArrayList<>();
                    map.put(a.getCategory(), list);
                    map.get(a.getCategory()).add(a);                   
                }
            }
        }
        return map;
        
    }
    public Date convertStringToDate(String strDate) {
        StringBuilder sb = new StringBuilder();
        sb.append(strDate.substring(6, 10)).append("-").append(strDate.substring(3, 5)).append("-").append(strDate.substring(0, 2));        
        return java.sql.Date.valueOf(sb.toString());
    }
}

