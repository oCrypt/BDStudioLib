package com.cahrypt.bdstudiolib.adapter.types;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import org.joml.Matrix4f;

import java.lang.reflect.Type;

public class Matrix4fAdapter implements JsonDeserializer<Matrix4f> {

    private float getFloat(JsonArray array, int index) {
        return array.get(index).getAsFloat();
    }

    // for future serialization
    /*@Override
    public JsonElement serialize(Matrix4f transformation, Type typeOfSrc, JsonSerializationContext context) {
        JsonArray array = new JsonArray();

        array.add(transformation.m00());
        array.add(transformation.m10());
        array.add(transformation.m20());
        array.add(transformation.m30());
        array.add(transformation.m01());
        array.add(transformation.m11());
        array.add(transformation.m21());
        array.add(transformation.m31());
        array.add(transformation.m02());
        array.add(transformation.m12());
        array.add(transformation.m22());
        array.add(transformation.m32());
        array.add(transformation.m03());
        array.add(transformation.m13());
        array.add(transformation.m23());
        array.add(transformation.m33());

        return array;
    }*/

    @Override
    public Matrix4f deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonArray transformationArray = json.getAsJsonObject().getAsJsonArray("transforms");

        return new Matrix4f(
                getFloat(transformationArray, 0), getFloat(transformationArray, 4), getFloat(transformationArray, 8), getFloat(transformationArray, 12),
                getFloat(transformationArray, 1), getFloat(transformationArray, 5), getFloat(transformationArray, 9), getFloat(transformationArray, 13),
                getFloat(transformationArray, 2), getFloat(transformationArray, 6), getFloat(transformationArray, 10), getFloat(transformationArray, 14),
                getFloat(transformationArray, 3), getFloat(transformationArray, 7), getFloat(transformationArray, 11), getFloat(transformationArray, 15)
        );
    }
}
