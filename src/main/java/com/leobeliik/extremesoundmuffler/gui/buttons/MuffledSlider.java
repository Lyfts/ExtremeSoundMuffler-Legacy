package com.leobeliik.extremesoundmuffler.gui.buttons;

import com.leobeliik.extremesoundmuffler.ESMConfig;
import com.leobeliik.extremesoundmuffler.SoundMuffler;
import com.leobeliik.extremesoundmuffler.gui.MainScreen;
import com.leobeliik.extremesoundmuffler.interfaces.ISoundLists;
import com.leobeliik.extremesoundmuffler.utils.Anchor;
import com.leobeliik.extremesoundmuffler.utils.PlayButtonSound;
import com.leobeliik.extremesoundmuffler.utils.SliderSound;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.leobeliik.extremesoundmuffler.utils.Icon.MUFFLE_OFF;
import static com.leobeliik.extremesoundmuffler.utils.Icon.MUFFLE_ON;
import static com.leobeliik.extremesoundmuffler.utils.Icon.PLAY;

public class MuffledSlider extends ESMButton implements ISoundLists {

    private final Anchor anchor;
    private float sliderValue;
    private ESMButton btnToggleSound;
    private final ResourceLocation sound;
    public static SliderSound tickSound;
    public static boolean showSlider = false;
    private boolean isDragging;
    private final List<ESMButton> subButtons = new ArrayList<>();
    private boolean muffled = false;

    public MuffledSlider(int x, int y, int width, int height, float sliderValue, ResourceLocation sound,
                         Anchor anchor) {
        super(0, x, y, width, height, sound.toString());
        this.sliderValue = sliderValue;
        this.sound = sound;
        this.anchor = anchor;
        refreshButtons();
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partial) {
        if (!isVisible()) return;
        SoundMuffler.renderGui();
        setTextColor(muffled ? cyanText : whiteText);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_LIGHTING);
        drawSubButtons(mc, mouseX, mouseY, partial);
        drawGradient(mouseX, mouseY);
        drawMessage(mc, mouseX, mouseY);
        mouseDragged(mc, mouseX, mouseY);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void drawMessage(Minecraft minecraft, int mouseX, int mouseY) {
        FontRenderer font = minecraft.fontRenderer;
        int v = Math.max(width, font.getStringWidth(displayString));
        if (showSlider && isMouseOver(mouseX, mouseY)) {
            drawCenteredString(
                font,
                "Volume: " + (int) (sliderValue * 100),
                x + (width / 2),
                y + 2,
                whiteText); // title
        } else {
            String msgTruncated;
            if (isMouseOver(mouseX, mouseY)) {
                msgTruncated = displayString;
                drawRect(
                    x + width + 3,
                    y,
                    x + v + 3,
                    y + font.FONT_HEIGHT + 2,
                    darkBG);
            } else {
                msgTruncated = font.trimStringToWidth(displayString, 205);
            }
            font.drawStringWithShadow(msgTruncated, x + 2, y + 2, textColor); // title
        }
    }

    private void drawGradient(int mouseX, int mouseY) {
        if (muffled) {
            drawTexturedModalRect(
                x,
                y - 1,
                0,
                234,
                (int) (sliderValue * (width - 6)) + 5,
                height + 1); // draw bg

            if (isMouseOver(mouseX, mouseY) && showSlider) {
                drawTexturedModalRect(
                    x + (int) (sliderValue * (width - 6)) + 1,
                    y + 1,
                    32,
                    224,
                    5,
                    9); // Slider
            }
        }
    }

    private void drawSubButtons(Minecraft mc, int mouseX, int mouseY, float partial) {
        btnToggleSound.setIcon(muffled ? MUFFLE_ON : MUFFLE_OFF);
        for (GuiButton button : subButtons) {
            button.drawButton(mc, mouseX, mouseY, partial);
        }
    }

    public void refreshButtons() {
        SoundHandler soundHandler = Minecraft.getMinecraft().getSoundHandler();
        subButtons.clear();
        int aX = ESMConfig.getLeftButtons() ? x - 24 : x + width + 5;
        subButtons.add(btnToggleSound = new ESMButton(0, aX, y, 11, 11, "", this::toggleSound));
        subButtons.add(new ESMButton(0, btnToggleSound.x + 12, y, 10, 10,
            () -> soundHandler.playSound(new PlayButtonSound(sound))).setIcon(PLAY));
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
                setSliderValue(ESMConfig.getDefaultMuteVolume());
                muffledSounds.put(sound, sliderValue);
                didMuffle = true;
            } else if (anchor.getAnchorPos() != null) {
                setSliderValue(ESMConfig.getDefaultMuteVolume());
                anchor.addSound(sound, sliderValue);
                didMuffle = true;
            }
            setMuffled(didMuffle);
        }
    }

    private void changeSliderValue(float mouseX) {
        setSliderValue((mouseX - (x + 4)) / (width - 8));
    }

    private void setSliderValue(float value) {
        sliderValue = MathHelper.clamp(value, 0.0F, 1F);
        setTickSound(sliderValue);
        if (sliderValue == 1F) {
            toggleSound();
        } else {
            updateVolume();
        }
    }

    @Override
    protected void mouseDragged(@NotNull Minecraft mc, int mouseX, int mouseY) {
        if (isDragging && showSlider) {
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
        stopTickSound();
        super.mouseReleased(mouseX, mouseY);
    }

    public static void stopTickSound() {
        if (tickSound != null) {
            tickSound.finishPlaying();
            tickSound = null;
        }
    }

    public void setTickSound(float volume) {
        SoundHandler soundHandler = Minecraft.getMinecraft()
            .getSoundHandler();
        if (volume == 1f || !muffled) {
            stopTickSound();
            return;
        }

        if (tickSound == null) {
            soundHandler.playSound(tickSound = new SliderSound(sound));
        }

        tickSound.setVolume(volume);
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
