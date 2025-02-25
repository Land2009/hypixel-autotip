package com.lando.autotip;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class AutoTipCommand extends CommandBase {

    private final MainModClass mod;

    public AutoTipCommand(MainModClass mod) {
        this.mod = mod;
    }

    @Override
    public String getCommandName() {
        return "hypixelautotip";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/hypixelautotip";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        // Öffne das GUI
        Minecraft.getMinecraft().displayGuiScreen(new AutoTipGUI());
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true; // Jeder Spieler kann den Befehl verwenden
    }
}
