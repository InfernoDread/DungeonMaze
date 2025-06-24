/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dungeonmaze;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import derbyDB.DerbyGameManager;
import java.sql.SQLException;


/**
 *
 * @author ysadd
 * The GameSaveAndLoad Class handles saving and loading functionality of the 
 * DungeonMaze Game. It manages the Serializing of the GameState, maintaining
 * save summaries, and managing the save index that is used to display saved
 * files on the Main Menu screen.
 */
public class GameSaveAndLoad 
{
    // Default contructor: No exiting or overwriting
    public static void saveGame(GameState state, String filename) throws SQLException 
    {
        saveGame(state, filename, false, false, true);
    }
    //Secondary Contructor: Used for exiting, overwriting, or both.
    public static void saveGame(GameState gameState, String filename, boolean exitAfterSave, boolean overwrite, boolean isFromDerby) throws SQLException
    {
        if (isFromDerby) 
        {
            DerbyGameManager.saveGameToDB(gameState, filename.replace(".dat", ""), overwrite);
            return;
        }

        File dir = new File("saved_games");
        if (!dir.exists()) 
        {
            dir.mkdir();
        }

        File file = new File(dir, filename);
        if (file.exists() && !overwrite) return;

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) 
        {
            oos.writeObject(gameState);
        } 
        catch (IOException e) 
        {
            System.out.println("Error saving game: " + e.getMessage());
        }
    }
    
    public static GameState loadGame(String filename) throws SQLException 
    {
        return DerbyGameManager.loadGameFromDB(filename.replace(".db", ""));
    }
    
    public static List<String> readSaveSummaries() //Summarises now from Derby
    {
        List<String> summaries = new ArrayList<>();
        try 
        {
            summaries = DerbyGameManager.getAllSaveSummaries();
        } 
        catch (SQLException e) 
        {
            JOptionPane.showMessageDialog(null, 
                "Error reading save summaries from database: " + e.getMessage(),
                "DB Summary Error", 
                JOptionPane.ERROR_MESSAGE);
        }
        return summaries;
    }
}