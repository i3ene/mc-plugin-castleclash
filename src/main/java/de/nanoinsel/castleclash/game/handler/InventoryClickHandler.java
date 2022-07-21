package de.nanoinsel.castleclash.game.handler;

import de.nanoinsel.castleclash.CastleClash;
import de.nanoinsel.castleclash.game.area.AreaType;
import de.nanoinsel.castleclash.teams.Team;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_18_R1.inventory.CraftInventoryPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryClickHandler {

    public static void Event(InventoryClickEvent e) {
        if (CastleClash.gameLobby.checkWorld(e.getWhoClicked())) return;

        switch (CastleClash.gameLobby.currentGamePhase) {
            case BUILDING:
                buildingDefault(e);
                break;
        }

        noneDefault(e);
    }

    public static void buildingDefault(InventoryClickEvent e) {
        if (AreaType.BUILDING.blocks.contains(e.getCursor().getType())) {
            e.setCurrentItem(null);
            e.setCancelled(true);
        }
    }

    public static void noneDefault(InventoryClickEvent e) {
        if (e.getClickedInventory() != null && e.getCurrentItem() != null && e.getView().getTitle().equals(CastleClash.gameLobby.invTitle)) {
            // Get player that clicked
            Player p = (Player) e.getWhoClicked();

            // If Quit
            if (e.getCurrentItem().getType() == CastleClash.gameLobby.quit) {
                CastleClash.gameLobby.removeFromTeams(p);
            } else {
                // Get Team based on selected Item
                Team t = CastleClash.gameLobby.teams.stream().filter(x -> x.getTeamColor().material == e.getCurrentItem().getType()).findFirst().get();
                // Add Player to Team
                CastleClash.gameLobby.addTotTeam(p, t);
            }

            // Cancel Click
            e.setCancelled(true);
            // Reopen Inventory
            CastleClash.gameLobby.updateTeamInv();
        }
    }
}
