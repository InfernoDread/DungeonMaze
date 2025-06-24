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
        saveGame(state, filename, false, false, false);
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
        if (filename.endsWith(".db")) 
        {
            return DerbyGameManager.loadGameFromDB(filename.replace(".db", ""));
        }

        File file = new File("saved_games", filename);
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) 
        {
            return (GameState) ois.readObject();
        } 
        catch (IOException | ClassNotFoundException e) 
        {
            System.out.println("Error loading game: " + e.getMessage());
            return null;
        }
    }
    
    public static void updateSaveIndex(GameState state, String filename) 
    {
        int floor = state.getCurrentFloor();
        int currentHP = state.getPlayer().getHealth();
        int maxHP = 100;
        int roomsCleared = state.getMap().getClearedRoomCount();
        
        String newEntry = String.format("%s|Floor: %d|HP: %d/%d|Rooms Cleared: %d",
                filename, floor, currentHP, maxHP, roomsCleared);
        
        File indexFile = new File("save_index.txt");
        List<String> lines = new ArrayList<>();
        boolean replaced = false;
        
        if (indexFile.exists()) 
        {
            try (BufferedReader reader = new BufferedReader(new FileReader(indexFile))) 
            {
                String line;
                while ((line = reader.readLine()) != null) 
                {
                    if (line.trim().startsWith(filename.trim() + "|")) 
                    {
                        lines.add(newEntry);
                        replaced = true;
                    } else 
                    {
                        lines.add(line);
                    }
                }
            } catch (IOException e) 
            {
                JOptionPane.showMessageDialog(null, "Error reading save index: "
                        + e.getMessage(), "Index Error", 
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        
        if (!replaced) 
        {
            lines.add(newEntry);
        }
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(indexFile))) 
        {
            for (String line : lines) 
            {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) 
        {
            JOptionPane.showMessageDialog(null, "Error writing save index: " + 
                    e.getMessage(), "Index Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static List<String> readSaveSummaries() 
    {
        File indexFile = new File("save_index.txt");
        List<String> summaries = new ArrayList<>();
        
        if (indexFile.exists()) 
        {
            try (BufferedReader reader = new BufferedReader(new FileReader(indexFile))) 
            {
                String line;
                while ((line = reader.readLine()) != null) 
                {
                    String[] parts = line.split("\\|");
                    if (parts.length > 0) 
                    {
                        File file = new File("saved_games/" + parts[0].trim());
                        if (file.exists()) 
                        {
                            summaries.add(line);
                        }
                    }
                }
            } catch (IOException e) 
            {
                JOptionPane.showMessageDialog(null, "Error reading save "
                        + "summaries: " + e.getMessage(), "Summary Error", 
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        return summaries;
    }
    
    public static void removeFromSaveIndex(String filename) 
    {
        File indexFile = new File("save_index.txt");
        if (!indexFile.exists()) return;
        
        List<String> lines = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(indexFile))) 
        {
            String line;
            while ((line = reader.readLine()) != null) 
            {
                String[] parts = line.split("\\|");
                if (parts.length > 0 && !parts[0].trim().equalsIgnoreCase(filename.trim())) 
                {
                    lines.add(line);
                }
            }
        } catch (IOException e) 
        {
            JOptionPane.showMessageDialog(null, "Error reading save index: " + 
                    e.getMessage(), "Delete Error", JOptionPane.ERROR_MESSAGE);
        }
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(indexFile))) 
        {
            for (String line : lines) 
            {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) 
        {
            JOptionPane.showMessageDialog(null, "Error writing save index: " + 
                    e.getMessage(), "Delete Error", JOptionPane.ERROR_MESSAGE);
        }
        
        File saveFile = new File("saved_games/" + filename);
        if (saveFile.exists()) 
        {
            if (!saveFile.delete()) 
            {
                JOptionPane.showMessageDialog(null, "Failed to delete save "
                        + "file: " + filename, "Delete Error", 
                        JOptionPane.WARNING_MESSAGE);
            }
        }
    }
}