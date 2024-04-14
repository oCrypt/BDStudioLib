package com.cahrypt.bdstudiolib;

import com.cahrypt.bdstudiolib.utils.BDStudioLibKeys;
import org.bukkit.entity.Display;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Transformation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public record DisplayModel(Map<String, Set<Display>> displays) {

    public static DisplayModel from(Display display) {
        Map<String, Set<Display>> displays = new HashMap<>();

        Display next = display;
        while (next != null) {
            PersistentDataContainer data = next.getPersistentDataContainer();

            String sibling = data.get(BDStudioLibKeys.SIBLING, PersistentDataType.STRING);

            if (sibling == null) {
                throw new IllegalArgumentException("Display does not have a sibling");
            }

            String path = data.get(BDStudioLibKeys.PATH, PersistentDataType.STRING);

            if (path == null) {
                throw new IllegalArgumentException("Display does not have a path");
            }

            displays.computeIfAbsent(path, key -> new HashSet<>()).add(display);
            next = (Display) display.getWorld().getEntity(UUID.fromString(sibling));
        }

        return new DisplayModel(displays);
    }

    public void consumeDisplays(Consumer<Display> consumer) {
        for (Set<Display> displaySet : displays.values()) {
            displaySet.forEach(consumer);
        }
    }

    public Set<Display> getDisplays(String path) {
        return displays.get(path);
    }

    public Set<String> getDisplayPaths() {
        return displays.keySet();
    }

    public boolean hasDisplayPath(String path) {
        return displays.containsKey(path);
    }

    public void transform(String path, Function<Transformation, Transformation> transformationFunction) {
        Set<Display> displays = getDisplays(path);

        if (displays == null) {
            return;
        }

        for (Display display : displays) {
            display.setTransformation(transformationFunction.apply(display.getTransformation()));
        }
    }

    public void transform(Function<Transformation, Transformation> transformationFunction) {
        consumeDisplays(display -> display.setTransformation(transformationFunction.apply(display.getTransformation())));
    }
}
