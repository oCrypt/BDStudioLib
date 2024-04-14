package com.cahrypt.bdstudiolib;

import com.cahrypt.bdstudiolib.utils.BDStudioLibKeys;
import org.bukkit.entity.Display;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

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

    public void rotateDisplays(String path, float yaw, float pitch) {
        Set<Display> displaySet = displays.get(path);
        if (displaySet == null) {
            throw new IllegalArgumentException("No displays found for path: " + path);
        }

        displaySet.forEach(display -> display.setRotation(display.getYaw() + yaw, display.getPitch() + pitch));
    }

    public void rotateDisplays(float yaw, float pitch) {
        for (Set<Display> displaySet : displays.values()) {
            displaySet.forEach(display -> display.setRotation(display.getYaw() + yaw, display.getPitch() + pitch));
        }
    }
}
