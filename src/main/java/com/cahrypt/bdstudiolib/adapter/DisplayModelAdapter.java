package com.cahrypt.bdstudiolib.adapter;

import com.cahrypt.bdstudiolib.DisplayEntitySchematic;
import com.cahrypt.bdstudiolib.DisplayModel;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import org.bukkit.Bukkit;
import org.bukkit.block.data.BlockData;
import org.joml.Matrix4f;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author oCrypt
 */
public class DisplayModelAdapter implements JsonDeserializer<DisplayModel> {

    private float getFloat(JsonArray array, int index) {
        System.out.println(array.get(index).getAsFloat());
        return array.get(index).getAsFloat();
    }

    @Override
    public DisplayModel deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        String modelName = jsonObject.get("name").getAsString();

        JsonArray array = jsonObject.getAsJsonArray("children");

        List<DisplayEntitySchematic> schematics = new ArrayList<>();

        for (JsonElement element : array) {
            JsonObject object = element.getAsJsonObject();

            boolean isBlockDisplay = object.get("isBlockDisplay").getAsBoolean();

            if (!isBlockDisplay) {
                continue;
            }

            String childName = object.get("name").getAsString();
            BlockData blockData = Bukkit.getServer().createBlockData(childName);

            JsonArray transformationArray = object.getAsJsonArray("transforms");

            Matrix4f transformation = new Matrix4f(
                    getFloat(transformationArray, 0), getFloat(transformationArray, 4), getFloat(transformationArray, 8), getFloat(transformationArray, 12),
                    getFloat(transformationArray, 1), getFloat(transformationArray, 5), getFloat(transformationArray, 9), getFloat(transformationArray, 13),
                    getFloat(transformationArray, 2), getFloat(transformationArray, 6), getFloat(transformationArray, 10), getFloat(transformationArray, 14),
                    getFloat(transformationArray, 3), getFloat(transformationArray, 7), getFloat(transformationArray, 11), getFloat(transformationArray, 15)
            );

            schematics.add(new DisplayEntitySchematic(blockData, transformation));
        }

        return new DisplayModel(modelName, schematics);
    }
}
