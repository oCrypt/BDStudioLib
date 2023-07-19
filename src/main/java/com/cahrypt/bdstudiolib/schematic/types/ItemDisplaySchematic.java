package com.cahrypt.bdstudiolib.schematic.types;

import com.cahrypt.bdstudiolib.schematic.DisplayEntitySchematic;
import org.bukkit.Location;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.joml.Matrix4f;

public class ItemDisplaySchematic extends DisplayEntitySchematic<ItemDisplay> {
    private final ItemStack itemStack;

    public ItemDisplaySchematic(ItemStack itemStack, Matrix4f transformation) {
        super(transformation);

        this.itemStack = itemStack;
    }

    @Override
    public ItemDisplay create(Location location) {
        ItemDisplay itemDisplay = location.getWorld().spawn(location, ItemDisplay.class);
        itemDisplay.setItemStack(itemStack);

        return itemDisplay;
    }
}
