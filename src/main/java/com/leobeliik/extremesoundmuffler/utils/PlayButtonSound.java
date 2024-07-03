package com.leobeliik.extremesoundmuffler.utils;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;

public class PlayButtonSound extends PositionedSoundRecord {

    public PlayButtonSound(ResourceLocation soundResource) {
        super(soundResource, 1f, 1f, 0, 0, 0);
    }

    @Override
    public float getVolume() {
        return 1f;
    }

    @Override
    public AttenuationType getAttenuationType() {
        return AttenuationType.NONE;
    }
}
