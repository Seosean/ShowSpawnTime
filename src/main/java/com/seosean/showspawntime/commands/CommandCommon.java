package com.seosean.showspawntime.commands;

import com.seosean.showspawntime.ShowSpawnTime;
import com.seosean.showspawntime.utils.ParseModes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.*;
import net.minecraftforge.fml.common.FMLLog;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.seosean.showspawntime.utils.Utils.isOtherLanguages;
import static com.seosean.showspawntime.utils.Utils.isSimplifiedChinese;
import static com.seosean.showspawntime.utils.Utils.isTraditionalChinese;

public class CommandCommon extends CommandBase{
    public CommandCommon() {
    }

    public String getCommandName() {
        return "sst";
    }

    public String getCommandUsage(ICommandSender sender) {
        return "sst";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args[0].equals("feature")) {
            if (args.length == 2 && args[1].equals("glitch")) {
                if(isOtherLanguages()){
                    IChatComponent configHover = new ChatComponentText(EnumChatFormatting.WHITE.toString() + "Click to reset the config.");
                    IChatComponent configfolder = new ChatComponentText(EnumChatFormatting.GOLD.toString() + " [RESET MOD CONFIG]");
                    IChatComponent glitchTips = new ChatComponentText(EnumChatFormatting.AQUA + ">> " + EnumChatFormatting.WHITE + "If you find out certain glitches in the config screen, you reset the config though clicking: ");
                    ChatStyle configs = new ChatStyle().setChatHoverEvent(new HoverEvent(net.minecraft.event.HoverEvent.Action.SHOW_TEXT, configHover)).setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sst feature glitch reset"));
                    configfolder.setChatStyle(configs);
                    Minecraft.getMinecraft().thePlayer.addChatComponentMessage(glitchTips.appendSibling(configfolder));
                }else if(isSimplifiedChinese()){
                    IChatComponent configHover = new ChatComponentText(EnumChatFormatting.WHITE.toString() + "点击重置配置文件");
                    IChatComponent configfolder = new ChatComponentText(EnumChatFormatting.GOLD.toString() + " [重置配置]");
                    IChatComponent glitchTips = new ChatComponentText(EnumChatFormatting.AQUA + ">> " + EnumChatFormatting.WHITE + "如果你发现配置界面出现问题， 请点击: ");
                    ChatStyle configs = new ChatStyle().setChatHoverEvent(new HoverEvent(net.minecraft.event.HoverEvent.Action.SHOW_TEXT, configHover)).setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sst feature glitch reset"));
                    configfolder.setChatStyle(configs);
                    Minecraft.getMinecraft().thePlayer.addChatComponentMessage(glitchTips.appendSibling(configfolder));
                }else if(isTraditionalChinese()){
                    IChatComponent configHover = new ChatComponentText(EnumChatFormatting.WHITE.toString() + "點擊重置配寘");
                    IChatComponent configfolder = new ChatComponentText(EnumChatFormatting.GOLD.toString() + " [重置配寘]");
                    IChatComponent glitchTips = new ChatComponentText(EnumChatFormatting.AQUA + ">> " + EnumChatFormatting.WHITE + "如果你發現配寘介面出現問題， 請點擊: ");
                    ChatStyle configs = new ChatStyle().setChatHoverEvent(new HoverEvent(net.minecraft.event.HoverEvent.Action.SHOW_TEXT, configHover)).setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sst feature glitch reset"));
                    configfolder.setChatStyle(configs);
                    Minecraft.getMinecraft().thePlayer.addChatComponentMessage(glitchTips.appendSibling(configfolder));
                }
            } else if(args.length == 3 && args[1].equals("glitch") && args[2].equals("reset")){

                IChatComponent configfolder = new ChatComponentText(EnumChatFormatting.RED.toString() + "The config file has been reset, you can now reedit with /sstconfig or /ssthud");
                Minecraft.getMinecraft().thePlayer.addChatComponentMessage(configfolder);

                File fileBak = new File(ShowSpawnTime.getInstance().getConfig().getConfigFile().getAbsolutePath() + "_" +
                        new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".errored");
                FMLLog.severe("An exception occurred while loading config file %s. This file will be renamed to %s " +
                        "and a new config file will be generated.", ShowSpawnTime.getInstance().getConfig().getConfigFile().getName(), fileBak.getName());
                ShowSpawnTime.getInstance().getConfig().getConfigFile().renameTo(fileBak);
                ShowSpawnTime.getInstance().ConfigLoad();
                ShowSpawnTime.getInstance().ConfigHUDLoad();
                ShowSpawnTime.getInstance().getConfig().load();
            } else if(args.length == 1){
                IChatComponent commands;
                if(isSimplifiedChinese()) {
                    commands = new ChatComponentText(EnumChatFormatting.YELLOW + "· " + EnumChatFormatting.GREEN + "/sst mode normal/hard/rip " + EnumChatFormatting.YELLOW + "用于在你掉线重连之后，纠正游戏难度的检测结果" + EnumChatFormatting.GRAY  + " (显示第三波剩余僵尸的功能)" +
                            EnumChatFormatting.YELLOW + "\n· " + EnumChatFormatting.GREEN + "/sst ins/max/ss 2/3/5/6/7 " + EnumChatFormatting.YELLOW + "用于在你掉线重连后，纠正增益回合的检测结果 " + EnumChatFormatting.GRAY  + "(显示增益倒计时的功能)" +
                            EnumChatFormatting.YELLOW + "\n· " + EnumChatFormatting.GREEN + "/sstconfig " + EnumChatFormatting.YELLOW + "用于打开配置界面" +
                            EnumChatFormatting.YELLOW + "\n· " + EnumChatFormatting.GREEN + "/ssthud " + EnumChatFormatting.YELLOW + "用于打开GUI界面。 "  + EnumChatFormatting.GRAY + "(如果你安装了Seosean的ZombiesAutoSplits，你就可以通过本指令修改ZombiesAutoSplits的HUD位置，你所编辑的内容会被正常应用)" +
                            EnumChatFormatting.YELLOW + "\n· " + EnumChatFormatting.GREEN + "/sst checkupdate " + EnumChatFormatting.YELLOW + "用于检测更新");
                }else if(isTraditionalChinese()) {
                    commands = new ChatComponentText(EnumChatFormatting.YELLOW + "· " + EnumChatFormatting.GREEN + "/sst mode normal/hard/rip " + EnumChatFormatting.YELLOW + "用於在你掉線重連之後，糾正遊戲難度的檢測結果" + EnumChatFormatting.GRAY  + " (顯示第三波剩餘僵屍的功能)" +
                            EnumChatFormatting.YELLOW + "\n· " + EnumChatFormatting.GREEN + "/sst ins/max/ss 2/3/5/6/7 " + EnumChatFormatting.YELLOW + "用於在你掉線重連後，糾正增益回合的檢測結果" + EnumChatFormatting.GRAY  + " （顯示增益倒數計時的功能）" +
                            EnumChatFormatting.YELLOW + "\n· " + EnumChatFormatting.GREEN + "/sstconfig " + EnumChatFormatting.YELLOW + "用於打開配寘介面" +
                            EnumChatFormatting.YELLOW + "\n· " + EnumChatFormatting.GREEN + "/ssthud " + EnumChatFormatting.YELLOW + "用于打開GUI介面。 "  + EnumChatFormatting.GOLD + "(如果你安裝了Seosean的ZombiesAutoSplits，你就可以通過本指令修改ZombiesAutoSplits的HUD位置，你所編輯的內容會被正常應用)" +
                            EnumChatFormatting.YELLOW + "\n· " + EnumChatFormatting.GREEN + "/sst checkupdate " + EnumChatFormatting.YELLOW + "用於檢測更新");
                }else {
                    commands = new ChatComponentText(EnumChatFormatting.YELLOW + "· " + EnumChatFormatting.GREEN + "/sst mode normal/hard/rip " + EnumChatFormatting.GRAY + "to correct the mode detection if you disconnect." + EnumChatFormatting.GRAY  + " (The feature about wave 3rd zombies display in sidebar.)" +
                            EnumChatFormatting.YELLOW + "\n· " + EnumChatFormatting.GREEN + "/sst ins/max/ss 2/3/5/6/7 " + EnumChatFormatting.YELLOW + "to correct the powerups pattern detection if you disconnect." + EnumChatFormatting.GRAY  + " (The feature about showing countdown of powerups.)" +
                            EnumChatFormatting.YELLOW + "\n· " + EnumChatFormatting.GREEN + "/sstconfig " + EnumChatFormatting.YELLOW + "to open config GUI." +
                            EnumChatFormatting.YELLOW + "\n· " + EnumChatFormatting.GREEN + "/ssthud " + EnumChatFormatting.YELLOW + "to open HUD GUI. "  + EnumChatFormatting.GRAY + "(You can edit the HUD of ZombiesAutoSplits by Seosean with this command as well if you installed it, and it works the same.)" +
                            EnumChatFormatting.YELLOW + "\n· " + EnumChatFormatting.GREEN + "/sst checkupdate " + EnumChatFormatting.YELLOW + "to check the latest version.");
                }
                Minecraft.getMinecraft().thePlayer.addChatComponentMessage(commands);
            }
        }else if(args.length == 1 && args[0].equals("checkupdate")){
            ShowSpawnTime.getInstance().getUpdateDetect().checkUpdates();
        }else if (args.length > 1 && args[0].equals("copy")){
            StringBuilder text = new StringBuilder();
            int i;
            if(args.length > 2){
                for (i = 1; i < args.length; ++i) {
                    text.append(args[i] + (args.length > i + 1 ? " " : ""));
                }
            }else{
                text.append(args[1]);
            }
            String copyingText = String.valueOf(text);
            StringSelection selection = new StringSelection(copyingText);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, null);
            String copiedText = "Copied Successfully!";
            String copiedTextCN = "复制成功！";
            IChatComponent copied = new ChatComponentText(EnumChatFormatting.GREEN + (isSimplifiedChinese()? copiedTextCN : copiedText));
            Minecraft.getMinecraft().thePlayer.addChatComponentMessage(copied);
        }else if(args.length > 1 && args[0].equals("ins")){
            if(Integer.valueOf(args[1]) == 2 || Integer.valueOf(args[1]) == 3) {
                ShowSpawnTime.getInstance().getPowerUpDetect().insRound = Integer.valueOf(args[1]);
                IChatComponent pattern = new ChatComponentText(EnumChatFormatting.GREEN + "Insta Kill Pattern has been set to " + EnumChatFormatting.RED + args[1]);
                Minecraft.getMinecraft().thePlayer.addChatComponentMessage(pattern);
                IChatComponent alert = new ChatComponentText(EnumChatFormatting.GRAY + "The pattern will still be reset by mod's detection in r2/r3/r5/... .");
                Minecraft.getMinecraft().thePlayer.addChatComponentMessage(alert);
            }
        }else if(args.length > 1 && args[0].equals("max")){
            if(Integer.valueOf(args[1]) == 2 || Integer.valueOf(args[1]) == 3) {
                ShowSpawnTime.getInstance().getPowerUpDetect().maxRound = Integer.valueOf(args[1]);
                IChatComponent pattern = new ChatComponentText(EnumChatFormatting.GREEN + "Max Ammo Pattern has been set to " + EnumChatFormatting.RED + args[1]);
                Minecraft.getMinecraft().thePlayer.addChatComponentMessage(pattern);
                IChatComponent alert = new ChatComponentText(EnumChatFormatting.GRAY + "The pattern will still be reset by mod's detection in r2/r3/r5/... .");
                Minecraft.getMinecraft().thePlayer.addChatComponentMessage(alert);
            }
        }else if(args.length > 1 && args[0].equals("ss")){
            if(Integer.valueOf(args[1]) == 5 || Integer.valueOf(args[1]) == 6 || Integer.valueOf(args[1]) == 7) {
                ShowSpawnTime.getInstance().getPowerUpDetect().ssRound = Integer.valueOf(args[1]);
                IChatComponent pattern = new ChatComponentText(EnumChatFormatting.GREEN + "Shopping Spree Pattern has been set to " + EnumChatFormatting.RED + args[1]);
                Minecraft.getMinecraft().thePlayer.addChatComponentMessage(pattern);
                IChatComponent alert = new ChatComponentText(EnumChatFormatting.GRAY + "The pattern will still be reset by mod's detection in r2/r3/r5/... .");
                Minecraft.getMinecraft().thePlayer.addChatComponentMessage(alert);
            }
        }else if(args.length > 1 && args[0].equals("mode")){
            if(args[1].equals("normal")) {
                ParseModes.diff = 1;
                IChatComponent pattern = new ChatComponentText(EnumChatFormatting.GREEN + "Game mode for wave 3rd counter has been set to " + EnumChatFormatting.RED + "Normal");
                Minecraft.getMinecraft().thePlayer.addChatComponentMessage(pattern);
            }else if(args[1].equals("hard")) {
                ParseModes.diff = 2;
                IChatComponent pattern = new ChatComponentText(EnumChatFormatting.GREEN + "Game mode for wave 3rd counter has been set to " + EnumChatFormatting.RED + "Hard");
                Minecraft.getMinecraft().thePlayer.addChatComponentMessage(pattern);
            }else if(args[1].equals("rip")) {
                ParseModes.diff = 3;
                IChatComponent pattern = new ChatComponentText(EnumChatFormatting.GREEN + "Game mode for wave 3rd counter has been set to " + EnumChatFormatting.RED + "RIP");
                Minecraft.getMinecraft().thePlayer.addChatComponentMessage(pattern);
            }
        }
    }

    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public java.util.List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        java.util.List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("mode");
            completions.add("max");
            completions.add("ins");
            completions.add("ss");
            completions.add("checkupdate");
        } else if (args.length == 2 && !args[0].equals("mode")) {
            if(args[0].equals("ins") || args[0].equals("max")) {
                completions.add("2");
                completions.add("3");
            }else if(args[0].equals("ss")){
                completions.add("5");
                completions.add("6");
                completions.add("7");
            }
        } else if (args.length == 2 && args[0].equals("mode")) {
            completions.add("normal");
            completions.add("hard");
            completions.add("rip");
        }
        return completions;
    }

    // $FF: synthetic method
    CommandCommon(Object x1) {
        this();
    }
}