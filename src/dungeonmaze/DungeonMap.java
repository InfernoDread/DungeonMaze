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
 * This class is responsible for creating and storing the Map of the Dungeon.
 * The Dungeon is created in a 5x5 grid with a total of 25 Rooms. Of these, 
 * there are 10 Enemy Rooms, 8 Treasure Rooms, 5 Empty Rooms, 1 Entry Room, and
 * 1 Exit (Boss) Room. (Entry Room is technically an Empty room with different 
 * coding and the Exit Room is technically an EnemyRoom with different coding)
 */
public class DungeonMap implements Serializable 
{
    private final Room[][] rooms;
    private final int ROWS = 5;
    private final int COLS = 5;
    
    private final int entryRow = 4;
    private int entryCol;
    
    public int getEntryRow() 
    {
        return entryRow;
    }
    
    public int getEntryCol() 
    {
        return entryCol;
    }
    
    public DungeonMap() 
    {
        rooms = new Room[ROWS][COLS];
        Random rand = new Random();
        
        //Available Rooms
        List<int[]> positions = new ArrayList<>();
        for (int row = 0; row < 5; row++) 
        {
            for (int col = 0; col < 5; col++) 
            {
                positions.add(new int[]{row, col});
            }
        }
        
        //Entry Room: (1 Only)
        entryCol = rand.nextInt(5);
        rooms[entryRow][entryCol] = new EmptyRoom("You stand at the dungeon entrance.");
        positions.removeIf(pos -> pos[0] == 4 && pos[1] == entryCol);
        
        //Exit/Boss Room: (1 Only)
        int bossCol = rand.nextInt(5);
        rooms[0][bossCol] = new EnemyRoom(
            "The final room rumbles with heat and power.",
            new ElderDragon()
        );
        positions.removeIf(pos -> pos[0] == 0 && pos[1] == bossCol);
        
        //Enemy Rooms: (10 Total)
        for (int i = 0; i < 10; i++) 
        {
            int[] pos = removeRandomPosition(positions, rand);
            int row = pos[0];  // use row, NOT column
            rooms[row][pos[1]] = new EnemyRoom(
                "An enemy lurks here...",
                generateEnemyForRow(row)
            );
        }
        
        //Treasure Rooms: (8 Total)
        for (int i = 0; i < 8; i++) 
        {
            int[] pos = removeRandomPosition(positions, rand);
            Item treasure = randomTreasure(rand);
            rooms[pos[0]][pos[1]] = new TreasureRoom(
                "You discover a hidden chest.",
                treasure
            );
        }
        
        //Empty Rooms: (5 Total)
        for (int[] pos : positions) 
        {
            rooms[pos[0]][pos[1]] = new EmptyRoom("An empty, dusty room.");
        }
    }
    
    public Room getRoom(int row, int col) 
    {
        if (row < 0 || col < 0 || row >= 5 || col >= 5) return null;
        return rooms[row][col];
    }
    
    private int[] removeRandomPosition(List<int[]> positions, Random rand) 
    {
        int index = rand.nextInt(positions.size());
        int[] pos = positions.get(index);
        positions.remove(index);
        return pos;
    }
    
    private Item randomTreasure(Random rand) 
    {
        int choice = rand.nextInt(4);
        return switch (choice) 
        {
            case 0 -> new HealthPotion("Small Healing Potion", "Restores 20 HP", 20);
            case 1 -> new HealthPotion("Large Healing Potion", "Restores 50 HP", 50);
            case 2 -> new Rune("Small Attack Rune", "Increases attack by 5", 5);
            case 3 -> new Rune("Large Attack Rune", "Increases attack by 15", 15);
            default -> new HealthPotion("Small Healing Potion", "Restores 20 HP", 20);
        };
    }
    
    private Enemy generateEnemyForRow(int row) 
    {
        return switch (row) 
        {
            case 4 -> new Skeleton();
            case 3 -> new Goblin();
            case 2 -> new Wolf();
            case 1 -> new Orc();
            case 0 -> new Orc();
            default -> new Skeleton();
        };
    }
    
    public int getClearedRoomCount() 
    {
        int count = 0;
        for (Room[] room1 : rooms) 
        {
            for (Room room : room1) 
            {
                if (room != null && room.isCleared()) 
                {
                    count++;
                }
            }
        }
        return count;
    }
}
