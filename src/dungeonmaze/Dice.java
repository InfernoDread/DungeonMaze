/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dungeonmaze;

import java.util.Random;

/**
 *
 * @author ysadd
 */
public class Dice 
{
    private static final Random rand = new Random();

    public static int roll(int sides) 
    {
        return rand.nextInt(sides) + 1;
    }
}
