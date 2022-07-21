package de.nanoinsel.castleclash.game.handler;

import de.nanoinsel.castleclash.CastleClash;
import de.nanoinsel.castleclash.game.area.Area;
import de.nanoinsel.castleclash.game.area.AreaResource;
import de.nanoinsel.castleclash.game.area.AreaType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockBreakHandler {

    public static boolean def = true;

    public static void Event(BlockBreakEvent e) {
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

    public static void protectedDefault(BlockBreakEvent e) {
        e.setCancelled(true);
    }

    public static void fieldDefault(BlockBreakEvent e) {
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

    public static void ressourceDefault(BlockBreakEvent e) {
        switch (CastleClash.gameLobby.currentGamePhase) {
            case BUILDING: case WAITING:
                ressourceProtected(e);
                break;
            case PREPARING: case FIGHTING:
                ressourceGuarded(e);
                break;
            default:
                e.setCancelled(def);
        }
    }

    public static void ressourceGuarded(BlockBreakEvent e) {
        if (AreaType.RESOURCE.blocks.contains(e.getBlock().getType())) return;
        else ressourceBlockBreak(e);
    }

    public static void ressourceProtected(BlockBreakEvent e) {
        e.setCancelled(true);
    }

    public static void ressourceBlockBreak(BlockBreakEvent e) {
        AreaResource r = CastleClash.gameLobby.findResource(e.getBlock().getType());
        if (r == null) {
            e.setCancelled(true);
            return;
        }

        e.getPlayer().getInventory().addItem(r.getItemStack());
        e.setDropItems(false);

        Bukkit.getScheduler().runTaskLater(CastleClash.self, () -> {
            e.getBlock().setType(Material.BEDROCK);
        }, 0);

        Bukkit.getScheduler().runTaskLater(CastleClash.self, () -> {
            e.getBlock().setType(r.getMaterial());

            if (e.getBlock().getBlockData() instanceof Leaves) {
                System.out.println("LEAVES");
                Leaves bd = (Leaves) e.getBlock().getBlockData();
                bd.setPersistent(true);
                e.getBlock().setBlockData(bd);
            }
        }, r.getCooldown());
    }


    public static void buildingDefault(BlockBreakEvent e) {
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

    public static void buildingProtected(BlockBreakEvent e) {
        e.setCancelled(true);
    }

    public static void buildingGuarded(BlockBreakEvent e) {
        if (AreaType.BUILDING.blocks.contains(e.getBlock().getType())) e.setCancelled(true);
    }

    public static void wallDefault(BlockBreakEvent e) {
        switch (CastleClash.gameLobby.currentGamePhase) {
            case WAITING: case BUILDING: case PREPARING:
                wallProtected(e);
                break;
            case FIGHTING:
                break;
            default:
                e.setCancelled(def);
        }
    }

    public static void wallProtected(BlockBreakEvent e) {
        e.setCancelled(true);
    }

}
