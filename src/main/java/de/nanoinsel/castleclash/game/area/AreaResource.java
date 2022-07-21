package de.nanoinsel.castleclash.game.area;

import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class AreaResource implements ConfigurationSerializable {
    private Material material;
    private Material item;
    private int minCount;
    private int maxCount;
    private int cooldown;

    public AreaResource(Material material, Material item, int minCount, int maxCount, int cooldown) {
        this.material = material;
        this.item = item;
        this.minCount = minCount;
        this.maxCount = maxCount;
        this.cooldown = cooldown;
    }

    public Material getMaterial() {
        return material;
    }

    public Material getItem() {
        return item;
    }

    public int getMinCount() {
        return minCount;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public int getCooldown() {
        return cooldown;
    }

    public ItemStack getItemStack() {
        int rnd = (int)(Math.random() * (double)maxCount) + minCount;
        return new ItemStack(item, rnd);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("material", material.name());
        map.put("item", item.name());
        map.put("minCount", minCount);
        map.put("maxCount", maxCount);
        map.put("cooldown", cooldown);
        return map;
    }

    public static AreaResource deserialize(Map<String, Object> args) {
        Material material;
        if(args.containsKey("material")) material = Material.valueOf(args.get("material").toString());
        else return null;

        Material item;
        if(args.containsKey("item")) item = Material.valueOf(args.get("item").toString());
        else return null;

        int minCount;
        if(args.containsKey("minCount")) minCount = Integer.parseInt(args.get("minCount").toString());
        else return null;

        int maxCount;
        if(args.containsKey("maxCount")) maxCount = Integer.parseInt(args.get("maxCount").toString());
        else return null;

        int cooldown;
        if(args.containsKey("cooldown")) cooldown = Integer.parseInt(args.get("cooldown").toString());
        else return null;

        return new AreaResource(material, item, minCount, maxCount, cooldown);
    }
}
