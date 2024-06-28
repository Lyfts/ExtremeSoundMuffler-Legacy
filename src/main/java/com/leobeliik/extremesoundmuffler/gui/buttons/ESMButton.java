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

    private final Runnable runnable;
    protected boolean renderNormal = false;
    protected boolean renderText = false;
    protected int textColor = whiteText;
    protected boolean hasTooltip = false;
    protected boolean renderTooltipAbove = true;
    protected String tooltip;
    protected Supplier<String> tooltipSupplier;
    protected BooleanSupplier visibilitySupplier;
    protected Icon icon;
    protected int iconWidth;
    protected int iconHeight;

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
                SoundMuffler.renderGui();
                Icon.drawIcon(xPosition, yPosition, iconWidth, iconHeight, icon);
            }
        }

        if (hasTooltip && isMouseOver(mouseX, mouseY)) {
            if (tooltipSupplier != null) tooltip = tooltipSupplier.get();
            int stringW = mc.fontRenderer.getStringWidth(tooltip) / 2;
            if (!renderTooltipAbove) {
                drawRect(
                    xPosition - stringW + 3,
                    yPosition + height + 2,
                    xPosition + stringW + 10,
                    yPosition + height + 15,
                    darkBG);
                drawCenteredString(mc.fontRenderer, tooltip, xPosition + 8, yPosition + height + 4, whiteText);
            } else {
                drawRect(xPosition - stringW + 3, yPosition - 2, xPosition + stringW + 10, yPosition - 15, darkBG);
                drawCenteredString(mc.fontRenderer, tooltip, xPosition + 8, yPosition - 12, whiteText);
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
        this.visible = supplier.getAsBoolean();
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

    public ESMButton setTooltip(String tooltip, boolean above) {
        this.tooltip = tooltip;
        this.renderTooltipAbove = above;
        this.hasTooltip = true;
        return this;
    }

    public ESMButton setTooltip(Supplier<String> supplier, boolean above) {
        this.tooltipSupplier = supplier;
        this.renderTooltipAbove = above;
        this.hasTooltip = true;
        return this;
    }

    public ESMButton setIcon(@Nullable Icon icon) {
        setIcon(icon, width, height);
        return this;
    }

    public ESMButton setIcon(@Nullable Icon icon, int width, int height) {
        this.icon = icon;
        this.iconWidth = width;
        this.iconHeight = height;
        return this;
    }

    public boolean isVisible() {
        return visible || (visibilitySupplier != null && visibilitySupplier.getAsBoolean());
    }

    public boolean isMouseOver(int mouseX, int mouseY) {
        return mouseX >= this.xPosition && mouseY >= this.yPosition
            && mouseX < this.xPosition + this.width
            && mouseY < this.yPosition + this.height;
    }
}
