package com.leobeliik.extremesoundmuffler.mixins.minecraft;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.client.audio.SoundPoolEntry;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.leobeliik.extremesoundmuffler.Config;
import com.leobeliik.extremesoundmuffler.gui.MainScreen;
import com.leobeliik.extremesoundmuffler.gui.buttons.PlaySoundButton;
import com.leobeliik.extremesoundmuffler.interfaces.ISoundLists;
import com.leobeliik.extremesoundmuffler.utils.Anchor;
import com.leobeliik.extremesoundmuffler.utils.ComparableResource;

@Mixin(SoundManager.class)
public abstract class SoundMixin implements ISoundLists {

    // @Inject(method = "calculateVolume", at = @At("RETURN"), cancellable = true)
    // private void calculateSoundVolume(ISound sound, CallbackInfoReturnable<Float> cir) {
    // if (isForbidden(sound) || PlaySoundButton.isFromPSB()) {
    // return;
    // }
    //
    // recentSoundsList.add(sound.getLocation());
    //
    // if (MainScreen.isMuffled()) {
    // if (muffledSounds.containsKey(sound.getLocation())) {
    // cir.setReturnValue(cir.getReturnValue() * muffledSounds.get(sound.getLocation()));
    // return;
    // }
    //
    // if (Config.getDisableAchors()) {
    // return;
    // }
    //
    // Anchor anchor = Anchor.getAnchor(sound);
    // if (anchor != null) {
    // cir.setReturnValue(cir.getReturnValue() * anchor.getMuffledSounds().get(sound.getLocation()));
    // }
    // }
    // }
    //
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

    @Inject(method = "getNormalizedVolume", at = @At("RETURN"), cancellable = true, remap = false)
    private void extremeSoundMuffler$checkSound(ISound sound, SoundPoolEntry p_148594_2_, SoundCategory p_148594_3_,
        CallbackInfoReturnable<Float> cir) {
        if (extremeSoundMuffler$isForbidden(sound) || PlaySoundButton.isFromPSB()) {
            return;
        }

        ComparableResource soundLocation = new ComparableResource(sound.getPositionedSoundLocation());

        recentSoundsList.add(soundLocation);

        if (MainScreen.isMuffled()) {
            if (muffledSounds.containsKey(soundLocation)) {
                cir.setReturnValue(cir.getReturnValue() * muffledSounds.get(soundLocation));
                return;
            }

            if (Config.getDisableAchors()) {
                return;
            }

            Anchor anchor = Anchor.getAnchor(sound);
            if (anchor != null) {
                cir.setReturnValue(
                    cir.getReturnValue() * anchor.getMuffledSounds()
                        .get(sound.getPositionedSoundLocation()));
            }
        }
    }
}
