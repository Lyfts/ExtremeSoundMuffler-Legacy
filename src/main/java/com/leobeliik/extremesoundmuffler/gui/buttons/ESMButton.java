package com.leobeliik.extremesoundmuffler.gui.buttons;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;

import org.lwjgl.opengl.GL11;

import com.leobeliik.extremesoundmuffler.SoundMuffler;
import com.leobeliik.extremesoundmuffler.interfaces.IColorsGui;
import com.leobeliik.extremesoundmuffler.utils.Icon;

import cpw.mods.fml.client.config.GuiButtonExt;

public class ESMButton extends GuiButtonExt implements IColorsGui {

    private Runnable runnable;
    protected boolean renderNormal = false;
    protected boolean renderText = false;
    protected int textColor = whiteText;
    protected Supplier<String> tooltipSupplier = () -> "";
    protected BooleanSupplier visibilitySupplier;
    protected Icon icon;
    protected int iconWidth;
    protected int iconHeight;
    protected int iconXOffset;
    protected int iconYOffset;

    public ESMButton(int id, int x, int y, int width, int height, String displayString) {
        this(id, x, y, width, height, displayString, null);
    }

    public ESMButton(int id, int x, int y, int width, int height, Runnable runnable) {
        this(id, x, y, width, height, "", runnable);
    }

    public ESMButton(int id, int x, int y, int width, int height, String displayString, Runnable runnable) {
        super(id, x, y, width, height, displayString);
        this.runnable = runnable;
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (isMouseOver(mouseX, mouseY) && enabled && isVisible() && runnable != null) {
            runnable.run();
            return true;
        }
        return isMouseOver(mouseX, mouseY);
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (!isVisible()) return;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        if (renderNormal) {
            super.drawButton(mc, mouseX, mouseY);
        } else {
            if (renderText) {
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
                    textColor);
            }

            if (icon != null) {
                SoundMuffler.bindTexture();
                icon.draw(xPosition + iconXOffset, yPosition + iconYOffset, iconWidth, iconHeight);
            }
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public ESMButton renderNormalButton(boolean state) {
        this.renderNormal = state;
        return this;
    }

    public ESMButton setVisible(boolean state) {
        this.visible = state;
        return this;
    }

    public ESMButton setVisible(BooleanSupplier supplier) {
        this.visibilitySupplier = supplier;
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

    public ESMButton setTextColor(int color) {
        this.textColor = color;
        return this;
    }

    public ESMButton setTooltip(String tooltip) {
        this.tooltipSupplier = () -> tooltip == null ? "" : tooltip;
        return this;
    }

    public ESMButton setTooltip(Supplier<String> supplier) {
        this.tooltipSupplier = supplier == null ? () -> "" : supplier;
        return this;
    }

    public ESMButton setIcon(@Nullable Icon icon) {
        setIcon(icon, width, height);
        return this;
    }

    public ESMButton setIcon(@Nullable Icon icon, int width, int height) {
        setIcon(icon, 0, 0, width, height);
        return this;
    }

    public ESMButton setIcon(@Nullable Icon icon, int xOffset, int yOffset, int width, int height) {
        this.icon = icon;
        this.iconWidth = width;
        this.iconHeight = height;
        this.iconXOffset = xOffset;
        this.iconYOffset = yOffset;
        return this;
    }

    public ESMButton setClickAction(Runnable runnable) {
        this.runnable = runnable;
        return this;
    }

    public boolean isVisible() {
        return visible && (visibilitySupplier == null || visibilitySupplier.getAsBoolean());
    }

    public boolean isMouseOver(int mouseX, int mouseY) {
        return mouseX >= this.xPosition && mouseY >= this.yPosition
            && mouseX < this.xPosition + this.width
            && mouseY < this.yPosition + this.height;
    }

    public String getTooltipForMouse(int mouseX, int mouseY) {
        if (!isVisible() || !isMouseOver(mouseX, mouseY)) return "";
        String tooltipText = tooltipSupplier.get();
        return tooltipText == null ? "" : tooltipText;
    }
}
