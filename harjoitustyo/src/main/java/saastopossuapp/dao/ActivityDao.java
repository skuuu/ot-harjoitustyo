
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
import saastopossuapp.database.Database;
import saastopossuapp.domain.Activity;


public class ActivityDao implements Dao <Activity, Integer, Integer>{
    private Database db;
    
    public ActivityDao (Database db){
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
        List <Activity> activities = new ArrayList<>();

        while (rs.next()){
            Activity activity = new Activity(rs.getInt("cents"));
            activity.setActivityId(rs.getInt("activityId"));
            activity.setDate(rs.getDate("date"));
            activity.setActivitysUser(rs.getString("activitysUser"));
            activities.add(activity);
        }
        stmt.close();
        rs.close();
        con.close();
        return activities;
        
    }
    //etsii yhden käyttäjän kaikki acticityt:
    public List<Activity> findAllByUser(String username) throws SQLException {
        Connection con = db.connect();
        PreparedStatement stmt = con.prepareStatement("SELECT * FROM Activity WHERE activitysuser = (?)");
        stmt.setString(1, username);
        
        ResultSet rs = stmt.executeQuery();
        List <Activity> activities = new ArrayList<>();

        while (rs.next()){
            Activity activity = new Activity(rs.getInt("cents"));
            activity.setActivityId(rs.getInt("activityId"));
            activity.setDate(rs.getDate("date"));
            activity.setActivitysUser(rs.getString("activitysUser"));
            activities.add(activity);
        }
        stmt.close();
        rs.close();
        con.close();
        return activities;
        
        
    }
    //etsii kahden daten ajalta kaikki activityt jotka liittyvät kyseiseen käyttäjätunnukseen:
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
        HashMap <Date, Integer> map = new HashMap<>();
        while (rs.next()){
            map.put(rs.getDate("date"), rs.getInt("cents"));
            System.out.println("tietokannasta saatu pvm: " + rs.getDate("date"));
        }
        stmt.close();
        rs.close();
        con.close();

        System.out.println("findall map: " + map);
        return map;
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
    public Activity save(Activity activity)throws SQLException{
        Connection conn = db.connect();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Activity"
                + " (activityId, activitysUser, cents, date)"
                + " VALUES (?,?,?,?)");
        
        stmt.setInt(1, findAll().size()+1);
        stmt.setString(2, activity.getActivitysUser());
        stmt.setInt(3, activity.getCents());
        stmt.setDate(4, activity.getDate()); 
        stmt.executeUpdate();
        stmt.close();

        return activity;
    }
}

