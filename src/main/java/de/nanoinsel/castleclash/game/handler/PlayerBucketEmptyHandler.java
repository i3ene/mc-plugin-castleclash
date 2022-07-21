package de.nanoinsel.castleclash.game.handler;

import de.nanoinsel.castleclash.CastleClash;
import de.nanoinsel.castleclash.game.area.Area;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

public class PlayerBucketEmptyHandler {

    public static boolean def = true;

    public static void Event(PlayerBucketEmptyEvent e) {
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

    public static void protectedDefault(PlayerBucketEmptyEvent e) {
        e.setCancelled(true);
    }

    public static void ressourceDefault(PlayerBucketEmptyEvent e) {
        e.setCancelled(true);
    }

    public static void buildingDefault(PlayerBucketEmptyEvent e) {
        return;
    }

}
