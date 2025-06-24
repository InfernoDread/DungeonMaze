/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dungeonmaze;

/**
 *
 * @author ysadd
 * The Orc is an instance of Enemy. It calls the Super Class to initialize 
 * the enemy as a Orc, with an HP of 60 and AP of 18.
 */
public class Orc extends Enemy
{
    public Orc() 
    {
        super("Orc", 60, 18);
    }
}
