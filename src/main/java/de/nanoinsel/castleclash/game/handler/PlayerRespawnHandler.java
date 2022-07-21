package de.nanoinsel.castleclash.game.handler;

import de.nanoinsel.castleclash.CastleClash;
import de.nanoinsel.castleclash.teams.Team;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawnHandler {

    public static void Event(PlayerRespawnEvent e) {
        if (CastleClash.gameLobby.checkWorld(e.getPlayer())) return;

        noneDefault(e);
    }

    public static void noneDefault(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        Team t = CastleClash.gameLobby.findTeam(p);
        if (t != null) e.setRespawnLocation(t.getRespawnLocation());
        else e.setRespawnLocation(CastleClash.gameLobby.getSpawnLocation());
    }
}
