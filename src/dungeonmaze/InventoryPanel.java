/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dungeonmaze;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author ysadd
 */
public class InventoryPanel extends JDialog 
{
    private int selectedIndex = -1;
    private final JButton useButton;
    private final JButton closeButton;
    
    public InventoryPanel(JFrame owner, Inventory inventory, Player player, GUI_GameUI guiUI, Runnable refreshCallback) 
    {
        super(owner, "Inventory", true);
        setLayout(new BorderLayout());
        
        List<Item> items = inventory.getItems();
        JPanel[] itemPanels = new JPanel[items.size()];
        
        JPanel gridPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        gridPanel.setBackground(Color.DARK_GRAY);
        
        for (int i = 0; i < items.size(); i++) 
        {
            Item item = items.get(i);
            ImageIcon originalIcon = item.getIcon();
            Image scaledImage = originalIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);
            JLabel imageLabel = new JLabel(scaledIcon);
            
            JLabel nameLabel = new JLabel("<html><center>" + item.getName() + "</center></html>");
            nameLabel.setForeground(Color.WHITE);
            nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            nameLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
            nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
            
            JLabel descLabel = new JLabel("<html><center>" + item.getDescription() + "</center></html>");
            descLabel.setForeground(Color.LIGHT_GRAY);
            descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            descLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
            descLabel.setHorizontalAlignment(SwingConstants.CENTER);    
            
            JPanel itemPanel = new JPanel();
            itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));
            itemPanel.setBackground(new Color(30, 30, 30));
            itemPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            itemPanel.setPreferredSize(new Dimension(130, 180));
            
            imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            itemPanel.add(imageLabel);
            itemPanel.add(Box.createVerticalStrut(5));
            itemPanel.add(nameLabel);
            itemPanel.add(descLabel);
            
            final int index = i;
            itemPanel.addMouseListener(new MouseAdapter() 
            {
                @Override
                public void mouseClicked(MouseEvent e) 
                {
                    selectedIndex = index;
                    for (JComponent panel : itemPanels) 
                    {
                        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                    }
                    itemPanel.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));
                }
            });
            
            itemPanels[i] = itemPanel;
            gridPanel.add(itemPanel);
        }
        
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        centerPanel.setBackground(Color.DARK_GRAY);
        centerPanel.add(gridPanel);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.DARK_GRAY);
        useButton = new JButton("Use");
        closeButton = new JButton("Close");
        
        useButton.addActionListener(e -> 
        {
            if (selectedIndex >= 0 && selectedIndex < items.size()) 
            {
                inventory.useItem(selectedIndex, player, guiUI);
                dispose();
                refreshCallback.run();
            }
        });
        
        closeButton.addActionListener(e -> dispose());
        
        buttonPanel.add(useButton);
        buttonPanel.add(closeButton);
        
        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        pack();
        setLocationRelativeTo(owner);
        setVisible(true);
    }
}
