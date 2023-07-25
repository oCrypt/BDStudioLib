package com.cahrypt.bdstudiolib;

import com.cahrypt.bdstudiolib.adapter.types.BlockComponentAdapter;
import com.cahrypt.bdstudiolib.adapter.types.DisplayCollectionAdapter;
import com.cahrypt.bdstudiolib.adapter.types.ItemComponentAdapter;
import com.cahrypt.bdstudiolib.adapter.types.TextComponentAdapter;
import com.cahrypt.bdstudiolib.collection.types.BlockDisplayComponent;
import com.cahrypt.bdstudiolib.collection.types.DisplayCollection;
import com.cahrypt.bdstudiolib.collection.types.ItemDisplayComponent;
import com.cahrypt.bdstudiolib.collection.types.TextDisplayComponent;
import com.cahrypt.gsonutils.GsonManager;
import com.google.gson.Gson;

public final class DisplayModelGsonUtil {
    private static final GsonManager GSON_MANAGER = new GsonManager();

    static {
        GSON_MANAGER.addAdapter(DisplayCollection.class, new DisplayCollectionAdapter());
        GSON_MANAGER.addAdapter(BlockDisplayComponent.class, new BlockComponentAdapter());
        GSON_MANAGER.addAdapter(ItemDisplayComponent.class, new ItemComponentAdapter());
        GSON_MANAGER.addAdapter(TextDisplayComponent.class, new TextComponentAdapter());
        GSON_MANAGER.cacheGson();
    }

    public static GsonManager getGsonManager() {
        return GSON_MANAGER;
    }

    public static Gson getGson() {
        return GSON_MANAGER.getGson();
    }
}
