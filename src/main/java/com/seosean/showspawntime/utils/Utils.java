package com.seosean.showspawntime.utils;

import com.seosean.showspawntime.mapFile.Rounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.LanguageManager;

import java.util.List;

public class Utils {
    public static boolean isOtherLanguages(){
        return !isSimplifiedChinese() && !isTraditionalChinese();
    }
    public static boolean isSimplifiedChinese(){
        Minecraft mc = Minecraft.getMinecraft();
        LanguageManager languageManager = mc.getLanguageManager();
        return languageManager.getCurrentLanguage().getLanguageCode().equals("zh_CN");
    }
    public static boolean isTraditionalChinese(){
        Minecraft mc = Minecraft.getMinecraft();
        LanguageManager languageManager = mc.getLanguageManager();
        return languageManager.getCurrentLanguage().getLanguageCode().equals("zh_TW");
    }

    public static boolean isTitleExist(String scoreboardTitle){
        return scoreboardTitle != null;
    }


    public static boolean isScoreBoardLegit(){
        List<String> scoreboardLines = Rounds.scoreboardLines;
        return scoreboardLines != null && scoreboardLines.size() >= 13;
    }

    public static boolean isInZombies(String scoreboardTitle){
        return scoreboardTitle.contains("ZOMBIES") || scoreboardTitle.contains("僵") || scoreboardTitle.contains("殭");
    }

    public static boolean isAllLegit(){
        String scoreboardTitle = Rounds.scoreboardTitle;
        return isTitleExist(scoreboardTitle) && isInZombies(scoreboardTitle) && isScoreBoardLegit();
    }

    private static String isInWhatMap(){
        try{
            return getScoreboardContents(12);
        }catch(Exception e){
            return "";
        }
    }

    public static boolean isInDEBB(){
        String theMap = isInWhatMap();
        return theMap.contains("Dead End") || theMap.contains("Bad Blood") || theMap.contains("穷") || theMap.contains("坏")|| theMap.contains("窮") || theMap.contains("壞");
    }

    public static boolean isInDE(){
        String theMap = isInWhatMap();
        return theMap.contains("Dead End")|| theMap.contains("穷")|| theMap.contains("窮");
    }

    public static boolean isInBB(){
        String theMap = isInWhatMap();
        return theMap.contains("Bad Blood") || theMap.contains("坏") || theMap.contains("壞");
    }

    public static boolean isInAA(){
        String theMap = isInWhatMap();
        return theMap.contains("Alien") || theMap.contains("外");
    }

    public static String getScoreboardContents(int line){
        try{
            List<String> scoreboardLines = Rounds.scoreboardLines;
            if(scoreboardLines.size() >= line + 1) {
                return scoreboardLines.get(line);
            }else{
                return "";
            }
        }catch(Exception e){
            return "";
        }
    }
}
