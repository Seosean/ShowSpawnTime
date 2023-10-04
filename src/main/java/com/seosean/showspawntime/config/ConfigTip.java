package com.seosean.showspawntime.config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.seosean.showspawntime.mapFile.Rounds.scoreboardTitle;
import static com.seosean.showspawntime.ShowSpawnTime.ShowConfigTip;
import static com.seosean.showspawntime.utils.Utils.*;

public class ConfigTip  {
    @SubscribeEvent
    public void playerConnectEvent(EntityJoinWorldEvent event) {
        if(!(event.entity instanceof EntityPlayer)) return;
        if(!event.entity.worldObj.isRemote) return;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc == null || mc.theWorld == null || mc.isSingleplayer() ) return;
        EntityPlayerSP p = mc.thePlayer;
        if(p==null) return;

        if(scoreboardTitle == null) return;

        if(!ShowConfigTip || !isInZombies(scoreboardTitle)){
                return;
        }

        if(isOtherLanguages()) {
            IChatComponent globalTips = new ChatComponentText(EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "ShowSpawnTime: " +
                    EnumChatFormatting.WHITE + "Some configs can be edited at ");
            IChatComponent configTips = new ChatComponentText(EnumChatFormatting.GOLD.toString() + EnumChatFormatting.BOLD + "CONFIG.");
            IChatComponent configHover = new ChatComponentText(EnumChatFormatting.WHITE.toString() + "Click to edit configs.");

            IChatComponent hudTips = new ChatComponentText(EnumChatFormatting.GOLD.toString() + EnumChatFormatting.BOLD + "  [HUD]");
            IChatComponent hudHover = new ChatComponentText(EnumChatFormatting.WHITE.toString() + "Click to edit HUDs");

            IChatComponent featuresTips = new ChatComponentText(EnumChatFormatting.GREEN + "  [Features]");
            IChatComponent featuresHover = new ChatComponentText(EnumChatFormatting.GREEN + "Click for more information about commands of this mod." +
                    EnumChatFormatting.WHITE + "\n· Show all wave intervals" +
                    EnumChatFormatting.WHITE + "\n· Make a sound when waves spawn" +
                    EnumChatFormatting.WHITE + "\n· Note current game time when rounds ends" +
                    EnumChatFormatting.WHITE + "\n· Tell you the time you take to clean up" +
                    EnumChatFormatting.WHITE + "\n· Show the queue of lightning rod" +
                    EnumChatFormatting.WHITE + "\n· Count down with sounds before 3rd wave" +
                    EnumChatFormatting.WHITE + "\n· Alert to AA \"boss\" with color" +
                    EnumChatFormatting.WHITE + "\n· Show teammates' health in sidebar + " +
                    EnumChatFormatting.WHITE + "\n· Show the amount of wave 3 zombies in sidebar" +
                    EnumChatFormatting.WHITE + "\n· Show the left time of powerups over your screen");

            IChatComponent glitchTips = new ChatComponentText(EnumChatFormatting.RED + "  [Click when config GUI glitches]");
            IChatComponent glitchHover = new ChatComponentText(EnumChatFormatting.WHITE + "Glitched?");

            ChatStyle configs = new ChatStyle().setChatHoverEvent(new HoverEvent(net.minecraft.event.HoverEvent.Action.SHOW_TEXT, configHover)).setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sstconfig"));
            ChatStyle huds = new ChatStyle().setChatHoverEvent(new HoverEvent(net.minecraft.event.HoverEvent.Action.SHOW_TEXT, hudHover)).setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ssthud"));

            ChatStyle features = new ChatStyle().setChatHoverEvent(new HoverEvent(net.minecraft.event.HoverEvent.Action.SHOW_TEXT, featuresHover)).setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sst feature"));
            ChatStyle buttom = new ChatStyle().setChatHoverEvent(new HoverEvent(net.minecraft.event.HoverEvent.Action.SHOW_TEXT, glitchHover)).setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sst feature glitch"));

            configTips.setChatStyle(configs);
            hudTips.setChatStyle(huds);

            featuresTips.setChatStyle(features);
            glitchTips.setChatStyle(buttom);

            IChatComponent commandsTips = new ChatComponentText("\n" + EnumChatFormatting.GRAY + "/sstconfig and /ssthud are ways to open config GUIs.");

            globalTips.appendSibling(configTips).appendSibling(hudTips).appendSibling(featuresTips).appendSibling(glitchTips).appendSibling(commandsTips);
            Minecraft.getMinecraft().thePlayer.addChatComponentMessage(globalTips);
        }else if(isSimplifiedChinese()){
            IChatComponent globalTips = new ChatComponentText(EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "ShowSpawnTime: " +
                    EnumChatFormatting.WHITE + "如需对功能细节进行编辑， 请点击： ");

            IChatComponent configTips = new ChatComponentText(EnumChatFormatting.GOLD.toString() + EnumChatFormatting.BOLD + "配置.");
            IChatComponent configHover = new ChatComponentText(EnumChatFormatting.WHITE.toString() + "点击编辑配置");

            IChatComponent hudTips = new ChatComponentText(EnumChatFormatting.GOLD.toString() + EnumChatFormatting.BOLD + "  [HUD]");
            IChatComponent hudHover = new ChatComponentText(EnumChatFormatting.WHITE.toString() + "点击编辑 HUD");

            IChatComponent featuresTips = new ChatComponentText(EnumChatFormatting.GREEN + "  [功能]");
            IChatComponent featuresHover = new ChatComponentText(EnumChatFormatting.GREEN + "点击查看本模组指令使用指南" +
                    EnumChatFormatting.WHITE + "\n· 显示回合内小波时间" +
                    EnumChatFormatting.WHITE + "\n· 僵尸生成时发出声音提示" +
                    EnumChatFormatting.WHITE + "\n· 在回合结束时显示本局至此时间" +
                    EnumChatFormatting.WHITE + "\n· 在回合结束时显示上一回合最后一波到回合结束的时长" +
                    EnumChatFormatting.WHITE + "\n· 显示闪电棒队列" +
                    EnumChatFormatting.WHITE + "\n· 在 DE/BB 图中对最后一波用声音进行倒计时" +
                    EnumChatFormatting.WHITE + "\n· 在 AA 中于右下角标记 TOO 和巨人生成的小波" +
                    EnumChatFormatting.WHITE + "\n· 在计分板上显示队友的生命值" +
                    EnumChatFormatting.WHITE + "\n· 在计分板上显示第三波僵尸的剩余数量" +
                    EnumChatFormatting.WHITE + "\n· 在屏幕上显示当前地图增益剩余时间");

            IChatComponent glitchTips = new ChatComponentText(EnumChatFormatting.RED + "  [配置界面故障时点击]");
            IChatComponent glitchHover = new ChatComponentText(EnumChatFormatting.WHITE + "故障了？");

            ChatStyle configs = new ChatStyle().setChatHoverEvent(new HoverEvent(net.minecraft.event.HoverEvent.Action.SHOW_TEXT, configHover)).setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sstconfig"));
            ChatStyle huds = new ChatStyle().setChatHoverEvent(new HoverEvent(net.minecraft.event.HoverEvent.Action.SHOW_TEXT, hudHover)).setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ssthud"));

            ChatStyle features = new ChatStyle().setChatHoverEvent(new HoverEvent(net.minecraft.event.HoverEvent.Action.SHOW_TEXT, featuresHover)).setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sst feature"));
            ChatStyle buttom = new ChatStyle().setChatHoverEvent(new HoverEvent(net.minecraft.event.HoverEvent.Action.SHOW_TEXT, glitchHover)).setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sst feature glitch"));

            configTips.setChatStyle(configs);
            hudTips.setChatStyle(huds);

            featuresTips.setChatStyle(features);
            glitchTips.setChatStyle(buttom);

            IChatComponent commandsTips = new ChatComponentText("\n" + EnumChatFormatting.GRAY + "/sstconfig 和 /ssthud 都可以用来编辑配置和HUD位置。");

            globalTips.appendSibling(configTips).appendSibling(hudTips).appendSibling(featuresTips).appendSibling(glitchTips).appendSibling(commandsTips);
            Minecraft.getMinecraft().thePlayer.addChatComponentMessage(globalTips);
        }else if(isTraditionalChinese()){
            IChatComponent globalTips = new ChatComponentText(EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "ShowSpawnTime: " +
                    EnumChatFormatting.WHITE + "如需對功能細節進行編輯， 請點擊： ");

            IChatComponent configTips = new ChatComponentText(EnumChatFormatting.GOLD.toString() + EnumChatFormatting.BOLD + "配寘");
            IChatComponent configHover = new ChatComponentText(EnumChatFormatting.WHITE.toString() + "點擊編輯配寘");

            IChatComponent hudTips = new ChatComponentText(EnumChatFormatting.GOLD.toString() + EnumChatFormatting.BOLD + "  [HUD]");
            IChatComponent hudHover = new ChatComponentText(EnumChatFormatting.WHITE.toString() + "點擊編輯 HUD");

            IChatComponent featuresTips = new ChatComponentText(EnumChatFormatting.GREEN + "  [功能]");
            IChatComponent featuresHover = new ChatComponentText(EnumChatFormatting.GREEN + "點擊查看本模組指令使用指南" +
                    EnumChatFormatting.WHITE + "\n· 顯示回合內小波時間" +
                    EnumChatFormatting.WHITE + "\n· 殭屍生成時發出聲音提示" +
                    EnumChatFormatting.WHITE + "\n· 在回合結束時顯示本局至此時間" +
                    EnumChatFormatting.WHITE + "\n· 在回合結束時顯示上一回合最後一波到回合結束的時長" +
                    EnumChatFormatting.WHITE + "\n· 顯示閃電棒隊列" +
                    EnumChatFormatting.WHITE + "\n· 在 DE/BB 圖中對最後一波進行聲音倒數計時" +
                    EnumChatFormatting.WHITE + "\n· 在 AA 中於右下角標記 TOO 和巨人生成的小波" +
                    EnumChatFormatting.WHITE + "\n· 在計分板上顯示隊友的生命值" +
                    EnumChatFormatting.WHITE + "\n· 在計分板上第三波僵屍的剩餘數量" +
                    EnumChatFormatting.WHITE + "\n· 在荧幕上顯示增益的倒數計時");
            IChatComponent glitchTips = new ChatComponentText(EnumChatFormatting.RED + "  [配寘介面故障時點擊]");
            IChatComponent glitchHover = new ChatComponentText(EnumChatFormatting.WHITE + "故障了？");

            ChatStyle configs = new ChatStyle().setChatHoverEvent(new HoverEvent(net.minecraft.event.HoverEvent.Action.SHOW_TEXT, configHover)).setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sstconfig"));
            ChatStyle huds = new ChatStyle().setChatHoverEvent(new HoverEvent(net.minecraft.event.HoverEvent.Action.SHOW_TEXT, hudHover)).setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ssthud"));

            ChatStyle features = new ChatStyle().setChatHoverEvent(new HoverEvent(net.minecraft.event.HoverEvent.Action.SHOW_TEXT, featuresHover)).setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sst feature"));
            ChatStyle buttom = new ChatStyle().setChatHoverEvent(new HoverEvent(net.minecraft.event.HoverEvent.Action.SHOW_TEXT, glitchHover)).setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sst feature glitch"));

            configTips.setChatStyle(configs);
            hudTips.setChatStyle(huds);

            featuresTips.setChatStyle(features);
            glitchTips.setChatStyle(buttom);

            IChatComponent commandsTips = new ChatComponentText("\n" +EnumChatFormatting.GRAY + "/sstconfig 和 /ssthud 都可以用來編輯配寘和HUD位置。");

            globalTips.appendSibling(configTips).appendSibling(hudTips).appendSibling(featuresTips).appendSibling(glitchTips).appendSibling(commandsTips);
            Minecraft.getMinecraft().thePlayer.addChatComponentMessage(globalTips);
        }

        ShowConfigTip = false;
        MinecraftForge.EVENT_BUS.unregister(ConfigTip.this);
    }
}
