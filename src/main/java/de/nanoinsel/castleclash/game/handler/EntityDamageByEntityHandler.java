package de.nanoinsel.castleclash.game.handler;

import de.nanoinsel.castleclash.CastleClash;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageByEntityHandler {

    public static void Event(EntityDamageByEntityEvent e) {
        if (CastleClash.gameLobby.checkWorld(e.getEntity())) return;

        noneDefault(e);
    }

    public static void noneDefault(EntityDamageByEntityEvent e) {
        // Prevent Damage from same Team
        if (e.getDamager() instanceof Player) {
            Player enemy = (Player) e.getDamager();
            if ((e.getEntity() instanceof Player)) {
                Player player = (Player) e.getEntity();
                if (CastleClash.gameLobby.findTeam(player) == CastleClash.gameLobby.findTeam(enemy)) e.setCancelled(true);
            } else {
                if (CastleClash.gameLobby.findBossTeam(e.getEntity()) == CastleClash.gameLobby.findTeam(enemy)) e.setCancelled(true);
            }
        }
    }
}
