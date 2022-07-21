package de.nanoinsel.castleclash.teams.trade;

import de.nanoinsel.castleclash.game.area.Area;
import de.nanoinsel.castleclash.game.area.AreaLocation;
import de.nanoinsel.castleclash.game.area.AreaType;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import java.util.HashMap;
import java.util.Map;

public class Trade implements ConfigurationSerializable {

    private String name;
    private Material reward;
    private int rewardAmount;
    private Material cost;
    private int costAmount;

    public Trade(String name, Material reward, int rewardAmount, Material cost, int costAmount) {
        this.name = name;
        this.reward = reward;
        this.rewardAmount = rewardAmount;
        this.cost = cost;
        this.costAmount = costAmount;
    }

    public String getName() {
        return name;
    }

    public MerchantRecipe getRecipe() {
        MerchantRecipe recipe = new MerchantRecipe(new ItemStack(this.reward, this.rewardAmount), 0, Integer.MAX_VALUE, false, 0, 1.0f, 1, 1);
        recipe.addIngredient(new ItemStack(this.cost, this.costAmount));
        return recipe;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", this.name);
        map.put("reward", this.reward.name());
        map.put("rewardAmount", this.rewardAmount);
        map.put("cost", this.cost.name());
        map.put("costAmount", this.costAmount);
        return map;
    }

    public static Trade deserialize(Map<String, Object> args) {
        String name;
        if(args.containsKey("name")) name = args.get("name").toString();
        else return null;

        Material reward;
        if(args.containsKey("reward")) reward = Material.valueOf(args.get("reward").toString());
        else return null;

        int rewardAmount;
        if(args.containsKey("rewardAmount")) rewardAmount = Integer.parseInt(args.get("rewardAmount").toString());
        else return null;

        Material cost;
        if(args.containsKey("cost")) cost = Material.valueOf(args.get("cost").toString());
        else return null;

        int costAmount;
        if(args.containsKey("costAmount")) costAmount = Integer.parseInt(args.get("costAmount").toString());
        else return null;

        return new Trade(name, reward, rewardAmount, cost, costAmount);
    }

}
