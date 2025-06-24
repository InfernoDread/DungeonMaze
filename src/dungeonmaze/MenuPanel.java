/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dungeonmaze;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

/**
 *
 * @author ysadd
 * Main Menu creates a Main Menu interface for the DungeonMaze Game. This is 
 * where players enter the game, create a new game and load or delete saved games.
 * The overall Layout and appearance of the Main Menu is coded here.
 */
public class MenuPanel extends JPanel 
{
    private DungeonFrame frame;
    private DefaultListModel<String> saveListModel;
    private JList<String> saveList;
    private Image backgroundImage;
    
    private JButton loadButton;
    private JButton deleteButton;

    public MenuPanel(DungeonFrame frame) 
    {
        this.frame = frame;

        try 
        {
            InputStream stream = getClass().getResourceAsStream(
                    "/dungeonmaze/Resources/Main_Menu_Background.png");
            if (stream != null) 
            {
                backgroundImage = ImageIO.read(stream);
            } else 
            {
                System.err.println("Main menu background image not found!");
            }
        } catch (IOException e) 
        {
            System.out.println("Failed to load background image.");
        }

        setLayout(new BorderLayout());
        
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);

        // Title Panel
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setOpaque(false);

        // Title Label
        JLabel title = new JLabel("Welcome to DungeonMaze", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 32));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Spacer between title and welcome message
        topPanel.add(Box.createVerticalStrut(10));
        topPanel.add(title);
        topPanel.add(Box.createVerticalStrut(30));

        // Welcome message
        JLabel welcomeMsg = new JLabel("<html><div style='text-align: center;"
                + "'>Welcome to the Dungeon Crawler!<br>Only the brave will "
                + "survive the dungeon.<br>Would you like to:</div></html>", 
                SwingConstants.CENTER);
        welcomeMsg.setFont(new Font("SansSerif", Font.PLAIN, 16));
        welcomeMsg.setForeground(Color.LIGHT_GRAY);
        welcomeMsg.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(welcomeMsg);
        
        contentPanel.add(topPanel, BorderLayout.NORTH);

        // Button Section
        JPanel middlePanel = new JPanel();
        middlePanel.setOpaque(false);
        middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.Y_AXIS));

        JButton newGameButton = new JButton("New Game");
        newGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        newGameButton.setMaximumSize(new Dimension(150, 40));
        newGameButton.setBackground(new Color(40, 40, 40));
        newGameButton.setForeground(Color.LIGHT_GRAY);
        newGameButton.setOpaque(true);
        newGameButton.setBorderPainted(true);
        
        newGameButton.addActionListener(e -> frame.startNewGame());
        middlePanel.add(Box.createVerticalStrut(10));
        middlePanel.add(newGameButton);
        middlePanel.add(Box.createVerticalStrut(20));
        
        // Load & Delete Saves Section
        middlePanel.add(initLoadGameSection());
        
        contentPanel.add(middlePanel, BorderLayout.SOUTH);
        add(contentPanel, BorderLayout.CENTER);

        // Quit Button
        JButton quitButton = new JButton("Quit");
        quitButton.setPreferredSize(new Dimension(80, 30));
        quitButton.setBackground(new Color(40, 40, 40));
        quitButton.setForeground(Color.LIGHT_GRAY);
        quitButton.setOpaque(true);
        quitButton.setBorderPainted(true);
        
        quitButton.addActionListener(e -> System.exit(0));
        JPanel quitPanel = new JPanel();
        quitPanel.setOpaque(false);
        quitPanel.add(quitButton);
        add(quitPanel, BorderLayout.SOUTH);
    }

    private JPanel initLoadGameSection() 
    {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        saveListModel = new DefaultListModel<>();
        saveList = new JList<>(saveListModel);
        saveList.setBackground(new Color(30, 30, 30));
        saveList.setForeground(Color.LIGHT_GRAY);
        
        JScrollPane scrollPane = new JScrollPane(saveList);
        scrollPane.setOpaque(true);
        scrollPane.getViewport().setOpaque(true);
        scrollPane.setPreferredSize(new Dimension(400, 100));
        
        loadButton = new JButton("Load Selected Game");
        loadButton.setBackground(new Color(40, 40, 40));
        loadButton.setForeground(Color.LIGHT_GRAY);
        loadButton.setOpaque(true);
        loadButton.setBorderPainted(false);
        loadButton.addActionListener((ActionEvent e) -> 
        {
            int index = saveList.getSelectedIndex();
            if (index != -1) 
            {
                String summary = saveListModel.getElementAt(index);
                String filename = summary.split("\\|")[0];
                GameState state = GameSaveAndLoad.loadGame(filename);
                if (state == null) return;
                
                if (state.gameIsComplete()) 
                {
                    int choice = JOptionPane.showOptionDialog(this,
                            """
                            This game has already been completed.
                            Would you like to overwrite the save or start a new game?""",
                        "Game Completed",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new Object[]{"Overwrite", "New Game"},
                        "New Game"
                    );

                    if (choice == JOptionPane.YES_OPTION) 
                    {
                        GameSaveAndLoad.removeFromSaveIndex(filename);
                        frame.startNewGameUsingName(filename.replace(".dat", ""));
                    } else if (choice == JOptionPane.NO_OPTION) 
                    {
                        frame.startNewGame();
                    }
                } else 
                {
                    frame.loadGame(filename);
                }
            } else 
            {
                JOptionPane.showMessageDialog(this, "Please select a saved game.");
            }
        });

        deleteButton = new JButton("Delete Selected Save");
        deleteButton.setBackground(new Color(40, 40, 40));
        deleteButton.setForeground(Color.LIGHT_GRAY);
        deleteButton.setOpaque(true);
        deleteButton.setBorderPainted(false);
        
        deleteButton.addActionListener(e -> 
        {
            int index = saveList.getSelectedIndex();
            if (index != -1) 
            {
                String summary = saveListModel.getElementAt(index);
                String filename = summary.split("\\|")[0];
                
                int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this save file?\n(" 
                            + filename + ")",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
                );
                
                if (confirm == JOptionPane.YES_OPTION) 
                {
                    GameSaveAndLoad.removeFromSaveIndex(filename);
                    loadSaveList();
                    JOptionPane.showMessageDialog(this, 
                            "Save deleted successfully.");
                }
            } else 
            {
                JOptionPane.showMessageDialog(this, 
                        "Please select a save to delete.");
            }
        });
        
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        buttonPanel.add(loadButton);
        buttonPanel.add(deleteButton);
        
        JLabel savedGamesLabel = new JLabel("Saved Games:");
        savedGamesLabel.setForeground(Color.LIGHT_GRAY);
        panel.add(savedGamesLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        loadSaveList();
        
        return panel;
    }
    
    public void loadSaveList() 
    {
        List<String> summaries = GameSaveAndLoad.readSaveSummaries();
        saveListModel.clear();
        
        if (summaries.isEmpty()) 
        {
            saveListModel.addElement("(No saved games found)");
        } 
        else 
        {
            for (String summary : summaries) 
            {
                saveListModel.addElement(summary);
            }
        }
        
        boolean hasSaves = !summaries.isEmpty() && 
                !summaries.get(0).contains("No saved games");
        loadButton.setEnabled(hasSaves);
        deleteButton.setEnabled(hasSaves);
    }
    
    public void reloadSaveList() 
    {
        loadSaveList();
    }
    
    @Override
    protected void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        if (backgroundImage != null) 
        {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}