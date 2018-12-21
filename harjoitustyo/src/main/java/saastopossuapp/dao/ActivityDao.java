
package saastopossuapp.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import saastopossuapp.domain.Activity;

/**
 * Class is responsible for database access operations related to Activities
 */
public class ActivityDao implements ActivityDaoInterface {
    private Database db;
    private String username;
    
    public ActivityDao(Database db, String username) {
        this.db = db;
        this.username = username;
    }
    
    
    /**
     * Method finds Activity based on activityId
     * @param activityId - Acitivitys Id
     * @return Activity or null, if Activity doesn't exist.
     * @throws java.sql.SQLException if fetching from database fails
     */
    public Activity findOne(Integer activityId) throws SQLException {
        Connection con = db.getConnection();
        PreparedStatement stmt = con.prepareStatement("SELECT * FROM Activity WHERE ActivityId=?");
        stmt.setInt(1, activityId);
        
        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }
        Activity activity = new Activity(rs.getString("activitysUser").trim(), rs.getInt("cents"), rs.getDate("date"), rs.getString("category").trim(), rs.getString("description"));
        activity.setActivityId(rs.getInt("activityId"));

        stmt.close();
        rs.close();
        con.close();
        return activity;
    }
    
    /**
     * Method finds all Activities from database
     * @return list of Activities
     * @throws java.sql.SQLException if fetching from database fails
     */
    @Override
    public List<Activity> findAll() throws SQLException {
        Connection con = db.getConnection();
        PreparedStatement stmt = con.prepareStatement("SELECT activitysuser, cents, date, category, activityid, description FROM Activity");
        List<Activity> activities = new ArrayList<>();
        
        ResultSet rs = stmt.executeQuery();
        if (!rs.next()) {
            return activities; 
        } else {
            do {
                Activity activity = new Activity(rs.getString("activitysuser").trim(), rs.getInt("cents"), rs.getDate("date"), rs.getString("category").trim(), rs.getString("description"));
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
    
    /**
     * Method deletes all Activities related to username
     * @param username - username whose Activities are being deleted
     * @throws java.sql.SQLException if deleting from database fails
     */
    @Override
    public void deleteAll(String username) throws SQLException {
        Connection conn = db.getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Activity WHERE activitysuser = (?)");

        stmt.setString(1, username);
        stmt.executeUpdate();
        stmt.close();
        conn.close(); 
    }
    
    /**
     * Method deletes one Activity by its activityId.
     * @param activityid - Activity's activityId
     * @throws java.sql.SQLException if deleting from database fails
     */
    @Override
    public void deleteOne(Integer activityid) throws SQLException {
        Connection conn = db.getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Activity WHERE activityid = (?)");

        stmt.setInt(1, activityid);
        stmt.executeUpdate();
        stmt.close();
        conn.close(); 
    }
    
    /**
     * Method saves Activity
     * @param activityToBeSaved - Activity that will be saved
     * @return activityToBeSaved
     * @throws java.sql.SQLException if saving to database fails
     */
    @Override
    public Activity save(Activity activityToBeSaved) throws SQLException {
        Connection conn = db.getConnection();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Activity (activitysuser, cents, date, category, description) VALUES (?,?,?,?,?)");
        stmt.setString(1, activityToBeSaved.getActivitysUser());
        stmt.setInt(2, activityToBeSaved.getCents());
        stmt.setDate(3, activityToBeSaved.getDate()); 
        stmt.setString(4, activityToBeSaved.getCategory());
        stmt.setString(5, activityToBeSaved.getDescription());
        
        stmt.executeUpdate();
        stmt.close();
        conn.close();
        return activityToBeSaved;
    }
    
    /**
     * Method finds all Activities by username
     * @param username - username of the userAccount related to Activity
     * @return ArrayList of Activities
     * @throws java.sql.SQLException if fetching from database fails
     */
    @Override
    public ArrayList<Activity> findAllByUsername(String username) throws SQLException {
        Connection con = db.getConnection();
        PreparedStatement stmt = con.prepareStatement("SELECT * FROM activity WHERE activitysuser = (?)");
        stmt.setString(1, username);
        ArrayList<Activity> activities = new ArrayList<>();
        
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Activity activity = new Activity(rs.getString("activitysUser").trim(), rs.getInt("cents"), rs.getDate("date"), rs.getString("category").trim(), rs.getString("description").trim());
            activity.setActivityId(rs.getInt("activityId"));
            activities.add(activity);
        }
        stmt.close();
        rs.close();
        con.close();
        Collections.sort(activities); 
        return activities;
    }
    
    /**
     * Method finds all Activities by username
     * @param fromDate - Date from expenses are being fetch
     * @param untilDate - Date until expenses are being fetch
     * @return ArrayList of Expenses
     * @throws java.sql.SQLException if fetching from database fails
     */
    @Override
    public ArrayList<Integer> findExpensesForTheChosenTimePeriod(Date fromDate, Date untilDate) throws SQLException {
        Connection con = db.getConnection();
        PreparedStatement stmt = con.prepareStatement("SELECT cents AS cents FROM activity WHERE date >= (?) AND date <= (?) AND activitysuser = (?)");
        stmt.setDate(1, fromDate);
        stmt.setDate(2, untilDate);
        stmt.setString(3, username);
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
    
    /**
     * Method finds all Activities by username and date, and puts them in a list 
     * so that Activities with the same date and category are united.
     * @param fromDate - Date from expenses are being fetch
     * @param untilDate - Date until expenses are being fetch
     * @param username - username of the user whose activities are being fetch
     * @return ArrayList of Activities
     * @throws java.sql.SQLException if fetching from database fails
     */
    @Override
    public ArrayList<Activity> getDailyActivities(Date fromDate, Date untilDate, String username) throws SQLException {
        this.username = username;
        ArrayList<Activity> activities = findAllByUsername(username);
        ArrayList<Activity> newlist = new ArrayList();
        
        for (int i = 0; i < activities.size(); i++) {
            Activity a = activities.get(i);
            if (!newlist.contains(a)) {
                newlist.add(a);
            } else {
                for (int e = 0; e < newlist.size(); e++) {
                    Activity b = newlist.get(e);
                    if (a.equals(b)) {
                        b.setCents(a.getCents() + b.getCents());
                    }
                }
            }
        }
        Collections.sort(newlist);
        return newlist;
    }
    
    /**
     * Method finds all Activities by category, and puts them in a map 
     * where key is the category and value is list of activities from that category. 
     * Activities with the same date and category are united.
     * @param fromDate - Date from expenses are being fetch
     * @param untilDate - Date until expenses are being fetch
     * @param username - username of the user whose activities are being fetch
     * @return TreeMap of Activities by category
     * @throws java.sql.SQLException if fetching from database fails
     */
    @Override
    public TreeMap<String, ArrayList<Activity>> findAllByCategory(Date fromDate, Date untilDate, String username) throws SQLException {
        this.username = username;
        TreeMap<String, ArrayList<Activity>> map = new TreeMap<>();
        ArrayList<Activity> expensesbyDate = getDailyActivities(fromDate, untilDate, username);
        
        if (expensesbyDate.isEmpty()) {
            return map;
        }
        for (Activity a: expensesbyDate) {
            if ((a.getDate().after(fromDate) | a.getDate().equals(fromDate)) && (a.getDate().before(untilDate) | a.getDate().equals(untilDate))) {
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
}

