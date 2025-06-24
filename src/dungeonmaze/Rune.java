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
 * The Rune class is a Subclass of Item and manages instances of Small Attack
 * Rune and Large Attack Rune items.
 */
public class Rune extends Item implements Serializable 
{
    private int attackBoost;
    public static final int SMALL_RUNE = 5;
    public static final int LARGE_RUNE = 15;
    
    public Rune(String name, String description, int attackBoost) 
    {
        super(name, description);
        this.attackBoost = attackBoost;
    }
    
    @Override
    public void use(Player player, GameUI ui) 
    {
        player.setAttackPower(attackBoost);
        ui.display("You equipped " + name + ". Your attack is now " + player.getAttackPower() + ".");
    }
    
    @Override
    public ImageIcon getIcon() 
    {
        String path = "";
        switch (attackBoost) 
        {
            case SMALL_RUNE -> path = "/dungeonmaze/Resources/Small_AP_Rune.png";
            case LARGE_RUNE -> path = "/dungeonmaze/Resources/Large_AP_Rune.png";
        }
        return new ImageIcon(getClass().getResource(path)); 
    }
}
