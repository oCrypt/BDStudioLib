package com.cahrypt.bdstudiolib.collection;

import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.joml.Matrix4f;

import java.util.List;

public abstract class CollectionComponent<T extends Display> {
    private final Matrix4f localTransformation;

    public CollectionComponent(Matrix4f localTransformation) {
        this.localTransformation = localTransformation;
    }

    public abstract List<T> getDisplays(Location location);

    public Matrix4f getLocalTransformation() {
        return localTransformation;
    }
}
