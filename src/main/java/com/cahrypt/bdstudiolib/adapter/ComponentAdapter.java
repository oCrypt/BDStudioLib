package com.cahrypt.bdstudiolib.adapter;

import com.cahrypt.bdstudiolib.collection.CollectionComponent;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import org.joml.Matrix4f;

public interface ComponentAdapter<T extends CollectionComponent<?>> extends JsonDeserializer<T> {

    private float getFloat(JsonArray array, int index) {
        return array.get(index).getAsFloat();
    }

    default Matrix4f deserializeTransformation(JsonObject jsonObject) {
        JsonArray transformationArray = jsonObject.getAsJsonArray("transforms");

        return new Matrix4f(
                getFloat(transformationArray, 0), getFloat(transformationArray, 4), getFloat(transformationArray, 8), getFloat(transformationArray, 12),
                getFloat(transformationArray, 1), getFloat(transformationArray, 5), getFloat(transformationArray, 9), getFloat(transformationArray, 13),
                getFloat(transformationArray, 2), getFloat(transformationArray, 6), getFloat(transformationArray, 10), getFloat(transformationArray, 14),
                getFloat(transformationArray, 3), getFloat(transformationArray, 7), getFloat(transformationArray, 11), getFloat(transformationArray, 15)
        );
    }

    default String deserializeName(JsonObject jsonObject) {
        return jsonObject.get("name").getAsString();
    }
}
