package com.leobeliik.extremesoundmuffler.mixins.minecraft;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundManager;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import com.leobeliik.extremesoundmuffler.Config;
import com.leobeliik.extremesoundmuffler.gui.MainScreen;
import com.leobeliik.extremesoundmuffler.gui.buttons.PlaySoundButton;
import com.leobeliik.extremesoundmuffler.interfaces.ISoundLists;
import com.leobeliik.extremesoundmuffler.utils.Anchor;
import com.leobeliik.extremesoundmuffler.utils.ComparableResource;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;

@Mixin(SoundManager.class)
public abstract class SoundMixin implements ISoundLists {

    @Unique
    private static boolean extremeSoundMuffler$isForbidden(ISound sound) {
        for (String fs : forbiddenSounds) {
            if (sound.getPositionedSoundLocation()
                .toString()
                .contains(fs)) {
                return true;
            }
        }
        return false;
    }

    @ModifyReturnValue(method = "getNormalizedVolume", at = @At("RETURN"))
    private float checkSound(float original, @Local(ordinal = 0, argsOnly = true) ISound sound) {
        if (extremeSoundMuffler$isForbidden(sound) || PlaySoundButton.isFromPSB()) {
            return original;
        }

        ComparableResource soundLocation = new ComparableResource(sound.getPositionedSoundLocation());

        recentSoundsList.add(soundLocation);

        if (MainScreen.isMuffled()) {
            if (muffledSounds.containsKey(soundLocation)) {
                return original * muffledSounds.get(soundLocation);
            }

            if (Config.getDisableAnchors()) {
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
