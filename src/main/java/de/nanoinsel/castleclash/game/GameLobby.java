package de.nanoinsel.castleclash.game;

import de.nanoinsel.castleclash.CastleClash;
import de.nanoinsel.castleclash.game.area.Area;
import de.nanoinsel.castleclash.game.area.AreaLocation;
import de.nanoinsel.castleclash.game.area.AreaResource;
import de.nanoinsel.castleclash.game.area.AreaType;
import de.nanoinsel.castleclash.teams.Team;
import de.nanoinsel.castleclash.teams.TeamColor;
import de.nanoinsel.castleclash.teams.TeamPlayer;
import de.nanoinsel.castleclash.teams.trade.Trade;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GameLobby {

    public ArrayList<Team> teams;
    public String invTitle;
    public Material quit;
    public GamePhase currentGamePhase;
    private Location spawnLocation;
    public Game game;
    public ArrayList<Area> areas;
    public ArrayList<AreaResource> resources;
    private ArrayList<TeamPlayer> teamPlayers;
    public ArrayList<Trade> trades;

    public GameLobby() {
        teams = new ArrayList<>();
        invTitle = ChatColor.DARK_GRAY + "Team Selection";
        quit = Material.BARRIER;
        currentGamePhase = GamePhase.WAITING;
        spawnLocation = CastleClash.self.getCustomConfig("stored").getLocation("lobby.position");
        game = new Game(this);
        areas = (ArrayList<Area>) CastleClash.self.getCustomConfig("stored").get("area.list", new ArrayList<Area>());
        resources = (ArrayList<AreaResource>) CastleClash.self.getCustomConfig("stored").get("resource.list", new ArrayList<AreaResource>());
        trades = (ArrayList<Trade>) CastleClash.self.getCustomConfig("stored").get("trade.list", new ArrayList<Trade>());
        teamPlayers = new ArrayList<>();

        System.out.println(areas.size());

        addTeam("Red", TeamColor.RED);
        addTeam("Blue", TeamColor.BLUE);
    }

    /** Ressources **/
    public ArrayList<AreaResource> getResources() {
        return resources;
    }

    public AreaResource findResource(Material material) {
        Optional<AreaResource> search = CastleClash.gameLobby.getResources().stream().filter(x -> x.getMaterial() == material).findFirst();
        if (!search.isPresent()) return null;
        else return  search.get();
    }

    public boolean addResource(AreaResource r) {
        boolean exists = false;
        Optional<AreaResource> search = CastleClash.gameLobby.getResources().stream().filter(x -> x.getMaterial().name().equalsIgnoreCase(r.getMaterial().name())).findFirst();
        if (search.isPresent()) {
            exists = true;
            removeResource(search.get());
        }

        resources.add(r);
        saveResources();
        return exists;
    }

    public boolean removeResource(AreaResource r) {
        boolean success = resources.remove(r);
        saveResources();
        return success;
    }

    private void saveResources() {
        CastleClash.self.getCustomConfig("stored").set("resource.list", resources);
        CastleClash.self.saveCustomConfig("stored");
    }


    /** Trades **/
    public ArrayList<Trade> getTrades() {
        return trades;
    }

    public boolean addTrade(Trade trade) {
        Optional<Trade> search = CastleClash.gameLobby.getTrades().stream().filter(x -> x.getName().equalsIgnoreCase(trade.getName())).findFirst();
        if (search.isPresent()) return false;

        boolean success = trades.add(trade);
        saveTrades();
        return success;
    }

    public boolean removeTrade(Trade trade) {
        boolean success = trades.remove(trade);
        saveTrades();
        return success;
    }

    private void saveTrades() {
        CastleClash.self.getCustomConfig("stored").set("trade.list", trades);
        CastleClash.self.saveCustomConfig("stored");
    }



    /** Areas **/
    public ArrayList<Area> getAreas() {
        return areas;
    }

    public boolean addArea(Area area) {
        Optional<Area> search = CastleClash.gameLobby.getAreas().stream().filter(x -> x.getName().equalsIgnoreCase(area.getName())).findFirst();
        if (search.isPresent()) return false;

        boolean success = areas.add(area);
        saveAreas();
        return success;
    }

    public boolean removeArea(Area area) {
        boolean success = areas.remove(area);
        saveAreas();
        return success;
    }

    private void saveAreas() {
        CastleClash.self.getCustomConfig("stored").set("area.list", areas);
        CastleClash.self.saveCustomConfig("stored");
    }


    /** Teams **/
    public void addTeam(String name, TeamColor color) {
        teams.add(new Team(name, color));
    }

    public void addTotTeam(Player p, Team t) {
        // Remove from other Teams
        for (Team team : teams) {
            if (team != t && team == findTeam(p)) team.leave(p);
        }

        // Add to Team
        t.join(p);
    }

    public void removeFromTeams(Player p) {
        // Remove from any Team
        for (Team team : teams) {
            team.leave(p);
        }
        p.sendMessage(ChatColor.YELLOW + "You left your team.");
    }

    public boolean isInTeam(Player p) {
        for (Team team : teams) {
            if (team.isInTeam(p)) return true;
        }
        return false;
    }

    public void checkTeamEntities() {
        int alive = 0;
        Team lastTeam = null;

        for (Team t : teams) {
            if (!t.getBoss().getEntity().isDead()) {
                lastTeam = t;
                alive++;
            }
        }

        if (currentGamePhase == GamePhase.FIGHTING && alive == 1 && lastTeam != null) {
            broadcast(ChatColor.GREEN + "Team " + lastTeam.getTeamColor().color + lastTeam.getName() + ChatColor.GREEN + " wins!");
            nextPhase();
        }
    }

    public void openTeamInv(Player p) {
        Inventory inv = Bukkit.createInventory(null, 9 * 1, invTitle);

        // Add for every Team an Item
        for (Team team: teams) {
            inv.setItem(teams.indexOf(team), team.getIcon());
        }

        // Quit Item
        if (isInTeam(p)) {
            ItemStack it = new ItemStack(quit, 1);
            ItemMeta m = it.getItemMeta();
            m.setDisplayName(ChatColor.RED + "Leave Team");
            it.setItemMeta(m);
            inv.setItem(inv.getSize() - 1, it);
        }

        p.openInventory(inv);
    }

    public Team findTeam(Player p) {
        for (Team team : teams) {
            if (team.isInTeam(p)) return team;
        }
        return null;
    }

    public Team findBossTeam(Entity boss) {
        for (Team team : teams) {
            if (team.getBoss().getEntity().getName().equals(boss.getName())) return team;
        }
        return null;
    }

    public void updateTeamInv() {
        for(Player p : Bukkit.getOnlinePlayers()) {
            if (p.getOpenInventory().getTitle() == invTitle) openTeamInv(p);
        }
    }


    public void teamPlayerQuit(Player p) {
        Team t = CastleClash.gameLobby.findTeam(p);
        if (t == null) return;

        teamPlayers.add(new TeamPlayer(p.getName(), t));
        removeFromTeams(p);
    }

    public void teamPlayerJoin(Player p) {
        Optional<TeamPlayer> player = teamPlayers.stream().filter(x -> x.getName().equals(p.getName())).findFirst();
        if (!player.isPresent()) return;

        TeamPlayer teamPlayer = player.get();
        teamPlayer.getTeam().join(p);
        teamPlayers.remove(teamPlayer);
    }


    /** Phase **/
    public void setPhase(GamePhase gamePhase) {
        currentGamePhase = gamePhase;
        checkPhase();
    }

    public void nextPhase() {
        currentGamePhase = currentGamePhase.next();
        checkPhase();
    }

    public void checkPhase() {
        switch (currentGamePhase) {
            case WAITING:
                broadcast(ChatColor.GREEN + "Waiting...");
                game.phaseWaiting();
                break;
            case BUILDING:
                broadcast(ChatColor.GREEN + "Start building!");
                game.phaseBuilding();
                break;
            case PREPARING:
                broadcast(ChatColor.GREEN + "Begin to prepare.");
                game.phasePreparing();
                break;
            case FIGHTING:
                broadcast(ChatColor.GREEN + "Fight!");
                game.phaseFighting();
                break;
        }
    }

    /** World **/
    public boolean checkWorld(Entity e) {
        return !e.getWorld().getName().equals(spawnLocation.getWorld().getName());
    }

    public void setSpawnLocation(Location location) {
        spawnLocation = location;
        CastleClash.self.getCustomConfig("stored").set("lobby.position", spawnLocation);
        CastleClash.self.saveCustomConfig("stored");
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public void join(Player p) {
        game.setPlayerWaiting(p);
    }

    public void broadcast(String message) {
        for (Team t : teams) {
            t.broadcast(message);
        }
    }

    public void wipeEntities() {
        for(Team t : teams) {
            // Kill every Boss
            if (t.getBoss().getEntity() != null && !t.getBoss().getEntity().isDead()) ((LivingEntity)t.getBoss().getEntity()).setHealth(0.0);
            // Kill every Trader
            if (t.getTrader().getEntity() != null && !t.getTrader().getEntity().isDead()) ((LivingEntity)t.getTrader().getEntity()).setHealth(0.0);
        }
    }

    /** Border **/
    public void createBorders() {
        List<Area> walls = areas.stream().filter(x -> x.getType() == AreaType.WALL).collect(Collectors.toList());
        for (Area wall : walls) {
            createBorder(wall.getStartLocation(), wall.getEndLocation());
        }
    }

    public void createBorder(AreaLocation start, AreaLocation end) {
        for (int x = start.getX(); x <= end.getX(); x++) {
            for (int y = start.getY(); y <= end.getY(); y++) {
                for (int z = start.getZ(); z <= end.getZ(); z++) {
                    Block block = spawnLocation.getWorld().getBlockAt(x, y, z);
                    if (block.getType() == Material.AIR) block.setType(Material.BEDROCK);
                }
            }
        }
    }

    public void removeBorders() {
        List<Area> walls = areas.stream().filter(x -> x.getType() == AreaType.WALL).collect(Collectors.toList());
        for (Area wall : walls) {
            removeBorder(wall.getStartLocation(), wall.getEndLocation());
        }
    }

    public void removeBorder(AreaLocation start, AreaLocation end) {
        for (int x = start.getX(); x <= end.getX(); x++) {
            for (int y = start.getY(); y <= end.getY(); y++) {
                for (int z = start.getZ(); z <= end.getZ(); z++) {
                    Block block = spawnLocation.getWorld().getBlockAt(x, y, z);
                    if (block.getType() == Material.BEDROCK) block.setType(Material.AIR);
                }
            }
        }
    }


    /** Test **/
    public void test(Player p) {
        // TODO: Test
    }

}
