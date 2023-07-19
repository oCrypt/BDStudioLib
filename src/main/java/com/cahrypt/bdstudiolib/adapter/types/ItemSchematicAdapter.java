package com.cahrypt.bdstudiolib.adapter.types;

import com.cahrypt.bdstudiolib.adapter.SchematicAdapter;
import com.cahrypt.bdstudiolib.schematic.DisplayEntitySchematic;
import com.cahrypt.bdstudiolib.schematic.types.ItemDisplaySchematic;
import com.google.gson.JsonObject;
import org.bukkit.Material;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.joml.Matrix4f;

public class ItemSchematicAdapter implements SchematicAdapter<ItemDisplay> {

    @Override
    public DisplayEntitySchematic<ItemDisplay> handleSchematic(JsonObject object, String name, Matrix4f transformation) {
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

        return new ItemDisplaySchematic(new ItemStack(material), transformation);
    }
}
