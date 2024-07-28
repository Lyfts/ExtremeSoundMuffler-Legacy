package com.leobeliik.extremesoundmuffler;

import java.util.Arrays;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import org.lwjgl.input.Keyboard;

import com.leobeliik.extremesoundmuffler.interfaces.ISoundLists;

public class ClientProxy extends CommonProxy {

    public static final KeyBinding openMufflerScreen = new KeyBinding("Open sound muffler screen", Keyboard.KEY_NONE,
            "ESM:Legacy");

    public void preInit(FMLPreInitializationEvent event) {
        ESMConfig.sync();
        ClientRegistry.registerKeyBinding(openMufflerScreen);
        ISoundLists.forbiddenSounds.addAll(Arrays.asList(ESMConfig.getForbiddenSounds()));
    }

    public void init(FMLInitializationEvent event) {}

    public void postInit(FMLPostInitializationEvent event) {}
}
