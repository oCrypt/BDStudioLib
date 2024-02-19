package com.cahrypt.bdstudiolib.collection.types;

import com.cahrypt.bdstudiolib.collection.BDComponent;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.joml.Matrix4f;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CollectionBDComponent extends BDComponent<Display> {
    private final Collection<BDComponent<Display>> BDComponents;

    public CollectionBDComponent(String name, Matrix4f localTransformation, Collection<BDComponent<Display>> BDComponents) {
        super(name, localTransformation);

        this.BDComponents = BDComponents;
    }

    public Set<BDComponent<Display>> getComponents() {
        return new HashSet<>(BDComponents);
    }

    @Override
    public Set<Display> getDisplays(Location location) {
        Set<Display> transformers = new HashSet<>();

        for (BDComponent<Display> BDComponent : BDComponents) {
            transformers.addAll(BDComponent.getDisplays(location));
        }

        return transformers;
    }
}
