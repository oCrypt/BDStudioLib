package com.cahrypt.bdstudiolib.collection.types;

import com.cahrypt.bdstudiolib.collection.CollectionComponent;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

public class DisplayCollection extends CollectionComponent<Display> {
    private final String name;
    private final List<CollectionComponent<Display>> components;

    public DisplayCollection(Matrix4f localTransformation, String name, List<CollectionComponent<Display>> components) {
        super(localTransformation);

        this.name = name;
        this.components = components;
    }

    public String getName() {
        return name;
    }

    public List<CollectionComponent<Display>> getComponents() {
        return new ArrayList<>(components);
    }

    @Override
    public List<Display> getDisplays(Location location) {
        List<Display> transformers = new ArrayList<>();

        for (CollectionComponent<Display> component : components) {
            transformers.addAll(component.getDisplays(location));
        }

        return transformers;
    }
}
