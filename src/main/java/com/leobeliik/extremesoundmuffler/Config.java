package com.leobeliik.extremesoundmuffler;

import net.minecraftforge.common.config.Configuration;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class Config {

    private static Configuration config;
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
            "Coordinates for the Muffler button in the player inventory. You can change this in game by holding the RMB over the button and draging it around");
        invButtonVertical = config.getInt(
            "invButtonY",
            CATEGORY_INVENTORY_BUTTON,
            7,
            Integer.MIN_VALUE,
            Integer.MAX_VALUE,
            "Coordinates for the Muffler button in the player inventory. You can change this in game by holding the RMB over the button and draging it around");
        disableAnchors = config.getBoolean("disableAnchors", CATEGORY_ANCHORS, false, "Disable the Anchors?");
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

    public static boolean getDisableAchors() {
        return false;
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

    public static void setInvButtonHorizontal(int invButtonHorizontal) {
        Config.invButtonHorizontal = invButtonHorizontal;
        config.save();
    }

    static int getInvButtonVertical() {
        return invButtonVertical;
    }

    public static void setInvButtonVertical(int invButtonVertical) {
        Config.invButtonVertical = invButtonVertical;
        config.save();
    }
}
