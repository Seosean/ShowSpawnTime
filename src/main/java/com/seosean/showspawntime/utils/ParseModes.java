package com.seosean.showspawntime.utils;

import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.seosean.showspawntime.mapFile.Rounds.colorRegex;
import static com.seosean.showspawntime.mapFile.Rounds.emojiRegex;

public class ParseModes {
    public static int diff = 1;

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        String message = event.message.getUnformattedText().replaceAll(emojiRegex, "").replaceAll(colorRegex, "");
        if(message.contains(":")){
            return;
        }
        if(message.contains("The game starts in 10 seconds!") || message.contains("游戏将在10秒开始") || message.contains("遊戲將在 10 秒後開始")){
            diff = 1;
        }
        if(message.contains("Hard Difficulty") || message.contains("困难") || message.contains("困難")){
            diff = 2;
        }else if(message.contains("RIP Difficulty") || message.contains("安息") || message.contains("RIP") ){
            diff = 3;
        }
    }
}
