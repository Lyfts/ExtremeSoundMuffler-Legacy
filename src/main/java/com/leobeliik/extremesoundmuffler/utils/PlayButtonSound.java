package com.leobeliik.extremesoundmuffler.utils;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import org.jetbrains.annotations.NotNull;

public class PlayButtonSound extends PositionedSoundRecord {

    public PlayButtonSound(ResourceLocation soundResource) {
        super(new SoundEvent(soundResource), SoundCategory.MASTER, 1f, 1f, 0, 0, 0);
    }

    @Override
    public float getVolume() {
        return 1f;
    }

    @Override
    public @NotNull AttenuationType getAttenuationType() {
        return AttenuationType.NONE;
    }
}
