package de.nanoinsel.castleclash.teams;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;

public enum TeamColor {
    RED("Red", ChatColor.RED, BarColor.RED, Material.RED_CONCRETE),
    BLUE("Blue", ChatColor.BLUE, BarColor.BLUE, Material.BLUE_CONCRETE),
    YELLOW("Yellow", ChatColor.YELLOW, BarColor.YELLOW, Material.YELLOW_CONCRETE),
    GREEN("Green", ChatColor.GREEN, BarColor.GREEN, Material.GREEN_CONCRETE);

    public final String name;
    public final ChatColor color;
    public final BarColor barColor;
    public final Material material;

    TeamColor(String name, ChatColor color, BarColor barColor, Material material) {
        this.name = name;
        this.color = color;
        this.barColor = barColor;
        this.material = material;
    }

    public String getName() {
        return this.name;
    }

}
