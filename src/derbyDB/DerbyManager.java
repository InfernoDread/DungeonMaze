/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package derbyDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author ysadd
 */
public class DerbyManager 
{
    private static final String DB_URL = "jdbc:derby:db/DungeonMazeDB;create=true";
    
    public static Connection getConnection() throws SQLException 
    {
        return DriverManager.getConnection(DB_URL);
    }
}