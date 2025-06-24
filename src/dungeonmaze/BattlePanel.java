/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dungeonmaze;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author ysadd
 * BattlePanel controls the Combat interface for the DungeonMaze game and 
 * manages all combat Logic. It features turn-based combat with outcomes 
 * determined by random Dice rolls, Battle Logs displayed in a Message Bubble, 
 * Enemy Specific Backgrounds, Status Labels for both Player and Enemy, and 
 * actions buttons (including Light Attack, Heavy Attack, Use Item (From 
 * Inventory), Flee and Save & Quit).
 */
public class BattlePanel extends JPanel 
{
    private DungeonFrame frame;
    private Player player;
    private Enemy enemy;
    private GameState gameState;
    private Runnable onBattleEnd;
    private GUI_GameUI guiUI;
    
    private Image backgroundImage;
    
    private final MessageBubble battleBubble;
    private final JLabel playerStatus;
    private final JLabel enemyStatus;
    private final JButton lightAttackButton, heavyAttackButton, itemButton, 
            fleeButton, saveAndQuitButton;
    private int prevX, prevY;
    
    private boolean isNewTurn = true;
    
    public BattlePanel(DungeonFrame frame, GamePanel gamePanel, 
                   Player player, Enemy enemy, GameState gameState, 
                   int prevX, int prevY, Runnable onBattleEnd) 
    {
        this.frame = frame;
        this.player = player;
        this.enemy = enemy;
        this.gameState = gameState;
        this.onBattleEnd = onBattleEnd;
        this.guiUI = new GUI_GameUI(this);
        this.prevX = prevX;
        this.prevY = prevY;
        
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);
        
        // Load Background Based on Enemy:
        try 
        {
            String imagePath = "/dungeonmaze/Resources/";

            switch (enemy.getName().toLowerCase().trim()) 
            {
                case "goblin" -> imagePath += "Goblin_Combat_Panel.png";
                case "skeleton" -> imagePath += "Skeleton_Combat_Panel.png";
                case "wolf" -> imagePath += "Wolf_Combat_Panel.png";
                case "orc" -> imagePath += "Orc_Combat_Panel.png";
                case "elder dragon" -> imagePath += "Elder_Dragon_Combat_Panel.png";
                default -> imagePath += "Game_Background.png";
            }
            //Debugging Purposes:
//            System.out.println("Trying to load background: " + imagePath);
            
            InputStream stream = getClass().getResourceAsStream(imagePath);
            if (stream == null) 
            {
                throw new IllegalArgumentException("Could not load image: " + imagePath);
            }
            backgroundImage = ImageIO.read(stream);
            //Debugging Purposes:
//            System.out.println("Background image dimensions: " + backgroundImage.getWidth(null) + "x" + backgroundImage.getHeight(null));
        } catch (IOException | IllegalArgumentException e) 
        {
            e.printStackTrace();
            System.err.println("Failed to load battle background.");
        }
        
        // Status Labels:
        playerStatus = new JLabel();
        enemyStatus = new JLabel();
        updateStatus();
        
        JPanel statusPanel = new JPanel(new GridLayout(1, 2));
        statusPanel.setOpaque(false);
        playerStatus.setOpaque(false);
        enemyStatus.setOpaque(false);
        statusPanel.add(playerStatus);
        statusPanel.add(enemyStatus);
        add(statusPanel, BorderLayout.NORTH);
        
        // Battle Log / Message Bubble:
        battleBubble = new MessageBubble();
        battleBubble.setMessage("You are Attacked by a " + enemy.getName() + "! Prepare for Battle!");
        battleBubble.setAppendMode(true);
        add(battleBubble, BorderLayout.CENTER);
        
        // Action Buttons:
        JPanel buttonPanel = new JPanel(new GridLayout(1, 5));
        
        lightAttackButton = new JButton("Light Attack");
        lightAttackButton.addActionListener(e -> performAttack("light"));
        
        heavyAttackButton = new JButton("Heavy Attack");
        heavyAttackButton.addActionListener(e -> performAttack("heavy"));
        
        itemButton = new JButton("Use Item");
        itemButton.addActionListener(e -> useItem());
        
        fleeButton = new JButton("Flee");
        fleeButton.addActionListener(e -> flee());
        
        saveAndQuitButton = new JButton("Save & Quit");
        saveAndQuitButton.addActionListener(e -> saveAndQuit());
        
        buttonPanel.add(lightAttackButton);
        buttonPanel.add(heavyAttackButton);
        buttonPanel.add(itemButton);
        buttonPanel.add(fleeButton);
        buttonPanel.add(saveAndQuitButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
        repaint();
    }
    
    private void updateStatus() 
    {
        playerStatus.setText(player.getName() + "'s HP: " + player.getHealth());
        enemyStatus.setText(enemy.getName() + "'s HP: " + enemy.getHealth());
        
        Font statusFont = new Font("SansSerif", Font.PLAIN, 14);
        playerStatus.setFont(statusFont);
        enemyStatus.setFont(statusFont);
        
        playerStatus.setForeground(Color.WHITE);
        enemyStatus.setForeground(Color.WHITE);
    }
    
    public void appendLog(String msg) 
    {
        if (isNewTurn) 
        {
            battleBubble.clearMessage();
            isNewTurn = false;
        }
        battleBubble.setMessage(msg);
    }
    
    private void performAttack(String type) 
    {
        int roll = Dice.roll(20);
        int damage;

        if (type.equals("light")) 
        {
            if (roll == 20) 
            {
                damage = player.getAttackPower();
                appendLog("Critical Hit! You dealt " + damage + " damage!");
            } else if (roll >= 5) 
            {
                damage = player.getAttackPower() / 2;
                appendLog("You hit lightly for " + damage + " damage.");
            } else 
            {
                damage = 0;
                appendLog("You missed your attack.");
            }
        } 
        else 
        {
            if (roll == 20) 
            {
                damage = player.getAttackPower() * 2;
                appendLog("Critical Hit! You dealt " + damage + " damage!");
            } else if (roll >= 12) 
            {
                damage = player.getAttackPower();
                appendLog("You hit hard for " + damage + " damage.");
            } else 
            {
                damage = 0;
                appendLog("You missed your heavy attack.");
            }
        }
        
        if (damage > 0) enemy.takeDamage(damage);
        updateStatus();
        checkBattleOutcome();
        
        if (enemy.isAlive()) performEnemyTurn();
        isNewTurn = true;
    }
    
    private void performEnemyTurn() 
    {
        int roll = Dice.roll(10);
        int damage;
        
        if (roll <= 3) 
        {
            appendLog("The " + enemy.getName() + " missed its attack!");
        } else if (roll <= 5) 
        {
            damage = enemy.getAttackPower() / 4;
            player.takeDamage(damage);
            appendLog("The " + enemy.getName() + " hit you lightly for " 
                    + damage + " damage.");
        } else if (roll <= 7) 
        {
            damage = enemy.getAttackPower() / 2;
            player.takeDamage(damage);
            appendLog("The " + enemy.getName() + " hit you for " 
                    + damage + " damage.");
        } else if (roll <= 9) 
        {
            damage = enemy.getAttackPower();
            player.takeDamage(damage);
            appendLog("The " + enemy.getName() + " hit you for " 
                    + damage + " damage.");
        } else 
        {
            damage = enemy.getAttackPower() * 2;
            player.takeDamage(damage);
            appendLog("Critical Hit! " + "The " + enemy.getName() 
                    + " struck you for " + damage + " damage!");
        }
        
        updateStatus();
        checkBattleOutcome();
        isNewTurn = true;
    }
    
    private void checkBattleOutcome() 
    {
        if (!player.isAlive()) 
        {
            JOptionPane.showMessageDialog(this, "You were defeated by the "
                    + enemy.getName() + ".\nGame Over.");
            
            int choice = JOptionPane.showOptionDialog(
                this,
                "What would you like to do next?",
                "Game Over",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new Object[]{"Return to Menu", "Exit Game"},
                "Return to Menu"
            );
            
            if (choice == JOptionPane.YES_OPTION) 
            {
                frame.showMenu();
            } 
            else 
            {
                System.exit(0);
            }
        } else if (!enemy.isAlive()) 
        {
            appendLog("You defeated the " + enemy.getName() + "!");
            disableButtons();
            
            if (enemy instanceof ElderDragon) 
            {
                appendLog("You have slain the Elder Dragon and escaped "
                        + "victorious!");
                gameState.setGameIsComplete(true);
                JOptionPane.showMessageDialog(this, "Victory! You beat the game.");
                GameSaveAndLoad.saveGame(gameState, player.getName() 
                        + ".dat", false, true);

                int choice = JOptionPane.showOptionDialog(
                    this,
                    "Game saved successfully!\nWhat would you like to do next?",
                    "Save Complete",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new Object[]{"Return to Menu", "Exit Game"},
                    "Return to Menu"
                );
                
                if (choice == JOptionPane.YES_OPTION) frame.showMenu();
                else System.exit(0);
            }
            
            javax.swing.Timer delayTimer = new javax.swing.Timer(3000, e -> 
            {
                ((javax.swing.Timer) e.getSource()).stop();
                onBattleEnd.run();
            });
            delayTimer.setRepeats(false);
            delayTimer.start();
        }
    }
    
    private void useItem() 
    {
        List<Item> items = player.getInventory().getItems();
        
        if (items.isEmpty()) 
        {
            appendLog("Your inventory is empty.");
            isNewTurn = true;
            return;
        }
        
        new InventoryPanel(
            frame,
            player.getInventory(),
            player,
            guiUI,
            () -> 
            {
                updateStatus();
                isNewTurn = true;
            }
        );
    }

    private void flee() 
    {
        disableButtons();
        appendLog("You fled from the battle.");
        
        javax.swing.Timer delayTimer = new javax.swing.Timer(1000, e -> 
        {
            ((javax.swing.Timer) e.getSource()).stop();
            player.setPosition(prevY, prevX);
            onBattleEnd.run();
        });
        delayTimer.setRepeats(false);
        delayTimer.start();
    }

    private void saveAndQuit() 
    {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to Save and Quit?",
            "Confirm Save & Quit",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) 
        {
            GameSaveAndLoad.saveGame(gameState, player.getName() + ".dat", false, true);

            int choice = JOptionPane.showOptionDialog(
                this,
                "Game saved successfully!\nWhat would you like to do next?",
                "Save Complete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new Object[]{"Return to Menu", "Exit Game"},
                "Return to Menu"
            );

            if (choice == JOptionPane.YES_OPTION) frame.showMenu();
            else System.exit(0);
        }
    }
    
    public void enableButtons() 
    {
        lightAttackButton.setEnabled(true);
        heavyAttackButton.setEnabled(true);
        itemButton.setEnabled(true);
        fleeButton.setEnabled(true);
    }
    
    public void disableButtons() 
    {
        lightAttackButton.setEnabled(false);
        heavyAttackButton.setEnabled(false);
        itemButton.setEnabled(false);
        fleeButton.setEnabled(false);
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