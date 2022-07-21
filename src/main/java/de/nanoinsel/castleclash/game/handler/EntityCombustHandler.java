package de.nanoinsel.castleclash.game.handler;

import de.nanoinsel.castleclash.CastleClash;
import de.nanoinsel.castleclash.teams.Team;
import org.bukkit.event.entity.EntityCombustEvent;

public class EntityCombustHandler {

    public static void Event(EntityCombustEvent e) {
        if (CastleClash.gameLobby.checkWorld(e.getEntity())) return;

        noneDefault(e);
    }

    public static void noneDefault(EntityCombustEvent e) {
        // Check if Team Boss is burning
        for (Team t : CastleClash.gameLobby.teams) {
            if (e.getEntity().getName().equals(t.getBoss().getEntity().getName())) {
                e.setCancelled(true);
                break;
            }
        }
    }
}
