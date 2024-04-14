package com.cahrypt.bdstudiolib.adapter;

import com.cahrypt.bdstudiolib.collection.BDComponent;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import org.joml.Matrix4f;

public interface ComponentAdapter<T extends BDComponent<?>> extends JsonDeserializer<T> {
    default String deserializeName(JsonObject jsonObject) {
        return jsonObject.get("name").getAsString();
    }

    default Matrix4f deserializeTransforms(JsonObject jsonObject, JsonDeserializationContext context) {
        return context.deserialize(jsonObject.getAsJsonArray("transforms"), Matrix4f.class);
    }
}
