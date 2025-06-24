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
 * The HealthPotion class is a Subclass of Item and manages instances of Small
 * Health Potion and Large Health Potion items.
 */
public class HealthPotion extends Item implements Serializable 
{
    private final int healingAmount;
    public static final int SMALL_HEAL = 20;
    public static final int LARGE_HEAL = 50;
    
    public HealthPotion(String name, String description, int healingAmount) 
    {
        super(name, description);
        this.healingAmount = healingAmount;
    }
    
    @Override
    public void use(Player player, GameUI ui) 
    {
        player.heal(healingAmount);
        ui.display("You used " + name + ". It restored " + healingAmount + " HP!");
    }
    
    @Override
    public ImageIcon getIcon() 
    {
        String path = "";
        switch (healingAmount) 
        {
            case SMALL_HEAL -> path = "/dungeonmaze/Resources/Small_HP_Potion.png";
            case LARGE_HEAL -> path = "/dungeonmaze/Resources/Large_HP_Potion.png";
        }
        return new ImageIcon(getClass().getResource(path));
    }
}
