package com.leobeliik.extremesoundmuffler.gui.buttons;

import java.util.Objects;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import com.leobeliik.extremesoundmuffler.Config;
import com.leobeliik.extremesoundmuffler.SoundMuffler;
import com.leobeliik.extremesoundmuffler.gui.MainScreen;
import com.leobeliik.extremesoundmuffler.interfaces.IColorsGui;
import com.leobeliik.extremesoundmuffler.interfaces.ISoundLists;
import com.leobeliik.extremesoundmuffler.utils.Anchor;
import com.leobeliik.extremesoundmuffler.utils.ComparableResource;

@SuppressWarnings("EmptyMethod")
public class MuffledSlider extends GuiButton implements ISoundLists, IColorsGui {

    private final String mainTitle = "ESM - Main Screen";
    private double sliderValue;
    private GuiButton btnToggleSound;
    private PlaySoundButton btnPlaySound;
    private final ComparableResource sound;
    public static ResourceLocation tickSound;
    public static boolean showSlider = false;
    private boolean isDragging;

    public MuffledSlider(int x, int y, int width, int height, double sliderValue, ComparableResource sound,
        String screenTitle, Anchor anchor) {
        super(0, x, y, width, height, sound.getResourceDomain() + ":" + sound.getResourcePath());
        this.sliderValue = sliderValue;
        this.sound = sound;
        setBtnToggleSound(screenTitle, sound, anchor);
        setBtnPlaySound(sound);
        packedFGColour = whiteText;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (!visible || !enabled) return;
        SoundMuffler.renderGui();
        this.field_146123_n = mouseX >= this.xPosition && mouseY >= this.yPosition
            && mouseX < this.xPosition + this.width
            && mouseY < this.yPosition + this.height;
        drawGradient();
        float v = packedFGColour == whiteText ? 213F : 202F;
        func_146110_a(btnToggleSound.xPosition, btnToggleSound.yPosition, 43f, v, 11, 11, 256, 256); // muffle button bg
        func_146110_a(btnPlaySound.xPosition, btnPlaySound.yPosition, 32f, 202f, 11, 11, 256, 256); // play button bg
        this.drawMessage(mc);
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
        }
    }

    private void setBtnToggleSound(String screenTitle, ResourceLocation sound, Anchor anchor) {
        int x = Config.getLeftButtons() ? this.xPosition - 24 : this.xPosition + width + 5;
        ComparableResource soundResource = new ComparableResource(sound);
        btnToggleSound = new GuiButton(0, x, this.yPosition, 11, 11, "") {

            @Override
            public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
                if (!super.mousePressed(mc, mouseX, mouseY)) return false;
                if (packedFGColour == cyanText) {
                    if (screenTitle.equals(mainTitle)) {
                        muffledSounds.remove(soundResource);
                    } else {
                        anchor.removeSound(soundResource);
                    }
                    packedFGColour = whiteText;
                    return true;
                } else {
                    if (screenTitle.equals(mainTitle)) {
                        setSliderValue(Config.getDefaultMuteVolume());
                        muffledSounds.put(soundResource, (float) sliderValue);
                        return true;
                    } else if (anchor.getAnchorPos() != null) {
                        setSliderValue(Config.getDefaultMuteVolume());
                        anchor.addSound(soundResource, (float) sliderValue);
                        return true;
                    }

                    return false;
                }
            }
        };
    }

    public GuiButton getBtnToggleSound() {
        return btnToggleSound;
    }

    private void setBtnPlaySound(ResourceLocation sound) {
        btnPlaySound = new PlaySoundButton(
            btnToggleSound.xPosition + 12,
            this.yPosition,
            PositionedSoundRecord.func_147673_a(sound));
    }

    public PlaySoundButton getBtnPlaySound() {
        return btnPlaySound;
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
        this.sliderValue = MathHelper.clamp_double(value, 0.0F, 1F);
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
        if (super.mousePressed(mc, mouseX, mouseY)) {
            // this.changeSliderValue((float) mouseX);
            isDragging = true;
            showSlider = true;
            tickSound = this.sound;
            return true;
        }
        return false;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        isDragging = false;
        super.mouseReleased(mouseX, mouseY);
    }

    private void updateVolume() {
        String screenTitle = MainScreen.getScreenTitle();

        if (screenTitle.equals(mainTitle)) {
            muffledSounds.replace(this.sound, (float) this.sliderValue);
        } else {
            Objects.requireNonNull(MainScreen.getAnchorByName(screenTitle))
                .replaceSound(this.sound, (float) this.sliderValue);
        }
    }
}
