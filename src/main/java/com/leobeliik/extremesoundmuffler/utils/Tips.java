package com.leobeliik.extremesoundmuffler.utils;

import java.util.Random;

import net.minecraft.client.resources.I18n;

public enum Tips {

    disable("esm.tip.disable"),
    change_volume("esm.tip.change_volume"),
    inv_button("esm.tip.inv_button"),
    inv_button_disable("esm.tip.inv_button_disable"),
    play_sound("esm.tip.play_sound"),
    unmuffle("esm.tip.unmuffle"),
    no_anchors("esm.tip.no_anchors"),
    sound_blacklist("esm.tip.sound_blacklist"),
    left_buttons("esm.tip.left_buttons"),
    dark_theme("esm.tip.dark_theme"),
    use_anchors("esm.tip.use_anchors"),
    set_anchors("esm.tip.set_anchors"),
    modify_anchors("esm.tip.modify_anchors"),
    modify_anchors_2("esm.tip.modify_anchors_2"),
    reset_recent_sounds("esm.tip.reset_recent_sounds");

    private static final Tips[] vals = values();

    private final String tip;

    Tips(String s) {
        tip = I18n.format(s);
    }

    public static String randomTip() {
        return vals[new Random().nextInt(vals.length)].toString();
    }

    public String toString() {
        return I18n.format("esm.main_screen.tip", tip);
    }
}
