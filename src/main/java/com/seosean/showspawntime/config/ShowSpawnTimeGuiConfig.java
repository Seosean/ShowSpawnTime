package com.seosean.showspawntime.config;

import com.seosean.showspawntime.ShowSpawnTime;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;

public class ShowSpawnTimeGuiConfig extends GuiConfig {

    public ShowSpawnTimeGuiConfig(GuiScreen parent) {
        super(parent,
                new ConfigElement
                        (ShowSpawnTime
                        .getInstance()
                        .getConfig()
                        .getCategory(Configuration.CATEGORY_GENERAL))
                        .getChildElements(),
                ShowSpawnTime.MODID,
                false,
                false,
                "ShowSpawnTime Configuration"
        );
    }

}
