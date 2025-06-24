/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dungeonmaze;

import java.io.Serializable;

/**
 *
 * @author ysadd
 * 
 */
public class TreasureRoom extends Room implements Serializable 
{
    private final Item item;
    
    public TreasureRoom(String desc, Item item) 
    {
        super(desc);
        this.item = item;
    }
    
    @Override
    public void enter(Player player, GameUI ui, GameState gameState) 
    {
        if (this.isCleared())
        {
            ui.display(description);
            ui.display("The chest has already been opened, Nothing remains.");
            return;
        }
        
        ui.display(description);
        ui.display("You found an item: " + item.getName() + " - " 
                + item.getDescription());
        player.addItem(item);
        this.setCleared(true);
    }
}
