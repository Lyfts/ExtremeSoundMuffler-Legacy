package com.leobeliik.extremesoundmuffler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.leobeliik.extremesoundmuffler.gui.MainScreen;
import com.leobeliik.extremesoundmuffler.gui.buttons.InvButton;
import com.leobeliik.extremesoundmuffler.utils.DataManager;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(
    modid = SoundMuffler.MODID,
    version = Tags.VERSION,
    name = SoundMuffler.MODNAME,
    acceptedMinecraftVersions = "[1.12.2]",
    acceptableRemoteVersions = "*",
    guiFactory = "com.leobeliik.extremesoundmuffler.GuiFactory")
public class SoundMuffler {

    public static final String MODID = "extremesoundmuffler";
    public static final String MODNAME = "Extreme Sound Muffler Legacy";
    public static final Logger LOGGER = LogManager.getLogger();

    @SidedProxy(
        serverSide = "com.leobeliik.extremesoundmuffler.CommonProxy",
        clientSide = "com.leobeliik.extremesoundmuffler.ClientProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance()
            .bus()
            .register(this);
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void onPostInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onPlayerLoggin(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        DataManager.loadData(
            event.manager.getSocketAddress()
                .toString());
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onGuiInit(GuiScreenEvent.InitGuiEvent.Post event) {
        GuiScreen screen = event.gui;
        if (Config.getDisableInventoryButton() || screen instanceof GuiContainerCreative) {
            return;
        }
        try {
            if (screen instanceof InventoryEffectRenderer inv) {
                event.buttonList
                    .add(new InvButton(inv, Config.getInvButtonHorizontal(), Config.getInvButtonVertical()));
            }
        } catch (NullPointerException e) {
            LOGGER.error(
                "Extreme sound muffler: Error trying to add the muffler button in the player's inventory. \n" + e);
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (ClientProxy.openMufflerScreen.isPressed()) {
            MainScreen.open();
        }
    }

    public static int getHotkey() {
        return ClientProxy.openMufflerScreen.getKeyCode();
    }

    public static void renderGui() {
        String texture = Config.useDarkTheme() ? "textures/gui/sm_gui_dark.png" : "textures/gui/sm_gui.png";
        Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(SoundMuffler.MODID, texture));
    }
}
