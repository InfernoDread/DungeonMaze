/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dungeonmaze;

import java.io.Serializable;
import javax.swing.ImageIcon;

/**
 *
 * @author ysadd
 * The Item class is the Parent class of all Items in the DungeonMaze game and 
 * controls the way items are stored and handled throughout the game.
 */
public abstract class Item implements Serializable
{
    protected String name;
    protected String description;
    
    public Item(String name, String description) 
    {
        this.name = name;
        this.description = description;
    }
    
    public String getName() 
    {
        return name;
    }
    
    public String getDescription() 
    {
        return description;
    }
    
    public abstract void use(Player player, GameUI ui);
    
    public abstract ImageIcon getIcon(); 
}
