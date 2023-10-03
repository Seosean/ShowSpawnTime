package com.seosean.showspawntime.utils;

import com.google.common.collect.Sets;
import com.seosean.showspawntime.ShowSpawnTime;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import static com.seosean.showspawntime.mapFile.Rounds.colorRegex;
import static com.seosean.showspawntime.mapFile.Rounds.emojiRegex;
import static com.seosean.showspawntime.mapFile.Rounds.parseSidebar;
import static com.seosean.showspawntime.utils.Utils.isAllLegit;
import static com.seosean.showspawntime.mapFile.InternalTimer.gameTick;

public class PowerUpDetect {
    public PowerUpDetect(){

    }

    private static boolean instaPicked;
    private static boolean maxPicked;
    private static boolean dgPicked;
    private static boolean ssPicked;
    private static boolean carPicked;
    private static boolean bgPicked;


    public Map<String, Integer> powerupHashMap = new HashMap<>();

    public void iniPowerupMap(){
        powerupHashMap.put("max", 0);
        powerupHashMap.put("dg", 0);
        powerupHashMap.put("ins", 0);
        powerupHashMap.put("ss", 0);
        powerupHashMap.put("car", 0);
        powerupHashMap.put("bg", 0);
        maxPicked = false;
        dgPicked = false;
        instaPicked = false;
        ssPicked = false;
        carPicked = false;
        bgPicked = false;
    }

    public void resetPowerup(){
        if(maxPicked) {
            powerupHashMap.put("max", 0);
            maxPicked = false;
        }
        if(dgPicked){
            powerupHashMap.put("dg", 0);
            dgPicked = false;
        }
        if(instaPicked){
            powerupHashMap.put("ins", 0);
            instaPicked = false;
        }
        if(ssPicked){
            powerupHashMap.put("ss", 0);
            ssPicked = false;
        }
        if(carPicked){
            powerupHashMap.put("car", 0);
            carPicked = false;
        }
        if(bgPicked){
            powerupHashMap.put("bg", 0);
            bgPicked = false;
        }
    }

    private static final Set<String> ALL_IN_ALL_LANGUAGES =  Sets.newHashSet("INSTA KILL", "瞬间击杀", "一擊必殺", "MAX AMMO", "弹药满载", "填滿彈藥", "DOUBLE GOLD", "双倍金钱", "雙倍金幣", "SHOPPING SPREE", "购物狂潮", "購物狂潮", "BONUS GOLD", "金钱加成", "額外金幣", "CARPENTER", "木匠", "木匠");

    public int maxRound = 0;
    public int insRound = 0;
    public int ssRound = 0;


    public List<String> restoredArmorStand = new ArrayList<>();

    private boolean isArmorStandLegit(EntityArmorStand armorStand){
        for(String name : ALL_IN_ALL_LANGUAGES){
            if(armorStand.getDisplayName().getUnformattedText().contains(name)){
                return true;
            }
        }
        return false;
    }
    public void findFloatingArmorStands(World world) {
//        List<EntityArmorStand> entityList = world.getEntitiesWithinAABB(EntityArmorStand.class, createPlayerCenteredBoundingBox(Minecraft.getMinecraft().thePlayer, 128, 128));
        List<EntityArmorStand> entityList = new CopyOnWriteArrayList<>(Minecraft.getMinecraft().theWorld.loadedEntityList.stream()
                .filter(entity -> entity instanceof EntityArmorStand)
                .filter(entity -> entity.hasCustomName())
                .map(entity -> (EntityArmorStand) entity)
                .filter(entity -> isArmorStandLegit(entity))
                .collect(Collectors.toList()));

        for (EntityArmorStand armorStand : entityList) {
            if (armorStand.hasCustomName() && !restoredArmorStand.contains((String.valueOf(armorStand.getDisplayName()).split(", "))[10].replace("}}", "")) && !restoredArmorStand.contains(String.valueOf(armorStand).split(", ")[0].split("/")[1].replace(" ", ""))) {
                String textComponent = String.valueOf(armorStand.getDisplayName());
                String[] textComponentSplits = textComponent.split(", ");
                String armorStandName = textComponentSplits[0].replace("TextComponent{text=", "").replace("\'", "");
                String armorStandUUID = textComponentSplits[10].replace("}}", "");
                String armorStandCode = String.valueOf(armorStand).split(", ")[0].split("/")[1];
                restoredArmorStand.add(armorStandUUID);
                restoredArmorStand.add(armorStandCode);

                if (armorStandName.contains("MAX AMMO") || armorStandName.contains("弹药满载") ||  armorStandName.contains("填滿彈藥")) {
                    powerupHashMap.put("max", 1200);
                    if(parseSidebar() == 2) {
                        maxRound = 2;
                    }else if(parseSidebar() == 3 && gameTick <= 9999){
                        maxRound = 2;
                    }else if(parseSidebar() == 3){
                        maxRound = 3;
                    }else if(parseSidebar() == 4 && gameTick <= 9999){
                        maxRound = 3;
                    }
                }

                else if (armorStandName.contains("DOUBLE GOLD") || armorStandName.contains("双倍金钱") ||  armorStandName.contains("雙倍金幣")) {
                    powerupHashMap.put("dg", 1200);
                }

                else if (armorStandName.contains("INSTA KILL") || armorStandName.contains("瞬间击杀") ||  armorStandName.contains("一擊必殺")) {
                    powerupHashMap.put("ins", 1200);
                    if(parseSidebar() == 2) {
                        insRound = 2;
                    }else if(gameTick <= 9999 && parseSidebar() == 3){
                        insRound = 2;
                    }else if(parseSidebar() == 3){
                        insRound = 3;
                    }else if(parseSidebar() == 4 && gameTick <= 9999){
                        insRound = 3;
                    }
                }

                else if (armorStandName.contains("SHOPPING SPREE") || armorStandName.contains("购物狂潮") ||  armorStandName.contains("購物狂潮")){
                    powerupHashMap.put("ss", 1200);
                    if(parseSidebar() == 5) {
                        ssRound = 5;
                    }else if(parseSidebar() == 6 && gameTick <= 9999){
                        insRound = 5;
                    }else if(parseSidebar() == 6){
                        ssRound = 6;
                    }else if(parseSidebar() == 7 && gameTick <= 9999){
                        insRound = 6;
                    }else if(parseSidebar() == 7){
                        ssRound = 7;
                    }else if(parseSidebar() == 8 && gameTick <= 9999){
                        insRound = 7;
                    }
                }

                else if (armorStandName.contains("CARPENTER") || armorStandName.contains("木匠")){
                    powerupHashMap.put("car", 1200);
                }

                else if (armorStandName.contains("BONUS GOLD") || armorStandName.contains("金钱加成") ||  armorStandName.contains("額外金幣")){
                    powerupHashMap.put("bg", 1200);
                }
            }
        }
    }

    public void powerupCountDown(){
        if(powerupHashMap.get("max") > 0){
            powerupHashMap.replace("max", powerupHashMap.get("max") - 1);
        }
        if(powerupHashMap.get("dg") > 0){
            powerupHashMap.replace("dg", powerupHashMap.get("dg") - 1);
        }
        if(powerupHashMap.get("ins") > 0){
            powerupHashMap.replace("ins", powerupHashMap.get("ins") - 1);
        }
        if(powerupHashMap.get("ss") > 0){
            powerupHashMap.replace("ss", powerupHashMap.get("ss") - 1);
        }
        if(powerupHashMap.get("car") > 0){
            powerupHashMap.replace("car", powerupHashMap.get("car") - 1);
        }
        if(powerupHashMap.get("bg") > 0){
            powerupHashMap.replace("bg", powerupHashMap.get("bg") - 1);
        }
    }

    private final FontRenderer fontRenderer = minecraft.fontRendererObj;
    public static final Minecraft minecraft = Minecraft.getMinecraft();
    @SubscribeEvent
    public void Round(RenderGameOverlayEvent.Post event1) {
        if (event1.type != RenderGameOverlayEvent.ElementType.TEXT) {
            return;
        }
        if(!isAllLegit())
            return;
        if(!ShowSpawnTime.PowerupAlertToggle){
            return;
        }

        ScaledResolution scaledResolution = new ScaledResolution(minecraft);
        int screenHeight = scaledResolution.getScaledHeight();

        float absoluteX = (float)ShowSpawnTime.getInstance().getXPowerup() * (float)scaledResolution.getScaledWidth();
        float absoluteY = (float)ShowSpawnTime.getInstance().getYPowerup() * (float)scaledResolution.getScaledHeight();

        String textSpliterDummy = " - ";
        String text100 = "Round";
        String textTitleDummy = "BONUS GOLD";

        String text01 = (getPowerupQueue().get(0).getValue()) == -1 ? text100 : "00:" + (((getPowerupQueue().get(0).getValue())/20 < 10) ? "0" + (getPowerupQueue().get(0).getValue())/20 : (getPowerupQueue().get(0).getValue())/20);

        String text11 = (getPowerupQueue().get(1).getValue()) == -1 ? text100 : "00:" + (((getPowerupQueue().get(1).getValue())/20 < 10) ? "0" + (getPowerupQueue().get(1).getValue())/20 : (getPowerupQueue().get(1).getValue())/20);

        String text21 = (getPowerupQueue().get(2).getValue()) == -1 ? text100 : "00:" + (((getPowerupQueue().get(2).getValue())/20 < 10) ? "0" + (getPowerupQueue().get(2).getValue())/20 : (getPowerupQueue().get(2).getValue())/20);

        String text31 = (getPowerupQueue().get(3).getValue()) == -1 ? text100 : "00:" + (((getPowerupQueue().get(3).getValue())/20 < 10) ? "0" + (getPowerupQueue().get(3).getValue())/20 : (getPowerupQueue().get(3).getValue())/20);

        String text41 = (getPowerupQueue().get(4).getValue()) == -1 ? text100 : "00:" + (((getPowerupQueue().get(4).getValue())/20 < 10) ? "0" + (getPowerupQueue().get(4).getValue())/20 : (getPowerupQueue().get(4).getValue())/20);

        String text51 = (getPowerupQueue().get(5).getValue()) == -1 ? text100 : "00:" + (((getPowerupQueue().get(5).getValue())/20 < 10) ? "0" + (getPowerupQueue().get(5).getValue())/20 : (getPowerupQueue().get(5).getValue())/20);

        int widthBasic = fontRenderer.getStringWidth("-");
        int widthTitleDummy = fontRenderer.getStringWidth(textTitleDummy);
        int widthSpliterDummy = fontRenderer.getStringWidth(textSpliterDummy);

        fontRenderer.drawStringWithShadow(getPowerupQueue().get(5).getValue() == 0 ? "" : getPowerupTitleName(getPowerupQueue().get(5).getKey()), absoluteX + widthBasic, absoluteY, getPowerupTitleColor(getPowerupQueue().get(5).getKey()));
        fontRenderer.drawStringWithShadow(getPowerupQueue().get(5).getValue() == 0 ? "" : textSpliterDummy, absoluteX + widthBasic + widthTitleDummy, absoluteY, 0xFFFFFF);
        fontRenderer.drawStringWithShadow(getPowerupQueue().get(5).getValue() == 0 ? "" : text51, absoluteX + widthBasic + widthTitleDummy + widthSpliterDummy, absoluteY, getPowerupCountDownColor(getPowerupQueue().get(5).getKey()));

        fontRenderer.drawStringWithShadow(getPowerupQueue().get(4).getValue() == 0 ? "" : getPowerupTitleName(getPowerupQueue().get(4).getKey()), absoluteX + widthBasic, absoluteY + fontRenderer.FONT_HEIGHT, getPowerupTitleColor(getPowerupQueue().get(4).getKey()));
        fontRenderer.drawStringWithShadow(getPowerupQueue().get(4).getValue() == 0 ? "" : textSpliterDummy, absoluteX + widthBasic + widthTitleDummy, absoluteY + fontRenderer.FONT_HEIGHT, 0xFFFFFF);
        fontRenderer.drawStringWithShadow(getPowerupQueue().get(4).getValue() == 0 ? "" : text41, absoluteX + widthBasic + widthTitleDummy + widthSpliterDummy, absoluteY + fontRenderer.FONT_HEIGHT, getPowerupCountDownColor(getPowerupQueue().get(4).getKey()));

        fontRenderer.drawStringWithShadow(getPowerupQueue().get(3).getValue() == 0 ? "" : getPowerupTitleName(getPowerupQueue().get(3).getKey()), absoluteX + widthBasic, absoluteY + fontRenderer.FONT_HEIGHT * 2, getPowerupTitleColor(getPowerupQueue().get(3).getKey()));
        fontRenderer.drawStringWithShadow(getPowerupQueue().get(3).getValue() == 0 ? "" : textSpliterDummy, absoluteX + widthBasic + widthTitleDummy, absoluteY + fontRenderer.FONT_HEIGHT * 2, 0xFFFFFF);
        fontRenderer.drawStringWithShadow(getPowerupQueue().get(3).getValue() == 0 ? "" : text31, absoluteX + widthBasic + widthTitleDummy + widthSpliterDummy, absoluteY + fontRenderer.FONT_HEIGHT * 2, getPowerupCountDownColor(getPowerupQueue().get(3).getKey()));

        fontRenderer.drawStringWithShadow(getPowerupQueue().get(2).getValue() == 0 ? "" : getPowerupTitleName(getPowerupQueue().get(2).getKey()), absoluteX + widthBasic, absoluteY + fontRenderer.FONT_HEIGHT * 3, getPowerupTitleColor(getPowerupQueue().get(2).getKey()));
        fontRenderer.drawStringWithShadow(getPowerupQueue().get(2).getValue() == 0 ? "" : textSpliterDummy, absoluteX + widthBasic + widthTitleDummy, absoluteY + fontRenderer.FONT_HEIGHT * 3, 0xFFFFFF);
        fontRenderer.drawStringWithShadow(getPowerupQueue().get(2).getValue() == 0 ? "" : text21, absoluteX + widthBasic + widthTitleDummy + widthSpliterDummy, absoluteY + fontRenderer.FONT_HEIGHT * 3, getPowerupCountDownColor(getPowerupQueue().get(2).getKey()));

        fontRenderer.drawStringWithShadow(getPowerupQueue().get(1).getValue() == 0 ? "" : getPowerupTitleName(getPowerupQueue().get(1).getKey()), absoluteX + widthBasic, absoluteY + fontRenderer.FONT_HEIGHT * 4, getPowerupTitleColor(getPowerupQueue().get(1).getKey()));
        fontRenderer.drawStringWithShadow(getPowerupQueue().get(1).getValue() == 0 ? "" : textSpliterDummy, absoluteX + widthBasic + widthTitleDummy, absoluteY + fontRenderer.FONT_HEIGHT * 4, 0xFFFFFF);
        fontRenderer.drawStringWithShadow(getPowerupQueue().get(1).getValue() == 0 ? "" : text11, absoluteX + widthBasic + widthTitleDummy + widthSpliterDummy, absoluteY + fontRenderer.FONT_HEIGHT * 4, getPowerupCountDownColor(getPowerupQueue().get(1).getKey()));

        fontRenderer.drawStringWithShadow(getPowerupQueue().get(0).getValue() == 0 ? "" : getPowerupTitleName(getPowerupQueue().get(0).getKey()), absoluteX + widthBasic, absoluteY + fontRenderer.FONT_HEIGHT * 5, getPowerupTitleColor(getPowerupQueue().get(0).getKey()));
        fontRenderer.drawStringWithShadow(getPowerupQueue().get(0).getValue() == 0 ? "" : textSpliterDummy, absoluteX + widthBasic + widthTitleDummy, absoluteY + fontRenderer.FONT_HEIGHT * 5, 0xFFFFFF);
        fontRenderer.drawStringWithShadow(getPowerupQueue().get(0).getValue() == 0 ? "" : text01, absoluteX + widthBasic + widthTitleDummy + widthSpliterDummy, absoluteY + fontRenderer.FONT_HEIGHT * 5, getPowerupCountDownColor(getPowerupQueue().get(0).getKey()));

    }

    private List<Map.Entry<String,Integer>> getPowerupQueue(){
        List<Map.Entry<String, Integer>> list = new ArrayList<>(powerupHashMap.entrySet());
        Collections.sort(list, new ValueComparator());
        return list;
    }

    private int getPowerupCountDownColor(String powerup){
        if(powerupHashMap.get(powerup) == -1){
            return 0xFF6666;
        }else{
            return 0x99CCFF;
        }
    }
    private int getPowerupTitleColor(String powerup){
        if(powerupHashMap.get(powerup) >= 0 && powerupHashMap.get(powerup)/20 <= 10 && (powerupHashMap.get(powerup)/20) % 2 == 0){
            return 0xFFFFFF;
        }else if(powerup.contains("ins")){
            return 0xFF5555;
        }else if(powerup.contains("max")){
            return 0x5555FF;
        }else if(powerup.contains("dg")){
            return 0xFFAA00;
        }else if(powerup.contains("ss")){
            return 0xAA00AA;
        }else if(powerup.contains("bg")){
            return 0xFFFF55;
        }else if(powerup.contains("car")){
            return 0x000099;
        }
        return 0x000000;
    }

    private String getPowerupTitleName(String powerup){
        if(powerup.contains("ins")){
            return "INSTA KILL";
        }else if(powerup.contains("max")){
            return "MAKS AMMO";
        }else if(powerup.contains("dg")){
            return "DOUBO GOLD";
        }else if(powerup.contains("ss")){
            return "SHOP SPREE";
        }else if(powerup.contains("car")){
            return "CARPENTER";
        }else if(powerup.contains("bg")){
            return "BONUS GOLD";
        }
        else{
            return "";
        }
    }

    public void iniPowerupAlert(){
        if(powerupHashMap.get("ins") == -1){
            powerupHashMap.put("ins", 0);
        }else if(powerupHashMap.get("ss") == -1){
            powerupHashMap.put("ss", 0);
        }else if(powerupHashMap.get("max") == -1){
            powerupHashMap.put("max", 0);
        }
    }

    public void iniPowerupPatterns(){
        insRound = 0;
        maxRound = 0;
        ssRound = 0;
    }

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        String message = event.message.getUnformattedText().replaceAll(emojiRegex, "").replaceAll(colorRegex, "");
        if(message.contains(":")) { return; }
        if(message.contains("Fight with your teammates against oncoming")){ ShowSpawnTime.getInstance().getPowerUpDetect().iniPowerupPatterns(); }
        if(!isAllLegit()) { return; }
        if(!ShowSpawnTime.PowerupAlertToggle){ return;}
        if (isAllLegit()){
            if(!message.contains("activated") && !message.contains("激活") && !message.contains("啟用")) { return; }
            if(message.contains("Insta Kill") || message.contains("瞬间") || message.contains("一擊")){
                instaPicked = true;
                if(insRound == 0 && parseSidebar() == 2){
                    insRound = 2;
                }else if(insRound == 0 && parseSidebar() == 3 && gameTick <= 9999){
                    insRound = 2;
                }else if(insRound == 0 && parseSidebar() == 3 && gameTick > 9999){
                    insRound = 3;
                }else if(parseSidebar() == 4 && insRound == 0){
                    insRound = 3;
                }
            }else if(message.contains("Double Gold") || message.contains("双倍") || message.contains("雙倍")){
                dgPicked = true;
            }else if(message.contains("Max Ammo") || message.contains("满载") || message.contains("彈藥")){
                maxPicked = true;
                if(maxRound == 0 && parseSidebar() == 2){
                    maxRound = 2;
                }else if(maxRound == 0 && parseSidebar() == 3 && gameTick <= 9999){
                    maxRound = 2;
                }else if(maxRound == 0 && gameTick > 9999 && parseSidebar() == 3){
                    maxRound = 3;
                }else if(parseSidebar() == 4 && maxRound == 0){
                    maxRound = 3;
                }
            }else if(message.contains("Shopping Spree") || message.contains("狂潮")){
                ssPicked = true;
                if(ssRound == 0 && parseSidebar() == 5){
                    ssRound = 5;
                }else if(ssRound == 0 && parseSidebar() == 6 && gameTick <= 9999){
                    ssRound = 5;
                }else if(ssRound == 0 && parseSidebar() == 6 && gameTick > 9999){
                    ssRound = 6;
                }else if(ssRound == 0 && parseSidebar() == 6){
                    ssRound = 6;
                }else if(ssRound == 0 && parseSidebar() == 7 && gameTick <= 9999){
                    ssRound = 6;
                }else if(ssRound == 0 && parseSidebar() == 7 && gameTick > 9999){
                    ssRound = 7;
                }else if(ssRound == 0 && parseSidebar() == 8){
                    ssRound = 7;
                }
            }else if(message.contains("Carpenter") || message.contains("木匠")){
                carPicked = true;
            }else if(message.contains("Bonus Gold") || message.contains("金钱加成") || message.contains("額外")){
                bgPicked = true;
            }
        }
    }
}
