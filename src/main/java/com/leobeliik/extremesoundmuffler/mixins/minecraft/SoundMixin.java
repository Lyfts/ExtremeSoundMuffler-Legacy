package com.leobeliik.extremesoundmuffler.mixins.minecraft;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.util.ResourceLocation;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import com.leobeliik.extremesoundmuffler.ESMConfig;
import com.leobeliik.extremesoundmuffler.gui.MainScreen;
import com.leobeliik.extremesoundmuffler.interfaces.ISoundLists;
import com.leobeliik.extremesoundmuffler.utils.Anchor;
import com.leobeliik.extremesoundmuffler.utils.PlayButtonSound;
import com.leobeliik.extremesoundmuffler.utils.SliderSound;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;

@Mixin(SoundManager.class)
public abstract class SoundMixin implements ISoundLists {

    @Unique
    private static boolean extremeSoundMuffler$isForbidden(ISound sound) {
        return forbiddenSounds.stream().anyMatch(fs -> sound.getSoundLocation()
                .toString()
                .contains(fs));
    }

    @ModifyReturnValue(method = "getClampedVolume", at = @At("RETURN"))
    private float checkSound(float original, @Local(ordinal = 0, argsOnly = true) ISound sound) {
        if (extremeSoundMuffler$isForbidden(sound) || sound instanceof PlayButtonSound ||
                sound instanceof SliderSound) {
            return original;
        }

        ResourceLocation soundLocation = sound.getSoundLocation();

        recentSoundsList.add(soundLocation);

        if (MainScreen.isMuffled()) {
            if (muffledSounds.containsKey(soundLocation)) {
                return original * muffledSounds.get(soundLocation);
            }

            if (ESMConfig.getDisableAnchors()) {
                return original;
            }

            Anchor anchor = Anchor.getAnchor(sound);
            if (anchor != null) {
                return original * anchor.getMuffledSounds()
                        .get(soundLocation);
            }
        }
        return original;
    }
}
