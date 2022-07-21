package de.nanoinsel.castleclash.game.inventory;

import de.nanoinsel.castleclash.game.GamePhase;
import org.bukkit.inventory.Inventory;

public class GameInventory {

    public static Inventory getInventory(GamePhase gamePhase) {
        switch (gamePhase) {
            case WAITING:
                return waitingInventory();
            case BUILDING:
                return buildingInventory();
            case PREPARING:
                return preparationInventory();
            case FIGHTING:
                return  fightingInventory();
            default:
                return null;
        }
    }

    public static Inventory waitingInventory() {
        return null;
    }

    public static Inventory buildingInventory() {
        return null;
    }

    public static Inventory preparationInventory() {
        return null;
    }

    public static Inventory fightingInventory() {
        return null;
    }
}
