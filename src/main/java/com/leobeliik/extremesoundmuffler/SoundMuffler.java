package com.leobeliik.extremesoundmuffler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.gtnewhorizon.gtnhlib.eventbus.EventBusSubscriber;
import com.leobeliik.extremesoundmuffler.gui.MainScreen;
import com.leobeliik.extremesoundmuffler.gui.buttons.InvButton;
import com.leobeliik.extremesoundmuffler.utils.DataManager;

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
    dependencies = "required-after:gtnhlib;",
    acceptedMinecraftVersions = "[1.7.10]",
    acceptableRemoteVersions = "*")
@EventBusSubscriber(side = Side.CLIENT)
public class SoundMuffler {

    public static final String MODID = "extremesoundmuffler";
    public static final String MODNAME = "Extreme Sound Muffler Legacy";
    public static final Logger LOGGER = LogManager.getLogger();
    public static final ResourceLocation DARK_TEXTURE = new ResourceLocation(
        SoundMuffler.MODID,
        "textures/gui/sm_gui_dark.png");
    public static final ResourceLocation TEXTURE = new ResourceLocation(SoundMuffler.MODID, "textures/gui/sm_gui.png");

    @SidedProxy(
        serverSide = "com.leobeliik.extremesoundmuffler.CommonProxy",
        clientSide = "com.leobeliik.extremesoundmuffler.ClientProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
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
    public static void onPlayerLogin(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        DataManager.loadData(
            event.manager.getSocketAddress()
                .toString());
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onGuiInit(GuiScreenEvent.InitGuiEvent.Post event) {
        if (ESMConfig.INV_BUTTONS.disableInventoryButton) {
            return;
        }

        GuiScreen screen = event.gui;
        try {
            if (screen.getClass() == GuiInventory.class) {
                // noinspection unchecked
                event.buttonList.add(
                    new InvButton(
                        (GuiInventory) screen,
                        ESMConfig.INV_BUTTONS.invButtonX,
                        ESMConfig.INV_BUTTONS.invButtonY));
            }
        } catch (NullPointerException ignored) {}
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        if (ClientProxy.openMufflerScreen.isPressed()) {
            MainScreen.open();
        }
    }

    public static int getHotkey() {
        return ClientProxy.openMufflerScreen.getKeyCode();
    }

    public static void bindTexture() {
        Minecraft.getMinecraft().renderEngine.bindTexture(ESMConfig.GENERAL.useDarkTheme ? DARK_TEXTURE : TEXTURE);
    }
}
