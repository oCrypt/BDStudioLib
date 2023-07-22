package com.cahrypt.bdstudiolib.adapter.types;

import com.cahrypt.bdstudiolib.DisplayModelGsonUtil;
import com.cahrypt.bdstudiolib.adapter.ComponentAdapter;
import com.cahrypt.bdstudiolib.collection.CollectionComponent;
import com.cahrypt.bdstudiolib.collection.types.BlockDisplayComponent;
import com.cahrypt.bdstudiolib.collection.types.DisplayCollection;
import com.cahrypt.bdstudiolib.collection.types.ItemDisplayComponent;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import org.bukkit.entity.Display;
import org.joml.Matrix4f;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DisplayCollectionAdapter implements ComponentAdapter<CollectionComponent<? extends Display>> {
    private static final Map<String, Class<? extends CollectionComponent<? extends Display>>> SCHEMATIC_IDENTIFIER_MAP = ImmutableMap.<String, Class<? extends CollectionComponent<?>>>builder()
            .put("isBlockDisplay", BlockDisplayComponent.class)
            .put("isItemDisplay", ItemDisplayComponent.class)
            .put("isCollection", DisplayCollection.class)
            .build();

    private static final Gson GSON = DisplayModelGsonUtil.getGson();

    private Class<? extends CollectionComponent<?>> getIdentifier(JsonObject object) {
        for (Map.Entry<String, Class<? extends CollectionComponent<?>>> entry : SCHEMATIC_IDENTIFIER_MAP.entrySet()) {
            if (object.has(entry.getKey())) {
                return entry.getValue();
            }
        }

        return null;
    }

    @Override
    public CollectionComponent<? extends Display> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String schematicName = deserializeName(jsonObject);

        Class<? extends CollectionComponent<?>> schematicClass = getIdentifier(jsonObject);

        if (schematicClass == null) {
            throw new JsonParseException("Could not find schematic identifier! Outdated schematic?");
        }

        if (schematicClass != DisplayCollection.class) {
            return GSON.fromJson(jsonObject, schematicClass);
        }

        List<CollectionComponent<Display>> components = new ArrayList<>();
        Matrix4f collectionTransformation = deserializeTransformation(jsonObject);
        JsonArray array = jsonObject.getAsJsonArray("children");

        for (JsonElement element : array) {
            components.add(GSON.fromJson(element.getAsJsonObject(), DisplayCollection.class));
        }

        return new DisplayCollection(collectionTransformation, schematicName, components);
    }
}
