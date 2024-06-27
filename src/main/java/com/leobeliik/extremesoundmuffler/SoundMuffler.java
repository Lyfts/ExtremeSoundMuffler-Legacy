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

// @Mod("extremesoundmuffler")
@Mod(
    modid = SoundMuffler.MODID,
    version = Tags.VERSION,
    name = SoundMuffler.MODNAME,
    acceptedMinecraftVersions = "[1.7.10]",
    // guiFactory = "com.caedis.duradisplay.config.GuiFactory",
    acceptableRemoteVersions = "*")
// dependencies = "after:gregtech@[5.09.43.63,);" + " after:EnderIO@[2.4.18,);")
public class SoundMuffler {

    public static final String MODID = "extremesoundmuffler";
    public static final String MODNAME = "Extreme Sound Muffler";
    private static final Logger LOGGER = LogManager.getLogger();

    @SidedProxy(
        serverSide = "com.leobeliik.extremesoundmuffler.CommonProxy",
        clientSide = "com.leobeliik.extremesoundmuffler.ClientProxy")
    public static CommonProxy proxy;

    // public SoundMuffler() {

    // ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class,
    // () -> new IExtensionPoint.DisplayTest(() -> "", (a, b) -> true));

    // ISoundLists.forbiddenSounds.addAll(Config.getForbiddenSounds());
    // }

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

    // private void clientInit(final FMLClientSetupEvent event) {
    // openMufflerScreen = new KeyMapping(
    // "Open sound muffler screen",
    // KeyConflictContext.IN_GAME,
    // InputConstants.UNKNOWN,
    // "key.categories.misc");
    // ClientRegistry.registerKeyBinding(openMufflerScreen);
    // }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onPlayerLoggin(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        DataManager.loadData();
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onGuiInit(GuiScreenEvent.InitGuiEvent.Post event) {
        GuiScreen screen = event.gui;
        if (Config.getDisableInventoryButton()
            || screen instanceof GuiContainerCreative /* || event.getWidgetList() == null */) {
            return;
        }
        try {
            if (screen instanceof InventoryEffectRenderer) {
                event.buttonList.add(
                    new InvButton(
                        (InventoryEffectRenderer) screen,
                        Config.getInvButtonHorizontal() + 50,
                        Config.getInvButtonVertical()));
                // event.addWidget(new InvButton((AbstractContainerScreen) screen, Config.getInvButtonHorizontal(),
                // Config.getInvButtonVertical()));
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
        // RenderSystem.setShaderTexture(0, (new ResourceLocation(SoundMuffler.MODID, texture)));
    }

}
