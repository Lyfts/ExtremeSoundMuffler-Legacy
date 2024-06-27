package com.leobeliik.extremesoundmuffler.gui.buttons;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;

import com.leobeliik.extremesoundmuffler.Config;
import com.leobeliik.extremesoundmuffler.SoundMuffler;
import com.leobeliik.extremesoundmuffler.gui.MainScreen;
import com.leobeliik.extremesoundmuffler.interfaces.IColorsGui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class InvButton extends GuiButton implements IColorsGui {

    private final Minecraft minecraft = Minecraft.getMinecraft();
    private final GuiContainer parent;
    private boolean hold = false;

    public InvButton(GuiContainer parentGui, int x, int y) {
        super(1001, parentGui.guiLeft + x, parentGui.guiTop + y, 11, 11, "");
        parent = parentGui;
    }

    // @Override
    // public void onPress() {
    // MainScreen.open();
    // }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            SoundMuffler.renderGui();
            func_146110_a(xPosition, yPosition, 43f, 202f, 11, 11, 256, 256);
            if (func_146115_a() && !hold) {
                drawCenteredString(
                    minecraft.fontRenderer,
                    "Muffler",
                    xPosition + 5,
                    this.yPosition + this.height + 1,
                    whiteText);
            }
            if (hold) {
                drag(mouseX, mouseY);
            }
        }
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (super.mousePressed(mc, mouseX, mouseY)) {
            if (GuiScreen.isCtrlKeyDown() && func_146115_a()) {
                hold = true;
            } else {
                MainScreen.open();
                return true;
            }
        }
        return false;/* super.mousePressed(mc, mouseX, mouseY); */
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        if (GuiScreen.isCtrlKeyDown() && func_146115_a()) {
            hold = false;
            Config.setInvButtonHorizontal(xPosition - parent.guiLeft);
            Config.setInvButtonVertical(yPosition - parent.guiTop);
        }
        super.mouseReleased(mouseX, mouseY);
    }

    private void drag(int mouseX, int mouseY) {
        xPosition = mouseX - (this.width / 2);
        yPosition = mouseY - (this.height / 2);
    }
}
