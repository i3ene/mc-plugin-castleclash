package de.nanoinsel.castleclash.game.handler;

import de.nanoinsel.castleclash.CastleClash;
import de.nanoinsel.castleclash.game.area.Area;
import de.nanoinsel.castleclash.game.area.AreaType;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceHandler {

    public static boolean def = true;

    public static void Event(BlockPlaceEvent e) {
        if (CastleClash.gameLobby.checkWorld(e.getPlayer())) return;

        for (Area area : CastleClash.gameLobby.areas) {
            if (area.isInArea(e.getBlock().getLocation())) {
                switch (area.getType()) {
                    case PROTECTED:
                        protectedDefault(e);
                        break;
                    case FIELD:
                        fieldDefault(e);
                        break;
                    case RESOURCE:
                        ressourceDefault(e);
                        break;
                    case BUILDING:
                        buildingDefault(e);
                        break;
                    case WALL:
                        wallDefault(e);
                        break;
                    default:
                        e.setCancelled(def);
                }
            }
        }
    }

    public static void protectedDefault(BlockPlaceEvent e) {
        e.setCancelled(true);
    }

    public static void fieldDefault(BlockPlaceEvent e) {
        switch (CastleClash.gameLobby.currentGamePhase) {
            case PREPARING: case FIGHTING:
                return;
        }

        boolean multi = false;
        for (Area area : CastleClash.gameLobby.areas) {
            if (area.getType() == AreaType.FIELD) continue;
            if (area.isInArea(e.getBlock().getLocation())) {
                multi = true;
                return;
            }
        }
        if (!multi) e.setCancelled(true);
    }

    public static void ressourceDefault(BlockPlaceEvent e) {
        switch (CastleClash.gameLobby.currentGamePhase) {
            case BUILDING: case WAITING: case PREPARING: case FIGHTING:
                ressourceProtected(e);
                break;
            default:
                e.setCancelled(def);
        }
    }

    public static void ressourceProtected(BlockPlaceEvent e) {
        e.setCancelled(true);
    }

    public static void buildingDefault(BlockPlaceEvent e) {
        switch (CastleClash.gameLobby.currentGamePhase) {
            case WAITING:
                buildingProtected(e);
                break;
            case BUILDING: case PREPARING: case FIGHTING:
                buildingGuarded(e);
                break;
            default:
                e.setCancelled(def);
        }
    }

    public static void buildingGuarded(BlockPlaceEvent e) {
        if (AreaType.BUILDING.blocks.contains(e.getBlock().getType())) e.setCancelled(true);
    }

    public static void buildingProtected(BlockPlaceEvent e) {
        e.setCancelled(true);
    }

    public static void wallDefault(BlockPlaceEvent e) {
        return;
    }

}
