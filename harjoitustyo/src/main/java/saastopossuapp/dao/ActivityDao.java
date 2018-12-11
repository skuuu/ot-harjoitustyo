
package saastopossuapp.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import saastopossuapp.domain.Activity;

/**
 * Class is responsible for database access operations related to Activities
 */
public class ActivityDao implements ActivityDaoInterface {
    private Database db;
    
    public ActivityDao(Database db) {
        this.db = db;
        
    }

    public Activity findOne(Integer key) throws SQLException {
        Connection con = db.getConnection();
        PreparedStatement stmt = con.prepareStatement("SELECT * FROM Activity WHERE ActivityId=?");
        stmt.setInt(1, key);
        ResultSet rs = stmt.executeQuery();
        
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Activity activity = new Activity(rs.getString("activitysUser").trim(), rs.getInt("cents"), rs.getDate("date"), rs.getString("category").trim());
        activity.setActivityId(rs.getInt("activityId"));

        stmt.close();
        rs.close();
        con.close();
        return activity;
    }
    
    @Override
    public List<Activity> findAll() throws SQLException {
        Connection con = db.getConnection();
        PreparedStatement stmt = con.prepareStatement("SELECT activitysuser, cents, date, category, activityid FROM Activity");
        
        ResultSet rs = stmt.executeQuery();
        List<Activity> activities = new ArrayList<>();
        
        if (!rs.next()) {
            return activities; 
        } else {
            do {
                Activity activity = new Activity(rs.getString("activitysuser").trim(), rs.getInt("cents"), rs.getDate("date"), rs.getString("category").trim());
                activity.setActivityId(rs.getInt("activityid"));
                activities.add(activity);
            }
            while (rs.next());
        } 
        stmt.close();
        rs.close();
        con.close();
        return activities;
        
    }
    @Override
    public void delete(String username) throws SQLException {
        Connection conn = db.getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Activity WHERE activitysuser = (?)");

        stmt.setString(1, username);
        stmt.executeUpdate();
        stmt.close();
        conn.close(); 

    }
    @Override
    public Activity saveOrUpdate(Activity lisattava, String username) throws SQLException {
        List<Activity> all = findAllByUser(username);
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getDate().equals(lisattava.getDate()) && all.get(i).getCategory().trim().equals(lisattava.getCategory().trim())) {
                return update(lisattava, all.get(i));
            }
        }
        return save(lisattava);
    }
    
    public Activity save(Activity lisattava) throws SQLException {
        int id = (findAll().size() + 1);
        Connection conn = db.getConnection();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Activity (activityid, activitysuser, cents, date, category) VALUES (?,?,?,?,?)");

        stmt.setInt(1, id);
        stmt.setString(2, lisattava.getActivitysUser());
        stmt.setInt(3, lisattava.getCents());
        stmt.setDate(4, lisattava.getDate()); 
        stmt.setString(5, lisattava.getCategory());
        stmt.executeUpdate();
        stmt.close();
        conn.close();
        return lisattava;
    }

    @Override
    public ArrayList<Activity> findAllByUser(String username) throws SQLException {
        Connection con = db.getConnection();
        PreparedStatement stmt = con.prepareStatement("SELECT * FROM Activity WHERE activitysuser = (?)");
        stmt.setString(1, username);
        
        ResultSet rs = stmt.executeQuery();
        ArrayList<Activity> activities = new ArrayList<>();

        while (rs.next()) {
            Activity activity = new Activity(rs.getString("activitysUser").trim(), rs.getInt("cents"), rs.getDate("date"), rs.getString("category").trim());
            activity.setActivityId(rs.getInt("activityId"));
            activities.add(activity);
        }
        stmt.close();
        rs.close();
        con.close();
        return activities;
        
        
    }
    @Override
    public Integer findSumOfExpensesByDate(Date after, Date before, String passwordField) throws SQLException {
        Connection con = db.getConnection();
        PreparedStatement stmt = con.prepareStatement("SELECT SUM(cents) AS cents FROM activity WHERE date >= (?) AND date <= (?) AND activitysuser = (?)");
        
        stmt.setDate(1, after);
        stmt.setDate(2, before);
        stmt.setString(3, passwordField);
        
        int cents = 0;
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            cents = rs.getInt("cents");
        }
        stmt.close();
        rs.close();
        con.close();
        return cents;
  
    }
    @Override
    public ArrayList<Integer> findExpensesByDate(LocalDate after, LocalDate before, String passwordField) throws SQLException {
        Connection con = db.getConnection();
        PreparedStatement stmt = con.prepareStatement("SELECT cents AS cents FROM activity WHERE date >= (?) AND date <= (?) AND activitysuser = (?)");
        
        stmt.setDate(1, localDateToDate(after));
        stmt.setDate(2, localDateToDate(before));
        stmt.setString(3, passwordField);
        
        ArrayList<Integer> centsList = new ArrayList<>();
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            centsList.add(rs.getInt("cents"));
        }
        stmt.close();
        rs.close();
        con.close();
        return centsList;
  
    }
    public Date localDateToDate(LocalDate date) {
        return java.sql.Date.valueOf(date);
    }
    @Override
    public Activity update(Activity existingActivity, Activity newActivity) throws SQLException {
        Connection con = db.getConnection();
        PreparedStatement stmt = con.prepareStatement("UPDATE Activity SET cents = ? WHERE activityid = ?");
        stmt.setInt(1, newActivity.getCents() + existingActivity.getCents());
        stmt.setInt(2, newActivity.getActivityId());

        stmt.executeUpdate();
        stmt.close();
        con.close();
        return newActivity;
        
    }
    @Override
    public HashMap<String, ArrayList<Activity>> findAllByCategory(Date after, Date before, String username) throws SQLException {
        HashMap<String, ArrayList<Activity>> map = new HashMap<>();
        ArrayList<Activity> activities = findAllByUser(username);
        if (activities.isEmpty()) {
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
        return mapWithArrangedValues(map);
    }
    
    public HashMap<String, ArrayList<Activity>> mapWithArrangedValues(HashMap<String, ArrayList<Activity>> map) {
        for (ArrayList<Activity> a : map.values()) {
            Collections.sort(a);
        }
        return map;
    }
}

