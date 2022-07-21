package de.nanoinsel.castleclash.game.area;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.util.BlockVector;

import java.util.HashMap;
import java.util.Map;

public class Area implements ConfigurationSerializable {
    private String name;
    private AreaType type;
    private AreaLocation startLocation;
    private AreaLocation endLocation;
    private String world;

    public Area(String name, AreaType type, AreaLocation startLocation, AreaLocation endLocation, String world) {
        this.name = name;
        this.type = type;
        this.world = world;
        setLocations(startLocation, endLocation);
    }

    public Area(String name, AreaType type, Location startLocation, Location endLocation) {
        this.name = name;
        this.type = type;
        setLocations(startLocation, endLocation);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AreaLocation getStartLocation() {
        return startLocation;
    }

    public AreaLocation getEndLocation() {
        return endLocation;
    }

    public void setLocations(Location startLocation, Location endLocation) {
        this.world = startLocation.getWorld().getName();
        setLocations(new AreaLocation(startLocation), new AreaLocation(endLocation));
    }

    public void setLocations(AreaLocation startLocation, AreaLocation endLocation) {
        int minX = Math.min(startLocation.getX(), endLocation.getX());
        int minY = Math.min(startLocation.getY(), endLocation.getY());
        int minZ = Math.min(startLocation.getZ(), endLocation.getZ());

        int maxX = Math.max(startLocation.getX(), endLocation.getX());
        int maxY = Math.max(startLocation.getY(), endLocation.getY());
        int maxZ = Math.max(startLocation.getZ(), endLocation.getZ());

        this.startLocation = new AreaLocation(minX, minY, minZ);
        this.endLocation = new AreaLocation(maxX, maxY, maxZ);
    }

    public AreaType getType() {
        return type;
    }

    public void setType(AreaType type) {
        this.type = type;
    }

    public boolean isInArea(Location location) {
        return location.getX() >= startLocation.getX() && location.getX() <= endLocation.getX() &&
                location.getY() >= startLocation.getY() && location.getY() <= endLocation.getY() &&
                location.getZ() >= startLocation.getZ() && location.getZ() <= endLocation.getZ();
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", name);
        map.put("type", type.name());
        map.put("startLocation", startLocation);
        map.put("endLocation", endLocation);
        map.put("world", world);
        return map;
    }

    public static Area deserialize(Map<String, Object> args) {
        String name;
        if(args.containsKey("name")) name = args.get("name").toString();
        else return null;

        AreaType type;
        if(args.containsKey("type")) type = AreaType.valueOf(args.get("type").toString());
        else return null;

        AreaLocation startLocation;
        if(args.containsKey("startLocation")) startLocation = (AreaLocation) args.get("startLocation");
        else return null;

        AreaLocation endLocation;
        if(args.containsKey("endLocation")) endLocation = (AreaLocation) args.get("endLocation");
        else return null;

        String world;
        if(args.containsKey("world")) world = args.get("world").toString();
        else return null;

        return new Area(name, type, startLocation, endLocation, world);
    }
}
