package com.lando.autotip;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiButton;

public class AutoTipGUI extends GuiScreen {

    @Override
    public void initGui() {
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 2 - 20, 200, 20, "Test Button"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, "Hypixel AutoTip Settings - by Lando", this.width / 2, 20, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            System.out.println("[HypixelAutoTip] Test Button clicked.");
        }
    }
}
