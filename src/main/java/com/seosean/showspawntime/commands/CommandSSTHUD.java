package com.seosean.showspawntime.commands;

import com.seosean.showspawntime.hudposition.ConfigGui;
import com.seosean.showspawntime.hudposition.DelayedTask;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public class CommandSSTHUD extends CommandBase {
    public CommandSSTHUD() {
    }

    public String getCommandName() {
        return "ssthud";
    }

    public String getCommandUsage(ICommandSender sender) {
        return "ssthud";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        new DelayedTask(() -> {
            Minecraft.getMinecraft().displayGuiScreen(new ConfigGui());
        }, 2);
    }

    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    // $FF: synthetic method
    CommandSSTHUD(Object x1) {
        this();
    }
}