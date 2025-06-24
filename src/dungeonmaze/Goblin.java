/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dungeonmaze;

/**
 *
 * @author ysadd
 * The Goblin is an instance of Enemy. It calls the Super Class to initialize 
 * the enemy as a Goblin, with an HP of 30 and AP of 10.
 */
public class Goblin extends Enemy
{
    public Goblin() 
    {
        super("Goblin", 30, 10);
    }
}
