package de.nanoinsel.castleclash.game.handler;

import de.nanoinsel.castleclash.CastleClash;
import de.nanoinsel.castleclash.teams.Team;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityDeathHandler {

    public static void Event(EntityDeathEvent e) {
        if (CastleClash.gameLobby.checkWorld(e.getEntity())) return;

        noneDefault(e);
    }

    public static void noneDefault(EntityDeathEvent e) {
        // Remove Health Bar
        String name = e.getEntity().getName();
        for (Team t : CastleClash.gameLobby.teams) {
            Entity entity = t.getBoss().getEntity();
            if (entity == null) break;
            if (entity.getName().equals(name)) {
                t.getBoss().getBar().removeAll();
                // Check Win
                CastleClash.gameLobby.checkTeamEntities();
                break;
            }
        }
    }
}
