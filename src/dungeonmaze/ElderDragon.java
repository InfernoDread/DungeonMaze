/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dungeonmaze;

/**
 *
 * @author ysadd
 * The ElderDragon is an instance of Enemy. It calls the Super Class to initialize 
 * the enemy as an ElderDragon, with an HP of 120 and AP of 36. This is the 
 * Dungeon Boss and thus it is the only Enemy that has it's own specific Into
 * Message.
 */
public class ElderDragon extends Enemy
{
    public ElderDragon() 
    {
        super("Elder Dragon", 120, 36);
    }

    @Override
    public String getIntroMessage() 
    {
        return "The Elder Dragon towers over you, its scales shimmering and its eyes burning with fire!";
    }
}
