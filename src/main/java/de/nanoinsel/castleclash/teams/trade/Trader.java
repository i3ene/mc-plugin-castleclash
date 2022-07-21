package de.nanoinsel.castleclash.teams.trade;

import de.nanoinsel.castleclash.CastleClash;
import de.nanoinsel.castleclash.teams.Team;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import java.util.ArrayList;
import java.util.List;

public class Trader {

    private Team team;
    private Location location;
    private Entity entity;

    public Trader(Team team) {
        this.team = team;
        String name = team.getName().toLowerCase();
        location = CastleClash.self.getCustomConfig("stored").getLocation("teams." + name + ".traderPosition");
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
        String name = team.getName().toLowerCase();
        CastleClash.self.getCustomConfig("stored").set("teams." + name + ".traderPosition", location);
        CastleClash.self.saveCustomConfig("stored");
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public void spawn() {
        entity = location.getWorld().spawnEntity(location, EntityType.VILLAGER);
        System.out.println(location);

        entity.setCustomName(team.getTeamColor().color + team.getName() + " Trader");
        entity.setCustomNameVisible(true);
        entity.setPersistent(true);
        entity.setSilent(true);

        LivingEntity le = ((LivingEntity)entity);
        le.setAI(false);
        le.setInvulnerable(true);
        le.setRemoveWhenFarAway(false);

        Villager vil = (Villager)entity;
        vil.setRecipes(getRecipes());

    }

    public List<MerchantRecipe> getRecipes() {
        List<MerchantRecipe> recipes = new ArrayList<>();

        for(Trade trade : CastleClash.gameLobby.getTrades()) {
            recipes.add(trade.getRecipe());
        }

        return recipes;
    }
}
