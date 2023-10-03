package com.seosean.showspawntime.mixins;

import com.seosean.showspawntime.ShowSpawnTime;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerCape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.seosean.showspawntime.ShowSpawnTime.getAlpha;

@Mixin({LayerCape.class})
public class MixinLayerCape {

    @Inject(method = "doRenderLayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;pushMatrix()V", shift = At.Shift.AFTER))
    private void doRenderLayerPre (AbstractClientPlayer entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale, CallbackInfo ci) {
        if(ShowSpawnTime.getInstance().PlayerInvisible && ShowSpawnTime.getInstance().isPlayerInvisible(entitylivingbaseIn)) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, getAlpha(entitylivingbaseIn));
            GlStateManager.depthMask(false);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 771);
            GlStateManager.alphaFunc(516, 0.003921569F);
        }
    }

    @Inject(method = "doRenderLayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;popMatrix()V", shift = At.Shift.BEFORE))
    private void doRenderLayerPost (AbstractClientPlayer entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale, CallbackInfo ci) {
        if(ShowSpawnTime.getInstance().PlayerInvisible && ShowSpawnTime.getInstance().isPlayerInvisible(entitylivingbaseIn)) {
            GlStateManager.disableBlend();
            GlStateManager.alphaFunc(516, 0.1F);
            GlStateManager.depthMask(true);
        }
    }
}