package com.cahrypt.bdstudiolib;

import com.cahrypt.bdstudiolib.adapter.DisplayModelAdapter;
import com.cahrypt.bdstudiolib.adapter.types.BlockSchematicAdapter;
import com.cahrypt.bdstudiolib.adapter.types.ItemSchematicAdapter;
import com.cahrypt.gsonutils.GsonManager;
import com.google.gson.Gson;

public final class DisplayModelGsonUtil {
    private static final GsonManager GSON_MANAGER = new GsonManager();

    static {
        GSON_MANAGER.addAdapter(DisplayModel.class, new DisplayModelAdapter());
        GSON_MANAGER.addAdapter(BlockSchematicAdapter.class, new BlockSchematicAdapter());
        GSON_MANAGER.addAdapter(ItemSchematicAdapter.class, new ItemSchematicAdapter());
        GSON_MANAGER.cacheGson();
    }

    public static GsonManager getGsonManager() {
        return GSON_MANAGER;
    }

    public static Gson getGson() {
        return GSON_MANAGER.getGson();
    }
}
