package com.seosean.showspawntime.mixins;

import com.seosean.showspawntime.mapFile.Rounds;
import com.seosean.showspawntime.ShowSpawnTime;
import com.seosean.showspawntime.mapFile.AAFeature;
import com.seosean.showspawntime.mapFile.InternalTimer;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.seosean.showspawntime.mapFile.Rounds.minecraft;
import static com.seosean.showspawntime.mapFile.InternalTimer.gameStarted;
import static com.seosean.showspawntime.utils.Utils.isAllLegit;
import static com.seosean.showspawntime.utils.Utils.isInAA;

@Mixin(WorldClient.class)
public class MixinWorldClient {
    private boolean AAr10 = false;
    @Inject(method = "playSound", at = @At(value = "RETURN"))
    private void playSound(double x, double y, double z, String soundName, float volume, float pitch, boolean distanceDelay, CallbackInfo callbackInfo){
        this.detectSound(soundName, pitch);
    }

    private void detectSound(String soundEffect, float pitch){
        if (soundEffect.equals("mob.wither.spawn") || soundEffect.equals("mob.enderdragon.end") || (soundEffect.equals("mob.guardian.curse") && !AAr10)) {
            try{
                ShowSpawnTime.getInstance().getSplitsTimer().startOrSplit();
            }catch (Exception e){
                IChatComponent message = new ChatComponentText(EnumChatFormatting.AQUA + "ShowSpawnTime: " + EnumChatFormatting.RED+ String.valueOf(e));
                minecraft.thePlayer.addChatComponentMessage(message);
            }
            AAFeature.playSound = false;
            Rounds.playSound = false;
            AAr10 = soundEffect.equals("mob.guardian.curse");
            InternalTimer.lrTimes = 0;
            if(!gameStarted){
                ShowSpawnTime.getInstance().getPowerUpDetect().iniPowerupMap();
            }
            gameStarted = !soundEffect.equals("mob.enderdragon.end");
            if (InternalTimer.displayTime > 100) {
                InternalTimer.displayTime = 100;
            }
        }
        if (ShowSpawnTime.LightningRodQueue) {
            if (isAllLegit()){
                if (isInAA()) {
                    if (soundEffect.equals("ambient.weather.thunder")) {
                        if(pitch != 2.0){
                            InternalTimer.lrTimes++;
                            InternalTimer.lrTimer = 100;
                            InternalTimer.displayTime = 160;
                        }
                    }
                }
            }
        }
    }
}
