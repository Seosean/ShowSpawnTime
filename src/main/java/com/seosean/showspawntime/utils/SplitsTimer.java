package com.seosean.showspawntime.utils;

import com.seosean.showspawntime.ShowSpawnTime;
import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.util.Objects;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import static com.seosean.showspawntime.utils.Utils.*;
import static com.seosean.showspawntime.utils.Utils.isSimplifiedChinese;
import static com.seosean.showspawntime.utils.Utils.isTraditionalChinese;
import static com.seosean.showspawntime.mapFile.InternalTimer.*;

public class SplitsTimer {

    public SplitsTimer(ScheduledExecutorService executor){
        this.executor = Objects.requireNonNull(executor, "executor");
    }
    public static int recordedRound = 0;

    private final ReentrantLock lock = new ReentrantLock();

    private final ScheduledExecutorService executor;

    private Future<?> future = null;

    public long millis = 0L;

    public void startOrSplit() {
        lock.lock();
        try {
            if (future == null) {
                future = executor.scheduleAtFixedRate(() -> {
                    lock.lock();
                    try {
                        gameTick += 10;
                        if(ShowSpawnTime.getInstance().DEBBCountDown){
                            DEBBW3CountDown();
                        }

                    }
                    finally {
                        lock.unlock();
                    }
                }, 0, 10, TimeUnit.MILLISECONDS);
            }
            else {
                lastRoundTime = gameTick;
                gameTick = 0;
                if (ShowSpawnTime.CleanUpTimeToggle) {
                        if (isAllLegit()) {
                            if (isInDE()) {
                                redundantLastRoundTime = lastRoundTime - ((recordedRound <= 3) ? (int) secondToTick(getDEW2Time(recordedRound - 1, true)) : (int) secondToTick(getDEW3Time(recordedRound - 1)));
                                redundantLastRoundTime = redundantLastRoundTime / 1000;
                            }
                            if (isInBB()) {
                                redundantLastRoundTime = lastRoundTime - ((recordedRound <= 4) ? (int) secondToTick(getBBW2Time(recordedRound - 1, true)) : (int) secondToTick(getBBW3Time(recordedRound - 1)));
                                redundantLastRoundTime = redundantLastRoundTime / 1000;
                            }
                            if (isInAA()) {
                                redundantLastRoundTime = lastRoundTime - (secondToTick(ShowSpawnTime.getInstance().getInternalTimer().getAAW6Time(recordedRound - 1)));
                                redundantLastRoundTime = redundantLastRoundTime / 1000;
                            }
                            if (isOtherLanguages()) {
                                IChatComponent redundantTimeTips = new ChatComponentText(EnumChatFormatting.YELLOW + "You took " + EnumChatFormatting.RED.toString() + EnumChatFormatting.BOLD + ((redundantLastRoundTime < 0) ? "--" : redundantLastRoundTime) + EnumChatFormatting.YELLOW + " seconds to clean up after the last wave.");
                                IChatComponent copy = new ChatComponentText(EnumChatFormatting.GREEN + "Copy");
                                redundantTimeTips.setChatStyle(new ChatStyle().setChatHoverEvent(new HoverEvent(net.minecraft.event.HoverEvent.Action.SHOW_TEXT, copy)).setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sst copy " + "You took " + ((redundantLastRoundTime < 0) ? "--" : redundantLastRoundTime) + " seconds to clean up after the last wave.")));
                                Minecraft.getMinecraft().thePlayer.addChatComponentMessage(redundantTimeTips);
                            } else if (isSimplifiedChinese()) {
                                IChatComponent redundantTimeTips = new ChatComponentText(EnumChatFormatting.YELLOW + "你在最后一波生成后， 用时 " + EnumChatFormatting.RED.toString() + EnumChatFormatting.BOLD + ((redundantLastRoundTime < 0) ? "--" : redundantLastRoundTime) + EnumChatFormatting.YELLOW + " 秒清完僵尸并结束回合");
                                IChatComponent copy = new ChatComponentText(EnumChatFormatting.GREEN + "复制");
                                redundantTimeTips.setChatStyle(new ChatStyle().setChatHoverEvent(new HoverEvent(net.minecraft.event.HoverEvent.Action.SHOW_TEXT, copy)).setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sst copy " + "你在最后一波生成后，用时 " + ((redundantLastRoundTime < 0) ? "--" : redundantLastRoundTime) + " 秒清完僵尸并结束回合")));
                                Minecraft.getMinecraft().thePlayer.addChatComponentMessage(redundantTimeTips);
                            } else if (isTraditionalChinese()) {
                                IChatComponent redundantTimeTips = new ChatComponentText(EnumChatFormatting.YELLOW + "你在最後一波生成後，用時 " + EnumChatFormatting.RED.toString() + EnumChatFormatting.BOLD + ((redundantLastRoundTime < 0) ? "--" : redundantLastRoundTime) + EnumChatFormatting.YELLOW + " 秒清完僵屍並結束回合");
                                IChatComponent copy = new ChatComponentText(EnumChatFormatting.GREEN + "複製");
                                redundantTimeTips.setChatStyle(new ChatStyle().setChatHoverEvent(new HoverEvent(net.minecraft.event.HoverEvent.Action.SHOW_TEXT, copy)).setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sst copy " + "你在最後一波生成後，用時 " + ((redundantLastRoundTime < 0) ? "--" : redundantLastRoundTime) + "秒清完僵屍並結束回合")));
                                Minecraft.getMinecraft().thePlayer.addChatComponentMessage(redundantTimeTips);
                            }
                    }
                }
            }
        }
        finally {
            lock.unlock();
        }
    }
//
//    public void cancel() {
//        lock.lock();
//        try {
//            if (future != null) {
//                future.cancel(false);
//                future = null;
//            }
//            millis = 0L;
//        }
//        finally {
//            lock.unlock();
//        }
//    }


    public static int lastRoundTime;
    public static double redundantLastRoundTime;

}
