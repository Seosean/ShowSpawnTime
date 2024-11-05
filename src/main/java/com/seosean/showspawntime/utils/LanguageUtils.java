package com.seosean.showspawntime.utils;

import com.seosean.showspawntime.ShowSpawnTime;
import com.seosean.showspawntime.config.LanguageConfiguration;
import com.seosean.showspawntime.handler.LanguageDetector;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LanguageUtils {
//    public static final List<String> ZOMBIES_TITLE = Arrays.asList("ZOMBIES", "僵尸末日", "殭屍末日");
    public static final List<String> ZOMBIES_LEFT = Arrays.asList("Zombies Left", "剩余僵尸", "剩下殭屍數", "Zbývající zombie", "Zombier tilbage", "Zombies over", "Zombeja jäljellä", "Zombies restants", "Zombies übrig", "Ζόμπι που Απομένουν", "Hátralévő Zombik", "Zombi Rimanenti", "残りゾンビ", "남은 좀비", "Zombier igjen", "Zombies Left", "Pozostałe zombi", "Zombies restantes", "Zumbis restantes", "Zombi Rămași", "Осталось зомби", "Zombies Restantes", "Zombier kvar", "Kalan Zombi", "Залишилося зомбі");
//    public static final List<String> ROUND = Arrays.asList("回合", ". kolo", "Runde", "Ronde", "Kierros", "Manche", "Runde", "Γύρος", ". Kör", "Round", "ラウンド", "라운드", "Runde", "Runda", "Horda", "Rodada", "Runda", "Раунд", "Ronda", "Runda", ". Tur", "Раунд");
//    public static final List<String> HAS_SPAWNED_TEXT = Arrays.asList("has spawned!", "已生成！", "Objevil se", "er dukket op!", "is gespawned!", "on syntynyt!", "est apparu!", "ist gespawnt!", "έχει εμφανιστεί", "leidéződött!", "è stato evocato!", "がスポーンしました！", "이(가) 소환되었습니다!", "har blitt fremkalt!", "Pojawił się!", "foi invocado!", "nasceu!", "s-a spawnat!", "появился!", "ha aparecido!", "har framstått!", "canlandı!", "з’явився!");
//    private static final List<String> MAP_THE_LAB = Arrays.asList("The Lab");
//    private static final List<String> MAP_DEAD_END = Arrays.asList("穷途末路", "窮途末路", "Dead End");
//    private static final List<String> MAP_BAD_BLOOD = Arrays.asList("坏血之宫", "壞血宮殿", "Bad Blood");
//    private static final List<String> MAP_ALIEN_ARCADIUM = Arrays.asList("外星游乐园", "外星遊樂園", "Alien Arcadium");
//    private static final List<String> MAPS = Arrays.asList("穷途末路", "窮途末路", "Dead End", "坏血之宫", "壞血宮殿", "Bad Blood", "外星游乐园", "外星遊樂園", "Alien Arcadium", "The Lab");
//    private static final List<String> REVIVE_TEXT = Arrays.asList("REVIVE", "等待復活", "等待救援", "OŽIVIT", "GENOPLIV", "REANIMEREN", "ELVYTÄ", "REANIMER", "WIEDERBELEBBAR", "ΑΝΑΖΩΟΓΟΝΗΣΗ", "ÉLESZTÉS", "RIANIMA", "気絶", "부활", "GJENOPPLIV", "WSKRZEŚ", "СБИТ С НОГ", "REVIVIR", "REÎNVIE", "ÅTERUPPLIVA", "YENIDEN CANLANDIR", "ЗБИТИЙ");
//    private static final List<String> DEAD_TEXT = Arrays.asList("DEAD", "已死亡", "已死亡", "MRTVÝ", "DØD", "DOOD", "KUOLLUT", "MORT", "TOT", "ΝΕΚΡΟΣ", "HALOTT", "MORTO", "死亡", "사망", "DØD", "ZGINĄŁ", "MORTO", "MORTO", "MORT", "МЁРТВ", "MUERTO", "DÖD", "ÖLDÜ", "МЕРТВИЙ");
//    private static final List<String> QUIT_TEXT = Arrays.asList("QUIT", "已退出", "已退出", "ODEJÍT", "FORLADT", "VERLATEN", "POISTU", "PARTI", "VERLASSEN", "ΕΞΟΔΟΣ", "KILÉPETT", "DISC.", "退出", "떠남", "FORLAT", "SURRENDERED", "WYJDŹ", "SAIU", "SAIU", "IEȘI", "ВЫШЕЛ", "SALIÓ", "AVSLUTA", "AYRIL", "ВИЙШОВ");

    public static void resetCache() {
        cacheSidebarContent = "";
        cacheZombiesLeftText = "";
//        cacheMapSideBarContent = "";
//        cacheMap = ZombiesMap.NULL;
    }

    private static String cacheSidebarContent = "";
    private static String cacheZombiesLeftText = "";
    public static String getZombiesLeftText() {

        String sidebarContent = ShowSpawnTime.getScoreboardManager().getContent(4);

        if (sidebarContent == null || sidebarContent.isEmpty()) {
            return "";
        }

        if (cacheSidebarContent.equals(sidebarContent)) {
            return cacheZombiesLeftText;
        }

        if (!sidebarContent.contains(":") && !sidebarContent.contains("：")) {
            return "";
        }

        String zombiesLeftText = StringUtils.trim(sidebarContent.contains(":") ? sidebarContent.split(":")[0] : sidebarContent.split("：")[0]);

        if (ZOMBIES_LEFT.contains(zombiesLeftText)) {
            cacheSidebarContent = sidebarContent;
            return cacheZombiesLeftText = zombiesLeftText;
        }

        return "";
    }
    public static boolean isZombiesLeft(String string) {
        string = StringUtils.trim(string);
        if (string.contains(":")) {
            string = string.split(":")[0];
        } else if (string.contains("：")) {
            string = string.split("：")[0];
        }
        return ZOMBIES_LEFT.contains(string.trim());
    }

    public static boolean isZombiesTitle(String string) {
        string = StringUtils.trim(string);
        return string.contains("ZOMBIES") || string.contains("僵尸末日") || string.contains("殭屍末日");
    }

    public static boolean isRoundTitle(String string) {
        LanguageDetector.detectLanguage();
        string = StringUtils.trim(string);
        return LanguageUtils.contains(string.trim(), "zombies.game.round");
    }

    public static int getRoundNumber(String string) {
        string = StringUtils.trim(string).trim();
        if (LanguageUtils.contains(string.trim(), "zombies.game.round")) {
            return StringUtils.getNumberInString(string);
        }
        return 0;
    }

//    private static String cacheMapSideBarContent = "";
//
//    private static ZombiesMap cacheMap = ZombiesMap.NULL;

    public static ZombiesMap getMap() {
        World world = Minecraft.getMinecraft().theWorld;
        if (world == null) {
            return ZombiesMap.NULL;
        }
        BlockPos blockPos = new BlockPos(0, 72, 12);
        IBlockState blockState = world.getBlockState(blockPos);
        Block block = blockState.getBlock();
        String blockName = block.getUnlocalizedName();
        switch (blockName) {
            case "tile.air":
               return ZombiesMap.THE_LAB;
            case "tile.woolCarpet":
                return ZombiesMap.ALIEN_ARCADIUM;
            case "tile.stonebricksmooth":
                return ZombiesMap.BAD_BLOOD;
            case "tile.cloth":
                return ZombiesMap.DEAD_END;
            case "tile.clayHardenedStained":
                return ZombiesMap.PRISON;
        }
        return ZombiesMap.NULL;
    }

//    @Deprecated
//    public static ZombiesMap getMap() {
//        String mapString = ShowSpawnTime.getScoreboardManager().getContent(ShowSpawnTime.getScoreboardManager().getSize() - 2);
//        if (cacheMapSideBarContent.equals(mapString)) {
//            return cacheMap;
//        }
//
//        Map<String, String> maps = new HashMap<>();
//        maps.put("zombies.map.thelab.lang", LanguageConfiguration.get("zombies.map.thelab"));
//        maps.put("zombies.map.deadend.lang", LanguageConfiguration.get("zombies.map.deadend"));
//        maps.put("zombies.map.badblood.lang", LanguageConfiguration.get("zombies.map.badblood"));
//        maps.put("zombies.map.alienarcadium.lang", LanguageConfiguration.get("zombies.map.alienarcadium"));
//        maps.put("zombies.map.thelab.origin", LanguageConfiguration.getOrigin("zombies.map.thelab"));
//        maps.put("zombies.map.deadend.origin", LanguageConfiguration.getOrigin("zombies.map.deadend"));
//        maps.put("zombies.map.badblood.origin", LanguageConfiguration.getOrigin("zombies.map.badblood"));
//        maps.put("zombies.map.alienarcadium.origin", LanguageConfiguration.getOrigin("zombies.map.alienarcadium"));
//        maps.put("zombies.map.thelab.cache", LanguageConfiguration.getCache("zombies.map.thelab"));
//        maps.put("zombies.map.deadend.cache", LanguageConfiguration.getCache("zombies.map.deadend"));
//        maps.put("zombies.map.badblood.cache", LanguageConfiguration.getCache("zombies.map.badblood"));
//        maps.put("zombies.map.alienarcadium.cache", LanguageConfiguration.getCache("zombies.map.alienarcadium"));
//        String mapName = "";
//        String matchedKey = "";
//
//        for (Map.Entry<String, String> entry : maps.entrySet()) {
//            if (entry.getValue().isEmpty()) {
//                continue;
//            }
//            Pattern pattern = Pattern.compile(entry.getValue());
//            Matcher matcher = pattern.matcher(mapString);
//
//            if (matcher.find()) {
//                mapName = matcher.group();
//                matchedKey = entry.getKey();
//                break;
//            }
//        }
//
//        if (mapName.isEmpty()) {
//            return ZombiesMap.NULL;
//        }
//
//        int lastDotIndex = matchedKey.lastIndexOf(".");
//        String result = matchedKey.substring(0, lastDotIndex);
//
//        switch (result) {
//            case "zombies.map.deadend": cacheMapSideBarContent = mapString; return cacheMap = ZombiesMap.DEAD_END;
//            case "zombies.map.badblood": cacheMapSideBarContent = mapString; return cacheMap = ZombiesMap.BAD_BLOOD;
//            case "zombies.map.alienarcadium": cacheMapSideBarContent = mapString; return cacheMap = ZombiesMap.ALIEN_ARCADIUM;
//            case "zombies.map.thelab": cacheMapSideBarContent = mapString; return cacheMap = ZombiesMap.THE_LAB;
//        }
//
//        return cacheMap = ZombiesMap.NULL;
//    }

    public static boolean equals(String string, String i18nKey) {
        string = StringUtils.trim(string);
        String i18nContent = LanguageConfiguration.get(i18nKey);
        String originalContent = LanguageConfiguration.getOrigin(i18nKey);
        String cacheContent = LanguageConfiguration.getCache(i18nKey);
        return (!i18nContent.isEmpty() && string.equalsIgnoreCase(i18nContent)) || (!originalContent.isEmpty() && string.equalsIgnoreCase(originalContent)) || (!cacheContent.isEmpty() && string.equalsIgnoreCase(cacheContent));
    }

    public static boolean contains(String string, String i18nKey) {
        string = StringUtils.trim(string);
        String i18nContent = LanguageConfiguration.get(i18nKey);
        String originalContent = LanguageConfiguration.getOrigin(i18nKey);
        String cacheContent = LanguageConfiguration.getCache(i18nKey);
        return (!i18nContent.isEmpty() && string.contains(i18nContent)) || (!originalContent.isEmpty() && string.contains(originalContent)) || (!cacheContent.isEmpty() && string.contains(cacheContent));
    }


    public enum ZombiesMap {
        NULL(new int[][]{}, 0),
        PRISON(new int[][]{{10,20},{10,20,30},{10,17,24,31},{10,17,24,31},{10,20,30},{10,20,30},{10,20,30},{10,25,40},{10,25,35},{10,25,45},{10,25,40},{10,25,37},{10,22,34},{10,25,37},{10,25,40},{10,22,37},{10,22,42},{10,25,45},{10,25,45},{10,25,40},{10,20,35,55,75},{10,25,40},{10,30,50},{10,30,50},{10,25,45},{10,30,50},{10,25,45},{10,30,50},{10,30,55},{10}}, 30),
        THE_LAB(new int[][]{{10,22},{10,22},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34}}, 40),
        DEAD_END(new int[][]{{10,20},{10,20},{10,20,35},{10,20,35},{10,22,37},{10,22,44},{10,25,47},{10,25,50},{10,22,38},{10,24,45},{10,25,48},{10,25,50},{10,25,50},{10,25,45},{10,25,46},{10,24,47},{10,24,47},{10,24,47},{10,24,47},{10,24,49},{10,23,44},{10,23,45},{10,23,42},{10,23,43},{10,23,43},{10,23,36},{10,24,44},{10,24,42},{10,24,42},{10,24,45}}, 30),
        BAD_BLOOD(new int[][]{{10,22},{10,22},{10,22},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,24,38},{10,24,38},{10,22,34},{10,24,38},{10,22,34}}, 30),
        ALIEN_ARCADIUM(new int[][]{{10,13,16,19},{10,14,18,22},{10,13,16,19},{10,14,17,21,25,28},{10,14,18,22,26,30},{10,14,19,23,28,32},{10,15,19,23,27,31},{10,15,20,25,30,35},{10,14,19,23,28,32},{10,16,22,27,33,38},{10,16,21,27,32,38},{10,16,22,28,34,40},{10,16,22,28,34,40},{10,16,21,26,31,36},{10,17,24,31,38,46},{10,16,22,27,33,38},{10,14,19,23,28,32},{10,14,19,23,28,32},{10,14,18,22,26,30},{10,15,21,26,31,36},{10,14,19,23,28,32},{10,14,19,23,28,34},{10,14,18,22,26,30},{10,14,19,23,28,32},{10},{10,23,36},{10,22,34},{10,20,30},{10,24,38},{10,22,34},{10,22,34},{10,21,32},{10,22,34},{10,22,34},{10},{10,22,34},{10,20,31},{10,22,34},{10,22,34},{10,22,34,37,45},{10,21,32},{10,22,34},{10,13,22,25,34,37},{10,22,34},{10,22,34,35},{10,21,32,35},{10,20,30},{10,20,30,33},{10,21,32},{10,22,34,37},{10,20,30,33},{10,22,34,37},{10,22,34,37},{10,20,32,35,39},{10,16,22,28,34,40},{10,14,18},{10,14,18},{10,22,34,37,38},{10,14,18,22,26,30},{10,20,30,33},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,27,32},{10,14,18,22,27,32},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{5},{5},{5},{5},{5}}, 105);

        private final int[][] timer;
        private final int maxRound;
        ZombiesMap(int[][] timer, int maxRound) {
            this.timer = timer;
            this.maxRound = maxRound;
        }

        public int[][] getTimer() {
            if (timer == null) {
                return new int[][]{};
            } else {
                return timer.clone();
            }
        }

        public int getMaxRound() {
            return maxRound;
        }


    }
    int[][] BLANK_WAVE_TIME = {{}};
    int[][] THE_LAB_WAVE_TIME = {{10,22},{10,22},{10,22},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34}};
    int[][] DEAD_END_WAVE_TIME = {{10,20},{10,20},{10,20,35},{10,20,35},{10,22,37},{10,22,44},{10,25,47},{10,25,50},{10,22,38},{10,24,45},{10,25,48},{10,25,50},{10,25,50},{10,25,45},{10,25,46},{10,24,47},{10,24,47},{10,24,47},{10,24,47},{10,24,49},{10,23,44},{10,23,45},{10,23,42},{10,23,43},{10,23,43},{10,23,36},{10,24,44},{10,24,42},{10,24,42},{10,24,45}};
    int[][] BAD_BLOOD_WAVE_TIME = {{10,22},{10,22},{10,22},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,24,38},{10,24,38},{10,22,34},{10,24,38},{10,22,34}};
    int[][] ALIEN_ARCADIUM_WAVE_TIME = {{10,13,16,19},{10,14,18,22},{10,13,16,19},{10,14,17,21,25,28},{10,14,18,22,26,30},{10,14,19,23,28,32},{10,15,19,23,27,31},{10,15,20,25,30,35},{10,14,19,23,28,32},{10,16,22,27,33,38},{10,16,21,27,32,38},{10,16,22,28,34,40},{10,16,22,28,34,40},{10,16,21,26,31,36},{10,17,24,31,38,46},{10,16,22,27,33,38},{10,14,19,23,28,32},{10,14,19,23,28,32},{10,14,18,22,26,30},{10,15,21,26,31,36},{10,14,19,23,28,32},{10,14,19,23,28,34},{10,14,18,22,26,30},{10,14,19,23,28,32},{10},{10,23,36},{10,22,34},{10,20,30},{10,24,38},{10,22,34},{10,22,34},{10,21,32},{10,22,34},{10,22,34},{10},{10,22,34},{10,20,31},{10,22,34},{10,22,34},{10,22,34,37,45},{10,21,32},{10,22,34},{10,13,22,25,34,37},{10,22,34},{10,22,34,35},{10,21,32,35},{10,20,30},{10,20,30,33},{10,21,32},{10,22,34,37},{10,20,30,33},{10,22,34,37},{10,22,34,37},{10,20,32,35,39},{10,16,22,28,34,40},{10,14,18},{10,14,18},{10,22,34,37,38},{10,14,18,22,26,30},{10,20,30,33},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,27,32},{10,14,18,22,27,32},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{5},{5},{5},{5},{5}};

}
