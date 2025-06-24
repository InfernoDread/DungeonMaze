/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dungeonmaze;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


/**
 *
 * @author ysadd
 * DungeonFrame is the class that manages the main application window for the 
 * DungeonMaze game. It controls the layouts and transitions of key panels such
 * as the Main Menu Panel, Game Panel, and Battle Panel.
 */
public class DungeonFrame extends JFrame 
{
    private final CardLayout cardLayout;
    private final JPanel mainPanel;

    private final MenuPanel menuPanel;
    private GamePanel gamePanel;
    private BattlePanel battlePanel;

    private DungeonEngine engine;

    public DungeonFrame() 
    {
        setTitle("DungeonMaze - GUI Edition");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setPreferredSize(new Dimension(800, 600));

        menuPanel = new MenuPanel(this);
        mainPanel.add(menuPanel, "Menu");

        add(mainPanel);
        setVisible(true);
    }

    public void startNewGame() 
    {
        String playerName;
        while (true) 
        {
            playerName = JOptionPane.showInputDialog(this, "Enter your name Adventurer:");
            if (playerName == null)
            {
                showMenu();
                return;
            }
            if (playerName.trim().isEmpty()) 
            {
                JOptionPane.showMessageDialog(this, "Name cannot be empty.");
                continue;
            }
            File saveFile = new File("saved_games/" + playerName.trim() + ".dat");
            if (saveFile.exists()) 
            {
                JOptionPane.showMessageDialog(this, "That name is already in use. Please choose a different name.");
            } 
            else 
            {
                break;
            }
        }

        startNewGameUsingName(playerName.trim());
    }

    public void startNewGameUsingName(String playerName) 
    {
        if (gamePanel != null) 
        {
            mainPanel.remove(gamePanel);
            gamePanel.removeAll();
            gamePanel.setVisible(false);
            gamePanel = null;
        }
        
        if (battlePanel != null) 
        {
            mainPanel.remove(battlePanel);
        }
        mainPanel.revalidate();
        mainPanel.repaint();
        
        DungeonMap map = new DungeonMap();
        int startRow = map.getEntryRow();
        int startCol = map.getEntryCol();
        Player player = new Player(startRow, startCol);
        player.setPlayerName(playerName.trim());    
        
        gamePanel = new GamePanel(this);
        GUI_GameUI guiUI = new GUI_GameUI(gamePanel);
        engine = new DungeonEngine(guiUI, player, map);
        
        gamePanel.setEngine(engine);
        gamePanel.renderCurrentRoom();
        gamePanel.refreshProfile();
        
        mainPanel.add(gamePanel, "Game");
        cardLayout.show(mainPanel, "Game");

        // Debug: print all remaining components in mainPanel
//        System.out.println("MainPanel now has:");
//        for (Component c : mainPanel.getComponents()) 
//        {
//            System.out.println(" - " + c.getClass().getName());
//        }

        GameSaveAndLoad.saveGame(engine.getGameState(), 
                playerName + ".dat", false, true);
    }

    public void loadGame(String filename) 
    {
        if (gamePanel != null) 
        {
            mainPanel.remove(gamePanel);
            gamePanel.removeAll();
            gamePanel.setVisible(false);
            gamePanel = null;
        }
        
        if (gamePanel != null) 
        {
            mainPanel.remove(gamePanel);
        }
        if (battlePanel != null) 
        {
            mainPanel.remove(battlePanel);
        }
        
        gamePanel = new GamePanel(this);
        GUI_GameUI guiUI = new GUI_GameUI(gamePanel);
        
        engine = new DungeonEngine(guiUI, null, null);
        engine.loadSavedGame(filename);
        gamePanel.setEngine(engine);  

        mainPanel.add(gamePanel, "Game");
        mainPanel.revalidate();
        mainPanel.repaint();
        cardLayout.show(mainPanel, "Game");
        
        // Debug: print all remaining components in mainPanel
//        System.out.println("MainPanel now has:");
//        for (Component c : mainPanel.getComponents()) 
//        {
//            System.out.println(" - " + c.getClass().getName());
//        }
        
        gamePanel.refreshProfile();
        gamePanel.renderCurrentRoom();
    }

    public void showMenu() 
    {
        menuPanel.reloadSaveList();
        cardLayout.show(mainPanel, "Menu");
    }

    public void startBattle(Player player, Enemy enemy, GameState gameState,
            int prevX, int prevY, Runnable onBattleEnd)
    {
        if (battlePanel != null) 
        {
            mainPanel.remove(battlePanel);
        }
        
        battlePanel = new BattlePanel(this, gamePanel, player, enemy,
                gameState, prevX, prevY, () -> 
        {
            mainPanel.remove(battlePanel);
            cardLayout.show(mainPanel, "Game");
            onBattleEnd.run();
        });
        mainPanel.add(battlePanel, "Battle");
        cardLayout.show(mainPanel, "Battle");
    }

    public GamePanel getGamePanel() 
    {
        return gamePanel;
    }
}
