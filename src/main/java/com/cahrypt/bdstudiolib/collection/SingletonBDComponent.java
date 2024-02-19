package com.cahrypt.bdstudiolib.collection;

import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.joml.Matrix4f;

import java.util.Collections;
import java.util.Set;

public abstract class SingletonBDComponent<T extends Display> extends BDComponent<T> {
    public SingletonBDComponent(String name, Matrix4f localTransformation) {
        super(name, localTransformation);
    }

    public abstract T getDisplay(Location location);

    @Override
    public Set<T> getDisplays(Location location) {
        return Collections.singleton(getDisplay(location));
    }
}
