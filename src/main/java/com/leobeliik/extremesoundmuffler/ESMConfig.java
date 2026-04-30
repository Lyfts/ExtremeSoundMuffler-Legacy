package com.leobeliik.extremesoundmuffler;

import com.gtnewhorizon.gtnhlib.config.Config;
import com.gtnewhorizon.gtnhlib.config.ConfigurationManager;

@Config(modid = "extremesoundmuffler", category = "")
@Config.LangKeyPattern(pattern = "esm.config.%cat.%field")
public class ESMConfig {

    public static final General GENERAL = new General();

    @Config.Name("inventory_button")
    public static final InventoryButton INV_BUTTONS = new InventoryButton();

    public static final Anchors ANCHORS = new Anchors();

    public static class General {

        @Config.DefaultStringList({ "ui.", "music.", "ambient." })
        @Config.Comment("Blacklisted Sounds - add the name of the sounds to blacklist, separated with comma")
        public String[] forbiddenSounds;

        @Config.DefaultBoolean(false)
        @Config.Comment("Allow the \"ALL\" sounds list to include the blacklisted sounds?")
        public boolean lawfulAllList;

        @Config.DefaultFloat(0f)
        @Config.RangeFloat(min = 0f, max = 0.9f)
        @Config.Comment("Volume set when pressed the mute button by default")
        public float defaultMuteVolume;

        @Config.DefaultBoolean(false)
        @Config.Comment("Set to true to move the muffle and play buttons to the left side of the GUI")
        public boolean leftButtons;

        @Config.DefaultBoolean(true)
        @Config.Comment("Set to false to disable the tips in the Muffler screen")
        public boolean showTip;

        @Config.DefaultBoolean(false)
        @Config.Comment("Set to true to use the dark theme")
        public boolean useDarkTheme;
    }

    public static class InventoryButton {

        @Config.DefaultBoolean(false)
        @Config.Comment("Set to true to disable the Muffle button in the player inventory")
        public boolean disableInventoryButton;

        @Config.DefaultInt(161)
        @Config.ModDetectedDefault(modID = "bogosorter", value = "149")
        @Config.Comment("Coordinates of the Muffler button in the player inventory. You can change this in game by holding CTRL and LMB over the button and dragging it around")
        public int invButtonX;

        @Config.DefaultInt(5)
        @Config.ModDetectedDefault(modID = "bogosorter", value = "5")
        @Config.Comment("Coordinates of the Muffler button in the player inventory. You can change this in game by holding CTRL and LMB over the button and dragging it around")
        public int invButtonY;
    }

    public static class Anchors {

        @Config.DefaultBoolean(false)
        @Config.Comment("Set to true to disable anchors")
        public boolean disableAnchors;
    }

    public static boolean getDisableAnchors() {
        return ANCHORS.disableAnchors;
    }

    public static boolean getLeftButtons() {
        return GENERAL.leftButtons;
    }

    public static void setInvButtonPosition(int x, int y) {
        ESMConfig.INV_BUTTONS.invButtonX = x;
        ESMConfig.INV_BUTTONS.invButtonY = y;
        ConfigurationManager.save(ESMConfig.class);
    }
}
