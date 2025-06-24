/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dungeonmaze;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author ysadd
 * The MessageBubble class is used to display Game play and Battle logs to the 
 * Player in both the GamePanel and BattlePanel.
 */
public class MessageBubble extends JPanel 
{
    private String fullMessage = "";
    private String visibleMessage = "";
    private int charIndex = 0;
    private Timer typingTimer;
    private int typingSpeed = 30;
    private boolean appendMode = false;
    
    public MessageBubble() 
    {
        setOpaque(false);
        setPreferredSize(new Dimension(500, 70));
    }
    
    public void setAppendMode(boolean enabled) 
    {
        this.appendMode = enabled;
    }
    
    public void setMessage(String message) 
    {
        if (appendMode) 
        {
            this.fullMessage += (fullMessage.isEmpty() ? "" : "\n") + message;
        } else 
        {
            this.fullMessage = message;
        }
        
        this.visibleMessage = "";
        this.charIndex = 0;
        
        if (typingTimer != null && typingTimer.isRunning()) 
        {
            typingTimer.stop();
        }
        
        typingTimer = new Timer(typingSpeed, e -> 
        {
            if (charIndex < fullMessage.length()) 
            {
                visibleMessage += fullMessage.charAt(charIndex++);
                repaint();
            } else 
            {
                typingTimer.stop();
            }
        });
        
        typingTimer.start();
    }
    
    public void setTypingSpeed(int millisecondsPerChar) 
    {
        this.typingSpeed = millisecondsPerChar;
    }
    
    @Override
    protected void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int padding = 20;
        int arc = 20;
        g2.setColor(new Color(255, 255, 255, 200));
        Font font = new Font("SansSerif", Font.PLAIN, 14);
        g2.setFont(font);
        
        FontMetrics fm = g2.getFontMetrics();
        java.util.List<String> lines = wrapLines(visibleMessage, getWidth() - 30, fm);
        
        int totalHeight = lines.size() * fm.getHeight() + padding;
        setPreferredSize(new Dimension(getWidth(), totalHeight));
        revalidate();
        
        g2.fillRoundRect(0, 0, getWidth(), totalHeight, arc, arc);
        
        g2.setColor(Color.GRAY);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(0, 0, getWidth(), totalHeight, arc, arc);
        
        g2.setColor(Color.BLACK);
        int x = 15;
        int y = 25;
        
        for (String line : lines) 
        {
            g2.drawString(line, x, y);
            y += fm.getHeight();
        }
        
        g2.dispose();
    }
    
    private java.util.List<String> wrapLines(String text, int maxWidth, FontMetrics fm) 
    {
        java.util.List<String> lines = new java.util.ArrayList<>();
        StringBuilder line = new StringBuilder();
        
        for (String segment : text.split("\n")) 
        {
            for (String word : segment.split(" ")) 
            {
                if (fm.stringWidth(line + word + " ") > maxWidth) 
                {
                    lines.add(line.toString());
                    line = new StringBuilder(word).append(" ");
                } 
                else 
                {
                    line.append(word).append(" ");
                }
            }
            lines.add(line.toString().trim());
            line = new StringBuilder();
        }
        
        return lines;
    }
    
    public void stopTyping() 
    {
        if (typingTimer != null) 
        {
            typingTimer.stop();
        }
    }
    
    public void clearMessage() 
    {
        fullMessage = "";
        visibleMessage = "";
        charIndex = 0;
        repaint();
    }
}
