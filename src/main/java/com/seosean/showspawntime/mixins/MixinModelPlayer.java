package com.seosean.showspawntime.mixins;

import com.seosean.showspawntime.ShowSpawnTime;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import static com.seosean.showspawntime.ShowSpawnTime.getAlpha;


@Mixin(ModelPlayer.class)
public abstract class MixinModelPlayer extends MixinModelBiped {
    @Shadow
    public ModelRenderer bipedLeftLegwear;
    @Shadow
    public ModelRenderer bipedRightLegwear;
    @Shadow
    public ModelRenderer bipedLeftArmwear;
    @Shadow
    public ModelRenderer bipedRightArmwear;
    @Shadow
    public ModelRenderer bipedBodyWear;


    public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale)
    {
        super.render(entityIn, p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale);
        GlStateManager.pushMatrix();
        if(ShowSpawnTime.getInstance().PlayerInvisible && ShowSpawnTime.getInstance().isPlayerInvisible(entityIn)) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, getAlpha(entityIn));
            GlStateManager.depthMask(false);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 771);
            GlStateManager.alphaFunc(516, 0.003921569F);
            int a;
        }
        if (this.isChild)
        {
            float f = 2.0F;
            GlStateManager.scale(1.0F / f, 1.0F / f, 1.0F / f);
            GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
            this.bipedLeftLegwear.render(scale);
            this.bipedRightLegwear.render(scale);
            this.bipedLeftArmwear.render(scale);
            this.bipedRightArmwear.render(scale);
            this.bipedBodyWear.render(scale);
        }
        else
        {
            if (entityIn.isSneaking())
            {
                GlStateManager.translate(0.0F, 0.2F, 0.0F);
            }

            this.bipedLeftLegwear.render(scale);
            this.bipedRightLegwear.render(scale);
            this.bipedLeftArmwear.render(scale);
            this.bipedRightArmwear.render(scale);
            this.bipedBodyWear.render(scale);
        }
        if(ShowSpawnTime.getInstance().PlayerInvisible && ShowSpawnTime.getInstance().isPlayerInvisible(entityIn)) {
            GlStateManager.disableBlend();
            GlStateManager.alphaFunc(516, 0.1F);
            GlStateManager.depthMask(true);
        }
        GlStateManager.popMatrix();
    }

}
