package com.leobeliik.extremesoundmuffler;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = SoundMuffler.MODID)
@Config(modid = SoundMuffler.MODID, category = "")
public class ESMConfig {

    @Config.LangKey("stat.generalButton")
    public static final General GENERAL = new General();

    @Config.Name("inventory_button")
    @Config.LangKey("stat.inventoryButton")
    @Config.Comment("Buttons can be moved by holding CTRL and LMB over the button and dragging it around")
    public static final Buttons BUTTONS = new Buttons();

    public static final Anchors ANCHORS = new Anchors();

    public static class General {
        @Config.Comment("Blacklisted Sounds - add the name of the sounds to blacklist, separated with comma")
        public String[] forbiddenSounds = new String[]{"ui.", "music.", "ambient."};

        @Config.Comment("Allow the \"ALL\" sounds list to include the blacklisted sounds?")
        public boolean lawfulAllList = false;

        @Config.Comment("Move the muffle and play buttons to the left side of the GUI")
        public boolean leftButtons = false;

        @Config.Comment("Volume set when pressed the mute button by default")
        @Config.RangeDouble(min = 0, max = 0.99)
        @Config.SlidingOption
        public double defaultMuteVolume = 0.0D;

        @Config.Comment("Show tips in the Muffler screen?")
        public boolean showTip = true;

        @Config.Comment("Whether or not use the dark theme")
        public boolean useDarkTheme = false;
    }

    public static class Buttons {
        @Config.Comment("Disable the Muffle button in the player inventory?")
        public boolean disableInventoryButton = false;

        @Config.Comment("X coordinate of the Muffler button in the player inventory.")
        public int invButtonX = 75;

        @Config.Comment("Y coordinate of the Muffler button in the player inventory.")
        public int invButtonY = 7;
    }

    public static class Anchors {
        @Config.Comment("Disable the Anchors?")
        public boolean disableAnchors = false;
    }

    static boolean getDisableInventoryButton() {
        return BUTTONS.disableInventoryButton;
    }

    static boolean useDarkTheme() {
        return GENERAL.useDarkTheme;
    }

    static String[] getForbiddenSounds() {
        return GENERAL.forbiddenSounds;
    }

    public static boolean getLawfulAllList() {
        return GENERAL.lawfulAllList;
    }

    public static boolean getDisableAnchors() {
        return ANCHORS.disableAnchors;
    }

    public static float getDefaultMuteVolume() {
        return (float) GENERAL.defaultMuteVolume;
    }

    public static boolean getLeftButtons() {
        return GENERAL.leftButtons;
    }

    public static boolean getShowTip() {
        return GENERAL.showTip;
    }

    public static void setInvButtonPosition(int x, int y) {
        BUTTONS.invButtonX = x;
        BUTTONS.invButtonY = y;
        sync();
    }

    public static void sync() {
        ConfigManager.sync(SoundMuffler.MODID, Config.Type.INSTANCE);
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(SoundMuffler.MODID)) {
            sync();
        }
    }
}
