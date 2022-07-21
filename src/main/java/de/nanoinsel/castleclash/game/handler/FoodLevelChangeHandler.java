package de.nanoinsel.castleclash.game.handler;

import de.nanoinsel.castleclash.CastleClash;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodLevelChangeHandler {

    public static void Event(FoodLevelChangeEvent e) {
        if (CastleClash.gameLobby.checkWorld(e.getEntity())) return;

        noneDefault(e);
    }

    public static void noneDefault(FoodLevelChangeEvent e) {
        switch (CastleClash.gameLobby.currentGamePhase) {
            case WAITING: case BUILDING:
                e.getEntity().setFoodLevel(100);
                e.setCancelled(true);
                return;
        }
    }

}
