package com.seosean.showspawntime.mapFile;

import com.seosean.showspawntime.ShowSpawnTime;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.opengl.GL11;

import static com.seosean.showspawntime.mapFile.Rounds.*;
import static com.seosean.showspawntime.utils.Utils.*;

public class InternalTimer {
    public static int gameTick;
    public static int lrTimes = 0;
    public static int oneWavesRound[] = {25,35,101,102,103,104,105};
    public static int threeWavesRound[] = {26,27,28,29,30,31,32,33,34,36,37,38,39,41,42,44,47,49,56,57};
    public static int fourWavesRound[] = {1,2,3,46,50,51,52,53,45,48,60};
    public static int fiveWavesRound[] = {40,54,58};
    public static int sixWavesRound[] = {4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,43,55,59,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100};
    public boolean setW1End(boolean isReal){
        return gameTick >= secondToTick(10 + (isReal ? 0 : (int)(ShowSpawnTime.HighlightDelay)));
    }

    public boolean setAAW1End(){
        if(ArrayUtils.contains(oneWavesRound, parseSidebar()) ||ArrayUtils.contains(threeWavesRound, parseSidebar()) || ArrayUtils.contains(fourWavesRound, parseSidebar()) || ArrayUtils.contains(fiveWavesRound, parseSidebar())) return gameTick >= 1;
        if(ArrayUtils.contains(sixWavesRound, parseSidebar())) return gameTick >= 10 * 1000;
        return gameTick >= 11451000;
    }

    public boolean setW2End(boolean isReal){
        if(isInDE()) return gameTick >= secondToTick(getDEW2Time(Rounds.parseSidebar(), isReal) + (isReal ? 0 : (int)(ShowSpawnTime.HighlightDelay)));

        else if(isInBB()) return gameTick >= secondToTick(getBBW2Time(Rounds.parseSidebar(), isReal) + (isReal ? 0 : (int)(ShowSpawnTime.HighlightDelay)));

        else if(isInAA()) return gameTick >= secondToTick(getAAW2Time(Rounds.parseSidebar()));

        else return false;
    }

    public boolean setDEBBW3End(){
        if(isInDE()) return gameTick >= secondToTick(getDEW3Time(Rounds.parseSidebar()));

        else if(isInBB()) return gameTick >= secondToTick(getBBW3Time(Rounds.parseSidebar()));

        return false;
    }

    public static void DEBBW3CountDown() {
        if (!isAllLegit()) return;
        if(isInDE()){
            if (gameTick == ((is2WavesRound()) ? secondToTick((getDEW2Time(Rounds.parseSidebar(), true) - 3)) : secondToTick((getDEW3Time(Rounds.parseSidebar()) - 3)))) {
                Minecraft.getMinecraft().thePlayer.playSound(ShowSpawnTime.TheLastWave, 2, (float) ShowSpawnTime.PrecededWavePitch);
            } else if (gameTick == ((is2WavesRound()) ? secondToTick((getDEW2Time(Rounds.parseSidebar(), true) - 2)) : secondToTick((getDEW3Time(Rounds.parseSidebar()) - 2)))) {
                Minecraft.getMinecraft().thePlayer.playSound(ShowSpawnTime.TheLastWave, 2, (float) ShowSpawnTime.PrecededWavePitch);
            } else if (gameTick == ((is2WavesRound()) ? secondToTick((getDEW2Time(Rounds.parseSidebar(), true) - 1)) : secondToTick((getDEW3Time(Rounds.parseSidebar()) - 1)))) {
                Minecraft.getMinecraft().thePlayer.playSound(ShowSpawnTime.TheLastWave, 2, (float) ShowSpawnTime.PrecededWavePitch);
            } else if (gameTick == ((is2WavesRound()) ? secondToTick((getDEW2Time(Rounds.parseSidebar(), true))) : secondToTick((getDEW3Time(Rounds.parseSidebar()))))) {
                Minecraft.getMinecraft().thePlayer.playSound(ShowSpawnTime.TheLastWave, 2, (float)ShowSpawnTime.TheLastWavePitch);
            }
        }else if(isInBB()){
            if (gameTick == ((is2WavesRound()) ? secondToTick((getBBW2Time(Rounds.parseSidebar(), true) - 3)) : secondToTick((getBBW3Time(Rounds.parseSidebar()) - 3)))) {
                Minecraft.getMinecraft().thePlayer.playSound(ShowSpawnTime.TheLastWave, 2, (float) ShowSpawnTime.PrecededWavePitch);
            } else if (gameTick == ((is2WavesRound()) ? secondToTick((getBBW2Time(Rounds.parseSidebar(), true)) - 2) : secondToTick((getBBW3Time(Rounds.parseSidebar()) - 2)))) {
                Minecraft.getMinecraft().thePlayer.playSound(ShowSpawnTime.TheLastWave, 2, (float) ShowSpawnTime.PrecededWavePitch);
            } else if (gameTick == ((is2WavesRound()) ? secondToTick((getBBW2Time(Rounds.parseSidebar(), true)) - 1) : secondToTick((getBBW3Time(Rounds.parseSidebar()) - 1)))) {
                Minecraft.getMinecraft().thePlayer.playSound(ShowSpawnTime.TheLastWave, 2, (float) ShowSpawnTime.PrecededWavePitch);
            } else if (gameTick == ((is2WavesRound()) ? secondToTick((getBBW2Time(Rounds.parseSidebar(), true))) : secondToTick((getBBW3Time(Rounds.parseSidebar()))))) {
                Minecraft.getMinecraft().thePlayer.playSound(ShowSpawnTime.TheLastWave, 2, (float)ShowSpawnTime.TheLastWavePitch);
            }
        }

    }

    public boolean setW3End(){
        return (isInAA()) && gameTick >= secondToTick(getAAW3Time(Rounds.parseSidebar()));
    }
    public boolean setW4End(){
        return (isInAA()) && gameTick >= secondToTick(getAAW4Time(Rounds.parseSidebar()));
    }
    public boolean setW5End(){
        return (isInAA()) && gameTick >= secondToTick(getAAW5Time(Rounds.parseSidebar()));
    }
    public boolean setW6End(){
        return (isInAA()) && gameTick >= secondToTick(getAAW6Time(Rounds.parseSidebar()));
    }






    public static boolean gameStarted = false;
    public static boolean lock = false;
    @SubscribeEvent
    public void timerW1End(TickEvent.ClientTickEvent event){
        Minecraft mc = Minecraft.getMinecraft();
        if (mc == null || mc.theWorld == null || mc.isSingleplayer() ) {
            return;
        }
        if(event.phase!= TickEvent.Phase.START)
            return;
        EntityPlayerSP p = mc.thePlayer;
        if(p==null)
            return;
        if(!gameStarted && !lock){
            lock = true;
            ShowSpawnTime.getInstance().getPowerUpDetect().restoredArmorStand.clear();
        }
        if(!isAllLegit()) {
            return;
        }

        if(ShowSpawnTime.PowerupAlertToggle) {
            ShowSpawnTime.getInstance().getPowerUpDetect().findFloatingArmorStands(mc.theWorld);
            ShowSpawnTime.getInstance().getPowerUpDetect().powerupCountDown();
            ShowSpawnTime.getInstance().getPowerUpDetect().resetPowerup();
        }else{
            ShowSpawnTime.getInstance().getPowerUpDetect().iniPowerupMap();
        }

        if(displayTime > 0)
            displayTime --;
        if(ShowSpawnTime.LightningRodQueue) {
            if(lrTimer > 0){
                lrTimer -=1 ;
            }else{
                lrTimes = 0;
            }
        }
    }
    public static int lrTimer = 0;
    public static int displayTime = 0;

    @SubscribeEvent
    public void playerConnectEvent(EntityJoinWorldEvent event) {
        if (!event.entity.worldObj.isRemote) return;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc == null || mc.theWorld == null || mc.isSingleplayer()) return;
        EntityPlayerSP p = mc.thePlayer;
        if (p == null) return;
        Entity entity = event.entity;
        if (!(entity instanceof EntityPlayer)) return;
        if (entity != minecraft.thePlayer){
            return;
        }
        gameStarted = false;
        gameTick = 0;
        lock = false;
    }

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        if(!isAllLegit()) {
            return;
        }
        String message = event.message.getUnformattedText().replaceAll(emojiRegex, "").replaceAll(colorRegex, "");
        if(message.contains("has spawned!") || message.contains("已生成")){
            if(!message.contains(":")){
                lrTimes --;
            }
        }
    }

    @SubscribeEvent
    public void toggleONRenderer(RenderGameOverlayEvent.Text event){
        Minecraft minecraft = Minecraft.getMinecraft();
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) {
            return;
        }
        if(!ShowSpawnTime.LightningRodQueue){
            return;
        }
        if(!isAllLegit()) {
            return;
        }

        if (displayTime > 0) {
            float f2 = (float) displayTime - event.partialTicks;
            int l1 = (int) (f2 * 255.0F / 20.0F);

            if (l1 > 255) {
                l1 = 255;
            }

            if (l1 > 8) {
                ScaledResolution scaledResolution = new ScaledResolution(minecraft);
                int screenWidth = scaledResolution.getScaledWidth();
                int screenHeight = scaledResolution.getScaledHeight();
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);


                String LRQueue1 = "❶";
                String LRQueue2 = "❷";
                String LRQueue3 = "❸";
                String LRQueue4 = "❹";
                String LRQueueCrossBar =  "▬";
                String LRQueueDummy1 = "❶ ▬ ❷ ▬ ❸ ▬ ❹";
                String LRQueueDummy2 = "❶ ▬ ❷";
                String LRQueueDummy3 = "▬ ❷ ▬ ❸ ▬";

                int triggeredColor = 0xFFFF00;
                int disabledColor = 0x808080;
                int crossbarColor = 0x00FF00;
                int firstLight;
                int secondLight;
                int thirthLight;
                int forthLight;

                if(lrTimes == 1){
                    firstLight = triggeredColor;
                    secondLight = disabledColor;
                    thirthLight = disabledColor;
                    forthLight = disabledColor;
                }else if(lrTimes == 2){
                    firstLight = triggeredColor;
                    secondLight = triggeredColor;
                    thirthLight = disabledColor;
                    forthLight = disabledColor;
                }else if(lrTimes == 3){
                    firstLight = triggeredColor;
                    secondLight = triggeredColor;
                    thirthLight = triggeredColor;
                    forthLight = disabledColor;
                }else if(lrTimes == 4){
                    firstLight = triggeredColor;
                    secondLight = triggeredColor;
                    thirthLight = triggeredColor;
                    forthLight = triggeredColor;
                }else{
                    firstLight = disabledColor;
                    secondLight = disabledColor;
                    thirthLight = disabledColor;
                    forthLight = disabledColor;
                }
                minecraft.fontRendererObj.drawStringWithShadow(LRQueue1,
                        screenWidth / 2 - minecraft.fontRendererObj.getStringWidth(LRQueueDummy1) / 2,
                        screenHeight / 1.2F,
                        firstLight + (l1 << 24 & -firstLight));
                minecraft.fontRendererObj.drawStringWithShadow(LRQueue2,
                        screenWidth / 2 - minecraft.fontRendererObj.getStringWidth(LRQueueDummy2) / 2,
                        screenHeight / 1.2F,
                        secondLight + (l1 << 24 & -secondLight));
                minecraft.fontRendererObj.drawStringWithShadow(LRQueue3,
                        screenWidth / 2 + minecraft.fontRendererObj.getStringWidth(LRQueueDummy2) / 2 - minecraft.fontRendererObj.getStringWidth(LRQueue1),
                        screenHeight / 1.2F,
                        thirthLight + (l1 << 24 & -thirthLight));
                minecraft.fontRendererObj.drawStringWithShadow(LRQueue4,
                        screenWidth / 2 + minecraft.fontRendererObj.getStringWidth(LRQueueDummy1) / 2 - minecraft.fontRendererObj.getStringWidth(LRQueue1),
                        screenHeight / 1.2F,
                        forthLight + (l1 << 24 & -forthLight));

                minecraft.fontRendererObj.drawStringWithShadow(LRQueueCrossBar,
                        screenWidth / 2 - minecraft.fontRendererObj.getStringWidth(LRQueueDummy3) / 2,
                        screenHeight / 1.2F,
                        crossbarColor + (l1 << 24 & -crossbarColor));
                minecraft.fontRendererObj.drawStringWithShadow(LRQueueCrossBar,
                        screenWidth / 2 - minecraft.fontRendererObj.getStringWidth(LRQueueCrossBar) / 2,
                        screenHeight / 1.2F,
                        crossbarColor + (l1 << 24 & -crossbarColor));
                minecraft.fontRendererObj.drawStringWithShadow(LRQueueCrossBar,
                        screenWidth / 2 + minecraft.fontRendererObj.getStringWidth(LRQueueDummy3) / 2 - minecraft.fontRendererObj.getStringWidth(LRQueueCrossBar),
                        screenHeight / 1.2F,
                        crossbarColor + (l1 << 24 & -crossbarColor));
                GlStateManager.disableBlend();
            }
        }
    }
    //count in TICK
    public static long getDEW2Time (int round, boolean isSound) {
        switch (round) {
            case 1: return (isSound? 20 : 10000);
            case 2: return (isSound? 20 : 10000);
            case 3: return 20;
            case 4: return 20;
            case 5: return 22;
            case 6: return 22;
            case 7: return 25;
            case 8: return 25;
            case 9: return 22;
            case 10: return 24;
            case 11: return 25;
            case 12: return 25;
            case 13: return 25;
            case 14: return 25;
            case 15: return 25;
            case 16: return 24;
            case 17: return 24;
            case 18: return 24;
            case 19: return 24;
            case 20: return 24;
            case 21: return 23;
            case 22: return 23;
            case 23: return 23;
            case 24: return 23;
            case 25: return 23;
            case 26: return 23;
            case 27: return 24;
            case 28: return 24;
            case 29: return 24;
            case 30: return 24;
            default: return 114514;
        }
    }

    public static long getDEW3Time(int round){
        switch (round) {
            case 1: return 5000;
            case 2: return 5000;
            case 3: return 35;
            case 4: return 35;
            case 5: return 37;
            case 6: return 44;
            case 7: return 47;
            case 8: return 50;
            case 9: return 38;
            case 10: return 45;
            case 11: return 48;
            case 12: return 50;
            case 13: return 50;
            case 14: return 45;
            case 15: return 46;
            case 16: return 47;
            case 17: return 47;
            case 18: return 47;
            case 19: return 47;
            case 20: return 49;
            case 21: return 44;
            case 22: return 45;
            case 23: return 42;
            case 24: return 43;
            case 25: return 43;
            case 26: return 36;
            case 27: return 44;
            case 28: return 42;
            case 29: return 42;
            case 30: return 45;
            default: return 11451;
        }
    }

    //count in TICK
    public static long getBBW2Time (int round, boolean isSound){
        switch (round){
            case 1: return (isSound? 22 : 5000);
            case 2: return (isSound? 22 : 5000);
            case 3: return (isSound? 22 : 5000);
            case 4: return 22;
            case 5: return 22;
            case 6: return 22;
            case 7: return 22;
            case 8: return 22;
            case 9: return 22;
            case 10: return 22;
            case 11: return 22;
            case 12: return 22;
            case 13: return 22;
            case 14: return 22;
            case 15: return 22;
            case 16: return 22;
            case 17: return 22;
            case 18: return 22;
            case 19: return 22;
            case 20: return 22;
            case 21: return 22;
            case 22: return 22;
            case 23: return 22;
            case 24: return 22;
            case 25: return 22;
            case 26: return 24;
            case 27: return 24;
            case 28: return 22;
            case 29: return 24;
            case 30: return 22;
            default: return 114514;
        }
    }
    public static long getBBW3Time (int round){
        switch (round){
            case 1: return 5000;
            case 2: return 5000;
            case 3: return 5000;
            case 4: return 34;
            case 5: return 34;
            case 6: return 34;
            case 7: return 34;
            case 8: return 34;
            case 9: return 34;
            case 10: return 34;
            case 11: return 34;
            case 12: return 34;
            case 13: return 34;
            case 14: return 34;
            case 15: return 34;
            case 16: return 34;
            case 17: return 34;
            case 18: return 34;
            case 19: return 34;
            case 20: return 34;
            case 21: return 34;
            case 22: return 34;
            case 23: return 34;
            case 24: return 34;
            case 25: return 34;
            case 26: return 38;
            case 27: return 38;
            case 28: return 34;
            case 29: return 38;
            case 30: return 34;
            default: return 11451;
        }
    }
    //count in SECOND all below.
    private double getAAW2Time(int round){
        switch (round){
            case 1: return 0.1;
            case 2: return 0.1;
            case 3: return 0.1;
            case 4: return 14;
            case 5: return 14;
            case 6: return 14;
            case 7: return 15;
            case 8: return 15;
            case 9: return 14;
            case 10: return 16;

            case 11: return 16;
            case 12: return 16;
            case 13: return 16;
            case 14: return 16;
            case 15: return 17;
            case 16: return 16;
            case 17: return 14;
            case 18: return 14;
            case 19: return 14;
            case 20: return 15;

            case 21: return 14;
            case 22: return 14;
            case 23: return 14;
            case 24: return 14;
            case 25: return 0.1;
            case 26: return 0.1;
            case 27: return 0.1;
            case 28: return 0.1;
            case 29: return 0.1;
            case 30: return 0.1;

            case 31: return 0.1;
            case 32: return 0.1;
            case 33: return 0.1;
            case 34: return 0.1;
            case 35: return 0.1;
            case 36: return 0.1;
            case 37: return 0.1;
            case 38: return 0.1;
            case 39: return 0.1;
            case 40: return 10;

            case 41: return 0.1;
            case 42: return 0.1;
            case 43: return 13;
            case 44: return 0.1;
            case 45: return 0.1;
            case 46: return 0.1;
            case 47: return 0.1;
            case 48: return 0.1;
            case 49: return 0.1;
            case 50: return 0.1;

            case 51: return 0.1;
            case 52: return 0.1;
            case 53: return 0.1;
            case 54: return 10;
            case 55: return 16;
            case 56: return 0.1;
            case 57: return 0.1;
            case 58: return 10;
            case 59: return 14;
            case 60: return 0.1;

            case 61: return 14;
            case 62: return 14;
            case 63: return 14;
            case 64: return 14;
            case 65: return 14;
            case 66: return 14;
            case 67: return 14;
            case 68: return 14;
            case 69: return 14;
            case 70: return 14;

            case 71: return 14;
            case 72: return 14;
            case 73: return 14;
            case 74: return 14;
            case 75: return 14;
            case 76: return 14;
            case 77: return 14;
            case 78: return 14;
            case 79: return 14;
            case 80: return 14;

            case 81: return 14;
            case 82: return 14;
            case 83: return 14;
            case 84: return 14;
            case 85: return 14;
            case 86: return 14;
            case 87: return 14;
            case 88: return 14;
            case 89: return 14;
            case 90: return 14;
            case 91: return 14;
            case 92: return 14;
            case 93: return 14;
            case 94: return 14;
            case 95: return 14;
            case 96: return 14;
            case 97: return 14;
            case 98: return 14;
            case 99: return 14;
            case 100: return 14;

            case 101: return 0.1;
            case 102: return 0.1;
            case 103: return 0.1;
            case 104: return 0.1;
            case 105: return 0.1;
            default: return 1000;
        }
    }

    private double getAAW3Time(int round) {
        switch (round){
            case 1: return 10;
            case 2: return 10;
            case 3: return 10;
            case 4: return 17;
            case 5: return 18;
            case 6: return 19;
            case 7: return 19;
            case 8: return 20;
            case 9: return 19;
            case 10: return 22;

            case 11: return 21;
            case 12: return 22;
            case 13: return 22;
            case 14: return 21;
            case 15: return 24;
            case 16: return 22;
            case 17: return 19;
            case 18: return 19;
            case 19: return 18;
            case 20: return 21;

            case 21: return 19;
            case 22: return 19;
            case 23: return 18;
            case 24: return 19;
            case 25: return 0.15;
            case 26: return 0.15;
            case 27: return 0.15;
            case 28: return 0.15;
            case 29: return 0.15;
            case 30: return 0.15;

            case 31: return 0.15;
            case 32: return 0.15;
            case 33: return 0.15;
            case 34: return 0.15;
            case 35: return 0.15;
            case 36: return 0.15;
            case 37: return 0.15;
            case 38: return 0.15;
            case 39: return 0.15;
            case 40: return 22;

            case 41: return 0.15;
            case 42: return 0.15;
            case 43: return 22;
            case 44: return 0.15;
            case 45: return 10;
            case 46: return 10;
            case 47: return 0.15;
            case 48: return 10;
            case 49: return 0.15;
            case 50: return 10;

            case 51: return 10;
            case 52: return 10;
            case 53: return 10;
            case 54: return 20;
            case 55: return 22;
            case 56: return 0.15;
            case 57: return 0.15;
            case 58: return 22;
            case 59: return 18;
            case 60: return 10;

            case 61: return 18;
            case 62: return 18;
            case 63: return 18;
            case 64: return 18;
            case 65: return 18;
            case 66: return 18;
            case 67: return 18;
            case 68: return 18;
            case 69: return 18;
            case 70: return 18;

            case 71: return 18;
            case 72: return 18;
            case 73: return 18;
            case 74: return 18;
            case 75: return 18;
            case 76: return 18;
            case 77: return 18;
            case 78: return 18;
            case 79: return 18;
            case 80: return 18;

            case 81: return 18;
            case 82: return 18;
            case 83: return 18;
            case 84: return 18;
            case 85: return 18;
            case 86: return 18;
            case 87: return 18;
            case 88: return 18;
            case 89: return 18;
            case 90: return 18;

            case 91: return 18;
            case 92: return 18;
            case 93: return 18;
            case 94: return 18;
            case 95: return 18;
            case 96: return 18;
            case 97: return 18;
            case 98: return 18;
            case 99: return 18;
            case 100: return 18;

            case 101: return 0.15;
            case 102: return 0.15;
            case 103: return 0.15;
            case 104: return 0.15;
            case 105: return 0.15;

            default: return 1000;
        }
    }

    private double getAAW4Time(int round){
        switch (round){
            case 1: return 13;
            case 2: return 14;
            case 3: return 13;
            case 4: return 21;
            case 5: return 22;
            case 6: return 23;
            case 7: return 23;
            case 8: return 25;
            case 9: return 23;
            case 10: return 27;

            case 11: return 27;
            case 12: return 28;
            case 13: return 28;
            case 14: return 26;
            case 15: return 31;
            case 16: return 27;
            case 17: return 23;
            case 18: return 23;
            case 19: return 22;
            case 20: return 26;

            case 21: return 23;
            case 22: return 23;
            case 23: return 22;
            case 24: return 23;
            case 25: return 0.2;
            case 26: return 10;
            case 27: return 10;
            case 28: return 10;
            case 29: return 10;
            case 30: return 10;

            case 31: return 10;
            case 32: return 10;
            case 33: return 10;
            case 34: return 10;
            case 35: return 0.2;
            case 36: return 10;
            case 37: return 10;
            case 38: return 10;
            case 39: return 10;
            case 40: return 34;

            case 41: return 10;
            case 42: return 10;
            case 43: return 25;
            case 44: return 10;
            case 45: return 22;
            case 46: return 21;
            case 47: return 10;
            case 48: return 20;
            case 49: return 10;
            case 50: return 22;

            case 51: return 20;
            case 52: return 22;
            case 53: return 22;
            case 54: return 32;
            case 55: return 28;
            case 56: return 10;
            case 57: return 10;
            case 58: return 34;
            case 59: return 22;
            case 60: return 20;

            case 61: return 22;
            case 62: return 22;
            case 63: return 22;
            case 64: return 22;
            case 65: return 22;
            case 66: return 22;
            case 67: return 22;
            case 68: return 22;
            case 69: return 22;
            case 70: return 22;

            case 71: return 22;
            case 72: return 22;
            case 73: return 22;
            case 74: return 22;
            case 75: return 22;
            case 76: return 22;
            case 77: return 22;
            case 78: return 22;
            case 79: return 22;
            case 80: return 22;

            case 81: return 22;
            case 82: return 22;
            case 83: return 22;
            case 84: return 22;
            case 85: return 22;
            case 86: return 22;
            case 87: return 22;
            case 88: return 22;
            case 89: return 22;
            case 90: return 22;

            case 91: return 22;
            case 92: return 22;
            case 93: return 22;
            case 94: return 22;
            case 95: return 22;
            case 96: return 22;
            case 97: return 22;
            case 98: return 22;
            case 99: return 22;
            case 100: return 22;

            case 101: return 0.2;
            case 102: return 0.2;
            case 103: return 0.2;
            case 104: return 0.2;
            case 105: return 0.2;

            default: return 1000;
        }
    }

    private double getAAW5Time(int round){
        switch (round){
            case 1: return 16;
            case 2: return 18;
            case 3: return 16;
            case 4: return 25;
            case 5: return 26;
            case 6: return 28;
            case 7: return 27;
            case 8: return 30;
            case 9: return 28;
            case 10: return 33;

            case 11: return 32;
            case 12: return 34;
            case 13: return 34;
            case 14: return 31;
            case 15: return 38;
            case 16: return 33;
            case 17: return 28;
            case 18: return 28;
            case 19: return 26;
            case 20: return 31;

            case 21: return 28;
            case 22: return 28;
            case 23: return 26;
            case 24: return 28;
            case 25: return 0.25;
            case 26: return 23;
            case 27: return 22;
            case 28: return 20;
            case 29: return 24;
            case 30: return 22;

            case 31: return 22;
            case 32: return 21;
            case 33: return 22;
            case 34: return 22;
            case 35: return 0.25;
            case 36: return 22;
            case 37: return 20;
            case 38: return 22;
            case 39: return 22;
            case 40: return 37;

            case 41: return 21;
            case 42: return 22;
            case 43: return 34;
            case 44: return 22;
            case 45: return 34;
            case 46: return 32;
            case 47: return 20;
            case 48: return 30;
            case 49: return 21;
            case 50: return 34;

            case 51: return 30;
            case 52: return 34;
            case 53: return 34;
            case 54: return 35;
            case 55: return 34;
            case 56: return 14;
            case 57: return 14;
            case 58: return 37;
            case 59: return 26;
            case 60: return 30;

            case 61: return 26;
            case 62: return 26;
            case 63: return 26;
            case 64: return 26;
            case 65: return 26;
            case 66: return 26;
            case 67: return 26;
            case 68: return 26;
            case 69: return 26;
            case 70: return 26;

            case 71: return 26;
            case 72: return 26;
            case 73: return 26;
            case 74: return 27;
            case 75: return 27;
            case 76: return 26;
            case 77: return 26;
            case 78: return 26;
            case 79: return 26;
            case 80: return 26;

            case 81: return 26;
            case 82: return 26;
            case 83: return 26;
            case 84: return 26;
            case 85: return 26;
            case 86: return 26;
            case 87: return 26;
            case 88: return 26;
            case 89: return 26;
            case 90: return 26;

            case 91: return 26;
            case 92: return 26;
            case 93: return 26;
            case 94: return 26;
            case 95: return 26;
            case 96: return 26;
            case 97: return 26;
            case 98: return 26;
            case 99: return 26;
            case 100: return 26;

            case 101: return 0.25;
            case 102: return 0.25;
            case 103: return 0.25;
            case 104: return 0.25;
            case 105: return 0.25;

            default: return 1000;
        }
    }

    public double getAAW6Time(int round){
        switch (round){
            case 1: return 19;
            case 2: return 22;
            case 3: return 19;
            case 4: return 28;
            case 5: return 30;
            case 6: return 32;
            case 7: return 31;
            case 8: return 35;
            case 9: return 32;
            case 10: return 38;

            case 11: return 38;
            case 12: return 40;
            case 13: return 40;
            case 14: return 36;
            case 15: return 46;
            case 16: return 38;
            case 17: return 32;
            case 18: return 32;
            case 19: return 30;
            case 20: return 36;

            case 21: return 32;
            case 22: return 34;
            case 23: return 30;
            case 24: return 32;
            case 25: return 10;
            case 26: return 36;
            case 27: return 34;
            case 28: return 30;
            case 29: return 38;
            case 30: return 34;

            case 31: return 34;
            case 32: return 32;
            case 33: return 34;
            case 34: return 34;
            case 35: return 10;
            case 36: return 34;
            case 37: return 31;
            case 38: return 34;
            case 39: return 34;
            case 40: return 45;

            case 41: return 32;
            case 42: return 34;
            case 43: return 37;
            case 44: return 34;
            case 45: return 35;
            case 46: return 35;
            case 47: return 30;
            case 48: return 33;
            case 49: return 32;
            case 50: return 37;

            case 51: return 33;
            case 52: return 37;
            case 53: return 37;
            case 54: return 39;
            case 55: return 40;
            case 56: return 18;
            case 57: return 18;
            case 58: return 38;
            case 59: return 30;
            case 60: return 33;

            case 61: return 30;
            case 62: return 30;
            case 63: return 30;
            case 64: return 30;
            case 65: return 30;
            case 66: return 30;
            case 67: return 30;
            case 68: return 30;
            case 69: return 30;
            case 70: return 30;

            case 71: return 30;
            case 72: return 30;
            case 73: return 30;
            case 74: return 32;
            case 75: return 32;
            case 76: return 30;
            case 77: return 30;
            case 78: return 30;
            case 79: return 30;
            case 80: return 30;

            case 81: return 30;
            case 82: return 30;
            case 83: return 30;
            case 84: return 30;
            case 85: return 30;
            case 86: return 30;
            case 87: return 30;
            case 88: return 30;
            case 89: return 30;
            case 90: return 30;

            case 91: return 30;
            case 92: return 30;
            case 93: return 30;
            case 94: return 30;
            case 95: return 30;
            case 96: return 30;
            case 97: return 30;
            case 98: return 30;
            case 99: return 30;
            case 100: return 30;

            case 101: return 5;
            case 102: return 5;
            case 103: return 5;
            case 104: return 5;
            case 105: return 5;
            default: return 1000;
        }
    }

    public static double secondToTick(double second){
        return second * 1000;
    }
}
