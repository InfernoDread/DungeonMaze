/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dungeonmaze;

import java.io.Serializable;

/**
 *
 * @author ysadd
 * The GameState class is responsible for compiling the serializable GameState 
 * for safe saving and loading. It stores the current game session, including 
 * the Player, Dungeon Map, and the Game Progress.
 */
public class GameState implements Serializable 
{
    private Player player;
    private DungeonMap map;
    private boolean gameComplete = false;
    
    public GameState(Player player, DungeonMap map) 
    {
        this.player = player;
        this.map = map;
    }
    
    public Player getPlayer() 
    {
        return player;
    }
    
    public void setPlayer(Player player) 
    {
        this.player = player;
    }
    
    public DungeonMap getMap() 
    {
        return map;
    }
    
    public void setMap(DungeonMap map) 
    {
        this.map = map;
    }
    
    public int getCurrentFloor() 
    {
        return player.getY();
    }
    
    public boolean gameIsComplete() 
    {
        return gameComplete;
    }
    
    public void setGameIsComplete(boolean complete) 
    {
        this.gameComplete = complete;
    }
}
