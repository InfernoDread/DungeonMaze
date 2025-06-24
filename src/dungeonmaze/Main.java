/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package dungeonmaze;

import javax.swing.SwingUtilities;

/**
 *
 * @author ysadd
 */
public class Main 
{
    public static void main(String[] args) 
    {
        SwingUtilities.invokeLater(() -> 
        {
            new DungeonFrame();
        });
    }
}
