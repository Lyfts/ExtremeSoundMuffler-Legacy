package com.leobeliik.extremesoundmuffler.utils;

import net.minecraftforge.fml.client.config.GuiUtils;

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

    public void draw(int x, int y, int width, int height) {
        GuiUtils.drawTexturedModalRect(x, y, u, v, width, height, 0);
    }
}
