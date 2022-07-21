package de.nanoinsel.castleclash.game;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum GamePhase {
    WAITING(Material.CLOCK, ChatColor.YELLOW),
    BUILDING(Material.COBBLESTONE, ChatColor.GRAY),
    PREPARING(Material.IRON_PICKAXE, ChatColor.DARK_GRAY),
    FIGHTING(Material.IRON_SWORD, ChatColor.RED);

    public final Material material;
    public final ChatColor color;

    GamePhase(Material material, ChatColor color) {
        this.material = material;
        this.color = color;
    }

    public GamePhase next() {
        int entry = this.ordinal() + 1;
        GamePhase[] gamePhases = values();

        if (gamePhases.length < entry + 1) return gamePhases[0];
        else return gamePhases[entry];
    }
}
