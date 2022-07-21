package de.nanoinsel.castleclash.game;

import de.nanoinsel.castleclash.CastleClash;
import de.nanoinsel.castleclash.game.area.Area;
import de.nanoinsel.castleclash.game.inventory.GameInventoryActions;
import de.nanoinsel.castleclash.teams.Team;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Game {

    private GameLobby gameLobby;

    public Game(GameLobby gameLobby) {
        this.gameLobby = gameLobby;
    }

    public void phaseWaiting() {
        gameLobby.createBorders();
        gameLobby.wipeEntities();

        for (Player p : Bukkit.getWorld(CastleClash.self.world).getPlayers()) {
            setPlayerWaiting(p);
        }
    }

    public void setPlayerWaiting(Player p) {
        // Set every Player in Lobby World to Adventure
        p.setGameMode(GameMode.ADVENTURE);
        // Set Regeneration
        p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, Integer.MAX_VALUE));
        // Teleport all Players in Lobby World to Lobby Spawn
        p.teleport(gameLobby.getSpawnLocation());
        setInventory(p, GamePhase.WAITING, true);
        p.setFoodLevel(100);
    }



    public void phaseBuilding() {
        for (Player p : Bukkit.getWorld(CastleClash.self.world).getPlayers()) {
            setPlayerBuilding(p);
        }
    }

    public void setPlayerBuilding(Player p) {
        Team t = gameLobby.findTeam(p);
        if (t == null) {
            p.getInventory().clear();
            p.setGameMode(GameMode.SPECTATOR);
            return;
        }

        // Set every Player in Lobby World to Creative
        p.setGameMode(GameMode.CREATIVE);
        // Remove Effect
        p.removePotionEffect(PotionEffectType.REGENERATION);
        // Teleport every Player in Lobby World to Team Spawn
        p.teleport(t.getRespawnLocation());
        setInventory(p, GamePhase.BUILDING, true);
    }



    public void phasePreparing() {
        for(Team t : gameLobby.teams) {
            // Spawn every Trader
            t.getTrader().spawn();
        }

        for (Player p : Bukkit.getWorld(CastleClash.self.world).getPlayers()) {
            setPlayerPreparing(p);
        }
    }

    public void setPlayerPreparing(Player p) {
        Team t = gameLobby.findTeam(p);
        if (t == null) {
            p.getInventory().clear();
            p.setGameMode(GameMode.SPECTATOR);
            return;
        }

        // Set every Player in Lobby World to Survival
        p.setGameMode(GameMode.SURVIVAL);
        // Remove Effect
        p.removePotionEffect(PotionEffectType.REGENERATION);
        // Teleport every Player in Lobby World to Team Spawn
        p.teleport(t.getRespawnLocation());
        setInventory(p, GamePhase.PREPARING, true);
    }



    public void phaseFighting() {
        gameLobby.removeBorders();

        for(Team t : gameLobby.teams) {
            // Spawn every Boss
            t.getBoss().spawn();
        }

        for (Player p : Bukkit.getWorld(CastleClash.self.world).getPlayers()) {
            setPlayerFighting(p);
        }
    }

    public void setPlayerFighting(Player p) {
        Team t = gameLobby.findTeam(p);
        if (t == null) {
            p.getInventory().clear();
            p.setGameMode(GameMode.SPECTATOR);
            return;
        }

        for(Team team : gameLobby.teams) {
            // Set Health Bars
            team.getBoss().getBar().addPlayer(p);
        }

        // Set every Player in Lobby World to Survival
        p.setGameMode(GameMode.SURVIVAL);
        // Remove Effect
        p.removePotionEffect(PotionEffectType.REGENERATION);
        setInventory(p, GamePhase.FIGHTING, false);
    }




    public void setInventory(Player p, GamePhase phase, boolean clearInventory) {
        if (clearInventory) p.getInventory().clear();
        for (GameInventoryActions a : GameInventoryActions.values()) {
            if (a.phase != phase) continue;
            p.getInventory().addItem(a.getItem());
        }
    }
}
