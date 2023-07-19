package com.cahrypt.bdstudiolib;

import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.BlockDisplay;
import org.joml.Matrix4f;

/**
 * Represents a single block in a display model.
 *
 * @author oCrypt
 */
public record DisplayEntitySchematic(BlockData blockData, Matrix4f transformation) {

    /**
     * Creates a BlockDisplay entity at the given location with the given rotation in radians.
     * @param location The location to spawn the BlockDisplay at.
     * @param pitchRotationRadians The pitch rotation in radians.
     * @param yawRotationRadians The yaw rotation in radians.
     * @return The BlockDisplay entity created.
     */
    public BlockDisplay create(Location location, double pitchRotationRadians, double yawRotationRadians) {
        BlockDisplay blockDisplay = location.getWorld().spawn(location, BlockDisplay.class);

        blockDisplay.setBlock(blockData);
        blockDisplay.setTransformationMatrix(transformation);

        blockDisplay.setRotation((float) (pitchRotationRadians * 180 / Math.PI), (float) (yawRotationRadians * 180 / Math.PI));

        return blockDisplay;
    }
}
