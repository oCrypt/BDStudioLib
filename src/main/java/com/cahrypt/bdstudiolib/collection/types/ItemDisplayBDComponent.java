package com.cahrypt.bdstudiolib.collection.types;

import com.cahrypt.bdstudiolib.collection.SingletonBDComponent;
import org.bukkit.Location;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.joml.Matrix4f;

public class ItemDisplayBDComponent extends SingletonBDComponent<ItemDisplay> {
    private final ItemStack itemStack;

    public ItemDisplayBDComponent(String name, Matrix4f localTransformation, ItemStack itemStack) {
        super(name, localTransformation);

        this.itemStack = itemStack;
    }

    @Override
    public ItemDisplay getDisplay(Location location) {
        ItemDisplay itemDisplay = location.getWorld().spawn(location, ItemDisplay.class);
        itemDisplay.setItemStack(itemStack);

        return itemDisplay;
    }

    public ItemStack getItemStack() {
        return itemStack.clone();
    }
}
