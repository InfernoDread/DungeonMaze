/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package derbyDB;

import dungeonmaze.GameState;
import dungeonmaze.Player;
import java.sql.*;
import javax.swing.JOptionPane;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author ysadd
 * Handles saving, loading, deleting, and summarizing GameState objects
 * in the Derby database for DungeonMaze.
 */
public class DerbyGameManager 
{
    public static void saveGameToDB(GameState gameState, String filename, boolean overwrite) throws SQLException {
        try (Connection conn = DerbyManager.getConnection()) 
        {
            boolean exists;
            try (PreparedStatement checkStmt = conn.prepareStatement("SELECT COUNT(*) FROM saves WHERE name = ?")) {
                checkStmt.setString(1, filename);
                ResultSet rs = checkStmt.executeQuery();
                rs.next();
                exists = rs.getInt(1) > 0;
            }
            
            if (exists && !overwrite) 
            {
                JOptionPane.showMessageDialog(null, "A save with this name already exists.", "Save Blocked", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Serialize game state
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(gameState);
            byte[] gameData = baos.toByteArray();
            
            if (exists) 
            {
                try (PreparedStatement updateStmt = conn.prepareStatement("UPDATE saves SET data = ? WHERE name = ?")) {
                    updateStmt.setBytes(1, gameData);
                    updateStmt.setString(2, filename);
                    updateStmt.executeUpdate();
                }
            } else 
            {
                try (PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO saves (name, data) VALUES (?, ?)")) {
                    insertStmt.setString(1, filename);
                    insertStmt.setBytes(2, gameData);
                    insertStmt.executeUpdate();
                }
            }
            
            JOptionPane.showMessageDialog(null, "Game saved to database!");
        } catch (IOException e) 
        {
            JOptionPane.showMessageDialog(null, "Error saving to DB: " + e.getMessage(), "DB Save Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static GameState loadGameFromDB(String filename) throws SQLException 
    {
        try (Connection conn = DerbyManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT data FROM saves WHERE name = ?")) 
        {
            
            stmt.setString(1, filename);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) 
            {
                byte[] data = rs.getBytes("data");
                try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) 
                {
                    return (GameState) ois.readObject();
                }
            } else 
            {
                JOptionPane.showMessageDialog(null, "No save found with name: " + filename, "Load Failed", JOptionPane.WARNING_MESSAGE);
                return null;
            }
        } catch (IOException | ClassNotFoundException e) 
        {
            JOptionPane.showMessageDialog(null, "Error loading from DB: " + e.getMessage(), "DB Load Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    
    public static void deleteSaveFromDB(String filename) throws SQLException {
        try (Connection conn = DerbyManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM saves WHERE name = ?")) 
        {
            stmt.setString(1, filename);
            int affected = stmt.executeUpdate();
            
            if (affected > 0) 
            {
                JOptionPane.showMessageDialog(null, "Save deleted from database!");
            } else 
            {
                JOptionPane.showMessageDialog(null, "No save found with that name.");
            }
        }
    }
    
    public static List<String> getAllSaveSummaries() throws SQLException 
    {
        List<String> summaries = new ArrayList<>();

        try (Connection conn = DerbyManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT name, data FROM saves")) 
        {

            while (rs.next()) 
            {
                String name = rs.getString("name");
                try (ObjectInputStream ois = new ObjectInputStream(rs.getBinaryStream("data"))) 
                {
                    GameState state = (GameState) ois.readObject();
                    Player player = state.getPlayer();

                    String summary = String.format(
                        "%s | Floor: %d | HP: %d/%d | Rooms Cleared: %d",
                        name,
                        state.getCurrentFloor(),
                        player.getHealth(),
                        100,
                        state.getMap().getClearedRoomCount()
                    );

                    summaries.add(summary);
                } catch (IOException | ClassNotFoundException ex) 
                {
                    summaries.add(name + " | (Corrupted Save)");
                }
            }
        }

        return summaries;
    }

    // Debug method:
//    public static void printSaveTableColumns() throws SQLException {
//        try (Connection conn = DerbyManager.getConnection()) {
//            DatabaseMetaData meta = conn.getMetaData();
//            ResultSet rs = meta.getColumns(null, null, "SAVES", null);
//            while (rs.next()) {
//                System.out.println(rs.getString("COLUMN_NAME") + " - " + rs.getString("TYPE_NAME"));
//            }
//            rs.close();
//        }
//    }
}