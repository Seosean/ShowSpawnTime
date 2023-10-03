package com.seosean.showspawntime.utils;

import com.seosean.showspawntime.ShowSpawnTime;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Render {
    @SubscribeEvent
    public void onRender(RenderPlayerEvent.Pre event) {
        EntityPlayer player = event.entityPlayer;
        if (player != Minecraft.getMinecraft().thePlayer && !player.isPlayerSleeping() && Render.withinDistance(player)) {
            event.setCanceled(ShowSpawnTime.getInstance().PlayerInvisible);
        }
    }

    private static boolean withinDistance(EntityPlayer other) {
        return getDistance(other) < 1.4;
    }

    private static double getDistance(EntityPlayer other) {
        return Minecraft.getMinecraft().thePlayer.getDistanceToEntity(other);
    }
}
