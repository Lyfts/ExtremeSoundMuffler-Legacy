package com.leobeliik.extremesoundmuffler.gui.buttons;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;

public class PlaySoundButton extends GuiButton {

    private final ISound sound;
    private static boolean isFromPSB = false;

    PlaySoundButton(int x, int y, ISound sound) {
        super(0, x, y, 10, 10, "");
        // this.setAlpha(0);
        this.sound = sound;
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        return false;
    }

    // @Override
    // public void playDownSound(SoundManager soundHandler) {
    // isFromPSB = true;
    // soundHandler.play(SimpleSoundInstance.forUI(this.sound, 1.0F));
    // isFromPSB = false;
    // //it maybe a mess but it does prevent to sounds to get muted when they're player from this button
    // }

    public void func_146113_a(SoundHandler soundHandlerIn) {
        isFromPSB = true;
        soundHandlerIn.playSound(sound);
        isFromPSB = false;
        // it maybe a mess but it does prevent to sounds to get muted when they're player from this button
    }

    public static boolean isFromPSB() {
        return isFromPSB;
    }

    // @Override
    // public void updateNarration(NarrationElementOutput elementOutput) {
    // elementOutput.add(NarratedElementType.TITLE, this.createNarrationMessage());
    // }
}
