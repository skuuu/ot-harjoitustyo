
package saastopossuapp.dao;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import saastopossuapp.domain.Activity;

/**
 * Class defines the Interface that ActivityDao implements.
 */
public interface ActivityDaoInterface {
    void deleteOne(Integer activityid) throws SQLException;
    ArrayList<Activity> getDailyActivities(Date fromDate, Date untilDate, String username) throws SQLException;
    ArrayList<Integer> findExpensesForTheChosenTimePeriod(Date fromDate, Date untilDate)throws SQLException;
    List<Activity> findAll() throws SQLException;
    void deleteAll(String username) throws SQLException;
    Activity save(Activity newActivity) throws SQLException;
    ArrayList<Activity> findAllByUsername(String username) throws SQLException;
    TreeMap<String, ArrayList<Activity>> findAllByCategory(Date fromDate, Date untilDate, String username) throws SQLException;
}
