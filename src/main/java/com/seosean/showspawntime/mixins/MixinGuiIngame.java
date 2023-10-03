package com.seosean.showspawntime.mixins;

import com.seosean.showspawntime.ShowSpawnTime;
import com.seosean.showspawntime.utils.LeftNotice;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import static com.seosean.showspawntime.mapFile.Rounds.colorRegex;
import static com.seosean.showspawntime.mapFile.Rounds.emojiRegex;
import static com.seosean.showspawntime.mapFile.Rounds.parseSidebar;
import static com.seosean.showspawntime.utils.ParseModes.diff;
import static com.seosean.showspawntime.utils.SplitsTimer.recordedRound;
import static com.seosean.showspawntime.utils.Utils.*;

@Mixin(GuiIngame.class)
public abstract class MixinGuiIngame {

    private static int[] r2MaxRoundsDE = {8, 12, 16, 21, 26};
    private static int[] r2MaxRoundsBB = {5, 8, 12, 16, 21, 26};
    private static int[] r3MaxRoundsDEBB = {6, 9, 13, 17, 22, 27};

    private static int[] r2MaxRoundsAA = {5, 8, 12, 16, 21, 26, 31, 36, 41, 46, 51, 61, 66, 71, 76, 81, 86, 91, 96};
    private static int[] r3MaxRoundsAA = {6, 9, 13, 17, 22, 27, 32, 37, 42, 47, 52, 62, 67, 72, 77, 82, 87, 92, 97};

    private static int[] r2InsRoundsDE = {8, 11, 14, 17, 23};
    private static int[] r2InsRoundsBB = {5, 8, 11, 14, 17, 23};
    private static int[] r3InsRoundsDEBB = {6, 9, 12, 18, 21, 24};

    private static int[] r2InsRoundsAA = {5, 8, 11, 14, 17, 20, 23};
    private static int[] r3InsRoundsAA = {6, 9, 12, 15, 18, 21};

    private static int[] r5SSRoundsAA = {15, 45, 55, 65, 75, 85, 95, 105};
    private static int[] r6SSRoundsAA = {16, 26, 36, 46, 66, 76, 86, 96};
    private static int[] r7SSRoundsAA = {17, 27, 37, 47, 67, 77, 87, 97};

    @Shadow public abstract FontRenderer getFontRenderer();

    @Shadow protected int lastPlayerHealth;

    @ModifyArg(method = "renderScoreboard", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;drawString(Ljava/lang/String;III)I", ordinal = 0), index = 0)
    private String modifyArgumentText(String text) {
        if(ShowSpawnTime.Wave3LeftNotice) {
            if (isAllLegit()) {
                if (text.contains("Zombies Left") || text.contains("剩余僵尸") || text.contains("剩下殭屍數")) {
                    if (isInDE()) {
                        boolean isCleared = Integer.valueOf((text.split("§")[2].replace("a", "").trim())) <= LeftNotice.getDEW3Left(parseSidebar(), diff);
                        return text.concat(EnumChatFormatting.WHITE + " | " + (isCleared ? EnumChatFormatting.GREEN : EnumChatFormatting.RED) + ((LeftNotice.getDEW3Left(parseSidebar(), diff) == 0) ? "" : LeftNotice.getDEW3Left(parseSidebar(), diff)));
                    } else if (isInBB()) {
                        boolean isCleared = Integer.valueOf((text.split("§")[2].replace("a", "").trim())) <= LeftNotice.getBBW3Left(parseSidebar(), diff);
                        return text.concat(EnumChatFormatting.WHITE + " | " + (isCleared ? EnumChatFormatting.GREEN : EnumChatFormatting.RED) + ((LeftNotice.getBBW3Left(parseSidebar(), diff) == 0) ? "" : LeftNotice.getBBW3Left(parseSidebar(), diff)));
                    }
                }
            }
        }
        if(ShowSpawnTime.PlayerHealthNotice) {
            if(isAllLegit()) {
                if (text.contains("§") && (text.contains(":") || text.contains("："))) {
                    String regex = "(?:[\uD83C\uDF00-\uD83D\uDDFF]|[\uD83E\uDD00-\uD83E\uDDFF]|[\uD83D\uDE00-\uD83D\uDE4F]|[\uD83D\uDE80-\uD83D\uDEFF]|[\u2600-\u26FF]\uFE0F?|[\u2700-\u27BF]\uFE0F?|\u24C2\uFE0F?|[\uD83C\uDDE6-\uD83C\uDDFF]{1,2}|[\uD83C\uDD70\uD83C\uDD71\uD83C\uDD7E\uD83C\uDD7F\uD83C\uDD8E\uD83C\uDD91-\uD83C\uDD9A]\uFE0F?|[\u0023\u002A\u0030-\u0039]\uFE0F?\u20E3|[\u2194-\u2199\u21A9-\u21AA]\uFE0F?|[\u2B05-\u2B07\u2B1B\u2B1C\u2B50\u2B55]\uFE0F?|[\u2934\u2935]\uFE0F?|[\u3030\u303D]\uFE0F?|[\u3297\u3299]\uFE0F?|[\uD83C\uDE01\uD83C\uDE02\uD83C\uDE1A\uD83C\uDE2F\uD83C\uDE32-\uD83C\uDE3A\uD83C\uDE50\uD83C\uDE51]\uFE0F?|[\u203C\u2049]\uFE0F?|[\u25AA\u25AB\u25B6\u25C0\u25FB-\u25FE]\uFE0F?|[\u00A9\u00AE]\uFE0F?|[\u2122\u2139]\uFE0F?|\uD83C\uDC04\uFE0F?|\uD83C\uDCCF\uFE0F?|[\u231A\u231B\u2328\u23CF\u23E9-\u23F3\u23F8-\u23FA]\uFE0F?)";
                    String colorRegex = "§[a-zA-Z0-9]";
                    String playerName = "";
                    if(text.contains(":")) {
                        playerName = text.split(":")[0].replaceAll(regex, "").replaceAll(colorRegex, "").trim();
                    }else if(text.contains("：")){
                        playerName = text.split("：")[0].replaceAll(regex, "").replaceAll(colorRegex, "").trim();
                    }
                    if (playerName.length() >= 2) {
                        if (getPlayerEntity(playerName) != null) {
                            String trippedText = text.replaceAll(regex, "").replaceAll(colorRegex, "").trim();
                            if (trippedText.contains(": REVIVE") || trippedText.contains(": QUIT") || trippedText.contains(": DEAD") || trippedText.contains("： 等待救援") || trippedText.contains("： 已退出") || trippedText.contains("： 已死亡") || trippedText.contains("： 等待復活") || trippedText.contains("： 已退出") || trippedText.contains("： 已死亡")) {
                                return text;
                            }
                            float health = getPlayerEntity(playerName).getHealth();
                            return EnumChatFormatting.WHITE + "(" + getColor(getPlayerEntity(playerName)) + (int) health + EnumChatFormatting.WHITE + ")" + " " + text;
                        }
                    }
                }
            }
        }
        return text;
    }

    //Contents
    @ModifyArg(method = "renderScoreboard", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;drawString(Ljava/lang/String;III)I", ordinal = 0), index = 1)
    private int modifyArgumentWidth0(int l1){
        if(ShowSpawnTime.PlayerHealthNotice || ShowSpawnTime.Wave3LeftNotice) {
            if (isAllLegit()) {
                String text = "(00)";
                return l1 - getFontRenderer().getStringWidth(text);
            }
        }
        return l1;
    }

    //Title
    @ModifyArg(method = "renderScoreboard", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;drawString(Ljava/lang/String;III)I", ordinal = 2), index = 1)
    private int modifyArgumentWidth1(int l1){
        if(ShowSpawnTime.PlayerHealthNotice || ShowSpawnTime.Wave3LeftNotice) {
            if (isAllLegit()) {
                String text = "(00)";
                return l1 - getFontRenderer().getStringWidth(text) / 2;
            }
        }
        return l1;
    }

    //Contents GUI
    @ModifyArg(method = "renderScoreboard", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiIngame;drawRect(IIIII)V", ordinal = 0), index = 0)
    private int modifyArgumentLeft0(int l1){
        if(ShowSpawnTime.PlayerHealthNotice || ShowSpawnTime.Wave3LeftNotice) {
            if (isAllLegit()) {
                String text = "(00)";
                return l1 - getFontRenderer().getStringWidth(text);
            }
        }
        return l1;
    }

    //Title GUI
    @ModifyArg(method = "renderScoreboard", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiIngame;drawRect(IIIII)V", ordinal = 1), index = 0)
    private int modifyArgumentLeft1(int l1){
        if(ShowSpawnTime.PlayerHealthNotice || ShowSpawnTime.Wave3LeftNotice) {
            if (isAllLegit()) {
                String text = "(00)";
                return l1 - getFontRenderer().getStringWidth(text);
            }
        }return l1;
    }

    //Bottom GUI
    @ModifyArg(method = "renderScoreboard", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiIngame;drawRect(IIIII)V", ordinal = 2), index = 0)
    private int modifyArgumentLeft2(int l1){
        if(ShowSpawnTime.PlayerHealthNotice || ShowSpawnTime.Wave3LeftNotice) {
            if (isAllLegit()) {
                String text = "(00)";
                return l1 - getFontRenderer().getStringWidth(text);
            }
        }
        return l1;
    }

//    private List<EntityPlayer> getPlayerList(){
//        List<EntityPlayer> playerList = new ArrayList<>();
//        playerList.addAll(Minecraft.getMinecraft().theWorld.loadedEntityList.stream()
//                  .filter(entity -> entity instanceof EntityPlayer)
//                  .map(entity -> (EntityPlayer)entity).collect(Collectors.toList()));
//        return playerList;
//    }

    private List<EntityPlayer> getPlayerList() {
        return new CopyOnWriteArrayList<>(Minecraft.getMinecraft().theWorld.loadedEntityList.stream()
                .filter(entity -> entity instanceof EntityPlayer)
                .map(entity -> (EntityPlayer) entity)
                .collect(Collectors.toList()));
    }

    private EntityPlayer getPlayerEntity(String name){
        List<EntityPlayer> playerList = this.getPlayerList();
        for(EntityPlayer entity: playerList){
            String entityName = entity.getDisplayNameString().trim();
            if(name.equals(entityName.trim())){
                return entity;
            }
        }
        return null;
    }

    private EnumChatFormatting getColor(EntityPlayer entityPlayer){
        int currentHealth = (int)entityPlayer.getHealth();
        if(currentHealth > entityPlayer.getMaxHealth() / 2){
            return EnumChatFormatting.GREEN;
        }else if(currentHealth > entityPlayer.getMaxHealth() / 4){
            return EnumChatFormatting.YELLOW;
        }else return EnumChatFormatting.RED;
    }

    @Inject(method = "displayTitle", at = @At(value = "RETURN"))
    private void displayTitle(String p_175178_1_, String p_175178_2_, int p_175178_3_, int p_175178_4_, int p_175178_5_, CallbackInfo callbackInfo){
        this.detectTitle(p_175178_1_, p_175178_2_);
    }

    private void detectTitle(String title, String subTitle){
        boolean record = false;
        int[] skipRound;

        if(!isAllLegit()){
            return;
        }
        if(title.isEmpty()){
            return;
        }
        String titleText = title.replaceAll(emojiRegex, "").replaceAll(colorRegex, "");
        if (!titleText.contains("Round ") && !titleText.contains("回合")) {
            return;
        }
        if (isInAA()){
            for (int i = 105; i > 0; i--) {
                if (titleText.contains(String.valueOf(i))) {
                    if(ShowSpawnTime.getInstance().getPowerUpDetect().powerupHashMap.values().contains(-1)){
                        for(Map.Entry entry : ShowSpawnTime.getInstance().getPowerUpDetect().powerupHashMap.entrySet()){
                            if((int)entry.getValue() == -1){
                                ShowSpawnTime.getInstance().getPowerUpDetect().powerupHashMap.put((String)entry.getKey(), 0);
                            }
                        }
                    }
                    int[] bossRounds = {25,35,56,57,101};
                    if(ArrayUtils.contains(bossRounds, i)){
                        ShowSpawnTime.getInstance().getPowerUpDetect().iniPowerupMap();
                    }
                    if(i == 1){
                        ShowSpawnTime.getInstance().getPowerUpDetect().iniPowerupPatterns();
                    }else if(i == 105){
                        ShowSpawnTime.getInstance().getPowerUpDetect().iniPowerupPatterns();
                    }
                    if(ShowSpawnTime.PowerupAlertToggle) {
                        if (ShowSpawnTime.getInstance().getPowerUpDetect().ssRound == 5 && ArrayUtils.contains(r5SSRoundsAA, i)) {
                            ShowSpawnTime.getInstance().getPowerUpDetect().powerupHashMap.put("ss", -1);
                        } else if (ShowSpawnTime.getInstance().getPowerUpDetect().ssRound == 6 && ArrayUtils.contains(r6SSRoundsAA, i)) {
                            ShowSpawnTime.getInstance().getPowerUpDetect().powerupHashMap.put("ss", -1);
                        } else if (ShowSpawnTime.getInstance().getPowerUpDetect().ssRound == 7 && ArrayUtils.contains(r7SSRoundsAA, i)) {
                            ShowSpawnTime.getInstance().getPowerUpDetect().powerupHashMap.put("ss", -1);
                        }

                        if (ShowSpawnTime.getInstance().getPowerUpDetect().maxRound == 2 && ArrayUtils.contains(r2MaxRoundsAA, i)) {
                            ShowSpawnTime.getInstance().getPowerUpDetect().powerupHashMap.put("max", -1);
                        } else if (ShowSpawnTime.getInstance().getPowerUpDetect().maxRound == 3 && ArrayUtils.contains(r3MaxRoundsAA, i)) {
                            ShowSpawnTime.getInstance().getPowerUpDetect().powerupHashMap.put("max", -1);
                        }

                        if (ShowSpawnTime.getInstance().getPowerUpDetect().insRound == 2 && ArrayUtils.contains(r2InsRoundsAA, i)) {
                            ShowSpawnTime.getInstance().getPowerUpDetect().powerupHashMap.put("ins", -1);
                        } else if (ShowSpawnTime.getInstance().getPowerUpDetect().insRound == 3 && ArrayUtils.contains(r3InsRoundsAA, i)) {
                            ShowSpawnTime.getInstance().getPowerUpDetect().powerupHashMap.put("ins", -1);
                        }
                    }
                    break;
                }
            }
        } else if(isInDEBB()) {
            for (int i = 30; i > 0; i--) {
                if (titleText.contains(String.valueOf(i))) {
                    if(ShowSpawnTime.getInstance().getPowerUpDetect().powerupHashMap.values().contains(-1)){
                        for(Map.Entry entry : ShowSpawnTime.getInstance().getPowerUpDetect().powerupHashMap.entrySet()){
                            if((int)entry.getValue() == -1){
                                ShowSpawnTime.getInstance().getPowerUpDetect().powerupHashMap.put((String)entry.getKey(), 0);
                            }
                        }
                    }
                    int[] bossRoundsDE = {5, 10, 15, 20, 25, 30};
                    int[] bossRoundsBB = {10, 15, 20, 25, 30};
                    if (ArrayUtils.contains(bossRoundsDE, i) && isInDE()) {
                        ShowSpawnTime.getInstance().getPowerUpDetect().iniPowerupMap();
                    } else if (ArrayUtils.contains(bossRoundsBB, i) && isInBB()) {
                        ShowSpawnTime.getInstance().getPowerUpDetect().iniPowerupMap();
                    }

                    if(i == 1){
                        ShowSpawnTime.getInstance().getPowerUpDetect().iniPowerupPatterns();
                    }else if(i == 30){
                        ShowSpawnTime.getInstance().getPowerUpDetect().iniPowerupPatterns();
                    }

                    if(ShowSpawnTime.PowerupAlertToggle) {
                        if (ShowSpawnTime.getInstance().getPowerUpDetect().maxRound == 2) {
                            if (ArrayUtils.contains(r2MaxRoundsDE, i) && isInDE()) {
                                ShowSpawnTime.getInstance().getPowerUpDetect().powerupHashMap.put("max", -1);
                            } else if (ArrayUtils.contains(r2MaxRoundsBB, i) && isInBB()) {
                                ShowSpawnTime.getInstance().getPowerUpDetect().powerupHashMap.put("max", -1);
                            }
                        } else if (ShowSpawnTime.getInstance().getPowerUpDetect().maxRound == 3 && ArrayUtils.contains(r3MaxRoundsDEBB, i)) {
                            ShowSpawnTime.getInstance().getPowerUpDetect().powerupHashMap.put("max", -1);
                        }

                        if (ShowSpawnTime.getInstance().getPowerUpDetect().insRound == 2) {
                            if (ArrayUtils.contains(r2InsRoundsDE, i) && isInDE()) {
                                ShowSpawnTime.getInstance().getPowerUpDetect().powerupHashMap.put("ins", -1);
                            } else if (ArrayUtils.contains(r2InsRoundsBB, i) && isInBB()) {
                                ShowSpawnTime.getInstance().getPowerUpDetect().powerupHashMap.put("ins", -1);
                            }
                        } else if (ShowSpawnTime.getInstance().getPowerUpDetect().insRound == 3 && ArrayUtils.contains(r3InsRoundsDEBB, i)) {
                            ShowSpawnTime.getInstance().getPowerUpDetect().powerupHashMap.put("ins", -1);
                        }
                    }
                    break;
                }
            }
        }
        if (isInAA()) {
            if (ShowSpawnTime.AARoundsRecordToggle.contains("OFF")) {
                return;
            }
            for (int i = 105; i > 0; i--) {
                if (titleText.contains("赢") || titleText.contains("贏")) {
                    recordedRound = 106;
                    record = true;
                    break;
                }
                if (titleText.contains(String.valueOf(i))) {
                    recordedRound = i;
                    if (ShowSpawnTime.AARoundsRecordToggle.contains("Quintuple")) {
                        if (i % 5 != 1 && i % 5 != 6) {
                            return;
                        }
                    } else if (ShowSpawnTime.AARoundsRecordToggle.contains("Tenfold")) {
                        if (i % 10 != 1) {
                            return;
                        }
                    }
                    record = true;
                    break;
                }
            }
            skipRound = new int[]{0, 1, 11, 21, 106};
        } else {
            if (ShowSpawnTime.DEBBRoundsRecordToggle.contains("OFF")) {
                return;
            }
            for (int i = 30; i > 0; i--) {
                if (titleText.contains("赢") || titleText.contains("贏")) {
                    recordedRound = 31;
                    record = true;
                    break;
                }
                if (titleText.contains(String.valueOf(i))) {
                    recordedRound = i;
                    if (ShowSpawnTime.DEBBRoundsRecordToggle.contains("Quintuple")) {
                        if (i % 5 != 1 && i % 5 != 6) {
                            return;
                        }
                    } else if (ShowSpawnTime.DEBBRoundsRecordToggle.contains("Tenfold")) {
                        if (i % 10 != 1) {
                            return;
                        }
                    }
                    record = true;
                    break;
                }
            }

            skipRound = new int[]{0, 1, 11, 21, 31};
        }

        if (record && !ArrayUtils.contains(skipRound, recordedRound)) {
            try {
                if(isOtherLanguages()){
                    String time = getScoreboardContents(11);
                    String[] splited;
                    if(time.contains("：")){
                        splited = time.split("：");
                    }else{
                        splited = time.split(" ");
                    }
                    IChatComponent crossBar = new ChatComponentText(EnumChatFormatting.GREEN.toString() + EnumChatFormatting.BOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬ ");
                    IChatComponent timing = new ChatComponentText(EnumChatFormatting.YELLOW + "                     You completed " + EnumChatFormatting.RED + "Round " + (recordedRound - 1) + EnumChatFormatting.YELLOW + " in " + EnumChatFormatting.GREEN + splited[1] + EnumChatFormatting.YELLOW + "!");
                    IChatComponent copy = new ChatComponentText(EnumChatFormatting.GREEN + "Copy");
                    timing.setChatStyle(new ChatStyle().setChatHoverEvent(new HoverEvent(net.minecraft.event.HoverEvent.Action.SHOW_TEXT, copy)).setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sst copy " + "You completed Round " + (recordedRound - 1) + " in " + splited[1].replace("§a", "").replace("\uD83D\uDC79", "") + "!")));
                    Minecraft.getMinecraft().thePlayer.addChatComponentMessage(crossBar);
                    Minecraft.getMinecraft().thePlayer.addChatComponentMessage(timing);
                    Minecraft.getMinecraft().thePlayer.addChatComponentMessage(crossBar);
                }else if(isSimplifiedChinese()){
                    String time = getScoreboardContents(11);
                    String[] splited;
                    if(time.contains("：")){
                        splited = time.split("：");
                    }else{
                        splited = time.split(" ");
                    }
                    IChatComponent crossBar = new ChatComponentText(EnumChatFormatting.GREEN.toString() + EnumChatFormatting.BOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬ ");
                    IChatComponent timing = new ChatComponentText(EnumChatFormatting.YELLOW + "                     你用时" + EnumChatFormatting.GREEN + splited[1] + EnumChatFormatting.YELLOW + "完成了" + EnumChatFormatting.RED + (recordedRound - 1) + EnumChatFormatting.YELLOW + "个回合！");
                    IChatComponent copy = new ChatComponentText(EnumChatFormatting.GREEN + "复制");
                    timing.setChatStyle(new ChatStyle().setChatHoverEvent(new HoverEvent(net.minecraft.event.HoverEvent.Action.SHOW_TEXT, copy)).setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sst copy " + "你用时" + splited[1].replace("§a", "").replace("\uD83D\uDC79", "") + "完成了" + (recordedRound - 1) + "个回合！")));
                    Minecraft.getMinecraft().thePlayer.addChatComponentMessage(crossBar);
                    Minecraft.getMinecraft().thePlayer.addChatComponentMessage(timing);
                    Minecraft.getMinecraft().thePlayer.addChatComponentMessage(crossBar);
                }else if(isTraditionalChinese()){
                    String time = getScoreboardContents(11);
                    String[] splited;
                    if(time.contains("：")){
                        splited = time.split("：");
                    }else{
                        splited = time.split(" ");
                    }
                    IChatComponent crossBar = new ChatComponentText(EnumChatFormatting.GREEN.toString() + EnumChatFormatting.BOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬ ");
                    IChatComponent timing = new ChatComponentText(EnumChatFormatting.YELLOW + "                     你用時了 " + EnumChatFormatting.GREEN + splited[1] + EnumChatFormatting.YELLOW + " 完成了 " + EnumChatFormatting.RED + (recordedRound - 1) + EnumChatFormatting.YELLOW + " 回合！");
                    IChatComponent copy = new ChatComponentText(EnumChatFormatting.GREEN + "複製");
                    timing.setChatStyle(new ChatStyle().setChatHoverEvent(new HoverEvent(net.minecraft.event.HoverEvent.Action.SHOW_TEXT, copy)).setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sst copy " + "你用時了 " + splited[1].replace("§a", "").replace("\uD83D\uDC79", "") + " 完成了 " + (recordedRound - 1) + " 回合！")));
                    Minecraft.getMinecraft().thePlayer.addChatComponentMessage(crossBar);
                    Minecraft.getMinecraft().thePlayer.addChatComponentMessage(timing);
                    Minecraft.getMinecraft().thePlayer.addChatComponentMessage(crossBar);
                }
            } catch (Exception e) {
                IChatComponent warning = new ChatComponentText(EnumChatFormatting.RED + "CANNOT RECORD THE TIME, PLEASE REPORT THIS TO Seosean");
                Minecraft.getMinecraft().thePlayer.addChatComponentMessage(warning);
            }
        }
    }
}
