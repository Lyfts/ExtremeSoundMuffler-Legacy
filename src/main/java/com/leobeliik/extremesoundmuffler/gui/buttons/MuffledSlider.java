package com.leobeliik.extremesoundmuffler.gui.buttons;

import static com.leobeliik.extremesoundmuffler.utils.Icon.MUFFLE_OFF;
import static com.leobeliik.extremesoundmuffler.utils.Icon.MUFFLE_ON;
import static com.leobeliik.extremesoundmuffler.utils.Icon.PLAY;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.leobeliik.extremesoundmuffler.Config;
import com.leobeliik.extremesoundmuffler.SoundMuffler;
import com.leobeliik.extremesoundmuffler.gui.MainScreen;
import com.leobeliik.extremesoundmuffler.interfaces.ISoundLists;
import com.leobeliik.extremesoundmuffler.utils.Anchor;
import com.leobeliik.extremesoundmuffler.utils.ComparableResource;

public class MuffledSlider extends ESMButton implements ISoundLists {

    private final Anchor anchor;
    private float sliderValue;
    private ESMButton btnToggleSound;
    private final ComparableResource sound;
    public static ResourceLocation tickSound;
    public static boolean showSlider = false;
    private boolean isDragging;
    private final List<ESMButton> subButtons = new ArrayList<>();
    private boolean muffled = false;

    public MuffledSlider(int x, int y, int width, int height, float sliderValue, ComparableResource sound,
        Anchor anchor) {
        super(0, x, y, width, height, sound.toString());
        this.sliderValue = sliderValue;
        this.sound = sound;
        this.anchor = anchor;
        refreshButtons();
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (!visible) return;
        SoundMuffler.renderGui();
        setTextColor(muffled ? cyanText : whiteText);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_LIGHTING);
        drawSubButtons(mc, mouseX, mouseY);
        drawGradient(mouseX, mouseY);
        drawMessage(mc, mouseX, mouseY);
        mouseDragged(mc, mouseX, mouseY);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void drawMessage(Minecraft minecraft, int mouseX, int mouseY) {
        FontRenderer font = minecraft.fontRenderer;
        int v = Math.max(width, font.getStringWidth(displayString));
        if (showSlider && visible && isMouseOver(mouseX, mouseY)) {
            drawCenteredString(
                font,
                "Volume: " + (int) (sliderValue * 100),
                this.xPosition + (this.width / 2),
                this.yPosition + 2,
                whiteText); // title
        } else {
            String msgTruncated;
            if (isMouseOver(mouseX, mouseY)) {
                msgTruncated = displayString;
                drawRect(
                    this.xPosition + this.width + 3,
                    this.yPosition,
                    this.xPosition + v + 3,
                    this.yPosition + font.FONT_HEIGHT + 2,
                    darkBG);
            } else {
                msgTruncated = font.trimStringToWidth(displayString, 205);
            }
            font.drawStringWithShadow(msgTruncated, this.xPosition + 2, this.yPosition + 2, textColor); // title
        }
    }

    private void drawGradient(int mouseX, int mouseY) {
        if (muffled) {
            drawTexturedModalRect(
                this.xPosition,
                this.yPosition - 1,
                0,
                234,
                (int) (sliderValue * (width - 6)) + 5,
                height + 1); // draw bg

            if (isMouseOver(mouseX, mouseY)) {
                drawTexturedModalRect(
                    this.xPosition + (int) (sliderValue * (width - 6)) + 1,
                    this.yPosition + 1,
                    32,
                    224,
                    5,
                    9); // Slider
            }
        }
    }

    private void drawSubButtons(Minecraft mc, int mouseX, int mouseY) {
        btnToggleSound.setIcon(muffled ? MUFFLE_ON : MUFFLE_OFF);
        for (GuiButton button : subButtons) {
            button.drawButton(mc, mouseX, mouseY);
        }
    }

    public void refreshButtons() {
        subButtons.clear();
        int x = Config.getLeftButtons() ? this.xPosition - 24 : this.xPosition + width + 5;
        subButtons.add(btnToggleSound = new ESMButton(0, x, this.yPosition, 11, 11, "", this::toggleSound));
        subButtons.add(new PlaySoundButton(btnToggleSound.xPosition + 12, this.yPosition, sound).setIcon(PLAY));
    }

    public ESMButton getBtnToggleSound() {
        return btnToggleSound;
    }

    private void toggleSound() {
        if (muffled) {
            if (MainScreen.isMain()) {
                muffledSounds.remove(sound);
            } else {
                anchor.removeSound(sound);
            }
            setMuffled(false);
        } else {
            boolean didMuffle = false;
            if (MainScreen.isMain()) {
                setSliderValue(Config.getDefaultMuteVolume());
                muffledSounds.put(sound, sliderValue);
                didMuffle = true;
            } else if (anchor.getAnchorPos() != null) {
                setSliderValue(Config.getDefaultMuteVolume());
                anchor.addSound(sound, sliderValue);
                didMuffle = true;
            }
            setMuffled(didMuffle);
        }
    }

    private void changeSliderValue(float mouseX) {
        setSliderValue((mouseX - (xPosition + 4)) / (width - 8));
    }

    private void setSliderValue(float value) {
        sliderValue = MathHelper.clamp_float(value, 0.0F, 1F);
        if (sliderValue == 1F) {
            toggleSound();
        } else {
            updateVolume();
        }
    }

    @Override
    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
        if (isDragging) {
            if (!muffled && canMuffle()) toggleSound();
            changeSliderValue((float) mouseX);
        }
        super.mouseDragged(mc, mouseX, mouseY);
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (!isVisible() || !enabled) return false;
        for (ESMButton button : subButtons) {
            if (!button.isMouseOver(mouseX, mouseY)) continue;
            button.mousePressed(mc, mouseX, mouseY);
        }

        if (isMouseOver(mouseX, mouseY)) {
            isDragging = true;
            showSlider = true;
            tickSound = this.sound;
            return true;
        }

        return false;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        if (!isVisible() || !enabled) return;

        for (ESMButton button : subButtons) {
            if (!button.isMouseOver(mouseX, mouseY)) continue;
            button.mouseReleased(mouseX, mouseY);
        }

        isDragging = false;
        super.mouseReleased(mouseX, mouseY);
    }

    private void updateVolume() {
        if (MainScreen.isMain()) {
            muffledSounds.replace(sound, sliderValue);
        } else {
            Objects.requireNonNull(MainScreen.getCurrentAnchor())
                .replaceSound(sound, sliderValue);
        }
    }

    public MuffledSlider setMuffled(boolean muffled) {
        this.muffled = muffled;
        return this;
    }

    private boolean canMuffle() {
        return MainScreen.isMain() || anchor.getAnchorPos() != null;
    }
}
