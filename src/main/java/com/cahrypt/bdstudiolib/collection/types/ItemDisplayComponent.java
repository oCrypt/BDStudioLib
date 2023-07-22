package com.cahrypt.bdstudiolib.collection.types;

import com.cahrypt.bdstudiolib.collection.CollectionComponent;
import org.bukkit.Location;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.joml.Matrix4f;

import java.util.Collections;
import java.util.List;

public class ItemDisplayComponent extends CollectionComponent<ItemDisplay> {
    private final ItemStack itemStack;

    public ItemDisplayComponent(Matrix4f localTransformation, ItemStack itemStack) {
        super(localTransformation);

        this.itemStack = itemStack;
    }

    @Override
    public List<ItemDisplay> getDisplays(Location location) {
        ItemDisplay itemDisplay = location.getWorld().spawn(location, ItemDisplay.class);
        itemDisplay.setItemStack(itemStack);

        return Collections.singletonList(itemDisplay);
    }
}
