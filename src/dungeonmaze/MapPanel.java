/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dungeonmaze;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *
 * @author ysadd
 * MapPanel is a suplimentary class for the GamePanel class. It displays a 
 * visual representation of the Dungeon Map, which is a 5x5 grid. Rooms that
 * the player has already cleared are highlighted in gray, while the room the 
 * Player is currently in is highlighted in Green.
 */
public class MapPanel extends JPanel 
{
    private final GameState gameState;

    public MapPanel(GameState gameState) 
    {
        this.gameState = gameState;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        if (gameState == null) return;
        
        Player player = gameState.getPlayer();
        DungeonMap map = gameState.getMap();
        
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        
        int maxTileWidth = panelWidth / 5;
        int maxTileHeight = panelHeight / 5;
        
        int tileWidth = Math.min(maxTileWidth, maxTileHeight * 2);
        int tileHeight = tileWidth / 2;
        
        int gridWidth = tileWidth * 5;
        int gridHeight = tileHeight * 5;
        
        int xOffset = (panelWidth - gridWidth) / 2;
        int yOffset = (panelHeight - gridHeight) / 2;
        
        for (int row = 0; row < 5; row++) 
        {
            for (int col = 0; col < 5; col++) 
            {
                Room room = map.getRoom(row, col);
                
                int x = xOffset + col * tileWidth;
                int y = yOffset + row * tileHeight;
                
                if (player.getY() == row && player.getX() == col) 
                {
                    g.setColor(Color.GREEN);
                } else if (room != null && room.isCleared()) 
                {
                    g.setColor(Color.LIGHT_GRAY);
                } else if (room != null) 
                {
                    g.setColor(Color.DARK_GRAY);
                } else 
                {
                    g.setColor(Color.BLACK);
                }
                
                g.fillRect(x, y, tileWidth, tileHeight);
                g.setColor(Color.WHITE);
                g.drawRect(x, y, tileWidth, tileHeight);
            }
        }
    }
    
    public void refresh() 
    {
        repaint();
    }
}
