package com.leobeliik.extremesoundmuffler;

import com.leobeliik.extremesoundmuffler.interfaces.ISoundLists;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ClientProxy extends CommonProxy {

    public static KeyBinding openMufflerScreen;

    public void preInit(FMLPreInitializationEvent event) {
        openMufflerScreen = new KeyBinding("Open sound muffler screen", Keyboard.KEY_NONE, "key.categories.misc");
        ClientRegistry.registerKeyBinding(openMufflerScreen);
        Config.init(event);
        ISoundLists.forbiddenSounds.addAll(
            Arrays.stream(Config.getForbiddenSounds())
                .collect(Collectors.toList()));
    }

    public void init(FMLInitializationEvent event) {

    }

    public void postInit(FMLPostInitializationEvent event) {

    }
}
