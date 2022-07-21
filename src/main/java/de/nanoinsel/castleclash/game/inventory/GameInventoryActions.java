package de.nanoinsel.castleclash.game.inventory;

import de.nanoinsel.castleclash.game.GamePhase;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public enum GameInventoryActions {
    CLEAR(null, null),
    SELECT_TEAM(Material.FEATHER, GamePhase.WAITING),
    OPTIONS(Material.NETHER_STAR, null);

    public final Material material;
    public final GamePhase phase;

    GameInventoryActions(Material material, GamePhase phase) {
        this.material = material;
        this.phase = phase;
    }

    public ItemStack getItem() {
        ItemStack it = new ItemStack(material, 1);
        ItemMeta m = it.getItemMeta();
        m.setDisplayName(ChatColor.DARK_PURPLE + name());
        it.setItemMeta(m);
        return it;
    }
}
