package de.nanoinsel.castleclash.teams;


import de.nanoinsel.castleclash.CastleClash;
import de.nanoinsel.castleclash.teams.boss.Boss;
import de.nanoinsel.castleclash.teams.trade.Trader;
import de.nanoinsel.castleclash.utils.NameTagChanger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.stream.Collectors;

public class Team {

    private String name;
    private TeamColor teamColor;
    private ArrayList<Player> players;

    private Location respawnLocation;
    private Boss boss;
    private Trader trader;

    public Team(String name, TeamColor teamColor) {
        this.name = name;
        this.teamColor = teamColor;
        this.players = new ArrayList<>();
        this.boss = new Boss(this);
        this.trader = new Trader(this);
        String sname = name.toLowerCase();
        respawnLocation = CastleClash.self.getCustomConfig("stored").getLocation("teams." + sname + ".position");
    }

    public String getName() {
        return name;
    }

    public TeamColor getTeamColor() {
        return teamColor;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public String getDisplay() {
        return teamColor.color + name + ": " + players.size();
    }

    public Boss getBoss() {
        return boss;
    }

    public void setBoss(Boss boss) {
        this.boss = boss;
    }

    public Trader getTrader() {
        return trader;
    }

    public void setTrader(Trader trader) {
        this.trader = trader;
    }

    public boolean isInTeam(Player p) {
        if (players.contains(p)) return true;
        else return false;
    }

    public boolean join(Player p) {
        if (isInTeam(p)) {
            p.sendMessage(ChatColor.GREEN + "You are already in this team!");
            return false;
        }

        broadcast(ChatColor.WHITE + p.getName() + ChatColor.GREEN + " joined your team.");
        players.add(p);

        setPlayerColor(p, teamColor.color);
        p.sendMessage(ChatColor.GREEN + "You joined team " + teamColor.color + name + ChatColor.GREEN + ".");
        return true;
    }

    public void setPlayerColor(Player p, ChatColor color) {
        p.setDisplayName(color + p.getName() + ChatColor.RESET);
        p.setPlayerListName(color + p.getName() + ChatColor.RESET);
        String prefix = color == ChatColor.RESET ? "" : (name + " ");
        NameTagChanger.changePlayerName(p, color + prefix, "", TeamAction.UPDATE);
    }

    public void broadcast(String message) {
        for (Player p : players) {
            p.sendMessage(message);
        }
    }

    public boolean leave(Player p) {
        setPlayerColor(p, ChatColor.RESET);
        boolean contained = players.remove(p);
        broadcast(p.getName() + ChatColor.YELLOW + " left your team.");
        return contained;
    }

    public ItemStack getIcon() {
        ItemStack it = new ItemStack(teamColor.material, 1);
        ItemMeta m = it.getItemMeta();

        m.setDisplayName(getDisplay());
        ArrayList<String> list = new ArrayList<>(players.stream().map(Player::getName).collect(Collectors.toList()));
        m.setLore(list);

        it.setItemMeta(m);
        return it;
    }

    public Location getRespawnLocation() {
        return respawnLocation;
    }

    public void setRespawnLocation(Location loc) {
        this.respawnLocation = loc;
        String name = this.name.toLowerCase();
        CastleClash.self.getCustomConfig("stored").set("teams." + name + ".position", respawnLocation);
        CastleClash.self.saveCustomConfig("stored");
    }

}
