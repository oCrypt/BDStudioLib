package com.cahrypt.bdstudiolib.adapter;

import com.cahrypt.bdstudiolib.schematic.DisplayEntitySchematic;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import org.bukkit.entity.Display;
import org.joml.Matrix4f;

import java.lang.reflect.Type;

public interface SchematicAdapter<T extends Display> extends JsonDeserializer<DisplayEntitySchematic<T>> {

    private float getFloat(JsonArray array, int index) {
        System.out.println(array.get(index).getAsFloat());
        return array.get(index).getAsFloat();
    }

    DisplayEntitySchematic<T> handleSchematic(JsonObject object, String name, Matrix4f transformation);

    @Override
    default DisplayEntitySchematic<T> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        JsonArray transformationArray = object.getAsJsonArray("transforms");

        String childName = object.get("name").getAsString();

        Matrix4f transformation = new Matrix4f(
                getFloat(transformationArray, 0), getFloat(transformationArray, 4), getFloat(transformationArray, 8), getFloat(transformationArray, 12),
                getFloat(transformationArray, 1), getFloat(transformationArray, 5), getFloat(transformationArray, 9), getFloat(transformationArray, 13),
                getFloat(transformationArray, 2), getFloat(transformationArray, 6), getFloat(transformationArray, 10), getFloat(transformationArray, 14),
                getFloat(transformationArray, 3), getFloat(transformationArray, 7), getFloat(transformationArray, 11), getFloat(transformationArray, 15)
        );

        return handleSchematic(object, childName, transformation);
    }
}
