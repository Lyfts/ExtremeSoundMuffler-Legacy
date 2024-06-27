package com.leobeliik.extremesoundmuffler.utils;

import net.minecraft.client.Minecraft;

import com.leobeliik.extremesoundmuffler.interfaces.IColorsGui;

import cpw.mods.fml.client.config.GuiButtonExt;

public class ESMButton extends GuiButtonExt implements IColorsGui {

    private final Runnable runnable;
    private boolean renderNormal = false;
    private boolean renderText = false;

    public ESMButton(int id, int x, int y, int width, int height, String displayString) {
        this(id, x, y, width, height, displayString, null);
    }

    public ESMButton(int id, int x, int y, int width, int height, Runnable runnable) {
        super(id, x, y, width, height, "");
        this.runnable = runnable;
    }

    public ESMButton(int id, int x, int y, int width, int height, String displayString, Runnable runnable) {
        super(id, x, y, width, height, displayString);
        this.runnable = runnable;
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (func_146115_a() && enabled && visible && runnable != null) {
            runnable.run();
            return true;
        }
        return func_146115_a();
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        this.field_146123_n = mouseX >= this.xPosition && mouseY >= this.yPosition
            && mouseX < this.xPosition + this.width
            && mouseY < this.yPosition + this.height;

        if (renderNormal) {
            super.drawButton(mc, mouseX, mouseY);
        } else if (renderText) {
            String buttonText = this.displayString;
            int strWidth = mc.fontRenderer.getStringWidth(buttonText);
            int ellipsisWidth = mc.fontRenderer.getStringWidth("...");

            if (strWidth > width - 6 && strWidth > ellipsisWidth)
                buttonText = mc.fontRenderer.trimStringToWidth(buttonText, width - 6 - ellipsisWidth)
                    .trim() + "...";

            this.drawCenteredString(
                mc.fontRenderer,
                buttonText,
                this.xPosition + this.width / 2,
                this.yPosition + (this.height - 8) / 2,
                greenText);
        }
    }

    public ESMButton renderNormalButton(boolean state) {
        this.renderNormal = state;
        return this;
    }

    public ESMButton setVisible(boolean state) {
        this.visible = state;
        return this;
    }

    public ESMButton setEnabled(boolean state) {
        this.enabled = state;
        return this;
    }

    public ESMButton setRenderText(boolean state) {
        this.renderText = state;
        return this;
    }
}
