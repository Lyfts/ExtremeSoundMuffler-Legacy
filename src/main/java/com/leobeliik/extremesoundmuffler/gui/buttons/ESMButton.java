package com.leobeliik.extremesoundmuffler.gui.buttons;

import com.leobeliik.extremesoundmuffler.SoundMuffler;
import com.leobeliik.extremesoundmuffler.interfaces.IColorsGui;
import com.leobeliik.extremesoundmuffler.utils.Icon;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public class ESMButton extends GuiButtonExt implements IColorsGui {

    private Runnable runnable;
    protected boolean renderNormal = false;
    protected boolean renderText = false;
    protected int textColor = whiteText;
    protected boolean renderTooltipAbove = true;
    protected String tooltip = "";
    protected Supplier<String> tooltipSupplier;
    protected BooleanSupplier visibilitySupplier;
    protected Icon icon;
    protected int iconWidth;
    protected int iconHeight;
    protected int iconXOffset;
    protected int iconYOffset;
    public boolean mouseOver;

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
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partial) {
        if (!isVisible()) return;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mouseOver = isMouseOver(mouseX, mouseY);

        if (renderNormal) {
            super.drawButton(mc, mouseX, mouseY, partial);
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
                    this.x + this.width / 2,
                    this.y + (this.height - 8) / 2,
                    textColor);
            }

            if (icon != null) {
                SoundMuffler.renderGui();
                icon.draw(x + iconXOffset, y + iconYOffset, iconWidth, iconHeight);
            }
        }

        if (hasTooltip() && isMouseOver(mouseX, mouseY)) {
            if (tooltipSupplier != null) tooltip = tooltipSupplier.get();
            int stringW = mc.fontRenderer.getStringWidth(tooltip) / 2;
            if (!renderTooltipAbove) {
                drawRect(
                    x - stringW + 3,
                    y + height + 2,
                    x + stringW + 10,
                    y + height + 15,
                    darkBG);
                drawCenteredString(mc.fontRenderer, tooltip, x + 8, y + height + 4, whiteText);
            } else {
                drawRect(x - stringW + 3, y - 2, x + stringW + 10, y - 15, darkBG);
                drawCenteredString(mc.fontRenderer, tooltip, x + 8, y - 12, whiteText);
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

    public ESMButton setTooltip(String tooltip, boolean above) {
        this.tooltip = tooltip;
        this.renderTooltipAbove = above;
        return this;
    }

    public ESMButton setTooltip(Supplier<String> supplier, boolean above) {
        this.tooltipSupplier = supplier;
        this.renderTooltipAbove = above;
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

    private boolean hasTooltip() {
        return !tooltip.isEmpty() || tooltipSupplier != null && !tooltipSupplier.get()
            .isEmpty();
    }

    public boolean isVisible() {
        return visible && (visibilitySupplier == null || visibilitySupplier.getAsBoolean());
    }

    public boolean isMouseOver(int mouseX, int mouseY) {
        return mouseX >= this.x && mouseY >= this.y
               && mouseX < this.x + this.width
               && mouseY < this.y + this.height;
    }
}
