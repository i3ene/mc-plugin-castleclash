package de.nanoinsel.castleclash.teams.boss;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.nanoinsel.castleclash.CastleClash;
import de.nanoinsel.castleclash.teams.Team;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.craftbukkit.v1_18_R1.boss.CraftBossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
import java.util.Base64;
import java.util.UUID;

public class Boss {

    private static final String skin = "http://textures.minecraft.net/texture/891a68929ff752eca20085262663e9e7dc3571117d5e4743f7bd80024826f3a6";

    private Team team;
    private Location location;
    private Entity entity;
    private BossBar bar;
    private double maxHealth;

    public Boss(Team team) {
        this.team = team;
        String name = team.getName().toLowerCase();
        location = CastleClash.self.getCustomConfig("stored").getLocation("teams." + name + ".bossPosition");
        maxHealth = CastleClash.self.getCustomConfig("stored").getDouble("teams." + name + ".bossHealth");
        bar = new CraftBossBar(team.getName(), team.getTeamColor().barColor, BarStyle.SOLID);
        bar.setVisible(true);
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
        String name = team.getName().toLowerCase();
        CastleClash.self.getCustomConfig("stored").set("teams." + name + ".bossPosition", location);
        CastleClash.self.saveCustomConfig("stored");
    }

    public void spawn() {
        entity = location.getWorld().spawnEntity(location, EntityType.ZOMBIE);
        System.out.println(location);

        entity.setCustomName(team.getTeamColor().color + "King " + team.getName());
        entity.setCustomNameVisible(true);
        entity.setGlowing(true);
        entity.setPersistent(true);
        entity.setSilent(true);

        LivingEntity le = ((LivingEntity)entity);
        le.setAI(false);
        le.setHealth(maxHealth);
        le.setRemoveWhenFarAway(false);

        EntityEquipment ee = le.getEquipment();
        ee.setHelmet(getSkull(skin));
        ee.setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE, 1));
        ee.setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS, 1));
        ee.setBoots(new ItemStack(Material.DIAMOND_BOOTS, 1));

        updateBossBar(0.0);
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public BossBar getBar() {
        return bar;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public void updateBossBar(double damage) {
        double currentHealth = ((LivingEntity)entity).getHealth() - damage;
        if (currentHealth < 0.0) currentHealth = 0.0;

        bar.setProgress(currentHealth / maxHealth);
    }

    public static ItemStack getSkull(String url) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        if(url.isEmpty())return item;


        SkullMeta itemMeta = (SkullMeta) item.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField = null;
        try
        {
            profileField = itemMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(itemMeta, profile);
        }
        catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e)
        {
            e.printStackTrace();
        }
        item.setItemMeta(itemMeta);
        return item;
    }
}
