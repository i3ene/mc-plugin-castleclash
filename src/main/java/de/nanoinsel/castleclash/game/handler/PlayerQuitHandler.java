package de.nanoinsel.castleclash.game.handler;

import de.nanoinsel.castleclash.CastleClash;
import de.nanoinsel.castleclash.teams.Team;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitHandler {

    public static void Event(PlayerQuitEvent e) {
        if (CastleClash.gameLobby.checkWorld(e.getPlayer())) return;

        noneDefault(e);
    }

    public static void noneDefault(PlayerQuitEvent e) {
        CastleClash.gameLobby.teamPlayerQuit(e.getPlayer());
    }
}
