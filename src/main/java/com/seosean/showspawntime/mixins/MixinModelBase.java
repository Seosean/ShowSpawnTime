package com.seosean.showspawntime.mixins;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;


@Mixin(ModelBase.class)
public abstract class MixinModelBase {
    @Shadow
    public boolean isChild = true;

    public MixinModelBase() {
    }

    @Shadow
    public abstract void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7);

    @Shadow
    public abstract void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6, Entity var7);
}