/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dungeonmaze;

/**
 *
 * @author ysadd
 * The Wolf is an instance of Enemy. It calls the Super Class to initialize 
 * the enemy as a Wolf, with an HP of 40 and AP of 12.
 */
public class Wolf extends Enemy
{
    public Wolf() 
    {
        super("Wolf", 40, 12);
    }
}
