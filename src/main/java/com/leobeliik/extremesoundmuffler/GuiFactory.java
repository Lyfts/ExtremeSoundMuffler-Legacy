package com.leobeliik.extremesoundmuffler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GuiFactory implements IModGuiFactory {

    @Override
    public void initialize(Minecraft minecraftInstance) {}

    @Override
    public boolean hasConfigGui() {
        return true;
    }

    @Override
    public GuiScreen createConfigGui(GuiScreen parentScreen) {
        return new GuiESMConfig(parentScreen);
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }

    public static class GuiESMConfig extends GuiConfig {

        public GuiESMConfig(GuiScreen parent) {
            super(
                Minecraft.getMinecraft().currentScreen,
                getConfigElements(),
                SoundMuffler.MODID,
                false,
                false,
                SoundMuffler.MODNAME + " Configuration");
        }

        private static List<IConfigElement> getConfigElements() {
            Configuration config = ESMConfig.config;
            return config.getCategoryNames()
                .stream()
                .map(name -> new ConfigElement(config.getCategory(name)))
                .collect(Collectors.toList());
        }
    }
}
