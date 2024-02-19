package com.cahrypt.bdstudiolib.adapter.types;

import com.cahrypt.bdstudiolib.adapter.ComponentAdapter;
import com.cahrypt.bdstudiolib.collection.types.BlockDisplayBDComponent;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import org.bukkit.Bukkit;
import org.bukkit.block.data.BlockData;
import org.joml.Matrix4f;

import java.lang.reflect.Type;

public class BlockComponentAdapter implements ComponentAdapter<BlockDisplayBDComponent> {

    @Override
    public BlockDisplayBDComponent deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();

        String name = deserializeName(object);
        Matrix4f transformation = deserializeTransforms(object, context);
        BlockData blockData = Bukkit.getServer().createBlockData(name);

        return new BlockDisplayBDComponent(name, transformation, blockData);
    }
}
