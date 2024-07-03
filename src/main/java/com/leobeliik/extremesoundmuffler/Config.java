package com.leobeliik.extremesoundmuffler;

import net.minecraftforge.common.config.Configuration;

import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Config {

    public static Configuration config;
    private static String[] forbiddenSounds;
    private static boolean lawfulAllList;
    private static boolean disableInventoryButton;
    private static boolean disableAnchors;
    private static boolean leftButtons;
    private static double defaultMuteVolume;
    private static boolean showTip;
    private static boolean useDarkTheme;
    private static int invButtonHorizontal;
    private static int invButtonVertical;
    static String CATEGORY_GENERAL = "general";
    static String CATEGORY_INVENTORY_BUTTON = "inventory_button";
    static String CATEGORY_ANCHORS = "Anchors";

    static void init(FMLPreInitializationEvent event) {
        config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();
        buildConfig();
        FMLCommonHandler.instance()
            .bus()
            .register(new Config());
    }

    private static void buildConfig() {
        forbiddenSounds = config.getStringList(
            "forbiddenSounds",
            CATEGORY_GENERAL,
            new String[] { "ui.", "music.", "ambient." },
            "Blacklisted Sounds - add the name of the sounds to blacklist, separated with comma");
        lawfulAllList = config.getBoolean(
            "lawfulAllList",
            CATEGORY_GENERAL,
            false,
            "Allow the \"ALL\" sounds list to include the blacklisted sounds?");
        defaultMuteVolume = config
            .get(CATEGORY_GENERAL, "defaultMuteVolume", 0, "Volume set when pressed the mute button by default", 0, 0.9)
            .getDouble();
        leftButtons = config.getBoolean(
            "leftButtons",
            CATEGORY_GENERAL,
            false,
            "Set to true to move the muffle and play buttons to the left side of the GUI");
        showTip = config.getBoolean("showTip", CATEGORY_GENERAL, true, "Show tips in the Muffler screen?");
        useDarkTheme = config.getBoolean("useDarkTheme", CATEGORY_GENERAL, false, "Whether or not use the dark theme");

        disableInventoryButton = config.getBoolean(
            "disableInventoryButton",
            CATEGORY_INVENTORY_BUTTON,
            false,
            "Disable the Muffle button in the player inventory?");
        invButtonHorizontal = config.getInt(
            "invButtonX",
            CATEGORY_INVENTORY_BUTTON,
            75,
            Integer.MIN_VALUE,
            Integer.MAX_VALUE,
            "Coordinates of the Muffler button in the player inventory. You can change this in game by holding CTRL and LMB over the button and dragging it around");
        invButtonVertical = config.getInt(
            "invButtonY",
            CATEGORY_INVENTORY_BUTTON,
            7,
            Integer.MIN_VALUE,
            Integer.MAX_VALUE,
            "Coordinates of the Muffler button in the player inventory. You can change this in game by holding CTRL and LMB over the button and dragging it around");
        disableAnchors = config.getBoolean("disableAnchors", CATEGORY_ANCHORS, false, "Disable the Anchors?");
        config.save();
    }

    static boolean getDisableInventoryButton() {
        return disableInventoryButton;
    }

    static boolean useDarkTheme() {
        return useDarkTheme;
    }

    static String[] getForbiddenSounds() {
        return forbiddenSounds;
    }

    public static boolean getLawfulAllList() {
        return lawfulAllList;
    }

    public static boolean getDisableAnchors() {
        return disableAnchors;
    }

    public static float getDefaultMuteVolume() {
        return (float) defaultMuteVolume;
    }

    public static boolean getLeftButtons() {
        return leftButtons;
    }

    public static boolean getShowTip() {
        return showTip;
    }

    static int getInvButtonHorizontal() {
        return invButtonHorizontal;
    }

    public static void setInvButtonPosition(int x, int y) {
        Config.invButtonHorizontal = x;
        config.get(CATEGORY_INVENTORY_BUTTON, "invButtonX", invButtonHorizontal)
            .set(x);
        Config.invButtonVertical = y;
        config.get(CATEGORY_INVENTORY_BUTTON, "invButtonY", invButtonVertical)
            .set(y);
        config.save();
    }

    static int getInvButtonVertical() {
        return invButtonVertical;
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(SoundMuffler.MODID)) {
            buildConfig();
        }
    }
}
