package com.cahrypt.bdstudiolib.adapter.types;

import com.cahrypt.bdstudiolib.adapter.ComponentAdapter;
import com.cahrypt.bdstudiolib.collection.BDComponent;
import com.cahrypt.bdstudiolib.collection.types.BlockDisplayBDComponent;
import com.cahrypt.bdstudiolib.collection.types.CollectionBDComponent;
import com.cahrypt.bdstudiolib.collection.types.ItemDisplayBDComponent;
import com.cahrypt.bdstudiolib.collection.types.TextDisplayBDComponent;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import org.bukkit.entity.Display;
import org.joml.Matrix4f;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CollectionAdapter implements ComponentAdapter<BDComponent<? extends Display>> {
    public static final String BLOCK_DISPLAY_IDENTIFIER = "isBlockDisplay";
    public static final String ITEM_DISPLAY_IDENTIFIER = "isItemDisplay";
    public static final String COLLECTION_IDENTIFIER = "isCollection";
    public static final String TEXT_DISPLAY_IDENTIFIER = "isTextDisplay";
    private static final Map<String, Class<? extends BDComponent<? extends Display>>> SCHEMATIC_IDENTIFIER_MAP = ImmutableMap.<String, Class<? extends BDComponent<?>>>builder()
            .put(BLOCK_DISPLAY_IDENTIFIER, BlockDisplayBDComponent.class)
            .put(ITEM_DISPLAY_IDENTIFIER, ItemDisplayBDComponent.class)
            .put(COLLECTION_IDENTIFIER, CollectionBDComponent.class)
            .put(TEXT_DISPLAY_IDENTIFIER, TextDisplayBDComponent.class)
            .build();

    private Class<? extends BDComponent<?>> getIdentifier(JsonObject object) {
        for (Map.Entry<String, Class<? extends BDComponent<?>>> entry : SCHEMATIC_IDENTIFIER_MAP.entrySet()) {
            if (object.has(entry.getKey())) {
                return entry.getValue();
            }
        }

        return null;
    }

    @Override
    public BDComponent<? extends Display> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String schematicName = deserializeName(jsonObject);

        Class<? extends BDComponent<?>> schematicClass = getIdentifier(jsonObject);

        if (schematicClass == null) {
            throw new JsonParseException("Could not find schematic identifier! Outdated schematic?");
        }

        if (schematicClass != CollectionBDComponent.class) {
            return context.deserialize(jsonObject, schematicClass);
        }

        Set<BDComponent<Display>> BDComponents = new HashSet<>();
        Matrix4f collectionTransformation = deserializeTransforms(jsonObject, context);
        JsonArray array = jsonObject.getAsJsonArray("children");

        for (JsonElement element : array) {
            BDComponents.add(context.deserialize(element, CollectionBDComponent.class));
        }

        return new CollectionBDComponent(schematicName, collectionTransformation, BDComponents);
    }
}
