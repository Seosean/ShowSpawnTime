package com.seosean.showspawntime.config;

import com.seosean.showspawntime.hudposition.ConfigGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

import java.util.Set;

public class ShowSpawnTimeGuiFactory implements IModGuiFactory {
    @Override
    public void initialize(Minecraft minecraft) {

    }

    public boolean hasConfigGui() {
        return true;
    }

    @Override
    public Class<? extends GuiScreen> mainConfigGuiClass() {
        return ShowSpawnTimeGuiConfig.class;
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }

    @Override
    public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
        return null;
    }
}
