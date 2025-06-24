/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dungeonmaze;

import java.io.Serializable;

/**
 *
 * @author ysadd
 * The Player class represents a player-controlled character with stats (Health 
 * and attack power) and inventory. This class controls the players movement, 
 * combat power, item interactions and state updates.
 */
public class Player implements Serializable
{
    private int health;
    private int x, y;
    private int attackPower = 15;
    private String name = "";
    
    private final Inventory inventory = new Inventory();
    
    public Player(int startY, int startX) 
    {
        this.health = 100;
        this.x = startX;
        this.y = startY;
    }
    
    public String getName()
    {
        return this.name;
    }
    
    public void setPlayerName(String name)
    {
        this.name = name;
    }
    
    public int getHealth() 
    { 
        return health; 
    }
    
    public void addItem(Item item) 
    {
        inventory.addItem(item);
    }
    
    public Inventory getInventory() 
    {
        return inventory;
    }
    
    public void move(String direction) 
    {
        switch (direction.toLowerCase()) 
        {
            case "north" -> y--;
            case "south" -> y++;
            case "east" -> x++;
            case "west" -> x--;
        }
    }
    
    public void takeDamage(int damage) 
    {
        health -= damage;
        if (health < 0) 
        {
            health = 0;
        }
    }
    
    public void heal(int amount) 
    {
        health += amount;
        if (health > 100) health = 100;
    }
    
    public int getAttackPower() 
    {
        return attackPower;
    }
    
    public void setAttackPower(int power) 
    {
        this.attackPower += power;
    }
    
    public int getX() 
    {
        return x;
    }
    
    public int getY() 
    {
        return y; 
    }    
    
    public boolean isAlive() 
    {
        return health > 0;
    }
    
    public void positionCorrector()
    {
        if (x > 4)
        {
            x = 4;
        }
        if (x < 0)
        {
            x = 0;
        }
        if (y > 4)
        {
            y = 4;
        }
        if (y < 0)
        {
            y = 0;
        }
    }
    
    public void setPosition(int newY, int newX) 
    {
        this.y = newY;
        this.x = newX;
    }
}