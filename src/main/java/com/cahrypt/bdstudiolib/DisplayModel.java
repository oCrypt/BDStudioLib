package com.cahrypt.bdstudiolib;


import com.cahrypt.bdstudiolib.collection.CollectionComponent;
import com.cahrypt.bdstudiolib.collection.types.DisplayCollection;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.joml.Matrix4f;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public record DisplayModel(DisplayCollection collection) {

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

        return new DisplayModel(DisplayModelGsonUtil.getGson().fromJson(uglyJson, DisplayCollection.class));
    }

    private Map<CollectionComponent<?>, List<Display>> buildCollection(DisplayCollection collection, Location location, Matrix4f basis) {
        Map<CollectionComponent<?>, List<Display>> displays = new HashMap<>();

        Matrix4f collectionBasis = collection.getLocalTransformation();
        Matrix4f newBasis = new Matrix4f(basis).mul(collectionBasis);

        for (CollectionComponent<Display> component : collection.getComponents()) {
            if (component instanceof DisplayCollection) {
                displays.putAll(buildCollection((DisplayCollection) component, location, newBasis));
                continue;
            }

            Matrix4f componentBasis = component.getLocalTransformation();
            List<Display> componentDisplays = component.getDisplays(location);

            componentDisplays.forEach(display -> display.setTransformationMatrix(new Matrix4f(newBasis).mul(componentBasis)));
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
        return buildCollection(collection, location, collection.getLocalTransformation());
    }
}
