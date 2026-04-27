package com.leobeliik.extremesoundmuffler.mixins.minecraft;

import java.util.List;

import net.minecraft.client.gui.GuiScreen;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GuiScreen.class)
public interface GuiScreenMixin {

    @Invoker("func_146283_a")
    void invokeFunc_146283_a(List<String> tooltipLines, int x, int y);
}
