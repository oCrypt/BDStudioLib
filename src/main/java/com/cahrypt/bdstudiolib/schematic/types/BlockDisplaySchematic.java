package com.cahrypt.bdstudiolib.schematic.types;

import com.cahrypt.bdstudiolib.schematic.DisplayEntitySchematic;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.BlockDisplay;
import org.joml.Matrix4f;

public class BlockDisplaySchematic extends DisplayEntitySchematic<BlockDisplay> {
    private final BlockData blockData;

    public BlockDisplaySchematic(BlockData blockData, Matrix4f transformation) {
        super(transformation);

        this.blockData = blockData;
    }

    public BlockDisplay create(Location location) {
        BlockDisplay blockDisplay = location.getWorld().spawn(location, BlockDisplay.class);
        blockDisplay.setBlock(blockData);

        return blockDisplay;
    }
}
