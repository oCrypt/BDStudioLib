package com.cahrypt.bdstudiolib.adapter;

import com.cahrypt.bdstudiolib.DisplayModel;
import com.cahrypt.bdstudiolib.DisplayModelGsonUtil;
import com.cahrypt.bdstudiolib.schematic.DisplayEntitySchematic;
import com.cahrypt.bdstudiolib.schematic.types.BlockDisplaySchematic;
import com.cahrypt.bdstudiolib.schematic.types.ItemDisplaySchematic;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DisplayModelAdapter implements JsonDeserializer<DisplayModel> {

    @Override
    public DisplayModel deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        String modelName = jsonObject.get("name").getAsString();

        JsonArray array = jsonObject.getAsJsonArray("children");

        List<DisplayEntitySchematic<?>> schematics = new ArrayList<>();

        for (JsonElement element : array) {
            JsonObject object = element.getAsJsonObject();

            JsonElement blockDisplayElement = object.get("isBlockDisplay");
            boolean isBlockDisplay = blockDisplayElement != null && blockDisplayElement.getAsBoolean();

            schematics.add(DisplayModelGsonUtil.getGsonManager().getGson().fromJson(element, isBlockDisplay ? BlockDisplaySchematic.class : ItemDisplaySchematic.class));
        }

        return new DisplayModel(modelName, schematics);
    }
}
