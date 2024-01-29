package com.cahrypt.bdstudiolib;

import com.cahrypt.bdstudiolib.adapter.types.BlockComponentAdapter;
import com.cahrypt.bdstudiolib.adapter.types.DisplayCollectionAdapter;
import com.cahrypt.bdstudiolib.adapter.types.ItemComponentAdapter;
import com.cahrypt.bdstudiolib.adapter.types.TextComponentAdapter;
import com.cahrypt.bdstudiolib.collection.types.BlockDisplayComponent;
import com.cahrypt.bdstudiolib.collection.types.DisplayCollection;
import com.cahrypt.bdstudiolib.collection.types.ItemDisplayComponent;
import com.cahrypt.bdstudiolib.collection.types.TextDisplayComponent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.papermc.paper.plugin.configuration.PluginMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Modifier;
import java.util.Arrays;

public class BDStudioLib extends JavaPlugin {
    private String version;
    private String[] authors;
    private Gson libGson;

    public static BDStudioLib getInstance() {
        return JavaPlugin.getPlugin(BDStudioLib.class);
    }

    private void logStatus(boolean enabled) {
        getLogger().info("BDStudioLib v" + version + " by " + Arrays.toString(authors) + " " + (enabled ? "enabled" : "disabled") + "!");
    }

    @Override
    @SuppressWarnings("UnstableApiUsage")
    public void onEnable() {
        PluginMeta pluginMeta = getPluginMeta();
        this.version = pluginMeta.getVersion();
        this.authors = pluginMeta.getAuthors().toArray(new String[0]);

        this.libGson = new GsonBuilder()
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


        logStatus(true);
    }

    @Override
    public void onDisable() {
        logStatus(false);
    }

    public Gson getGson() {
        return libGson;
    }
}
