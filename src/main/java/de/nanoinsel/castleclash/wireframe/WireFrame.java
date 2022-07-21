package de.nanoinsel.castleclash.wireframe;

import net.minecraft.core.BaseBlockPosition;
import net.minecraft.world.level.block.BlockStructure;
import net.minecraft.world.level.block.entity.TileEntityStructure;
import net.minecraft.world.level.block.state.properties.BlockStateEnum;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Structure;
import org.bukkit.block.data.type.StructureBlock;
import org.bukkit.block.structure.UsageMode;
import org.bukkit.craftbukkit.v1_18_R1.block.CraftBlockEntityState;
import org.bukkit.craftbukkit.v1_18_R1.block.CraftStructureBlock;
import org.bukkit.craftbukkit.v1_18_R1.block.data.CraftBlockData;
import org.bukkit.craftbukkit.v1_18_R1.block.impl.CraftStructure;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.BlockVector;

import java.lang.reflect.Field;

import static de.nanoinsel.castleclash.utils.FieldAccessor.changeField;
import static de.nanoinsel.castleclash.utils.FieldAccessor.changeMethod;

public class WireFrame {

    private Location location;
    private BlockVector vector;

    static {
        //changeField(CraftBlockEntityState.class, "snapshot", null);
    }

    public WireFrame(Location location, BlockVector vector) {
        set(location, vector);
    }

    public void set(Location location, BlockVector vector) {
        this.location = location;
        this.vector = vector;

        Block block = location.getBlock();
        block.setType(Material.STRUCTURE_BLOCK);
        CraftStructureBlock csb = (CraftStructureBlock) block.getState();

        csb.setUsageMode(UsageMode.SAVE);
        StructureBlock sb = (StructureBlock) csb.getBlockData();
        sb.setMode(StructureBlock.Mode.SAVE);


        CraftStructure cs = (CraftStructure) csb.getBlockData();
        cs.setMode(StructureBlock.Mode.SAVE);

        CraftBlockData cbd = (CraftBlockData) cs;



        Object en = changeMethod(CraftBlockData.class, "getEnum", null, BlockStructure.class, "mode");
        System.out.println(((BlockStateEnum)en).a());

        try {
            Field f = CraftBlockEntityState.class.getDeclaredField("snapshot");
            f.setAccessible(true);
            TileEntityStructure tes = (TileEntityStructure) f.get(csb);
            tes.i = new BaseBlockPosition(50, 50, 50);
        } catch (Exception ex) {
            System.out.println(ChatColor.RED + "" + ex);
        }

        System.out.println(ChatColor.GREEN + "" + csb.getStructureSize());
    }
}
