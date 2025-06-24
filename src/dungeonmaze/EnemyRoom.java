/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dungeonmaze;

import java.io.Serializable;

/**
 *
 * @author ysadd
 * EnemyRoom is an Instance of Room which contains an Enemy (One of 5 depending
 * on the Floor (Y-Coordinate) and the Room Type). When this rooms is entered 
 * (using the enter() method from the parent class Room.java) it triggers a 
 * transition into the BattlePanel featuring Fading GUI animations. The room is 
 * updated upon victory or escape.
 */
public class EnemyRoom extends Room implements Serializable 
{
    private Enemy enemy;

    public EnemyRoom(String desc, Enemy enemy) 
    {
        super(desc);
        this.enemy = enemy;
    }

    @Override
    public void enter(Player player, GameUI ui, GameState gameState) 
    {
        enter(player, ui, gameState, player.getX(), player.getY());
    }
    
    public void enter(Player player, GameUI ui, GameState gameState, int prevX, int prevY) 
    {
        if (this.isCleared()) 
        {
            ui.display(description);
            ui.display("You have defeated the " + enemy.getName() + "!");
            return;
        }

        ui.display(description);
        ui.display(enemy.getIntroMessage());

        if (ui instanceof GUI_GameUI guiUI) 
        {
            GamePanel gamePanel = guiUI.getGamePanel();
            DungeonFrame frame = gamePanel.getFrame();
            
            gamePanel.setInputEnabled(false);
            int delay = 2500;
            
            if (enemy.getName().equalsIgnoreCase("Elder Dragon")) 
            {
                delay = 4000;
            }
            
            javax.swing.Timer timer = new javax.swing.Timer(delay, e -> 
            {
                gamePanel.setInputEnabled(true);
                gamePanel.fadeIn(() -> 
                {
                    frame.startBattle(player, enemy, gameState, prevX, prevY, 
                            () -> 
                    {
                        gamePanel.fadeOut(() -> 
                        {
                            this.setCleared(!enemy.isAlive());
                            gamePanel.renderCurrentRoom();
                            
                            new javax.swing.Timer(50, ev -> 
                            {
                                ((javax.swing.Timer) ev.getSource()).stop();
                                if (!enemy.isAlive()) 
                                {
                                    gamePanel.appendMessage("You leave the "
                                            + "room victorious.");
                                } 
                                else 
                                {
                                    gamePanel.appendMessage("You fled "
                                            + "Successfully and may return "
                                            + "later...");
                                }
                            }).start();
                        });
                    });
                });
            });
            timer.setRepeats(false);
            timer.start();
        }
    }
    
    public Enemy getEnemy() 
    {
        return enemy;
    }
}
