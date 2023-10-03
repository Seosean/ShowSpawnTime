package com.seosean.showspawntime;

import com.seosean.showspawntime.api.ApiHandler;
import com.seosean.showspawntime.api.ConfigApi;
import com.seosean.showspawntime.api.ConfigApiImpl;
import com.seosean.showspawntime.commands.CommandCommon;
import com.seosean.showspawntime.commands.CommandSSTConfig;
import com.seosean.showspawntime.commands.CommandSSTHUD;
import com.seosean.showspawntime.mapFile.Rounds;
import com.seosean.showspawntime.utils.*;
import com.seosean.showspawntime.config.ConfigTip;
import com.seosean.showspawntime.mapFile.AAFeature;
import com.seosean.showspawntime.mapFile.InternalTimer;
import com.seosean.zombiesautosplits.ZombiesAutoSplits;
import com.seosean.zombiesautosplits.api.ApiManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import scala.collection.script.Update;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Mod(modid = ShowSpawnTime.MODID,
        version = ShowSpawnTime.VERSION,
        guiFactory = "com.seosean.showspawntime.config.ShowSpawnTimeGuiFactory"
)
public class ShowSpawnTime
{
    public static final String MODID = "ShowSpawnTime";
    public static final String VERSION = "1.15.0";
    private static ShowSpawnTime instance;
    private Logger logger;
    private Configuration config;
    public static double HighlightDelay;
    public static boolean PlaySound;
    public static boolean PlayDEBBSound;
    public static String PrecededWave;
    public static String TheLastWave;
    public static double PrecededWavePitch;
    public static double TheLastWavePitch;
    public static boolean ShowConfigTip;
    public static boolean ColorAlert;
    public static String AARoundsRecordToggle;
    public static String DEBBRoundsRecordToggle;
    public static boolean CleanUpTimeToggle;
    public static boolean LightningRodQueue;
    public static boolean PowerupAlertToggle;
    public static boolean Wave3LeftNotice;
    public static boolean PlayerHealthNotice;
    public boolean DEBBCountDown;
    public boolean PlayerInvisible;
    public static String[] AARoundsRecord = new String[]{"OFF", "Quintuple", "Tenfold","ALL"};
    public static String[] DEBBRoundsRecord = new String[]{"OFF", "Quintuple", "Tenfold","ALL"};
    public KeyBinding keyToggleCountDown = new KeyBinding("Wave 3 Count Down", Keyboard.KEY_NONE, "Show Spawn Time");
    public KeyBinding keyTogglePlayerInvisible = new KeyBinding("Player Invisible", Keyboard.KEY_NONE, "Show Spawn Time");

    private InternalTimer internalTimer;
    private SplitsTimer splitsTimer;
    private PowerUpDetect powerUpDetect;
    private UpdateDetect updateDetect;
    private Utils utils;
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    public InternalTimer getInternalTimer() { return internalTimer; }
    public SplitsTimer getSplitsTimer() {return splitsTimer;}
    public PowerUpDetect getPowerUpDetect() { return powerUpDetect; }
    public Utils getUtils() { return utils; }
    public UpdateDetect getUpdateDetect() {
        return updateDetect;
    }

    @EventHandler
    public void preinit(FMLPreInitializationEvent event){
        logger = event.getModLog();
        config = new Configuration(event.getSuggestedConfigurationFile());
        this.ConfigLoad();
        this.ConfigHUDLoad();
    }
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        internalTimer = new InternalTimer();
        updateDetect = new UpdateDetect();
        instance = this;
        ClientRegistry.registerKeyBinding(this.keyToggleCountDown);
        ClientRegistry.registerKeyBinding(this.keyTogglePlayerInvisible);
        MinecraftForge.EVENT_BUS.register(new Rounds());
        MinecraftForge.EVENT_BUS.register(powerUpDetect = new PowerUpDetect());
        MinecraftForge.EVENT_BUS.register(internalTimer);
        MinecraftForge.EVENT_BUS.register(new AAFeature());
        MinecraftForge.EVENT_BUS.register(new LeftNotice());
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new ConfigTip());
        MinecraftForge.EVENT_BUS.register(new Render());
        MinecraftForge.EVENT_BUS.register(new ParseModes());
        MinecraftForge.EVENT_BUS.register(updateDetect);
        ClientCommandHandler.instance.registerCommand(new CommandCommon());
        ClientCommandHandler.instance.registerCommand(new CommandSSTConfig());
        ClientCommandHandler.instance.registerCommand(new CommandSSTHUD());
        ShowConfigTip = true;
        splitsTimer = new SplitsTimer(executor);
        this.getPowerUpDetect().iniPowerupMap();
        utils = new Utils();
        fontRendererObj = Minecraft.getMinecraft().fontRendererObj;
        minecraft =  Minecraft.getMinecraft();
        // 在合适的时机进行初始化和注册
        ConfigApi apiInstance = new ConfigApiImpl();
// 进行其他必要的操作
        ApiHandler.getInstance().registerApiInstance(ConfigApi.class, apiInstance);
    }

    public static ShowSpawnTime getInstance() {
        return instance;
    }

    public Configuration getConfig() {
        return config;
    }

    public void reload(){
        config.save();
    }

    public Logger getLogger()
    {
        return logger;
    }
    public void ConfigHUDLoad() {
        config.load();
        logger.info("Started loading config. ");
        XSpawnTime = config.get(Configuration.CATEGORY_CLIENT, "XSpawnTime", -1, "X").getDouble();
        YSpawnTime = config.get(Configuration.CATEGORY_CLIENT, "YSpawnTime", -1, "Y").getDouble();
        XPowerup = config.get(Configuration.CATEGORY_CLIENT, "XPowerup", -1, "X").getDouble();
        YPowerup = config.get(Configuration.CATEGORY_CLIENT, "YPowerup", -1, "Y").getDouble();
        config.save();
        logger.info("Finished loading HUD config. ");
    }

    public void ConfigLoad(){
        config.load();
        logger.info("Started loading config. ");

        String comment;
        String commentPlaySound;
        String commentDEBBPlaySound;
        String commentPlaySoundPrecededWave;
        String commentPlaySoundLastWave;
        String commentPlaySoundPrecededWavePitch;
        String commentPlaySoundLastWavePitch;
        String commentDangerAlert;
        String commentAAAllRoundsRecord;
        String commentDEBBAllRoundsRecord;
        String commentCleanUpTimeTook;
        String commentLightningRodHelper;
        String commentPowerupAlert;
        String commentWave3LeftNotice;
        String commentPlayerHealthNotice;
        comment = "How long will the highlight delayed after a wave spawns in **SECOND**. \nNotice it only works in Dead End and Bad Blood.";
        HighlightDelay = config.get(Configuration.CATEGORY_GENERAL, "Highlight Delay", 2.0, comment, -10 , 10).getDouble();

        commentPlaySound = "Turn on/off the sound of wave spawning in AA.";
        PlaySound = config.get(Configuration.CATEGORY_GENERAL, "Toggle AA Sound", true, commentPlaySound).getBoolean();

        commentDEBBPlaySound = "Turn on/off the sound of wave spawning in DE and BB.";
        PlayDEBBSound = config.get(Configuration.CATEGORY_GENERAL, "Toggle DE/BB Sound", false, commentDEBBPlaySound).getBoolean();


        commentPlaySoundPrecededWave = "What sound will be played when a wave spawns except the last wave. \nYou can search the sounds you want at https://minecraft.fandom.com/wiki/Sounds.json/Java_Edition_values_before_1.9 \nChinese wiki: https://minecraft.fandom.com/zh/wiki/Sounds.json/Java%E7%89%881.9%E5%89%8D";
        PrecededWave = config.get(Configuration.CATEGORY_GENERAL, "Preceded Wave Sound", "note.pling", commentPlaySoundPrecededWave).getString();

        commentPlaySoundPrecededWavePitch = "The pitch setting of PrecededWave.";
        PrecededWavePitch = config.get(Configuration.CATEGORY_GENERAL, "Preceded Wave Pitch", 2.0, commentPlaySoundPrecededWavePitch, 0, 2).getDouble();

        commentPlaySoundLastWave = "What sound will be played when the last wave spawns.";
        TheLastWave = config.get(Configuration.CATEGORY_GENERAL, "Last Wave Sound", "random.orb", commentPlaySoundLastWave).getString();

        commentPlaySoundLastWavePitch = "The pitch setting of TheLastWave.";
        TheLastWavePitch = config.get(Configuration.CATEGORY_GENERAL, "Last Wave Pitch", 0.5, commentPlaySoundLastWavePitch, 0, 2).getDouble();

        commentDangerAlert = "Turn on/off the color alert to The Old One and Giants. \nOnly works in AA.";
        ColorAlert = config.get(Configuration.CATEGORY_GENERAL, "AA Boss Alert", true, commentDangerAlert).getBoolean();

        commentAAAllRoundsRecord  = "Turn on/off the round timing similar to round 10/20/105 in AA.";
        AARoundsRecordToggle = config.getString("AA Rounds Record Timing", Configuration.CATEGORY_GENERAL, "ALL", commentAAAllRoundsRecord, AARoundsRecord);

        commentDEBBAllRoundsRecord = "Turn on/off the round timing similar to round 10/20/30 in DE/BB.";
        DEBBRoundsRecordToggle = config.getString("DE/BB Rounds Record Timing", Configuration.CATEGORY_GENERAL, "ALL", commentDEBBAllRoundsRecord, DEBBRoundsRecord);

        commentCleanUpTimeTook = "Turn on/off the tip of time took to clean up.";
        CleanUpTimeToggle = config.get(Configuration.CATEGORY_GENERAL, "Clean Up Time Tips", true, commentCleanUpTimeTook).getBoolean();

        commentLightningRodHelper = "Turn on/off the helper of lightning rod queue in AA.";
        LightningRodQueue = config.get(Configuration.CATEGORY_GENERAL, "LR Queue Helper", true, commentLightningRodHelper).getBoolean();

        commentPowerupAlert = "Remind you when this is a powerup-round. Start counting down when a powerup spawns";
        PowerupAlertToggle = config.get(Configuration.CATEGORY_GENERAL, "Powerup Alert", true, commentPowerupAlert).getBoolean();

        commentWave3LeftNotice = "Enhance the ScoreBoard which shows you the amount of zombies in wave 3rd in DE/BB.";
        Wave3LeftNotice = config.get(Configuration.CATEGORY_GENERAL, "Wave 3rd Left Notice", true, commentWave3LeftNotice).getBoolean();

        commentPlayerHealthNotice = "Enhance the ScoreBoard which shows the health of players.";
        PlayerHealthNotice = config.get(Configuration.CATEGORY_GENERAL, "Player Health Notice", true, commentPlayerHealthNotice).getBoolean();
//new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth() - Minecraft.getMinecraft().fontRendererObj.getStringWidth("W1 00:10")
        //new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight() - Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT * 4

        config.save();
        logger.info("Finished loading config. ");
    }
    public static double XSpawnTime;
    public static double YSpawnTime;
    public static double XPowerup;
    public static double YPowerup;
    public static double XSplitter;
    public static double YSplitter;

    private FontRenderer fontRendererObj;
    private Minecraft minecraft;
    public double getXSpawnTime(){
        int screenWidth = new ScaledResolution(minecraft).getScaledWidth();
        if(ShowSpawnTime.XSpawnTime < 0){
            return 1 - (double)this.fontRendererObj.getStringWidth("➤ W2 00:00") / (double)screenWidth;
        }
        return XSpawnTime;
    }

    public double getYSpawnTime(){
        int screenHeight = new ScaledResolution(minecraft).getScaledHeight();
        if(ShowSpawnTime.YSpawnTime < 0){
            return 1 - (double)Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT * 7 / (double)screenHeight;
        }
        return YSpawnTime;
    }

    public double getXPowerup(){
        if(ShowSpawnTime.XPowerup < 0){
            return 0;
        }
        return XPowerup;
    }

    public double getYPowerup(){
        int screenHeight = new ScaledResolution(minecraft).getScaledHeight();
        if(ShowSpawnTime.YPowerup < 0){
            return 0.5 - (float)fontRendererObj.FONT_HEIGHT * 4 / (double)screenHeight;
        }
        return YPowerup;
    }

    public double getXSplitter(){
        int screenWidth = new ScaledResolution(minecraft).getScaledWidth();
        if(ShowSpawnTime.XSplitter < 0){
            return 1 - (double)fontRendererObj.getStringWidth("0:00:0") / (double)screenWidth;
        }
        return XSplitter;
    }

    public double getYSplitter(){
        int screenHeight = new ScaledResolution(minecraft).getScaledHeight();
        if(ShowSpawnTime.YSplitter < 0){
            return 1 - fontRendererObj.FONT_HEIGHT / (double)screenHeight;
        }
        return YSplitter;
    }
    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.modID.equals(ShowSpawnTime.MODID) && (event.configID == null || !event.configID.equals("dummyID"))) {
            config.save();
            this.ConfigLoad();
        }
    }

    private static float linearCrease(float number) {
        if (number >= 1.4 && number <= 4){
            return 0.0533F * number + 0.0258F;
        } else if(number > 4 && number <= 6){
            return 0.15F * number - 0.4126F;
        }else if (number > 6 && number <= 7.71){
            return 0.3F * number - 1.313F;
        }else{
            return 0;
        }
    }

    public static float getAlpha(Entity entity){
        return linearCrease(Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entity));
    }

    @SubscribeEvent
    public void toggle(InputEvent.KeyInputEvent event) {
        if (Keyboard.getEventKey() == this.keyToggleCountDown.getKeyCode() && Keyboard.getEventKeyState() && !Keyboard.isRepeatEvent() && Minecraft.getMinecraft().currentScreen == null) {
            this.DEBBCountDown = !this.DEBBCountDown;
            if (this.DEBBCountDown) {
                IChatComponent on = new ChatComponentText(EnumChatFormatting.YELLOW + "Toggled Count Down " + EnumChatFormatting.GREEN + "ON"  + EnumChatFormatting.YELLOW + "!");
                Minecraft.getMinecraft().thePlayer.addChatComponentMessage(on);
            } else {
                IChatComponent off = new ChatComponentText(EnumChatFormatting.YELLOW + "Toggled Count Down " + EnumChatFormatting.RED + "OFF"  + EnumChatFormatting.YELLOW + "!");
                Minecraft.getMinecraft().thePlayer.addChatComponentMessage(off);
            }
        }else if (Keyboard.getEventKey() == this.keyTogglePlayerInvisible.getKeyCode() && Keyboard.getEventKeyState() && !Keyboard.isRepeatEvent() && Minecraft.getMinecraft().currentScreen == null) {
            this.PlayerInvisible = !this.PlayerInvisible;
            if (this.PlayerInvisible) {
                IChatComponent on = new ChatComponentText(EnumChatFormatting.YELLOW + "Toggled Player Invisible " + EnumChatFormatting.GREEN + "ON"  + EnumChatFormatting.YELLOW + "!");
                Minecraft.getMinecraft().thePlayer.addChatComponentMessage(on);
            } else {
                IChatComponent off = new ChatComponentText(EnumChatFormatting.YELLOW + "Toggled Player Invisible " + EnumChatFormatting.RED + "OFF"  + EnumChatFormatting.YELLOW + "!");
                Minecraft.getMinecraft().thePlayer.addChatComponentMessage(off);
            }
        }
    }

    public boolean isPlayerInvisible(Entity entity){
        boolean flag = !entity.isInvisible();
        boolean flag1 = !flag && !entity.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer);
        boolean flag2 = entity != Minecraft.getMinecraft().thePlayer && entity instanceof EntityPlayer && Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entity) < 7.02F;
        return (flag || flag1) && (flag1 || flag2);
    }
}
