package com.cahrypt.bdstudiolib.collection;

import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.joml.Matrix4f;

import java.util.Collection;

public abstract class BDComponent<T extends Display> {
    private final String name;
    private final Matrix4f localTransformation;

    public BDComponent(String name, Matrix4f localTransformation) {
        this.name = name;
        this.localTransformation = localTransformation;
    }

    public abstract Collection<T> getDisplays(Location location);

    public Matrix4f getLocalTransformation() {
        return localTransformation;
    }

    public String getName() {
        return name;
    }
}
