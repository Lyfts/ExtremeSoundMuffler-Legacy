package com.leobeliik.extremesoundmuffler.utils;

import cpw.mods.fml.client.config.GuiUtils;

public enum Icon {

    PLAY(32, 202),
    MUFFLE_ON(43, 202),
    MUFFLE_OFF(43, 213),
    MUFFLE(54, 202),
    RESET(54, 217),
    EDIT_ANCHOR(32, 213),
    ANCHOR(71, 202);

    final int u;
    final int v;

    Icon(int x, int y) {
        this.u = x;
        this.v = y;
    }

    public static void drawIcon(int x, int y, int width, int height, Icon icon) {
        GuiUtils.drawTexturedModalRect(x, y, icon.u, icon.v, width, height, 0);
    }
}
