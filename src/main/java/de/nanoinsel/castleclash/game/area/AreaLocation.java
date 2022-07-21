package de.nanoinsel.castleclash.game.area;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;

public class AreaLocation implements ConfigurationSerializable {

    private int x;
    private int y;
    private int z;

    public AreaLocation(String x, String y, String z) {
        this.x = Integer.parseInt(x);
        this.y = Integer.parseInt(y);
        this.z = Integer.parseInt(z);
    }

    public AreaLocation(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public AreaLocation(Location location) {
        this.x = (int) location.getX();
        this.y = (int) location.getY();
        this.z = (int) location.getZ();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("x", x);
        map.put("y", y);
        map.put("z", z);
        return map;
    }


    public static AreaLocation deserialize(Map<String, Object> args) {
        int x;
        if(args.containsKey("x")) x = Integer.parseInt(args.get("x").toString());
        else return null;

        int y;
        if(args.containsKey("y")) y = Integer.parseInt(args.get("y").toString());
        else return null;

        int z;
        if(args.containsKey("z")) z = Integer.parseInt(args.get("z").toString());
        else return null;

        return new AreaLocation(x, y, z);
    }

}
