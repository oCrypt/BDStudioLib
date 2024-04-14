package com.cahrypt.bdstudiolib;

import com.cahrypt.bdstudiolib.adapter.types.BlockComponentAdapter;
import com.cahrypt.bdstudiolib.adapter.types.CollectionAdapter;
import com.cahrypt.bdstudiolib.adapter.types.ItemComponentAdapter;
import com.cahrypt.bdstudiolib.adapter.types.Matrix4fAdapter;
import com.cahrypt.bdstudiolib.adapter.types.TextComponentAdapter;
import com.cahrypt.bdstudiolib.collection.BDComponent;
import com.cahrypt.bdstudiolib.collection.types.BlockDisplayBDComponent;
import com.cahrypt.bdstudiolib.collection.types.CollectionBDComponent;
import com.cahrypt.bdstudiolib.collection.types.ItemDisplayBDComponent;
import com.cahrypt.bdstudiolib.collection.types.TextDisplayBDComponent;
import com.cahrypt.bdstudiolib.utils.BDStudioLibKeys;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;

public record DisplayModelSchematic(CollectionBDComponent collection) {
    private static final Matrix4f IDENTITY = new Matrix4f();
    private static final Gson GSON = new GsonBuilder()
            .disableHtmlEscaping()
            .serializeNulls()
            .setPrettyPrinting()
            .enableComplexMapKeySerialization()
            .excludeFieldsWithModifiers(Modifier.TRANSIENT, Modifier.STATIC)
            .registerTypeAdapter(CollectionBDComponent.class, new CollectionAdapter())
            .registerTypeAdapter(BlockDisplayBDComponent.class, new BlockComponentAdapter())
            .registerTypeAdapter(ItemDisplayBDComponent.class, new ItemComponentAdapter())
            .registerTypeAdapter(TextDisplayBDComponent.class, new TextComponentAdapter())
            .registerTypeAdapter(Matrix4f.class, new Matrix4fAdapter())
            .create();

    /**
     * Creates a {@link DisplayModelSchematic} from a BDStudio file.
     * @param bdStudioFile The BDStudio file to read from. This file is expected to exist.
     * @return The {@link DisplayModelSchematic} created from the BDStudio file.
     * @throws IOException If an I/O error occurs.
     */
    public static DisplayModelSchematic fromBDStudioFile(File bdStudioFile) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(bdStudioFile));
        String base64EncodedLine = reader.readLine();
        reader.close();

        byte[] compressedData = Base64.getDecoder().decode(base64EncodedLine);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(compressedData);
        String uglyJson = getUglyString(inputStream);

        return new DisplayModelSchematic(GSON.fromJson(uglyJson, CollectionBDComponent.class));
    }

    @NotNull
    private static String getUglyString(ByteArrayInputStream inputStream) throws IOException {
        GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int read;

        while ((read = gzipInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, read);
        }

        String uglyJson = outputStream.toString();

        gzipInputStream.close();
        outputStream.close();

        uglyJson = uglyJson.substring(1, uglyJson.length() - 1);
        return uglyJson;
    }

    private Map<String, Set<Display>> buildCollection(CollectionBDComponent collection, Location location, Matrix4f transform, String name) {
        Map<String, Set<Display>> displays = new HashMap<>();

        Matrix4f collectionTransform = collection.getLocalTransformation();
        Matrix4f newTransform = new Matrix4f(transform).mul(collectionTransform);

        String newPath = name + "." + collection.getName();

        for (BDComponent<Display> BDComponent : collection.getComponents()) {
            if (BDComponent instanceof CollectionBDComponent displayCollectionComponent) {
                displays.putAll(buildCollection(displayCollectionComponent, location, newTransform, newPath));
                continue;
            }

            Matrix4f componentTransform = BDComponent.getLocalTransformation();
            Collection<Display> componentDisplays = BDComponent.getDisplays(location);

            componentDisplays.forEach(display -> {
                display.setTransformationMatrix(new Matrix4f(newTransform).mul(componentTransform));
                display.getPersistentDataContainer().set(BDStudioLibKeys.PATH, PersistentDataType.STRING, newPath);
            });

            displays.computeIfAbsent(newPath, k -> new HashSet<>()).addAll(componentDisplays);
        }

        return displays;
    }

    /**
     * Spawns the {@link DisplayModelSchematic} at the given location.
     * @param location The location to spawn the {@link DisplayModelSchematic} at.
     * @return The {@link DisplayModel} that was spawned.
     */
    public DisplayModel spawn(String name, Location location) {
        Map<String, Set<Display>> displays = buildCollection(collection, location, IDENTITY, name.replace(".", "_"));

        Iterator<Display> iterator = displays.values()
                .stream()
                .flatMap(Set::stream)
                .iterator();

        Display firstDisplay = iterator.next();
        String prevUUID = firstDisplay.getUniqueId().toString();

        while (iterator.hasNext()) {
            Display display = iterator.next();
            display.getPersistentDataContainer().set(BDStudioLibKeys.SIBLING, PersistentDataType.STRING, prevUUID);
            prevUUID = display.getUniqueId().toString();
        }

        firstDisplay.getPersistentDataContainer().set(BDStudioLibKeys.SIBLING, PersistentDataType.STRING, prevUUID);

        return new DisplayModel(displays);
    }
}
