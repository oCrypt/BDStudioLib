package com.cahrypt.bdstudiolib.collection.types;

import com.cahrypt.bdstudiolib.collection.SingletonBDComponent;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.BlockDisplay;
import org.joml.Matrix4f;

public class BlockDisplayBDComponent extends SingletonBDComponent<BlockDisplay> {
    private final BlockData blockData;

    public BlockDisplayBDComponent(String name, Matrix4f localTransformation, BlockData blockData) {
        super(name, localTransformation);

        this.blockData = blockData;
    }

    @Override
    public BlockDisplay getDisplay(Location location) {
        BlockDisplay blockDisplay = location.getWorld().spawn(location, BlockDisplay.class);
        blockDisplay.setBlock(blockData);

        return blockDisplay;
    }

    public BlockData getBlockData() {
        return blockData;
    }
}
