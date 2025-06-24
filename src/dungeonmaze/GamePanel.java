/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dungeonmaze;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.OverlayLayout;
import javax.swing.SwingUtilities;
import javax.swing.Timer;


/**
 *
 * @author ysadd
 * GamePanel is the main in-game UI component for DungeonMaze. It displays an 
 * overlay of three panels over a background image. The top Panel consists of an 
 * message bubble, the middle panel consist of a Map View of the dungeon and the
 * bottom panel consists of a Player Profile, D-Pad navigation, and the Save &
 * Quit + Inventory Buttons.
 */
public class GamePanel extends JPanel 
{
    private DungeonFrame frame;
    DungeonEngine engine;
    private JPanel contentPanel;

    private MessageBubble messageBubble;
    private BottomPanel bottomPanel;
    private MapPanel mapPanel;
    private FadePanel fadePanel;
    
    private Image backgroundImage;
    private int prevX, prevY;

    public GamePanel(DungeonFrame frame) 
    {
        this.frame = frame;

        try 
        {
            InputStream stream = getClass().getResourceAsStream("/dungeonmaze/Resources/Game_Background.png");
            if (stream != null) 
            {
                backgroundImage = ImageIO.read(stream);
            }
        } catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

    public void setEngine(DungeonEngine engine) 
    {
        this.engine = engine;
        
        if (contentPanel != null) 
        {
            this.remove(contentPanel);
        }
        
        this.removeAll();
        initUI();
        bottomPanel.initPanelsAfterEngineReady();
        
        this.revalidate();
        this.repaint();
        
        SwingUtilities.invokeLater(() -> 
        {
            if (this.getTopLevelAncestor() != null) 
            {
                this.getTopLevelAncestor().repaint();
            }
        });
    }

    private void initUI() 
    {
        setLayout(new BorderLayout());

        // Main content panel with background image:
        contentPanel = new JPanel(new BorderLayout()) 
        {
            @Override
            protected void paintComponent(Graphics g) 
            {
                super.paintComponent(g);
                if (backgroundImage != null) 
                {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        contentPanel.setOpaque(false);

        // Top Panel Containing Message Bubble:
        messageBubble = new MessageBubble();
        JPanel textBubblePanel = new JPanel(new BorderLayout());
        textBubblePanel.setOpaque(false);
        textBubblePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        textBubblePanel.add(messageBubble, BorderLayout.CENTER);
        contentPanel.add(textBubblePanel, BorderLayout.NORTH);

        // Center Panel Containing Map Display:
        mapPanel = new MapPanel(engine.getGameState());
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        centerWrapper.add(mapPanel, gbc);
        contentPanel.add(centerWrapper, BorderLayout.CENTER);

        // Bottom Panel Containing Profile, D-Pad & Menu Buttons (Save & Quit + Inventory)
        bottomPanel = new BottomPanel(this);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        contentPanel.add(bottomPanel, BorderLayout.SOUTH);
        bottomPanel.initPanelsAfterEngineReady();

        // Fade Panel for Fading Visual Effect:
        fadePanel = new FadePanel();
        fadePanel.setOpaque(false);
        
        JPanel layeredContainer = new JPanel();
        layeredContainer.setLayout(new OverlayLayout(layeredContainer));
        layeredContainer.add(fadePanel);      // on top
        layeredContainer.add(contentPanel);   // behind

        add(layeredContainer, BorderLayout.CENTER);
    }


    public void initiateBattle(Enemy enemy) 
    {
        Player player = engine.getPlayer();
        prevX = player.getX();
        prevY = player.getY();
        frame.startBattle(player, enemy, engine.getGameState(), prevX, prevY, this::renderCurrentRoom);
    }

    public void move(String direction) 
    {
        Player player = engine.getPlayer();
        int previous_X_Coordinate = player.getX();
        int previous_Y_Coordinate = player.getY();

        player.move(direction);
        Room room = engine.getMap().getRoom(player.getY(), player.getX());

        if (room == null) 
        {
            appendMessage("You hit a wall.");
            player.positionCorrector();
        } else 
        {
            appendMessage("Current Position: (" + player.getY() + ", " 
                    + player.getX() + ")");
            if (room instanceof EnemyRoom enemyRoom) 
            {
                enemyRoom.enter(player, new GUI_GameUI(this), 
                        engine.getGameState(), previous_X_Coordinate, 
                        previous_Y_Coordinate);
            } else {
                room.enter(player, new GUI_GameUI(this), engine.getGameState());
            }
        }

        bottomPanel.updatePositionLabel(player.getY(), player.getX());
        
        refreshProfile();
        mapPanel.refresh();
    }

    public void renderCurrentRoom() 
    {
        Player player = engine.getPlayer();
        Room room = engine.getMap().getRoom(player.getY(), player.getX());

        if (room == null) 
        {
            appendMessage("You hit a wall.");
            player.positionCorrector();
        } else 
        {
            appendMessage("Current Position: (" + player.getY() + ", " 
                    + player.getX() + ")");
            room.enter(player, new GUI_GameUI(this), engine.getGameState());
        }

        bottomPanel.updatePositionLabel(player.getY(), player.getX());
        
        refreshProfile();
    }

    public void appendMessage(String msg) 
    {
        messageBubble.stopTyping();
        messageBubble.setMessage(msg);
    }

    public void showInventory() 
    {
        Player player = engine.getPlayer();
        Inventory inventory = player.getInventory();

        if (inventory.isEmpty()) 
        {
            JOptionPane.showMessageDialog(this, "Your inventory is empty.");
            return;
        }

        new InventoryPanel(
            frame,
            inventory,
            player,
            new GUI_GameUI(this),
            () -> refreshProfile()
        );
    }

    public void saveGame() throws SQLException 
    {
        String filename = engine.getPlayer().getName() + ".dat";
        GameSaveAndLoad.saveGame(engine.getGameState(), filename, false, true, true);

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

        if (choice == JOptionPane.YES_OPTION) 
        {
            frame.showMenu();
        } else if (choice == JOptionPane.NO_OPTION) 
        {
            JOptionPane.showMessageDialog(this, "Goodbye, Hope to see you again soon!");
            System.exit(0);
        }
    }
    
    public void setInputEnabled(boolean enabled) 
    {
        bottomPanel.setButtonsEnabled(enabled);
    }
    
    public void fadeIn(Runnable afterFadeIn) 
    {
        Timer timer = new Timer(40, null);
        timer.addActionListener(new java.awt.event.ActionListener() 
        {
            float alpha = 0f;

            @Override
            public void actionPerformed(ActionEvent e) 
            {
                alpha += 0.05f;
                fadePanel.setAlpha(alpha);
                if (alpha >= 1f) 
                {
                    timer.stop();
                    if (afterFadeIn != null) afterFadeIn.run();
                }
            }
        });
        timer.start();
    }

    public void fadeOut(Runnable afterFadeOut) 
    {
        Timer timer = new Timer(40, null);
        timer.addActionListener(new java.awt.event.ActionListener() 
        {
            float alpha = 1f;

            @Override
            public void actionPerformed(ActionEvent e) 
            {
                alpha -= 0.05f;
                fadePanel.setAlpha(alpha);
                if (alpha <= 0f) 
                {
                    timer.stop();
                    if (afterFadeOut != null) afterFadeOut.run();
                }
            }
        });
        timer.start();
    }

    // === Accessors ===
    public MessageBubble getMessageBubble() 
    {
        return messageBubble;
    }
    
    public void refreshProfile() 
    {
        bottomPanel.refreshProfile();
    }

    public DungeonFrame getFrame() 
    {
        return frame;
    }
    
    public DungeonEngine getEngine() 
    {
        return engine;
    }
}