package com.seosean.showspawntime.mapFile;

import com.seosean.showspawntime.ShowSpawnTime;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.lang3.ArrayUtils;

import java.util.*;

import static com.seosean.showspawntime.mapFile.Rounds.*;
import static com.seosean.showspawntime.ShowSpawnTime.PlaySound;
import static com.seosean.showspawntime.utils.Utils.*;

public class AAFeature {

    private final FontRenderer fontRenderer;

    private static Map<Integer, List<Integer>> o1RoundsToWaves = new HashMap<>();
    private static Map<Integer, List<Integer>> giantRoundsToWaves = new HashMap<>();
    private static Map<Integer, List<Integer>> o1AndGiantRoundsToWaves = new HashMap<>();
    private static Set<Integer> o1RKeySet = new HashSet<>();
    private static Set<Integer> giantsRKeySet = new HashSet<>();
    private static Set<Integer> o1GiantsRKeySet = new HashSet<>();

    public AAFeature() {
        this.fontRenderer = minecraft.fontRendererObj;
        o1OnlyWave();
        giantsOnlyWave();
        o1AndGiantsWave();
        o1RKeySet = o1RoundsToWaves.keySet();
        giantsRKeySet = giantRoundsToWaves.keySet();
        o1GiantsRKeySet = o1AndGiantRoundsToWaves.keySet();
    }

    private static int getColor(int round, int wave){
        if(!ShowSpawnTime.ColorAlert){
            return 0xFFFF00;
        }
        if(o1RKeySet.contains(round) && o1RoundsToWaves.get(round).contains(wave)){
            return 0x00FF00;
        }
        if(giantsRKeySet.contains(round) && giantRoundsToWaves.get(round).contains(wave)){
            return 0x0099FF;
        }
        if(o1GiantsRKeySet.contains(round) && o1AndGiantRoundsToWaves.get(round).contains(wave)){
            return 0xFF0000;
        }
        return 0xFFFF00;
    }

    private static int getDisableColor(int round, int wave){
        if(!ShowSpawnTime.ColorAlert){
            return 0xA7A7A7;
        }
        if(o1RKeySet.contains(round) && o1RoundsToWaves.get(round).contains(wave)){
            return 0x006666;
        }
        if(giantsRKeySet.contains(round) && giantRoundsToWaves.get(round).contains(wave)){
            return 0x663399;
        }
        if(o1GiantsRKeySet.contains(round) && o1AndGiantRoundsToWaves.get(round).contains(wave)){
            return 0x783300;
        }
        return 0x808080;
    }

    private static int getPassedColor(){
        return 0x5a5a5a;
    }

    private com.seosean.showspawntime.mapFile.InternalTimer InternalTimer = ShowSpawnTime.getInstance().getInternalTimer();
    @SubscribeEvent
    public void Round(RenderGameOverlayEvent.Post event1) {
        if (event1.type != RenderGameOverlayEvent.ElementType.TEXT) {
            return;
        }
        if(!isAllLegit()) {
            return;
        }
        if(!isInAA()){
            return;
        }
        ScaledResolution scaledResolution = new ScaledResolution(minecraft);
        int widthW = fontRenderer.getStringWidth("➤ ");
        float absoluteX = (float)ShowSpawnTime.getInstance().getXSpawnTime() * (float)scaledResolution.getScaledWidth();
        float absoluteY = (float)ShowSpawnTime.getInstance().getYSpawnTime() * (float)scaledResolution.getScaledHeight();
        if(parseSidebar() != 0){
            fontRenderer.drawStringWithShadow("➤ ", absoluteX, absoluteY + this.fontRenderer.FONT_HEIGHT * height, 0xCC00CC);
        }
        fontRenderer.drawStringWithShadow(MapChoose.getAAW1Time(parseSidebar()), absoluteX + widthW, absoluteY, getColorW1());
        fontRenderer.drawStringWithShadow(AAFeature.getAAW2Time(parseSidebar()), absoluteX + widthW, absoluteY + this.fontRenderer.FONT_HEIGHT, getColorW2());
        fontRenderer.drawStringWithShadow(AAFeature.getAAW3Time(parseSidebar()), absoluteX + widthW, absoluteY + this.fontRenderer.FONT_HEIGHT * 2, getColorW3());
        fontRenderer.drawStringWithShadow(AAFeature.getAAW4Time(parseSidebar()), absoluteX + widthW, absoluteY + this.fontRenderer.FONT_HEIGHT * 3, getColorW4());
        fontRenderer.drawStringWithShadow(AAFeature.getAAW5Time(parseSidebar()), absoluteX + widthW, absoluteY + this.fontRenderer.FONT_HEIGHT * 4, getColorW5());
        fontRenderer.drawStringWithShadow(AAFeature.getAAW6Time(parseSidebar()), absoluteX + widthW, absoluteY + this.fontRenderer.FONT_HEIGHT * 5, getColorW6());
        getSoundWhenW6Start();
    }

    public static boolean playSound = false;
    private int height;

    private int getColorW1(){
        if(InternalTimer.setAAW1End()){
            return getPassedColor();
        }
        if(ArrayUtils.contains(com.seosean.showspawntime.mapFile.InternalTimer.sixWavesRound, parseSidebar())){
            height = 0;
        }else
            height = 6;
        return getColor(parseSidebar(), 1);
    }

    private int getColorW2(){
        if(InternalTimer.setAAW1End() && !InternalTimer.setW2End(false)){
            if(!playSound){
                playSound = true;
                if(ArrayUtils.contains(com.seosean.showspawntime.mapFile.InternalTimer.sixWavesRound, parseSidebar()))
                    if(PlaySound)
                            Minecraft.getMinecraft().thePlayer.playSound(ShowSpawnTime.PrecededWave, 2, (float)ShowSpawnTime.PrecededWavePitch);
            }
            if(ArrayUtils.contains(com.seosean.showspawntime.mapFile.InternalTimer.sixWavesRound, parseSidebar()) || ArrayUtils.contains(com.seosean.showspawntime.mapFile.InternalTimer.fiveWavesRound, parseSidebar())){
                height = 1;
            } else{
                height = 6;
            }
            return getColor(parseSidebar(), 2);
        }
        if(InternalTimer.setAAW1End() && InternalTimer.setW2End(false)){
            return getPassedColor();
        }
        return getDisableColor(parseSidebar(), 2);
    }

    private int getColorW3(){
        if(InternalTimer.setW2End(false) && !InternalTimer.setW3End()){
            if(playSound){
                playSound = false;
                if(ArrayUtils.contains(com.seosean.showspawntime.mapFile.InternalTimer.sixWavesRound, parseSidebar()) || ArrayUtils.contains(com.seosean.showspawntime.mapFile.InternalTimer.fiveWavesRound, parseSidebar()))
                    if(PlaySound)
                        Minecraft.getMinecraft().thePlayer.playSound(ShowSpawnTime.PrecededWave, 2, (float)ShowSpawnTime.PrecededWavePitch);
            }
            if(ArrayUtils.contains(com.seosean.showspawntime.mapFile.InternalTimer.sixWavesRound, parseSidebar()) || ArrayUtils.contains(com.seosean.showspawntime.mapFile.InternalTimer.fiveWavesRound, parseSidebar()) || ArrayUtils.contains(com.seosean.showspawntime.mapFile.InternalTimer.fourWavesRound, parseSidebar())){
                height = 2;
            }else
                height = 6;
            return getColor(parseSidebar(), 3);
        }
        if(InternalTimer.setW2End(false) && InternalTimer.setW3End()) {
            return getPassedColor();
        }
            return getDisableColor(parseSidebar(), 3);
    }

    private int getColorW4(){
        if(InternalTimer.setW3End() && !InternalTimer.setW4End()){
            if(!playSound){
                playSound = true;
                if(ArrayUtils.contains(com.seosean.showspawntime.mapFile.InternalTimer.sixWavesRound, parseSidebar()) || ArrayUtils.contains(com.seosean.showspawntime.mapFile.InternalTimer.fiveWavesRound, parseSidebar()) || ArrayUtils.contains(com.seosean.showspawntime.mapFile.InternalTimer.fourWavesRound, parseSidebar()))
                    if(PlaySound)
                        Minecraft.getMinecraft().thePlayer.playSound(ShowSpawnTime.PrecededWave, 2, (float)ShowSpawnTime.PrecededWavePitch);
            }
            if(ArrayUtils.contains(com.seosean.showspawntime.mapFile.InternalTimer.sixWavesRound, parseSidebar()) || ArrayUtils.contains(com.seosean.showspawntime.mapFile.InternalTimer.fiveWavesRound, parseSidebar()) || ArrayUtils.contains(com.seosean.showspawntime.mapFile.InternalTimer.fourWavesRound, parseSidebar()) || ArrayUtils.contains(com.seosean.showspawntime.mapFile.InternalTimer.threeWavesRound, parseSidebar())){
                height = 3;
            }else
                height = 6;
            return getColor(parseSidebar(), 4);
        }
        if(InternalTimer.setW3End() && InternalTimer.setW4End()) {
            return getPassedColor();
        }
        return getDisableColor(parseSidebar(), 4);
    }

    private int getColorW5(){
        if(InternalTimer.setW4End() && !InternalTimer.setW5End()){
            if(playSound){
                playSound = false;
                if(ArrayUtils.contains(com.seosean.showspawntime.mapFile.InternalTimer.sixWavesRound, parseSidebar()) || ArrayUtils.contains(com.seosean.showspawntime.mapFile.InternalTimer.fiveWavesRound, parseSidebar()) || ArrayUtils.contains(com.seosean.showspawntime.mapFile.InternalTimer.fourWavesRound, parseSidebar()) || ArrayUtils.contains(com.seosean.showspawntime.mapFile.InternalTimer.threeWavesRound, parseSidebar()) )
                    if(PlaySound)
                        Minecraft.getMinecraft().thePlayer.playSound(ShowSpawnTime.PrecededWave, 2, (float)ShowSpawnTime.PrecededWavePitch);
            }
            if(ArrayUtils.contains(com.seosean.showspawntime.mapFile.InternalTimer.sixWavesRound, parseSidebar()) || ArrayUtils.contains(com.seosean.showspawntime.mapFile.InternalTimer.fiveWavesRound, parseSidebar()) || ArrayUtils.contains(com.seosean.showspawntime.mapFile.InternalTimer.fourWavesRound, parseSidebar()) || ArrayUtils.contains(com.seosean.showspawntime.mapFile.InternalTimer.threeWavesRound, parseSidebar())){
                height = 4;
            }else
                height = 6;
            return getColor(parseSidebar(), 5);
        }
        if(InternalTimer.setW4End() && InternalTimer.setW5End()) {
            return getPassedColor();
        }
        return getDisableColor(parseSidebar(), 5);
    }

    private int getColorW6(){
        if(InternalTimer.setW5End() && !InternalTimer.setW6End()){
            if(!playSound){
                playSound = true;
                if(ArrayUtils.contains(com.seosean.showspawntime.mapFile.InternalTimer.sixWavesRound, parseSidebar()) || ArrayUtils.contains(com.seosean.showspawntime.mapFile.InternalTimer.fiveWavesRound, parseSidebar()) || ArrayUtils.contains(com.seosean.showspawntime.mapFile.InternalTimer.fourWavesRound, parseSidebar()) || ArrayUtils.contains(com.seosean.showspawntime.mapFile.InternalTimer.threeWavesRound, parseSidebar()))
                    if(PlaySound)
                        Minecraft.getMinecraft().thePlayer.playSound(ShowSpawnTime.PrecededWave, 2, (float)ShowSpawnTime.PrecededWavePitch);
            }
        }
        if(InternalTimer.setW5End()){
            if(ArrayUtils.contains(com.seosean.showspawntime.mapFile.InternalTimer.sixWavesRound, parseSidebar()) || ArrayUtils.contains(com.seosean.showspawntime.mapFile.InternalTimer.fiveWavesRound, parseSidebar()) || ArrayUtils.contains(com.seosean.showspawntime.mapFile.InternalTimer.fourWavesRound, parseSidebar()) || ArrayUtils.contains(com.seosean.showspawntime.mapFile.InternalTimer.threeWavesRound, parseSidebar()) || ArrayUtils.contains(com.seosean.showspawntime.mapFile.InternalTimer.oneWavesRound, parseSidebar())){
                height = 5;
            }else
                height = 6;
            return getColor(parseSidebar(), 6);
        }
        return getDisableColor(parseSidebar(), 6);
    }

    private void getSoundWhenW6Start(){
        if(InternalTimer.setW6End()){
            if(playSound){
                playSound = false;
                if(ArrayUtils.contains(com.seosean.showspawntime.mapFile.InternalTimer.sixWavesRound, parseSidebar()) || ArrayUtils.contains(com.seosean.showspawntime.mapFile.InternalTimer.fiveWavesRound, parseSidebar()) || ArrayUtils.contains(com.seosean.showspawntime.mapFile.InternalTimer.fourWavesRound, parseSidebar()) || ArrayUtils.contains(com.seosean.showspawntime.mapFile.InternalTimer.threeWavesRound, parseSidebar()) || ArrayUtils.contains(com.seosean.showspawntime.mapFile.InternalTimer.oneWavesRound, parseSidebar()))
                    if(PlaySound)
                        Minecraft.getMinecraft().thePlayer.playSound(ShowSpawnTime.TheLastWave, 2, (float)ShowSpawnTime.TheLastWavePitch);
            }
        }
    }


    private static String getAAW2Time(int round){
        if(isAllLegit()) {
            if (isInAA()) {
                return MapChoose.mapAlienArcadiumW2(round);
            }

        } return "";

    }

    private static String getAAW3Time(int round){
        if(isAllLegit()) {
            if (isInAA()) {
                return MapChoose.mapAlienArcadiumW3(round);
            }

        } return "";

    }

    private static String getAAW4Time(int round){
        if(isAllLegit()) {
            if (isInAA()) {
                return MapChoose.mapAlienArcadiumW4(round);
            }
        } return "";

    }

    private static String getAAW5Time(int round){
        if(isAllLegit()) {
            if (isInAA()) {
                return MapChoose.mapAlienArcadiumW5(round);
            }
        } return "";

    }

    private static String getAAW6Time(int round){
        if(isAllLegit()) {
            if (isInAA()) {
                return MapChoose.mapAlienArcadiumW6(round);
            }
        } return "";

    }

    private static void o1OnlyWave(){
        o1RoundsToWaves.put(40, Arrays.asList(6));

        o1RoundsToWaves.put(45, Arrays.asList(5,6));

        o1RoundsToWaves.put(46, Arrays.asList(6));

        o1RoundsToWaves.put(48, Arrays.asList(6));

        o1RoundsToWaves.put(54, Arrays.asList(6));

        o1RoundsToWaves.put(55, Arrays.asList(6));

        o1RoundsToWaves.put(58, Arrays.asList(6));

        o1RoundsToWaves.put(59, Arrays.asList(1, 2, 3, 4, 5, 6));

        o1RoundsToWaves.put(60, Arrays.asList(5,6));

        o1RoundsToWaves.put(64, Arrays.asList(5,6));

        o1RoundsToWaves.put(67, Arrays.asList(6));

        o1RoundsToWaves.put(68, Arrays.asList(5,6));

        o1RoundsToWaves.put(69, Arrays.asList(5,6));

        o1RoundsToWaves.put(70, Arrays.asList(2,3));

        o1RoundsToWaves.put(74, Arrays.asList(4,5,6));

        o1RoundsToWaves.put(77, Arrays.asList(6));

        o1RoundsToWaves.put(78, Arrays.asList(5,6));

        o1RoundsToWaves.put(79, Arrays.asList(5,6));

        o1RoundsToWaves.put(80, Arrays.asList(2,3));

        o1RoundsToWaves.put(84, Arrays.asList(4,5,6));

        o1RoundsToWaves.put(87, Arrays.asList(6));

        o1RoundsToWaves.put(88, Arrays.asList(5,6));

        o1RoundsToWaves.put(89, Arrays.asList(5,6));

        o1RoundsToWaves.put(90, Arrays.asList(2,3));

        o1RoundsToWaves.put(94, Arrays.asList(4,5,6));

        o1RoundsToWaves.put(97, Arrays.asList(6));

        o1RoundsToWaves.put(98, Arrays.asList(5,6));

        o1RoundsToWaves.put(99, Arrays.asList(5,6));

        o1RoundsToWaves.put(100, Arrays.asList(2,3));
    }

    private static void giantsOnlyWave() {
        giantRoundsToWaves.put(15, Arrays.asList(6));

        giantRoundsToWaves.put(20, Arrays.asList(3,5));

        giantRoundsToWaves.put(22, Arrays.asList(4,6));

        giantRoundsToWaves.put(24, Arrays.asList(2,4,6));

        giantRoundsToWaves.put(30, Arrays.asList(4,5,6));

        giantRoundsToWaves.put(36, Arrays.asList(5,6));

        giantRoundsToWaves.put(37, Arrays.asList(5,6));

        giantRoundsToWaves.put(38, Arrays.asList(5,6));

        giantRoundsToWaves.put(39, Arrays.asList(5,6));

        giantRoundsToWaves.put(40, Arrays.asList(3,4));

        giantRoundsToWaves.put(41, Arrays.asList(5,6));

        giantRoundsToWaves.put(42, Arrays.asList(4,5,6));

        giantRoundsToWaves.put(43, Arrays.asList(2,4,6));

        giantRoundsToWaves.put(44, Arrays.asList(4,5,6));

        giantRoundsToWaves.put(45, Arrays.asList(4));

        giantRoundsToWaves.put(47, Arrays.asList(6));

        giantRoundsToWaves.put(50, Arrays.asList(4,6));

        giantRoundsToWaves.put(51, Arrays.asList(4,6));

        giantRoundsToWaves.put(52, Arrays.asList(4,6));

        giantRoundsToWaves.put(53, Arrays.asList(4,6));

        giantRoundsToWaves.put(54, Arrays.asList(5));

        giantRoundsToWaves.put(55, Arrays.asList(1,2,3,4));

        giantRoundsToWaves.put(58, Arrays.asList(5));

        giantRoundsToWaves.put(65, Arrays.asList(4,5,6));

        giantRoundsToWaves.put(75, Arrays.asList(4,5,6));

        giantRoundsToWaves.put(85, Arrays.asList(4,5,6));

        giantRoundsToWaves.put(95, Arrays.asList(4,5,6));
    }

    private static void o1AndGiantsWave() {
        o1AndGiantRoundsToWaves.put(54,Arrays.asList(3));

        o1AndGiantRoundsToWaves.put(55, Arrays.asList(5));

        o1AndGiantRoundsToWaves.put(58, Arrays.asList(3));

        o1AndGiantRoundsToWaves.put(70, Arrays.asList(4,5,6));

        o1AndGiantRoundsToWaves.put(80, Arrays.asList(4,5,6));

        o1AndGiantRoundsToWaves.put(90, Arrays.asList(4,5,6));

        o1AndGiantRoundsToWaves.put(100, Arrays.asList(4,5,6));
    }
}
