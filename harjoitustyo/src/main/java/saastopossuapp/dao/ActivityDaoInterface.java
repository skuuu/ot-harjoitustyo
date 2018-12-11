
package saastopossuapp.dao;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import saastopossuapp.domain.Activity;

public interface ActivityDaoInterface {
    
    ArrayList<Integer> findExpensesByDate(LocalDate after, LocalDate before, String passwordField)throws SQLException;
    List<Activity> findAll() throws SQLException;
    void delete(String username) throws SQLException;
    Activity saveOrUpdate(Activity lisattava, String password) throws SQLException;
    
    
    ArrayList<Activity> findAllByUser(String username) throws SQLException;
    Integer findSumOfExpensesByDate(Date after, Date before, String passwordField) throws SQLException;
    Activity update(Activity a, Activity korvattava) throws SQLException;  
    HashMap<String, ArrayList<Activity>> findAllByCategory(Date after, Date before, String password) throws SQLException;
}
