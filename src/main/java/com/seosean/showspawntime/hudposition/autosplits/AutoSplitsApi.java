package com.seosean.showspawntime.hudposition.autosplits;

import com.seosean.zombiesautosplits.api.ApiManager;
import com.seosean.zombiesautosplits.api.CoordinateApi;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

import java.util.List;

public class AutoSplitsApi {
    public static double XSplitter;
    public static double YSplitter;
    public static CoordinateApi modApi;

    public static boolean isAutoSplitsInstalled(){
        List<ModContainer> mods = Loader.instance().getActiveModList();
        for (ModContainer mod : mods) {
            if ("zombiesautosplits".equals(mod.getModId())) {
                return true;
            }
        }
        return false;
    }
        public static boolean ApiManager(){
            if (ApiManager.getInstance().getApiInstance(CoordinateApi.class) != null) {
                modApi = ApiManager.getInstance().getApiInstance(CoordinateApi.class);
                XSplitter = modApi.getX();
                YSplitter = modApi.getY();
                return true;
            }
            return false;
        }
}
