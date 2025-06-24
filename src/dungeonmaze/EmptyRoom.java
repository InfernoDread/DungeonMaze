/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dungeonmaze;

import java.io.Serializable;

/**
 *
 * @author ysadd
 */
public class EmptyRoom extends Room implements Serializable 
{
    public EmptyRoom(String desc) 
    {
        super(desc);
    }

    @Override
    public void enter(Player player, GameUI ui, GameState gameState) 
    {
        ui.display(description);
        this.setCleared(true);
    }
}
