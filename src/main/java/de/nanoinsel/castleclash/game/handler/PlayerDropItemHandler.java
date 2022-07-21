package de.nanoinsel.castleclash.game.handler;

import de.nanoinsel.castleclash.CastleClash;
import de.nanoinsel.castleclash.game.inventory.GameInventoryActions;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerDropItemHandler {

    public static void Event(PlayerDropItemEvent e) {
        noneDefault(e);
    }

    public static void noneDefault(PlayerDropItemEvent e) {
        for (GameInventoryActions a : GameInventoryActions.values()) {
            if (a.phase != CastleClash.gameLobby.currentGamePhase) continue;
            if (a.material == e.getItemDrop().getItemStack().getType()) e.setCancelled(true);
        }
    }
}
