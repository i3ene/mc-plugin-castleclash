package de.nanoinsel.castleclash.game.handler;

import de.nanoinsel.castleclash.CastleClash;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinHandler {

    public static void Event(PlayerJoinEvent e) {
        noneDefault(e);
    }

    public static void noneDefault(PlayerJoinEvent e) {
        CastleClash.gameLobby.teamPlayerJoin(e.getPlayer());
        switch (CastleClash.gameLobby.currentGamePhase) {
            case WAITING:
                CastleClash.gameLobby.game.setPlayerWaiting(e.getPlayer());
                break;
            case BUILDING:
                CastleClash.gameLobby.game.setPlayerBuilding(e.getPlayer());
                break;
            case PREPARING:
                CastleClash.gameLobby.game.setPlayerPreparing(e.getPlayer());
                break;
            case FIGHTING:
                CastleClash.gameLobby.game.setPlayerFighting(e.getPlayer());
                break;
        }
    }
}
