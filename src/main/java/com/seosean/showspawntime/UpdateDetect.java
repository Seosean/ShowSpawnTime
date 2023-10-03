package com.seosean.showspawntime;


import com.seosean.showspawntime.config.ConfigTip;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class UpdateDetect {
    @SubscribeEvent
    public void playerConnectEvent(EntityJoinWorldEvent event) {
        if (!(event.entity instanceof EntityPlayer)) return;
        if (!event.entity.worldObj.isRemote) return;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc == null || mc.theWorld == null || mc.isSingleplayer()) return;
        EntityPlayerSP p = mc.thePlayer;
        if (p == null) return;
        this.checkUpdates();
        MinecraftForge.EVENT_BUS.unregister(this);
    }
    public void checkUpdates() {
        new Thread(() -> {
            try {
                URL url = new URL("https://raw.githubusercontent.com/Seosean/ShowSpawnTime/main/build.gradle");
                URLConnection connection = url.openConnection();
                connection.setReadTimeout(5000);
                connection.addRequestProperty("User-Agent", "ShowSpawnTime update checker");
                connection.setDoOutput(true);
                final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String currentLine;
                String newestVersion = "";
                while ((currentLine = reader.readLine()) != null) {
                    if (currentLine.contains("version = \"")) {
                        String[] newestVersionSplit = currentLine.split(Pattern.quote("version = \""));
                        newestVersionSplit = newestVersionSplit[1].split(Pattern.quote("\""));
                        newestVersion = newestVersionSplit[0];
                        break;
                    }
                }
                reader.close();
                List<Integer> newestVersionNumbers = new ArrayList<>();
                List<Integer> thisVersionNumbers = new ArrayList<>();
                try {
                    for (String s : newestVersion.split(Pattern.quote("."))) {
                        newestVersionNumbers.add(Integer.parseInt(s));
                    }
                    for (String s : ShowSpawnTime.VERSION.split(Pattern.quote("."))) {
                        thisVersionNumbers.add(Integer.parseInt(s));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return;
                }
                for (int i = 0; i < 3; i++) {
                    if (i >= newestVersionNumbers.size() ) {
                        newestVersionNumbers.add(i, 0);
                    }
                    if (i >= thisVersionNumbers.size()) {
                        thisVersionNumbers.add(i, 0);
                    }
                    if (newestVersionNumbers.get(i) > thisVersionNumbers.get(i)) {
                        IChatComponent newVersion = new ChatComponentText(EnumChatFormatting.AQUA+ "ShowSpawnTime: " + EnumChatFormatting.GREEN + "A new version " + newestVersion + " is available. Download it by clicking here.");
                        newVersion.setChatStyle(newVersion.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/biscuut/LobbyGlow/releases")));
                        Minecraft.getMinecraft().thePlayer.addChatComponentMessage(newVersion);
                        break;
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();

    }


}
