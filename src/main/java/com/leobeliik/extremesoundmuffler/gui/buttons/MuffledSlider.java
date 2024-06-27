package com.leobeliik.extremesoundmuffler.gui.buttons;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.leobeliik.extremesoundmuffler.Config;
import com.leobeliik.extremesoundmuffler.SoundMuffler;
import com.leobeliik.extremesoundmuffler.gui.MainScreen;
import com.leobeliik.extremesoundmuffler.interfaces.IColorsGui;
import com.leobeliik.extremesoundmuffler.interfaces.ISoundLists;
import com.leobeliik.extremesoundmuffler.utils.Anchor;
import com.leobeliik.extremesoundmuffler.utils.ComparableResource;
import com.leobeliik.extremesoundmuffler.utils.ESMButton;

@SuppressWarnings("EmptyMethod")
public class MuffledSlider extends GuiButton implements ISoundLists, IColorsGui {

    private final String mainTitle = "ESM - Main Screen";
    private final String screenTitle;
    private final Anchor anchor;
    private float sliderValue;
    private GuiButton btnToggleSound;
    private PlaySoundButton btnPlaySound;
    private final ComparableResource sound;
    public static ResourceLocation tickSound;
    public static boolean showSlider = false;
    private boolean isDragging;
    private final List<GuiButton> subButtons = new ArrayList<>();

    public MuffledSlider(int x, int y, int width, int height, float sliderValue, ComparableResource sound,
        String screenTitle, Anchor anchor) {
        super(0, x, y, width, height, sound.getResourceDomain() + ":" + sound.getResourcePath());
        this.sliderValue = sliderValue;
        this.sound = sound;
        this.screenTitle = screenTitle;
        this.anchor = anchor;
        packedFGColour = whiteText;
        refreshButtons();
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (!visible || !enabled) return;
        SoundMuffler.renderGui();
        this.field_146123_n = mouseX >= this.xPosition && mouseY >= this.yPosition
            && mouseX < this.xPosition + this.width
            && mouseY < this.yPosition + this.height;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        drawGradient();
        int v = packedFGColour == whiteText ? 213 : 202;
        drawTexturedModalRect(btnToggleSound.xPosition, btnToggleSound.yPosition, 43, v, 11, 11);
        drawTexturedModalRect(btnPlaySound.xPosition, btnPlaySound.yPosition, 32, 202, 11, 11);
        this.drawMessage(mc);

        for (GuiButton button : subButtons) {
            button.drawButton(mc, mouseX, mouseY);
        }

        this.mouseDragged(mc, mouseX, mouseY);
    }

    private void drawMessage(Minecraft minecraft) {
        FontRenderer font = minecraft.fontRenderer;
        int v = Math.max(this.width, font.getStringWidth(displayString));
        if (showSlider && this.visible && this.func_146115_a()) {
            drawCenteredString(
                font,
                "Volume: " + (int) (sliderValue * 100),
                this.xPosition + (this.width / 2),
                this.yPosition + 2,
                whiteText); // title
        } else {
            String msgTruncated;
            if (this.field_146123_n) {
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
            font.drawStringWithShadow(msgTruncated, this.xPosition + 2, this.yPosition + 2, packedFGColour); // title
        }
    }

    private void drawGradient() {
        if (packedFGColour == cyanText) {
            func_146110_a(
                this.xPosition,
                this.yPosition - 1,
                0f,
                234f,
                (int) (sliderValue * (width - 6)) + 5,
                height + 1,
                256,
                256); // draw bg

            if (this.field_146123_n) {
                func_146110_a(
                    this.xPosition + (int) (sliderValue * (width - 6)) + 1,
                    this.yPosition + 1,
                    32f,
                    224f,
                    5,
                    9,
                    256,
                    256); // Slider
            }
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    public void refreshButtons() {
        subButtons.clear();
        int x = Config.getLeftButtons() ? this.xPosition - 24 : this.xPosition + width + 5;
        btnToggleSound = new ESMButton(0, x, this.yPosition, 11, 11, "", this::toggleSound);
        btnPlaySound = new PlaySoundButton(btnToggleSound.xPosition + 12, this.yPosition, sound);
        subButtons.add(btnToggleSound);
        subButtons.add(btnPlaySound);
    }

    public GuiButton getBtnToggleSound() {
        return btnToggleSound;
    }

    public PlaySoundButton getBtnPlaySound() {
        return btnPlaySound;
    }

    private void toggleSound() {
        if (packedFGColour == cyanText) {
            if (screenTitle.equals(mainTitle)) {
                muffledSounds.remove(sound);
            } else {
                anchor.removeSound(sound);
            }
            packedFGColour = whiteText;
        } else {
            if (screenTitle.equals(mainTitle)) {
                setSliderValue(Config.getDefaultMuteVolume());
                muffledSounds.put(sound, sliderValue);
            } else if (anchor.getAnchorPos() != null) {
                setSliderValue(Config.getDefaultMuteVolume());
                anchor.addSound(sound, sliderValue);
            }
            packedFGColour = cyanText;
        }
    }

    // @Override
    // public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    // boolean flag = keyCode == 263;
    // if (flag || keyCode == 262) {
    // float f = flag ? -1.0F : 1.0F;
    // this.setSliderValue((float) (this.sliderValue + (f / (this.width - 8))));
    // }
    // return false;
    // }

    private void changeSliderValue(float mouseX) {
        this.setSliderValue((mouseX - (this.xPosition + 4)) / (this.width - 8));
    }

    private void setSliderValue(float value) {
        this.sliderValue = MathHelper.clamp_float(value, 0.0F, 1F);
        updateVolume();
    }

    @Override
    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
        if (isDragging) {
            this.changeSliderValue((float) mouseX);
        }
        super.mouseDragged(mc, mouseX, mouseY);
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (!visible || !enabled) return false;
        for (GuiButton button : subButtons) {
            if (!button.func_146115_a()) continue;
            button.mousePressed(mc, mouseX, mouseY);
        }

        if (super.mousePressed(mc, mouseX, mouseY)) {
            isDragging = true;
            showSlider = true;
            tickSound = this.sound;
            return true;
        }

        return false;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        if (!visible || !enabled) return;

        for (GuiButton button : subButtons) {
            if (!button.func_146115_a()) continue;
            button.mouseReleased(mouseX, mouseY);
        }

        isDragging = false;
        super.mouseReleased(mouseX, mouseY);
    }

    private void updateVolume() {
        String screenTitle = MainScreen.getScreenTitle();

        if (screenTitle.equals(mainTitle)) {
            muffledSounds.replace(this.sound, this.sliderValue);
        } else {
            Objects.requireNonNull(MainScreen.getAnchorByName(screenTitle))
                .replaceSound(this.sound, this.sliderValue);
        }
    }
}
