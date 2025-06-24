/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dungeonmaze;

/**
 *
 * @author ysadd
 * GUI_GameUI is the graphical implementation of the GameUI interface and was
 * created to replace the old CUI_GameUI from previous implementations. It 
 * manages display messages for both GamePanel and BattlePanel.
 */
public class GUI_GameUI implements GameUI
{
    private GamePanel gamePanel;
    private BattlePanel battlePanel;

    public GUI_GameUI(GamePanel gamePanel) 
    {
        this.gamePanel = gamePanel;
    }

    public GUI_GameUI(BattlePanel battlePanel) 
    {
        this.battlePanel = battlePanel;
    }

    @Override
    public void display(String message) 
    {
        if (battlePanel != null) 
        {
            battlePanel.appendLog(message);
        } else if (gamePanel != null) 
        {
            gamePanel.appendMessage(message);
        }
    }

    @Override
    public String getInput() 
    {
        throw new UnsupportedOperationException("getInput() is not supported in GUI mode.");
    }

    public GamePanel getGamePanel() 
    {
        return gamePanel;
    }

    public BattlePanel getBattlePanel() 
    {
        return battlePanel;
    }
}
