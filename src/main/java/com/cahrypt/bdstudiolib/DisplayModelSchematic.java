package com.cahrypt.bdstudiolib;

import com.cahrypt.bdstudiolib.adapter.types.BlockComponentAdapter;
import com.cahrypt.bdstudiolib.adapter.types.DisplayCollectionAdapter;
import com.cahrypt.bdstudiolib.adapter.types.ItemComponentAdapter;
import com.cahrypt.bdstudiolib.adapter.types.Matrix4fAdapter;
import com.cahrypt.bdstudiolib.adapter.types.TextComponentAdapter;
import com.cahrypt.bdstudiolib.collection.BDComponent;
import com.cahrypt.bdstudiolib.collection.types.BlockDisplayBDComponent;
import com.cahrypt.bdstudiolib.collection.types.CollectionBDComponent;
import com.cahrypt.bdstudiolib.collection.types.ItemDisplayBDComponent;
import com.cahrypt.bdstudiolib.collection.types.TextDisplayBDComponent;
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
import java.util.Collection;
import java.util.HashSet;
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
            .registerTypeAdapter(CollectionBDComponent.class, new DisplayCollectionAdapter())
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

    private Set<Display> buildCollection(CollectionBDComponent collection, Location location, Matrix4f transform) {
        Set<Display> displays = new HashSet<>();

        Matrix4f collectionTransform = collection.getLocalTransformation();
        Matrix4f newTransform = new Matrix4f(transform).mul(collectionTransform);

        for (BDComponent<Display> BDComponent : collection.getComponents()) {
            if (BDComponent instanceof CollectionBDComponent displayCollectionComponent) {
                displays.addAll(buildCollection(displayCollectionComponent, location, newTransform));
                continue;
            }

            Matrix4f componentTransform = BDComponent.getLocalTransformation();
            Collection<Display> componentDisplays = BDComponent.getDisplays(location);

            componentDisplays.forEach(display -> display.setTransformationMatrix(new Matrix4f(newTransform).mul(componentTransform)));
            displays.addAll(componentDisplays);
        }

        return displays;
    }

    /**
     * Spawns the {@link DisplayModelSchematic} at the given location.
     * @param location The location to spawn the {@link DisplayModelSchematic} at.
     * @return A {@link Map} of {@link BDComponent}s to {@link Display}s.
     */
    public Set<Display> spawn(Location location) {
        return buildCollection(collection, location, IDENTITY);
    }
}
