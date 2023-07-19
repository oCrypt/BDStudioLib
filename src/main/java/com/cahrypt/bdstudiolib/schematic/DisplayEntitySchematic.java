package com.cahrypt.bdstudiolib.schematic;

import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.joml.Matrix4f;

public abstract class DisplayEntitySchematic<T extends Display> {
    protected final Matrix4f transformation;

    public DisplayEntitySchematic(Matrix4f transformation) {
        this.transformation = transformation;
    }

    protected abstract T create(Location location);

    public T create(Location location, double pitchRotationRadians, double yawRotationRadians) {
        T display = create(location);

        display.setTransformationMatrix(transformation);
        display.setRotation((float) (pitchRotationRadians * 180 / Math.PI), (float) (yawRotationRadians * 180 / Math.PI));

        return display;
    }
}
