package com.leobeliik.extremesoundmuffler.gui.buttons;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;

import com.leobeliik.extremesoundmuffler.Config;
import com.leobeliik.extremesoundmuffler.gui.MainScreen;
import com.leobeliik.extremesoundmuffler.interfaces.IColorsGui;
import com.leobeliik.extremesoundmuffler.utils.Icon;

public class InvButton extends ESMButton implements IColorsGui {

    private final GuiContainer parent;
    private boolean hold = false;

    public InvButton(GuiContainer parentGui, int x, int y) {
        super(1001, parentGui.guiLeft + x, parentGui.guiTop + y, 11, 11, "");
        parent = parentGui;
        setIcon(Icon.MUFFLE_ON, 11, 11);
        setTooltip("Muffler", true);
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (isVisible() && hold) {
            drag(mouseX, mouseY);
        }

        super.drawButton(mc, mouseX, mouseY);
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (super.mousePressed(mc, mouseX, mouseY)) {
            if (GuiScreen.isCtrlKeyDown() && isMouseOver(mouseX, mouseY)) {
                hold = true;
            } else {
                MainScreen.open();
            }
            return true;
        }
        return false;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        if (hold) {
            hold = false;
            Config.setInvButtonPosition(xPosition - parent.guiLeft, yPosition - parent.guiTop);
        }
        super.mouseReleased(mouseX, mouseY);
    }

    private void drag(int mouseX, int mouseY) {
        xPosition = mouseX - (this.width / 2);
        yPosition = mouseY - (this.height / 2);
    }
}
