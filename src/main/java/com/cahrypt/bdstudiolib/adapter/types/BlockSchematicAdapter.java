package com.cahrypt.bdstudiolib.adapter.types;

import com.cahrypt.bdstudiolib.adapter.SchematicAdapter;
import com.cahrypt.bdstudiolib.schematic.DisplayEntitySchematic;
import com.cahrypt.bdstudiolib.schematic.types.BlockDisplaySchematic;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.BlockDisplay;
import org.joml.Matrix4f;

public class BlockSchematicAdapter implements SchematicAdapter<BlockDisplay> {

    @Override
    public DisplayEntitySchematic<BlockDisplay> handleSchematic(JsonObject object, String name, Matrix4f transformation) {
        BlockData blockData = Bukkit.getServer().createBlockData(name);
        return new BlockDisplaySchematic(blockData, transformation);
    }
}
