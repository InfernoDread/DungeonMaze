/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package derbyDB;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author ysadd
 * DBInitializer sets up the saves table in the embedded Derby database
 * for the DungeonMaze game. This should be called once at application startup.
 * Creates the 'saves' table if it does not already exist.
 * This table stores the serialized GameState objects for each save.
 */
public class DBInitializer 
{
    public static void createSavesTableIfNotExists() throws SQLException 
    {
        try (Connection conn = DerbyManager.getConnection(); 
             Statement stmt = conn.createStatement()) 
        {

            String sql = "CREATE TABLE saves (" +
                         "name VARCHAR(50) PRIMARY KEY, " +
                         "data BLOB" +
                         ")";
            stmt.executeUpdate(sql);
        } catch (SQLException e) 
        {
            if (!"X0Y32".equals(e.getSQLState())) 
            {
                throw e;
            }
        }
    }
}
