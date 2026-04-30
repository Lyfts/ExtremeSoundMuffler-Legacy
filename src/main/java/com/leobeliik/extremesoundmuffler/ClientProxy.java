package com.leobeliik.extremesoundmuffler;

import java.util.Arrays;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;

import com.gtnewhorizon.gtnhlib.config.ConfigurationManager;
import com.leobeliik.extremesoundmuffler.interfaces.ISoundLists;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

    public static KeyBinding openMufflerScreen;

    static {
        ConfigurationManager.registerConfig(ESMConfig.class);
    }

    public void preInit(FMLPreInitializationEvent event) {
        openMufflerScreen = new KeyBinding(I18n.format("esm.key.open_muffler_gui"), Keyboard.KEY_NONE, "ESM:Legacy");
        ClientRegistry.registerKeyBinding(openMufflerScreen);
        ISoundLists.forbiddenSounds.addAll(Arrays.asList(ESMConfig.GENERAL.forbiddenSounds));
    }
}
