package com.cahrypt.bdstudiolib;

import com.cahrypt.bdstudiolib.adapter.DisplayModelAdapter;
import com.cahrypt.gsonutils.GsonManager;

final class DisplayModelGsonUtil {
    private static final GsonManager GSON_MANAGER = new GsonManager();

    static {
        GSON_MANAGER.addAdapter(DisplayModel.class, new DisplayModelAdapter());
        GSON_MANAGER.cacheGson();
    }

    static DisplayModel parseJson(String json) {
        return GSON_MANAGER.getGson().fromJson(json, DisplayModel.class);
    }
}
