package com.cahrypt.bdstudiolib;

import org.bukkit.Location;
import org.bukkit.entity.BlockDisplay;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.zip.GZIPInputStream;

/**
 * Represents a {@link org.bukkit.entity.BlockDisplay} model composed of multiple {@link DisplayEntitySchematic}s.
 * @param name The name of the model.
 * @param schematics The list of {@link DisplayEntitySchematic}s that make up the model.
 *
 * @author oCrypt
 */
public record DisplayModel(String name, List<DisplayEntitySchematic> schematics) {

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

        return DisplayModelGsonUtil.parseJson(uglyJson);
    }

    /**
     * Spawns the {@link DisplayModel} at the specified location with the specified rotation in radians. Degrees are for the weak.
     * @param location The location to spawn the model at.
     * @param pitchRotationRadians The pitch rotation in radians.
     * @param yawRotationRadians The yaw rotation in radians.
     * @return The list of {@link BlockDisplay}s that make up the model.
     */
    public List<BlockDisplay> spawn(Location location, double pitchRotationRadians, double yawRotationRadians) {
        List<BlockDisplay> involvedDisplays = new ArrayList<>();

        for (DisplayEntitySchematic schematic : schematics) {
            involvedDisplays.add(schematic.create(location, pitchRotationRadians, yawRotationRadians));
        }

        return involvedDisplays;
    }
}
