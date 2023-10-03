package com.seosean.showspawntime.mixins;

import com.seosean.showspawntime.ShowSpawnTime;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Render.class)
public class MixinRender {

    @Inject(method = "renderEntityOnFire", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;color(FFFF)V", shift = At.Shift.AFTER))
    private void renderEntityOnFire (Entity entity, double x, double y, double z, float partialTicks, CallbackInfo c) {
        if(ShowSpawnTime.getInstance().PlayerInvisible && entity instanceof EntityPlayer && entity != Minecraft.getMinecraft().thePlayer) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entity) < 1.4 ? 0 : 1.0F);
        }
    }
}
