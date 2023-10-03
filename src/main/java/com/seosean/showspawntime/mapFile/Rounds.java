package com.seosean.showspawntime.mapFile;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.seosean.showspawntime.ShowSpawnTime;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.scoreboard.*;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.seosean.showspawntime.ShowSpawnTime.PlayDEBBSound;
import static com.seosean.showspawntime.utils.Utils.*;


public class Rounds {

    public Rounds() {
        this.fontRenderer = minecraft.fontRendererObj;
    }

    private InternalTimer internalTimer = ShowSpawnTime.getInstance().getInternalTimer();


    @SubscribeEvent
    public void Round(RenderGameOverlayEvent.Post event1) {
        if (event1.type != RenderGameOverlayEvent.ElementType.TEXT) {
            return;
        }
        if(!isAllLegit())
            return;
        if(isInDEBB()){
            ScaledResolution scaledResolution = new ScaledResolution(minecraft);
            int widthW = fontRenderer.getStringWidth("➤ ");
            int widthString = fontRenderer.getStringWidth("➤ W3 00:00");
            float widthNoWave3 = widthString - fontRenderer.getStringWidth("NO WAVE 3");
            float absoluteX = (float)ShowSpawnTime.getInstance().getXSpawnTime() * (float)scaledResolution.getScaledWidth();
            float absoluteY = (float)ShowSpawnTime.getInstance().getYSpawnTime() * (float)scaledResolution.getScaledHeight();
            if(parseSidebar() != 0){
                fontRenderer.drawStringWithShadow("➤ ", absoluteX, absoluteY + this.fontRenderer.FONT_HEIGHT * height, 0xCC00CC);
            }
            fontRenderer.drawStringWithShadow(Rounds.getW1Time(), absoluteX + widthW, absoluteY + this.fontRenderer.FONT_HEIGHT * 3, getColorW1());
            fontRenderer.drawStringWithShadow(Rounds.getW2Time(parseSidebar()), absoluteX + widthW, absoluteY + this.fontRenderer.FONT_HEIGHT * 4, getColorW2());
            fontRenderer.drawStringWithShadow(Rounds.getW3Time(parseSidebar()), absoluteX + (Rounds.getW3Time(parseSidebar()).equals("NO WAVE 3") ? widthNoWave3 : widthW), absoluteY + this.fontRenderer.FONT_HEIGHT * 5, getColorW3());
            getSoundW3Approach();
        }
    }

    private int height;
    //默认第一行显色
    private int getColorW1(){
        if(internalTimer.setW1End(false)){
            return 0x5a5a5a;
        }
        height = 3;
        return 0xFFFF00;
    }

    public static boolean playSound = false;

    private void getSoundW1Approach(){
        if(internalTimer.setW1End(true) && !internalTimer.setW2End(true)){
            if(!playSound){
                playSound = true;
                if(PlayDEBBSound){
                    Minecraft.getMinecraft().thePlayer.playSound(ShowSpawnTime.PrecededWave, 2, (float)ShowSpawnTime.PrecededWavePitch);
                }
            }
        }
    }
    //默认第二行不显色
    private int getColorW2(){
        getSoundW1Approach();
        if(internalTimer.setW1End(false) && !internalTimer.setW2End(false)){
            height = 4;
            return 0xFFFF00;
        }
        if(internalTimer.setW1End(false) && internalTimer.setW2End(false)){
            return 0x5a5a5a;
        }
        return 0x808080;
    }

    public static boolean is2WavesRound(){
        if(!isAllLegit()) return false;
        if(isInBB()){
            if(parseSidebar() == 1 || parseSidebar() == 2 || parseSidebar() == 3){
                return true;
            }
        }else if(isInDE()){
            if(parseSidebar() == 1 || parseSidebar() == 2){
                return true;
            }
        }
        return false;
    }
    private void getSoundW2Approach(){
        if(internalTimer.setW2End(true) && !internalTimer.setDEBBW3End()){
            if(playSound){
                playSound = false;
                if(PlayDEBBSound){
                    if(!ShowSpawnTime.getInstance().DEBBCountDown) {
                        Minecraft.getMinecraft().thePlayer.playSound(
                                is2WavesRound() ? ShowSpawnTime.TheLastWave : ShowSpawnTime.PrecededWave,
                                2,
                                is2WavesRound() ? (float) ShowSpawnTime.TheLastWavePitch : (float) ShowSpawnTime.PrecededWavePitch);
                    }else{
                        if(!is2WavesRound()){
                            Minecraft.getMinecraft().thePlayer.playSound(
                                    ShowSpawnTime.PrecededWave,
                                    2,
                                    (float) ShowSpawnTime.PrecededWavePitch);
                        }
                    }
                }
            }
        }
    }

    //同上
    private int getColorW3(){
        getSoundW2Approach();
        if(internalTimer.setW2End(false)){
            height = 5;
            return 0xFFFF00;
        }
        return 0x808080;
    }

    private void getSoundW3Approach(){
        if(internalTimer.setDEBBW3End()){
            if(!playSound){
                playSound = true;
                if(PlayDEBBSound){
                    if(!ShowSpawnTime.getInstance().DEBBCountDown){
                        Minecraft.getMinecraft().thePlayer.playSound(ShowSpawnTime.TheLastWave, 2, (float)ShowSpawnTime.TheLastWavePitch);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void getSB(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc == null || mc.theWorld == null || mc.isSingleplayer()) {
            return;
        }

        if (event.phase != TickEvent.Phase.START)
            return;
        EntityPlayerSP p = mc.thePlayer;
        if (p == null)
            return;


        Scoreboard scoreboard = mc.theWorld.getScoreboard();
        ScoreObjective sidebarObjective = scoreboard.getObjectiveInDisplaySlot(1);
        if (sidebarObjective == null) {
            return;
        }
        scoreboardTitle = sidebarObjective.getDisplayName().replaceAll(emojiRegex, "").replaceAll(colorRegex, "").trim();

        scoreboardLines = new ArrayList<>();
        Collection<Score> scores = scoreboard.getSortedScores(sidebarObjective);
        List<Score> filteredScores = new CopyOnWriteArrayList<>(scores.stream()
                .filter(p_apply_1_ -> p_apply_1_.getPlayerName() != null && !p_apply_1_.getPlayerName().startsWith("#"))
                .collect(Collectors.toList()));
        if (filteredScores.size() > 15) {
            scores = Lists.newArrayList(Iterables.skip(filteredScores, scores.size() - 15));
        } else {
            scores = filteredScores;
        }

        Collections.reverse(filteredScores);

        for (Score line : scores) {
            ScorePlayerTeam team = scoreboard.getPlayersTeam(line.getPlayerName());
            String scoreboardLine = ScorePlayerTeam.formatPlayerName(team, line.getPlayerName()).trim();
            scoreboardLines.add(scoreboardLine.replaceAll(emojiRegex, "").replaceAll(colorRegex, "").trim());
        }
    }




    //从计分板信息获取回合数
    public static int parseSidebar() {
        int Round = 0;

        if (!isAllLegit()) {
            return 0;
        }
        String roundText = getScoreboardContents(2);

        if(isInAA()){
            for (int r = 105; r > 0; r--) {
                if (roundText.contains(String.valueOf(r))) {
                    Round = r;
                    return Round;
                }
            }
        }


        if (isInDEBB()) {
            for (int r = 30; r > 0; r--) {
                if (roundText.contains(String.valueOf(r))) {
                    Round = r;
                    break;
                }
            }
        }
        return Round;
    }


    //获取第一回合数据
    public static String getW1Time(){
        if (isAllLegit()) {
            if (parseSidebar() != 0) {
                    return "W1 00:10";
            }
        }
        return "";
    }

    private static String getW2Time(int round){
        if (isAllLegit()) {
            if(isInDE()){
                return MapChoose.mapDeadEndW2(round);
            }
            else if (isInBB())  {
                return MapChoose.mapBadBloodW2(round);
            }
        }
        return "";
    }

    private static String getW3Time(int round){
        if (isAllLegit()) {
            if(isInDE()){
                return MapChoose.mapDeadEndW3(round);
            }
            if(isInBB()){
                return MapChoose.mapBadBloodW3(round);
            }
        }
        return "";
    }

    public static List<String> scoreboardLines;

    public static String scoreboardTitle;

    private final FontRenderer fontRenderer;

    public static final Minecraft minecraft = Minecraft.getMinecraft();

    private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)§[0-9A-Fa-fK-Ok-oRrZz]");

    public static final String emojiRegex = "(?:[\uD83C\uDF00-\uD83D\uDDFF]|[\uD83E\uDD00-\uD83E\uDDFF]|[\uD83D\uDE00-\uD83D\uDE4F]|[\uD83D\uDE80-\uD83D\uDEFF]|[\u2600-\u26FF]\uFE0F?|[\u2700-\u27BF]\uFE0F?|\u24C2\uFE0F?|[\uD83C\uDDE6-\uD83C\uDDFF]{1,2}|[\uD83C\uDD70\uD83C\uDD71\uD83C\uDD7E\uD83C\uDD7F\uD83C\uDD8E\uD83C\uDD91-\uD83C\uDD9A]\uFE0F?|[\u0023\u002A\u0030-\u0039]\uFE0F?\u20E3|[\u2194-\u2199\u21A9-\u21AA]\uFE0F?|[\u2B05-\u2B07\u2B1B\u2B1C\u2B50\u2B55]\uFE0F?|[\u2934\u2935]\uFE0F?|[\u3030\u303D]\uFE0F?|[\u3297\u3299]\uFE0F?|[\uD83C\uDE01\uD83C\uDE02\uD83C\uDE1A\uD83C\uDE2F\uD83C\uDE32-\uD83C\uDE3A\uD83C\uDE50\uD83C\uDE51]\uFE0F?|[\u203C\u2049]\uFE0F?|[\u25AA\u25AB\u25B6\u25C0\u25FB-\u25FE]\uFE0F?|[\u00A9\u00AE]\uFE0F?|[\u2122\u2139]\uFE0F?|\uD83C\uDC04\uFE0F?|\uD83C\uDCCF\uFE0F?|[\u231A\u231B\u2328\u23CF\u23E9-\u23F3\u23F8-\u23FA]\uFE0F?)";

    public static final String colorRegex = "§[a-zA-Z0-9]";

    public static String stripColor(final String input) {
        return STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
    }
}
