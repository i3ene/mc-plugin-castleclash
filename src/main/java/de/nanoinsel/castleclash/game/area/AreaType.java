package de.nanoinsel.castleclash.game.area;

import de.nanoinsel.castleclash.CastleClash;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public enum AreaType {
    DEFAULT,
    PROTECTED, // Every Block is protected
    FIELD, // Only protected during specific phases
    BUILDING, // Specified Blocks only are protected
    RESOURCE, // Specified Blocks only are not protected
    WALL;

    public ArrayList<Material> blocks;

    AreaType() {
        this.blocks = new ArrayList<>();
        for (String block : CastleClash.self.getCustomConfig("stored").getStringList("area.type." + this.name() + ".blocks")) {
            blocks.add(Material.valueOf(block));
        }
    }

    public boolean addBlock(String block) {
        Material search = Material.valueOf(block);
        if (blocks.contains(search)) return false;
        blocks.add(search);
        save();
        return true;
    }

    public boolean removeBlock(String block) {
        boolean success = blocks.remove(Material.valueOf(block));
        save();
        return success;
    }

    private void save() {
        List<String> list = blocks.stream().map(Material::name).collect(Collectors.toList());
        CastleClash.self.getCustomConfig("stored").set("area.type." + this.name() + ".blocks", list);
        CastleClash.self.saveCustomConfig("stored");
    }
}
