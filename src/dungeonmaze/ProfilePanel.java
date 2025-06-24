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
import java.awt.Image;
import java.awt.Insets;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author ysadd
 * ProfilePanel is a suplimentary class that generates the Player's Profile 
 * Panel for the BottomPanel to display in the GamePanel. It retrieved the
 * Player's stat info directly from the GameState and displays it on the Bottom
 * Left corner with dynamic updating in real time.
 */
public class ProfilePanel extends JPanel 
{
    private JLabel imageLabel;
    private final JLabel nameLabel, hpLabel, atkLabel;
    private GameState gameState;
    private Image profilePic;

    public ProfilePanel(GameState gameState) 
    {
        this.gameState = gameState;
        
        Player player  = gameState.getPlayer();

        setOpaque(false);
        setPreferredSize(new Dimension(260, 140));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setLayout(new BorderLayout());

        //Profile Image:
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.TOP);
        imageLabel.setPreferredSize(new Dimension(100, 100));
        
        try 
        {
            InputStream stream = getClass().getResourceAsStream("/dungeonmaze"
                    + "/Resources/Adventurer_Profile.png");
            if (stream != null) 
            {
                profilePic = ImageIO.read(stream);
            } else 
            {
                System.err.println("Main menu background image not found!");
            }
            Image scaledProfilePic = profilePic.getScaledInstance(100, 100,
                    Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaledProfilePic));
        } catch (IOException e) 
        {
            imageLabel.setText("No Image");
        }

        // Player Stats: Name, HP, AP
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new GridLayout(3, 1, 4, 4));
        statsPanel.setOpaque(false);
        
        nameLabel = styledLabel("Name: " + player.getName());
        hpLabel = styledLabel("HP: " + player.getHealth() + "/" + 100);
        atkLabel = styledLabel("AP: " + player.getAttackPower());
        
        statsPanel.add(wrapped(nameLabel));
        statsPanel.add(wrapped(hpLabel));
        statsPanel.add(wrapped(atkLabel));
        
        // Combine Profile Panel:
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(imageLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.6;
        gbc.insets = new Insets(0, 10, 0, 0);
        mainPanel.add(statsPanel, gbc);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private JLabel styledLabel(String text) 
    {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("SansSerif", Font.PLAIN, 12));
        return label;
    }
    
    private JPanel wrapped(JLabel label) 
    {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(new Color(40, 40, 40));
        wrapper.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        wrapper.add(label, BorderLayout.CENTER);
        return wrapper;
    }
    
    public void refreshStats() 
    {
        Player player = gameState.getPlayer();
        nameLabel.setText("Name: " + player.getName());
        hpLabel.setText("HP: " + player.getHealth() + "/" + 100);
        atkLabel.setText("AP: " + player.getAttackPower());
    }
}