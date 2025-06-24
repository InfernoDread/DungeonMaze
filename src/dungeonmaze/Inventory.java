/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dungeonmaze;

import java.io.Serializable;
import java.util.*;

/**
 *
 * @author ysadd
 * The Inventory class controls the Player's inventory storage and 
 * functionalities including adding and using Items.
 */
public class Inventory implements Serializable 
{
    private final List<Item> inventory = new ArrayList<>();

    public void addItem(Item item) 
    {
        inventory.add(item);
    }

    public void useItem(int itemIndex, Player player, GameUI ui) 
    {
        if (itemIndex < 0 || itemIndex >= inventory.size()) 
        {
            if (ui instanceof GUI_GameUI) 
            {
                return;
            }
            return;
        }

        Item item = inventory.get(itemIndex);
        item.use(player, ui);
        inventory.remove(itemIndex);
    }

    public boolean isEmpty() 
    {
        return inventory.isEmpty();
    }
    
    public List<Item> getItems() 
    {
        return inventory;
    }
}
