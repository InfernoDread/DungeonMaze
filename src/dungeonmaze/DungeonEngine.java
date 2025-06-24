/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dungeonmaze;

/**
 *
 * @author ysadd
 * DungeonEngine controls the GameState  as the core controller for the 
 * DungeonMaze game. It manages all aspects of GameState such as Player, Dungeon
 * Map, and overall Game Progress for GUI integrated saving and loading 
 * functionality
 */
public class DungeonEngine 
{
    private Player player;
    private DungeonMap map;
    private GameState gameState;
    
    public DungeonEngine(GameUI ui, Player player, DungeonMap map) 
    {
        this.map = map;
        this.player = player;
        this.gameState = new GameState(player, map);
    }
    
    public void loadSavedGame(String fileName) 
    {
        gameState = GameSaveAndLoad.loadGame(fileName);

        if (gameState != null) 
        {
            this.player = gameState.getPlayer();
            this.map = gameState.getMap();
        } 
        else 
        {
            this.map = new DungeonMap();
            this.player = new Player(map.getEntryRow(), map.getEntryCol());
            this.gameState = new GameState(player, map);
        }
    }

    public Player getPlayer() 
    {
        return player;
    }

    public DungeonMap getMap() 
    {
        return map;
    }

    public GameState getGameState() 
    {
        return gameState;
    }
}
