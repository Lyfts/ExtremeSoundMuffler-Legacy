package com.leobeliik.extremesoundmuffler;

import java.util.Arrays;

import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;

import com.leobeliik.extremesoundmuffler.interfaces.ISoundLists;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

    public static KeyBinding openMufflerScreen;

    public void preInit(FMLPreInitializationEvent event) {
        openMufflerScreen = new KeyBinding("Open sound muffler screen", Keyboard.KEY_NONE, "ESM:Legacy");
        ClientRegistry.registerKeyBinding(openMufflerScreen);
        Config.init(event);
        ISoundLists.forbiddenSounds.addAll(Arrays.asList(Config.getForbiddenSounds()));
    }

    public void init(FMLInitializationEvent event) {}

    public void postInit(FMLPostInitializationEvent event) {}
}
