package de.nanoinsel.castleclash.game.handler;

import de.nanoinsel.castleclash.CastleClash;
import de.nanoinsel.castleclash.game.area.Area;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;

public class PlayerBucketFillHandler {

    public static boolean def = true;

    public static void Event(PlayerBucketFillEvent e) {
        if (CastleClash.gameLobby.checkWorld(e.getPlayer())) return;

        for (Area area : CastleClash.gameLobby.areas) {
            if (area.isInArea(e.getBlock().getLocation())) {
                switch (area.getType()) {
                    case PROTECTED:
                        protectedDefault(e);
                        break;
                    case RESOURCE:
                        ressourceDefault(e);
                        break;
                    case BUILDING:
                        buildingDefault(e);
                        break;
                    default:
                        e.setCancelled(def);
                }
            }
        }
    }

    public static void protectedDefault(PlayerBucketFillEvent e) {
        e.setCancelled(true);
    }

    public static void ressourceDefault(PlayerBucketFillEvent e) {
        e.setCancelled(true);
    }

    public static void buildingDefault(PlayerBucketFillEvent e) {
        return;
    }
}
