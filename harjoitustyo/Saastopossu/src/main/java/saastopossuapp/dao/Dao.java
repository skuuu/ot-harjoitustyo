
package saastopossuapp.dao;

import java.sql.*;
import java.util.List;

public interface Dao <T,K,M>{
    T findOne(M key) throws SQLException; 
    List<T> findAll() throws SQLException; 
    void delete(String username) throws SQLException;
}
