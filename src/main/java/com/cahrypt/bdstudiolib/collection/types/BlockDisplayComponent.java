package com.cahrypt.bdstudiolib.collection.types;

import com.cahrypt.bdstudiolib.collection.CollectionComponent;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.BlockDisplay;
import org.joml.Matrix4f;

import java.util.Collections;
import java.util.List;

public class BlockDisplayComponent extends CollectionComponent<BlockDisplay> {
    private final BlockData blockData;

    public BlockDisplayComponent(Matrix4f localTransformation, BlockData blockData) {
        super(localTransformation);

        this.blockData = blockData;
    }

    @Override
    public List<BlockDisplay> getDisplays(Location location) {
        BlockDisplay blockDisplay = location.getWorld().spawn(location, BlockDisplay.class);
        blockDisplay.setBlock(blockData);

        return Collections.singletonList(blockDisplay);
    }
}
