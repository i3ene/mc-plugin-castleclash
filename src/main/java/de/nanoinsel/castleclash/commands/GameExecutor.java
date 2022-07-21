package de.nanoinsel.castleclash.commands;

import de.nanoinsel.castleclash.CastleClash;
import de.nanoinsel.castleclash.game.GamePhase;
import de.nanoinsel.castleclash.game.area.Area;
import de.nanoinsel.castleclash.game.area.AreaLocation;
import de.nanoinsel.castleclash.game.area.AreaResource;
import de.nanoinsel.castleclash.game.area.AreaType;
import de.nanoinsel.castleclash.teams.Team;
import de.nanoinsel.castleclash.teams.trade.Trade;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class GameExecutor implements CommandExecutor {
    private ArrayList<Team> teams;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only Players can use this command!");
            return false;
        }
        Player p = (Player) sender;


        if (args.length < 1) {
            p.sendMessage(ChatColor.RED + "Use /cc <team | lobby | phase | area | resource | trade>");
            return false;
        }


        switch (args[0].toLowerCase()) {
            case "team":
                cTeams(p, args);
                break;
            case "lobby":
                cLobby(p, args);
                break;
            case "phase":
                cPhase(p, args);
                break;
            case "area":
                cArea(p, args);
                break;
            case "resource":
                cResource(p, args);
                break;
            case "trade":
                cTrade(p, args);
                break;
            case "test":
                CastleClash.gameLobby.test(p);
                break;
            default:
                p.sendMessage(ChatColor.YELLOW + "No such command found");
        }

        return false;
    }

    public GameExecutor(ArrayList<Team> teams) {
        this.teams = teams;
    }


    // cc trade [args]
    public void cTrade(Player p, String[] args) {
        if (args.length < 2) {
            p.sendMessage(ChatColor.RED + "Use /cc trade <add | remove>");
            return;
        }

        switch (args[1]) {
            case "add":
                cTradeAdd(p, args);
                break;
            case "remove":
                cTradeRemove(p, args);
                break;
            default:
                p.sendMessage(ChatColor.YELLOW + "No such command found");
        }
    }

    // cc trade add [name] {rewardMaterial} [rewardAmount] {costMaterial} [costAmount]
    public void cTradeAdd(Player p, String[] args) {
        if (args.length < 3) {
            p.sendMessage(ChatColor.RED + "Use /cc trade add [name] <rewardMaterial> [rewardAmount] <costMaterial> [costAmount]");
            return;
        }

        Material rewardMaterial = Material.valueOf(args[3]);
        int rewardAmount = Integer.parseInt(args[4]);
        Material costMaterial = Material.valueOf(args[5]);
        int costAmount = Integer.parseInt(args[6]);


        boolean success = CastleClash.gameLobby.addTrade(
                new Trade(args[2], rewardMaterial, rewardAmount, costMaterial, costAmount)
        );
        if (success) p.sendMessage(ChatColor.GREEN + "Trade " + ChatColor.WHITE + args[2] + ChatColor.GREEN + " set!");
        else p.sendMessage(ChatColor.RED + "Trade " + ChatColor.WHITE + args[2] + ChatColor.RED + " could not be added!");
    }

    // cc trade remove [name]
    public void cTradeRemove(Player p, String[] args) {
        if (args.length < 3) {
            p.sendMessage(ChatColor.RED + "Use /cc trade remove [name]");
            return;
        }

        Optional<Trade> search = CastleClash.gameLobby.getTrades().stream().filter(x -> x.getName().equalsIgnoreCase(args[2])).findFirst();
        if (!search.isPresent()) {
            p.sendMessage(ChatColor.YELLOW + "Specified area not found.");
            return;
        }

        if (CastleClash.gameLobby.removeTrade(search.get())) p.sendMessage(ChatColor.GREEN + "Trade " + ChatColor.WHITE + args[2] + ChatColor.GREEN + " removed!");
        else p.sendMessage(ChatColor.RED + "Trade " + ChatColor.WHITE + args[2] + ChatColor.RED + " does not exist!");
    }



    // cc resource [args]
    public void cResource(Player p, String[] args) {
        if (args.length < 2) {
            p.sendMessage(ChatColor.RED + "Use /cc resource <set | remove>");
            return;
        }

        switch (args[1]) {
            case "set":
                cResourceSet(p, args);
                break;
            case "remove":
                cResourceRemove(p, args);
                break;
            default:
                p.sendMessage(ChatColor.YELLOW + "No such command found");
        }
    }

    // cc resource set [material] [item] [minCount] [maxCount] [cooldown]
    public void cResourceSet(Player p, String[] args) {
        if (args.length < 7) {
            p.sendMessage(ChatColor.RED + "Use /cc resource set [material] [item] [minCount] [maxCount] [cooldown]");
            return;
        }

        Material material = Material.valueOf(args[2]);
        Material item = Material.valueOf(args[3]);
        int minCount = Integer.parseInt(args[4]);
        int maxCount = Integer.parseInt(args[5]);
        int cooldown = Integer.parseInt(args[6]);

        boolean exists = CastleClash.gameLobby.addResource(new AreaResource(material, item, minCount, maxCount, cooldown));
        if (!exists) p.sendMessage(ChatColor.GREEN + "Material " + ChatColor.WHITE + args[2] + ChatColor.GREEN + " set!");
        else p.sendMessage(ChatColor.YELLOW + "Material " + ChatColor.WHITE + args[2] + ChatColor.YELLOW + " has been updated!");
    }

    // cc resource remove {material}
    public void cResourceRemove(Player p, String[] args) {
        if (args.length < 3) {
            p.sendMessage(ChatColor.RED + "Use /cc resource remove {material}");
            return;
        }

        Optional<AreaResource> search = CastleClash.gameLobby.getResources().stream().filter(x -> x.getMaterial().name().equalsIgnoreCase(args[2])).findFirst();
        if (!search.isPresent()) {
            p.sendMessage(ChatColor.YELLOW + "Specified material not found.");
            return;
        }

        if (CastleClash.gameLobby.removeResource(search.get())) p.sendMessage(ChatColor.GREEN + "Material " + ChatColor.WHITE + args[2] + ChatColor.GREEN + " removed!");
        else p.sendMessage(ChatColor.RED + "Material " + ChatColor.WHITE + args[2] + ChatColor.RED + " does not exist!");
    }



    // cc area [args]
    public void cArea(Player p, String[] args) {
        if (args.length < 2) {
            p.sendMessage(ChatColor.RED + "Use /cc phase <set | remove | block>");
            return;
        }

        switch (args[1]) {
            case "remove":
                cAreaRemove(p, args);
                break;
            case "set":
                cAreaSet(p, args);
                break;
            case "block":
                cAreaBlock(p, args);
                break;
            default:
                p.sendMessage(ChatColor.YELLOW + "No such command found");
        }
    }

    // cc area block [type]
    public void cAreaBlock(Player p, String[] args) {
        if (args.length < 3) {
            p.sendMessage(ChatColor.RED + "Use /cc area block [type]");
            return;
        }

        try {
            AreaType type = AreaType.valueOf(args[2].toUpperCase());
            cAreaBlockType(p, args, type);
        } catch (Exception ex) {
            p.sendMessage(ChatColor.YELLOW + "No such type found");
        }
    }

    // cc area block [type] [action] {block}
    public void cAreaBlockType(Player p, String[] args, AreaType type) {
        if (args.length < 5) {
            p.sendMessage(ChatColor.RED + "Use /cc area block [type] <add | remove> {block}");
            return;
        }

        switch(args[3].toLowerCase()) {
            case "add":
                type.addBlock(args[4]);
                p.sendMessage(ChatColor.GREEN + "Block " + ChatColor.WHITE + args[4] + ChatColor.GREEN + " added to " + ChatColor.WHITE + type + ChatColor.GREEN + "!");
                break;
            case "remove":
                type.removeBlock(args[4]);
                p.sendMessage(ChatColor.GREEN + "Block " + ChatColor.WHITE + args[4] + ChatColor.GREEN + " removed from " + ChatColor.WHITE + type + ChatColor.GREEN + "!");
                break;
            default:
                p.sendMessage(ChatColor.YELLOW + "No such command found");
        }
    }



    // cc area remove [name]
    public void cAreaRemove(Player p, String[] args) {
        if (args.length < 3) {
            p.sendMessage(ChatColor.RED + "Use /cc area remove [name]");
            return;
        }

        Optional<Area> search = CastleClash.gameLobby.getAreas().stream().filter(x -> x.getName().equalsIgnoreCase(args[2])).findFirst();
        if (!search.isPresent()) {
            p.sendMessage(ChatColor.YELLOW + "Specified area not found.");
            return;
        }

        if (CastleClash.gameLobby.removeArea(search.get())) p.sendMessage(ChatColor.GREEN + "Area " + ChatColor.WHITE + args[2] + ChatColor.GREEN + " removed!");
        else p.sendMessage(ChatColor.RED + "Area " + ChatColor.WHITE + args[2] + ChatColor.RED + " does not exist!");
    }

    // cc area set [type]
    public void cAreaSet(Player p, String[] args) {
        if (args.length < 3) {
            p.sendMessage(ChatColor.RED + "Use /cc area set [type]");
            return;
        }

        switch (args[2].toLowerCase()) {
            case "resource":
                cAreaSetType(p, args, AreaType.RESOURCE);
                break;
            case "building":
                cAreaSetType(p, args, AreaType.BUILDING);
                break;
            case "protected":
                cAreaSetType(p, args, AreaType.PROTECTED);
                break;
            case "wall":
                cAreaSetType(p, args, AreaType.WALL);
                break;
            default:
                p.sendMessage(ChatColor.YELLOW + "No such command found");
        }
    }

    // cc area set [type] {pos1} {pos2} {name}
    public void cAreaSetType(Player p, String[] args, AreaType type) {
        if (args.length < 10) {
            p.sendMessage(ChatColor.RED + "Use /cc area set [type] <[x1] [y1] [z1]> <[x2] [y2] [z2]> {name}");
            return;
        }

        boolean success = CastleClash.gameLobby.addArea(
                new Area(args[9], type,
                new AreaLocation(args[3], args[4], args[5]),
                new AreaLocation(args[6], args[7], args[8]),
                p.getWorld().getName())
        );
        if (success) p.sendMessage(ChatColor.GREEN + "Area " + ChatColor.WHITE + args[9] + ChatColor.GREEN + " set!");
        else p.sendMessage(ChatColor.RED + "Area " + ChatColor.WHITE + args[9] + ChatColor.RED + " could not be added!");
    }



    // cc phase [args]
    public void cPhase(Player p, String[] args) {
        if (args.length < 2) {
            p.sendMessage(ChatColor.RED + "Use /cc phase <next | set>");
            return;
        }

        switch (args[1]) {
            case "next":
                cPhaseNext(p, args);
                break;
            case "set":
                cPhaseSet(p, args);
                break;
            default:
                p.sendMessage(ChatColor.YELLOW + "No such command found");
        }
    }

    // cc phase next
    public void cPhaseNext(Player p, String[] args) {
        CastleClash.gameLobby.nextPhase();
    }

    // cc phase set [phase]
    public void cPhaseSet(Player p, String[] args) {
        if (args.length < 3) {
            p.sendMessage(ChatColor.RED + "Use /cc phase set <" + Arrays.toString(Arrays.stream(GamePhase.values()).map(x -> x.name()).toArray()) + ">");
            return;
        }

        GamePhase phase = GamePhase.valueOf(args[2]);
        if (phase == null) {
            p.sendMessage(ChatColor.YELLOW + "No such phase found");
            return;
        }
        CastleClash.gameLobby.setPhase(phase);
    }


    // cc lobby [args]
    public void cLobby(Player p, String[] args) {
        if (args.length < 2) {
            p.sendMessage(ChatColor.RED + "Use /cc lobby <join | spawn | reset>");
            return;
        }

        switch (args[1]) {
            case "spawn":
                cLobbySpawn(p, args);
                break;
            case "join":
                cLobbyJoin(p, args);
                break;
            case "reset":
                // TODO: Reset
                break;
            default:
                p.sendMessage(ChatColor.YELLOW + "No such command found");
        }
    }

    // cc lobby spawn <{x} {y} {z} | {x} {y} {z} {world}>
    public void cLobbySpawn(Player p, String[] args) {
        switch (args.length) {
            case 2:
                cLobbySpawnSet(p, args);
                break;
            case 5:
                cLobbySpawnXYZ(p, args);
                break;
            case 6:
                cLobbySpawnWorldXYZ(p, args);
                break;
            default:
                p.sendMessage(ChatColor.RED + "Use /cc lobby spawn [x] [y] [z] {world}");
        }
    }

    // cc lobby join
    public void cLobbyJoin(Player p, String[] args) {
        p.sendMessage(ChatColor.GREEN + "Joining lobby...");
        CastleClash.gameLobby.join(p);
    }

    // cc lobby spawn
    public void cLobbySpawnSet(Player p, String[] args) {
        CastleClash.gameLobby.setSpawnLocation(p.getLocation());
        p.sendMessage(ChatColor.GREEN + "Lobby set at current position");
    }

    // cc lobby spawn {x} {y} {z}
    public void cLobbySpawnXYZ(Player p, String[] args) {
        CastleClash.gameLobby.setSpawnLocation(new Location(p.getWorld(), Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4])));
        p.sendMessage(ChatColor.GREEN + "Lobby position set");
    }

    // cc lobby spawn {x} {y} {z} {world}
    public void cLobbySpawnWorldXYZ(Player p, String[] args) {
        World world = Bukkit.getWorld(args[5]);
        if (world == null) {
            p.sendMessage(ChatColor.RED + "World " + ChatColor.GRAY + args[2] + ChatColor.RED + " does not exist!");
        } else {
            CastleClash.gameLobby.setSpawnLocation(new Location(world, Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4])));
            p.sendMessage(ChatColor.GREEN + "Lobby set for world " + ChatColor.GRAY + args[5]);
        }
    }


    // cc team [args]
    public void cTeams(Player p, String[] args) {
        if (args.length < 3) {
            p.sendMessage(ChatColor.RED + "Use /cc team [team] <spawn | boss | trader>");
            return;
        }

        Optional<Team> search = teams.stream().filter(x -> x.getName().equalsIgnoreCase(args[1])).findFirst();
        if (!search.isPresent()) {
            p.sendMessage(ChatColor.YELLOW + "Specified team not found.");
            return;
        }

        Team t = search.get();
        switch (args[2]) {
            case "spawn":
                cTeamSpawn(p, args, t);
                break;
            case "boss":
                cTeamBoss(p, args, t);
                break;
            case "trader":
                cTeamTrader(p, args, t);
                break;
            case "add":
                cTeamAdd(p, args, t);
                break;
            case "remove":
                cTeamRemove(p, args, t);
                break;
            default:
                p.sendMessage(ChatColor.YELLOW + "No such command found");
        }
    }

    // cc team [team] spawn
    public void cTeamSpawn(Player p, String[] args, Team t) {
        t.setRespawnLocation(p.getLocation());
        p.sendMessage(ChatColor.GREEN + "Spawn set for " + t.getTeamColor().color + t.getName());
    }

    // cc team [team] boss
    public void cTeamBoss(Player p, String[] args, Team t) {
        t.getBoss().setLocation(p.getLocation());
        p.sendMessage(ChatColor.GREEN + "Boss spawn set for " + t.getTeamColor().color + t.getName());
    }

    // cc team [team] trader
    public void cTeamTrader(Player p, String[] args, Team t) {
        t.getTrader().setLocation(p.getLocation());
        p.sendMessage(ChatColor.GREEN + "Trader spawn set for " + t.getTeamColor().color + t.getName());
    }

    // cc team [team] add [player]
    public void cTeamAdd(Player p, String[] args, Team t) {
        CastleClash.gameLobby.addTotTeam(p, t);
        p.sendMessage(ChatColor.GREEN + "Player " + ChatColor.WHITE + p.getName() + ChatColor.GREEN + " added to team " + t.getTeamColor().color + t.getName());
    }

    // cc team [team] remove [player]
    public void cTeamRemove(Player p, String[] args, Team t) {
        t.leave(p);
        p.sendMessage(ChatColor.GREEN + "Player " + ChatColor.WHITE + p.getName() + ChatColor.GREEN + " removed from team " + t.getTeamColor().color + t.getName());
    }

}
