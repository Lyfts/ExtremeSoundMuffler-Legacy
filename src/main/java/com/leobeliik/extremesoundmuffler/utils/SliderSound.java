package com.leobeliik.extremesoundmuffler.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ResourceLocation;

public class SliderSound extends MovingSound {

    private final EntityPlayerSP player;

    public SliderSound(ResourceLocation sound) {
        super(sound);
        this.player = Minecraft.getMinecraft().thePlayer;
    }

    @Override
    public void update() {}

    public void finishPlaying() {
        donePlaying = true;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    @Override
    public float getXPosF() {
        return (float) player.posX;
    }

    @Override
    public float getYPosF() {
        return (float) player.posY;
    }

    @Override
    public float getZPosF() {
        return (float) player.posZ;
    }

    @Override
    public float getPitch() {
        return 1f;
    }

    @Override
    public boolean canRepeat() {
        return !donePlaying;
    }

    @Override
    public int getRepeatDelay() {
        return 0;
    }

    @Override
    public AttenuationType getAttenuationType() {
        return AttenuationType.NONE;
    }
}
