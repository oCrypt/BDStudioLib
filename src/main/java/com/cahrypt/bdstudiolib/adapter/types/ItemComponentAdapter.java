package com.cahrypt.bdstudiolib.adapter.types;

import com.cahrypt.bdstudiolib.adapter.ComponentAdapter;
import com.cahrypt.bdstudiolib.collection.types.ItemDisplayComponent;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.joml.Matrix4f;

import java.lang.reflect.Type;

public class ItemComponentAdapter implements ComponentAdapter<ItemDisplayComponent> {

    @Override
    public ItemDisplayComponent deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();

        String name = deserializeName(object);
        Matrix4f transformation = deserializeTransformation(object);

        int endIndex = name.indexOf("[");
        Material material = Material.getMaterial(name.substring(0, endIndex == -1 ? 0 : endIndex).toUpperCase());

        if (material == null) {
            throw new IllegalArgumentException("Material " + name + " does not exist!");
        }

        ItemStack itemStack = new ItemStack(material);

        try {
            ItemMeta meta = itemStack.getItemMeta();
            meta.setCustomModelData(object.get("nbt").getAsInt());
            itemStack.setItemMeta(meta);
        } catch (NumberFormatException ignored) {
        }

        return new ItemDisplayComponent(transformation, new ItemStack(material));
    }
}
