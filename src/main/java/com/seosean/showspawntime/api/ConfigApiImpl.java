package com.seosean.showspawntime.api;

import static com.seosean.showspawntime.ShowSpawnTime.XSplitter;
import static com.seosean.showspawntime.ShowSpawnTime.YSplitter;

public class ConfigApiImpl implements ConfigApi {
    @Override
    public double getX(){
        return XSplitter;
    }

    @Override
    public double getY(){
        return YSplitter;
    }
}
