package com.leobeliik.extremesoundmuffler.gui.buttons;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;

public class PlaySoundButton extends ESMButton {

    private final ResourceLocation sound;
    private static boolean isFromPSB = false;

    PlaySoundButton(int x, int y, ResourceLocation sound) {
        super(0, x, y, 10, 10, "");
        this.sound = sound;
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        this.func_146113_a(mc.getSoundHandler());
        return false;
    }

    public void func_146113_a(SoundHandler soundHandlerIn) {
        isFromPSB = true;
        // TODO: Sound category
        soundHandlerIn.playSound(
            new PositionedSoundRecord(sound, SoundCategory.MASTER,
                1.0F, 1.0F, false,
                0, ISound.AttenuationType.NONE,
                0.0F, 0.0F, 0.0F)
        );
        isFromPSB = false;
    }

    public static boolean isFromPSB() {
        return isFromPSB;
    }
}
