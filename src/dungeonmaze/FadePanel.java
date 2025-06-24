/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dungeonmaze;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

/**
 *
 * @author ysadd
 * The FadePanel class creates a visual effect of Fading in and out when 
 * transitioning between GamePanel and BattlePanel, when combat is triggered or 
 * victory is achieved or when the player flees from battle.
 */
public class FadePanel extends JPanel 
{
    private float alpha = 0.0f;
    
    public FadePanel() 
    {
        setOpaque(false);
    }
    
    public void setAlpha(float alpha) 
    {
        this.alpha = Math.max(0, Math.min(1, alpha));
        repaint();
    }
    
    public float getAlpha() 
    {
        return alpha;
    }
    
    @Override
    protected void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        if (alpha > 0) 
        {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setComposite(AlphaComposite.getInstance
                (AlphaComposite.SRC_OVER, alpha));
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
        }
    }
}