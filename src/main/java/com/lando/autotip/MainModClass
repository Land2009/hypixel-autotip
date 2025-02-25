package com.lando.autotip;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

@Mod(modid = "hypixelautotip", name = "Hypixel AutoTip", version = "3.2.5", acceptedMinecraftVersions = "[1.8.9]")
public class MainModClass {

    private boolean isOnHypixel = false;
    private Timer timer;
    private Configuration config;
    private int joinDelay = 5000; // Standardverzögerung: 5 Sekunden
    private int tipInterval = 600000; // Standardintervall: 10 Minuten
    private Map<String, String> customCommands = new HashMap<String, String>();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        // Konfigurationsdatei laden
        File configFile = new File(event.getModConfigurationDirectory(), "hypixelautotip.cfg");
        config = new Configuration(configFile);
        loadConfig();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        // Event-Handler registrieren
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(this);

        // Befehl /hypixelautotip registrieren
        ClientCommandHandler.instance.registerCommand(new AutoTipCommand(this));
    }

    private void loadConfig() {
        config.load();
        joinDelay = config.getInt("joinDelay", Configuration.CATEGORY_GENERAL, 5000, 1000, 60000, "Delay after joining (in milliseconds)");
        tipInterval = config.getInt("tipInterval", Configuration.CATEGORY_GENERAL, 600000, 120000, 3600000, "Interval between commands (in milliseconds)");

        // Load custom commands
        customCommands.clear();
        String[] commands = config.getStringList("customCommands", Configuration.CATEGORY_GENERAL, new String[]{}, "Custom commands (Format: 'b=/bedwars')");
        for (String command : commands) {
            String[] parts = command.split("=");
            if (parts.length == 2) {
                customCommands.put(parts[0], parts[1]);
            }
        }

        if (config.hasChanged()) {
            config.save();
        }
    }

    private void saveConfig() {
        config.get(Configuration.CATEGORY_GENERAL, "joinDelay", 5000).set(joinDelay);
        config.get(Configuration.CATEGORY_GENERAL, "tipInterval", 600000).set(tipInterval);

        // Save custom commands
        String[] commands = new String[customCommands.size()];
        int i = 0;
        for (Map.Entry<String, String> entry : customCommands.entrySet()) {
            commands[i++] = entry.getKey() + "=" + entry.getValue();
        }
        config.get(Configuration.CATEGORY_GENERAL, "customCommands", new String[]{}).set(commands);

        config.save();
    }

    @SubscribeEvent
    public void onServerConnect(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        String serverIP = Minecraft.getMinecraft().getCurrentServerData().serverIP;
        if (serverIP != null && serverIP.contains("hypixel.net")) {
            isOnHypixel = true;

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    Minecraft.getMinecraft().thePlayer.sendChatMessage("/tipall");
                }
            }, joinDelay);

            startRandomTimer();
        } else {
            isOnHypixel = false;
        }
    }

    private void startRandomTimer() {
        if (timer != null) {
            timer.cancel();
        }

        timer = new Timer();
        scheduleNextTip();
    }

    private void scheduleNextTip() {
        if (!isOnHypixel) {
            return;
        }

        long delay = tipInterval + (new Random().nextInt(5) * 60000); // Random interval
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/tipall");
                scheduleNextTip();
            }
        }, delay);
    }

    @SubscribeEvent
    public void onClientDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        isOnHypixel = false;
    }

    @SubscribeEvent
    public void onChatMessage(ClientChatReceivedEvent event) {
        if (!isOnHypixel) return; // Only active on Hypixel servers

        String message = event.message.getUnformattedText(); // Chat message as plain text

        // Check if the message matches a custom command
        for (Map.Entry<String, String> entry : customCommands.entrySet()) {
            if (message.startsWith("/" + entry.getKey())) {
                // Send the custom command
                Minecraft.getMinecraft().thePlayer.sendChatMessage(entry.getValue());
                event.setCanceled(true); // Prevent the original command from being sent
                break;
            }
        }
    }

    public int getJoinDelay() {
        return joinDelay;
    }

    public void setJoinDelay(int joinDelay) {
        this.joinDelay = joinDelay;
        saveConfig();
    }

    public int getTipInterval() {
        return tipInterval;
    }

    public void setTipInterval(int tipInterval) {
        this.tipInterval = tipInterval;
        saveConfig();
    }

    public Map<String, String> getCustomCommands() {
        return customCommands;
    }

    public void addCustomCommand(String shortcut, String command) {
        customCommands.put(shortcut, command);
        saveConfig();
    }

    public void removeCustomCommand(String shortcut) {
        customCommands.remove(shortcut);
        saveConfig();
    }
}
