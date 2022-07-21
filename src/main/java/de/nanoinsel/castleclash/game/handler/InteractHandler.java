package de.nanoinsel.castleclash.game.handler;

import de.nanoinsel.castleclash.CastleClash;
import de.nanoinsel.castleclash.game.inventory.GameInventoryActions;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractHandler {

    public static void Event(PlayerInteractEvent e) {
        if (CastleClash.gameLobby.checkWorld(e.getPlayer())) return;

        switch (CastleClash.gameLobby.currentGamePhase) {
            case WAITING:
                waitingDefault(e);
                break;
        }

    }

    public static void waitingDefault(PlayerInteractEvent e) {
        if (e.getPlayer().getInventory().getItemInMainHand().getType() == GameInventoryActions.SELECT_TEAM.material) {
            // Open Team inventory
            CastleClash.gameLobby.openTeamInv(e.getPlayer());
        }
    }

}
