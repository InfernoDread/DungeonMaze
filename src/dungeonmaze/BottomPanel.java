/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dungeonmaze;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.sql.SQLException;

/**
 *
 * @author ysadd
 * The BottomPanel class is responsible for the lower section of the GamePanel
 * interface. It consists of the Player Profile visuals, D-Pad movement and the 
 * Menu Buttons (Save & Quit + Inventory)
 */
public class BottomPanel extends JPanel 
{
    private final GamePanel gamePanel;
    private JLabel positionLabel;
    private JButton northBtn, southBtn, eastBtn, westBtn;
    private JButton inventoryBtn, saveBtn;
    private ProfilePanel playerProfile;
    
    public BottomPanel(GamePanel gamePanel) 
    {
        this.gamePanel = gamePanel;
        setLayout(new BorderLayout());
        setOpaque(false);
    }
    
    public void initPanelsAfterEngineReady() 
    {
        removeAll();
        
        add(createProfilePanel(), BorderLayout.WEST);
        add(createDPadPanel(), BorderLayout.CENTER);
        add(createMenuButtonsPanel(), BorderLayout.EAST);
        
        revalidate();
        repaint();
    }

    private JPanel createProfilePanel() 
    {
        playerProfile = new ProfilePanel(gamePanel.getEngine().getGameState());
        return playerProfile;
    }

    private JPanel createDPadPanel() 
    {
        JPanel wheelPanel = new JPanel(new GridBagLayout());
        wheelPanel.setOpaque(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;
        
        Dimension buttonSize = new Dimension(50, 40);
        
        northBtn = new JButton("N");
        northBtn.setPreferredSize(buttonSize);
        northBtn.addActionListener(e -> gamePanel.move("north"));
        gbc.gridx = 1;
        gbc.gridy = 0;
        wheelPanel.add(northBtn, gbc);
        
        westBtn = new JButton("W");
        westBtn.setPreferredSize(buttonSize);
        westBtn.addActionListener(e -> gamePanel.move("west"));
        gbc.gridx = 0;
        gbc.gridy = 1;
        wheelPanel.add(westBtn, gbc);
        
        positionLabel = new JLabel("(?, ?)", SwingConstants.CENTER);
        positionLabel.setForeground(new Color(255, 255, 255, 180));
        positionLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        gbc.gridx = 1;
        gbc.gridy = 1;
        wheelPanel.add(positionLabel, gbc);
        
        eastBtn = new JButton("E");
        eastBtn.setPreferredSize(buttonSize);
        eastBtn.addActionListener(e -> gamePanel.move("east"));
        gbc.gridx = 2;
        gbc.gridy = 1;
        wheelPanel.add(eastBtn, gbc);
        
        southBtn = new JButton("S");
        southBtn.setPreferredSize(buttonSize);
        southBtn.addActionListener(e -> gamePanel.move("south"));
        gbc.gridx = 1;
        gbc.gridy = 2;
        wheelPanel.add(southBtn, gbc);

        return wheelPanel;
    }
    
    private JPanel createMenuButtonsPanel() 
    {
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1));
        buttonPanel.setOpaque(true);
        buttonPanel.setPreferredSize(new Dimension(140, 100));
        
        inventoryBtn = new JButton("Inventory");
        saveBtn = new JButton("Save & Quit");
        
        Color bubbleColor = new Color(40, 40, 40);
        Color textColor = Color.WHITE;
        
        for (JButton btn : new JButton[]{inventoryBtn, saveBtn}) 
        {
            btn.setBackground(bubbleColor);
            btn.setForeground(textColor);
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        }
        
        inventoryBtn.addActionListener(e -> gamePanel.showInventory());
        saveBtn.addActionListener(e -> {
            try 
            {
                gamePanel.saveGame();
            } 
            catch (SQLException ex) 
            {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to save game: " 
                        + ex.getMessage(), "Database Error", 
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        
        buttonPanel.add(inventoryBtn);
        buttonPanel.add(saveBtn);
        return buttonPanel;
    }
    
    public void updatePositionLabel(int y, int x) 
    {
        if (positionLabel != null) 
        {
            positionLabel.setText("(" + y + ", " + x + ")");
        }
    }
    
    public void refreshProfile() 
    {
        if (playerProfile != null) 
        {
            playerProfile.refreshStats();
        }
    }
    
    public void setButtonsEnabled(boolean enabled) 
    {
        if (northBtn != null) northBtn.setEnabled(enabled);
        if (southBtn != null) southBtn.setEnabled(enabled);
        if (eastBtn != null) eastBtn.setEnabled(enabled);
        if (westBtn != null) westBtn.setEnabled(enabled);
        if (inventoryBtn != null) inventoryBtn.setEnabled(enabled);
        if (saveBtn != null) saveBtn.setEnabled(enabled);
    }
}