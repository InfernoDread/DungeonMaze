/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dungeonmaze;

import java.io.Serializable;

/**
 *
 * @author ysadd
 * The class is the Superclass for all room types (such as EnemyRoom, 
 * TreasureRoom, EmptyRoom) and handles Room behaviors within the Dungeon Map.
 */
public abstract class Room implements Serializable 
{
    private boolean cleared = false;    
    
    protected String description;

    public Room(String desc) 
    {
        this.description = desc;
    }
    
    public abstract void enter(Player player, GameUI ui, GameState gameState);
    
    public boolean isCleared() 
    {
        return cleared;
    }
    
    public void setCleared(boolean cleared) 
    {
        this.cleared = cleared;
    }
}
