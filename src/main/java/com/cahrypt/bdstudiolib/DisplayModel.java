package com.cahrypt.bdstudiolib;

import com.cahrypt.bdstudiolib.adapter.types.BlockComponentAdapter;
import com.cahrypt.bdstudiolib.adapter.types.DisplayCollectionAdapter;
import com.cahrypt.bdstudiolib.adapter.types.ItemComponentAdapter;
import com.cahrypt.bdstudiolib.adapter.types.TextComponentAdapter;
import com.cahrypt.bdstudiolib.collection.CollectionComponent;
import com.cahrypt.bdstudiolib.collection.types.BlockDisplayComponent;
import com.cahrypt.bdstudiolib.collection.types.DisplayCollection;
import com.cahrypt.bdstudiolib.collection.types.ItemDisplayComponent;
import com.cahrypt.bdstudiolib.collection.types.TextDisplayComponent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.Location;
import org.bukkit.entity.Display;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public record DisplayModel(DisplayCollection collection) {
    private static final Matrix4f IDENTITY = new Matrix4f();
    private static final Gson GSON = new GsonBuilder()
            .disableHtmlEscaping()
            .serializeNulls()
            .setPrettyPrinting()
            .enableComplexMapKeySerialization()
            .excludeFieldsWithModifiers(Modifier.TRANSIENT, Modifier.STATIC)
            .registerTypeAdapter(DisplayCollection.class, new DisplayCollectionAdapter())
            .registerTypeAdapter(BlockDisplayComponent.class, new BlockComponentAdapter())
            .registerTypeAdapter(ItemDisplayComponent.class, new ItemComponentAdapter())
            .registerTypeAdapter(TextDisplayComponent.class, new TextComponentAdapter())
            .create();

    /**
     * Creates a {@link DisplayModel} from a BDStudio file.
     * @param bdStudioFile The BDStudio file to read from. This file is expected to exist.
     * @return The {@link DisplayModel} created from the BDStudio file.
     * @throws IOException If an I/O error occurs.
     */
    public static DisplayModel fromBDStudioFile(File bdStudioFile) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(bdStudioFile));
        String base64EncodedLine = reader.readLine();
        reader.close();

        byte[] compressedData = Base64.getDecoder().decode(base64EncodedLine);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(compressedData);
        String uglyJson = getUglyString(inputStream);

        return new DisplayModel(GSON.fromJson(uglyJson, DisplayCollection.class));
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

    private Map<CollectionComponent<?>, List<Display>> buildCollection(DisplayCollection collection, Location location, Matrix4f transform) {
        Map<CollectionComponent<?>, List<Display>> displays = new HashMap<>();

        Matrix4f collectionTransform = collection.getLocalTransformation();
        Matrix4f newTransform = new Matrix4f(transform).mul(collectionTransform);

        for (CollectionComponent<Display> component : collection.getComponents()) {
            if (component instanceof DisplayCollection displayCollection) {
                displays.putAll(buildCollection(displayCollection, location, newTransform));
                continue;
            }

            Matrix4f componentTransform = component.getLocalTransformation();
            List<Display> componentDisplays = component.getDisplays(location);

            componentDisplays.forEach(display -> display.setTransformationMatrix(new Matrix4f(newTransform).mul(componentTransform)));
            displays.put(component, componentDisplays);
        }

        return displays;
    }

    /**
     * Spawns the {@link DisplayModel} at the given location.
     * @param location The location to spawn the {@link DisplayModel} at.
     * @return A {@link Map} of {@link CollectionComponent}s to {@link Display}s.
     */
    public Map<CollectionComponent<?>, List<Display>> spawn(Location location) {
        return buildCollection(collection, location, IDENTITY);
    }
}
