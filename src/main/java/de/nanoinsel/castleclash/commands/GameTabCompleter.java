package de.nanoinsel.castleclash.commands;

import de.nanoinsel.castleclash.CastleClash;
import de.nanoinsel.castleclash.game.GamePhase;
import de.nanoinsel.castleclash.game.area.Area;
import de.nanoinsel.castleclash.game.area.AreaResource;
import de.nanoinsel.castleclash.game.area.AreaType;
import de.nanoinsel.castleclash.teams.Team;
import de.nanoinsel.castleclash.teams.TeamColor;
import de.nanoinsel.castleclash.teams.trade.Trade;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class GameTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only Players can use this command!");
            return null;
        }
        Player p = (Player) sender;

        if (args.length == 1) return  Arrays.asList("team", "lobby", "phase", "area", "trade", "resource");

        switch (args[0]) {
            case "team":
                return cTeams(p, args);
            case "lobby":
                return cLobby(p, args);
            case "phase":
                return cPhase(p, args);
            case "area":
                return cArea(p, args);
            case "trade":
                return cTrade(p, args);
            case "resource":
                return cResource(p, args);
            default:
                return Arrays.asList("");
        }
    }


    public List<String> cTrade(Player p, String[] args) {
        if (args.length == 2) return Arrays.asList("add", "remove");

        switch (args[1].toLowerCase()) {
            case "add":
                return cTradeAdd(p, args);
            case "remove":
                return cTradeRemove(p, args);
            default:
                return Arrays.asList("");
        }
    }

    public List<String> cTradeRemove(Player p, String[] args) {
        if (args.length == 3) return CastleClash.gameLobby.getTrades().stream().map(Trade::getName).collect(Collectors.toList());
        return Arrays.asList("");
    }

    public List<String> cTradeAdd(Player p, String[] args) {
        switch (args.length) {
            case 3:
                return Arrays.asList("<name>");
            case 4: case 6:
                return Arrays.stream(Material.values()).map(Material::name).filter(x -> x.contains(args[args.length - 1].toUpperCase())).collect(Collectors.toList());
            case 5: case 7:
                return Arrays.asList("<amount>");
            default:
                return Arrays.asList("");
        }
    }


    public List<String> cResource(Player p, String[] args) {
        if (args.length == 2) return Arrays.asList("set", "remove");

        switch (args[1].toLowerCase()) {
            case "set":
                return cResourceSet(p, args);
            case "remove":
                return cResourceRemove(p, args);
            default:
                return Arrays.asList("");
        }
    }


    public List<String> cResourceSet(Player p, String[] args) {
        switch (args.length) {
            case 3: case 4:
                return Arrays.stream(Material.values()).map(Material::name).filter(x -> x.contains(args[args.length - 1].toUpperCase())).collect(Collectors.toList());
            case 5:
                return Arrays.asList("<min>");
            case 6:
                return Arrays.asList("<max>");
            case 7:
                return Arrays.asList("<cooldown>");
            default:
                return Arrays.asList("");
        }
    }


    public List<String> cResourceRemove(Player p, String[] args) {
        if (args.length == 3) return CastleClash.gameLobby.getResources().stream().map(AreaResource::getMaterial).map(Material::name).collect(Collectors.toList());
        return Arrays.asList("");
    }



    public List<String> cArea(Player p, String[] args) {
        if (args.length == 2) return Arrays.asList("set", "remove", "block");

        switch (args[1]) {
            case "remove":
                return cAreaRemove(p, args);
            case "set":
                return cAreaSet(p, args);
            case "block":
                return  cAreaBlock(p, args);
            default:
                return Arrays.asList("");
        }
    }

    public List<String> cAreaBlock(Player p, String[] args) {
        if (args.length == 3) return Arrays.stream(AreaType.values()).map(AreaType::name).collect(Collectors.toList());
        if (args.length == 4) return Arrays.asList("add", "remove");


        switch (args[3].toLowerCase()) {
            case "add":
                if (args.length == 5) return Arrays.stream(Material.values()).map(Material::name).filter(x -> x.contains(args[4].toUpperCase())).collect(Collectors.toList());
            case "remove":
                if (args.length == 5) {
                    try {
                        return AreaType.valueOf(args[2]).blocks.stream().map(Material::name).filter(x -> x.contains(args[4].toUpperCase())).collect(Collectors.toList());
                    } catch (Exception ex) { }
                }
            default:
                return Arrays.asList("");
        }
    }

    public List<String> cAreaSet(Player p, String[] args) {
        if (args.length == 3) return Arrays.stream(AreaType.values()).map(AreaType::name).collect(Collectors.toList());

        switch (args.length) {
            case 4: case 7:
                return Arrays.asList(p.getLocation().getBlock().getX() + "");
            case 5: case 8:
                return Arrays.asList(p.getLocation().getBlock().getY() + "");
            case 6: case 9:
                return Arrays.asList(p.getLocation().getBlock().getZ() + "");
            case 10:
                return Arrays.asList("name");
            default:
                return Arrays.asList("");
        }
    }

    public List<String> cAreaRemove(Player p, String[] args) {
        if (args.length == 3) return CastleClash.gameLobby.getAreas().stream().map(Area::getName).collect(Collectors.toList());
        return Arrays.asList("");
    }



    public List<String> cPhase(Player p, String[] args) {
        if (args.length == 2) return Arrays.asList("next", "set");

        switch (args[1]) {
            case "next":
                return cPhaseNext(p, args);
            case "set":
                return cPhaseSet(p, args);
            default:
                return Arrays.asList("");
        }
    }

    public List<String> cPhaseNext(Player p, String[] args) {
        return Arrays.asList("");
    }

    public List<String> cPhaseSet(Player p, String[] args) {
        if (args.length == 3) return Arrays.stream(GamePhase.values()).map(GamePhase::name).collect(Collectors.toList());
        return Arrays.asList("");
    }


    public List<String> cLobby(Player p, String[] args) {
        if (args.length == 2) return Arrays.asList("join", "spawn", "reset");

        switch (args[1]) {
            case "spawn":
                return cLobbySpawn(p, args);
            default:
                return Arrays.asList("");
        }
    }

    public List<String> cLobbySpawn(Player p, String[] args) {
        switch (args.length) {
            case 3:
                return Arrays.asList((int)p.getLocation().getX() + "");
            case 4:
                return Arrays.asList((int)p.getLocation().getY() + "");
            case 5:
                return Arrays.asList((int)p.getLocation().getZ() + "");
            case 6:
                return Bukkit.getWorlds().stream().map(World::getName).collect(Collectors.toList());
            default:
                return Arrays.asList("");
        }
    }


    public List<String> cTeams(Player p, String[] args) {
        if (args.length == 2) return Arrays.stream(TeamColor.values()).map(TeamColor::getName).collect(Collectors.toList());
        if (args.length == 3) return Arrays.asList("spawn", "boss", "trader", "add", "remove");

        switch (args[2]) {
            case "spawn":
                return cTeamSpawn(p, args);
            case "boss":
                return cTeamBoss(p, args);
            case "trader":
                return cTeamTrader(p, args);
            case "add":
                return cTeamAdd(p, args);
            case "remove":
                return cTeamRemove(p, args);
            default:
                return Arrays.asList("");
        }
    }

    public List<String> cTeamSpawn(Player p, String[] args) {
        return Arrays.asList("");
    }

    public List<String> cTeamBoss(Player p, String[] args) {
        return Arrays.asList("");
    }

    public List<String> cTeamTrader(Player p, String[] args) {
        return Arrays.asList("");
    }

    public List<String> cTeamAdd(Player p, String[] args) {
        if (args.length == 4) return getPlayers();
        return Arrays.asList("");
    }

    public List<String> cTeamRemove(Player p, String[] args) {
        if (args.length == 4) return getPlayers();
        return Arrays.asList("");
    }



    private List<String> getPlayers() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }
}
