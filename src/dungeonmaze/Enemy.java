/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dungeonmaze;

import java.io.Serializable;

/**
 *
 * @author ysadd
 * This class represents an Enemy with variable damage output and critical hit 
 * chance. Enemies vary depending on floor, and have varying attackPower and 
 * health. Enemy behavior and selection is handled in EnemyRoom.java.
 */
public class Enemy implements Serializable 
{
    private final String name;
    private int health;
    private final int attackPower;
    
    public Enemy(String name, int health, int attackPower) 
    {
        this.name = name;
        this.health = health;
        this.attackPower = attackPower;
    }
    
    public String getIntroMessage() 
    {
        return "The Room was a Monster's Den! An angry looking " + name 
                + " appears!";
    }
    
    public String getName() 
    {
        return name;
    }
    
    public int getHealth() 
    { 
        return health; 
    }
    
    public int getAttackPower() 
    { 
        return attackPower;
    }
    
    public void takeDamage(int damage) 
    {
        health -= damage;
        if (health < 0)
        {
            health = 0;
        }
    }
    
    public boolean isAlive() 
    {
        return health > 0;
    }
}
