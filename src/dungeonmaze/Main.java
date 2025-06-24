/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package dungeonmaze;

import derbyDB.DerbyGameManager;
import javax.swing.SwingUtilities;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ysadd
 */
public class Main 
{
    public static void main(String[] args) throws SQLException
    {
        SwingUtilities.invokeLater(() -> 
        {
            
            try 
            {
                new DungeonFrame();
                //Debugging code:
//                DerbyGameManager.printSaveTableColumns();
            } catch (SQLException ex) 
            {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
}
