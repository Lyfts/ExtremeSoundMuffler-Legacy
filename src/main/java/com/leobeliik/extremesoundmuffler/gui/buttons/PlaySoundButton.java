package com.leobeliik.extremesoundmuffler.gui.buttons;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class PlaySoundButton extends ESMButton {

    private final ResourceLocation sound;
    private static boolean isFromPSB = false;

    PlaySoundButton(int x, int y, ResourceLocation sound) {
        super(0, x, y, 10, 10, "");
        this.sound = sound;
        setClickAction(this::playSound);
    }

    public void playSound() {
        isFromPSB = true;
        Minecraft.getMinecraft().getSoundHandler().playSound(
            PositionedSoundRecord.getRecord(new SoundEvent(sound), 1.0F, 1.0F));
        isFromPSB = false;
    }

    public static boolean isFromPSB() {
        return isFromPSB;
    }
}
