package com.leobeliik.extremesoundmuffler.gui;

import static com.leobeliik.extremesoundmuffler.SoundMuffler.renderGui;
import static com.leobeliik.extremesoundmuffler.gui.MainScreen.ListMode.*;
import static com.leobeliik.extremesoundmuffler.utils.Icon.ANCHOR;
import static com.leobeliik.extremesoundmuffler.utils.Icon.EDIT_ANCHOR;
import static com.leobeliik.extremesoundmuffler.utils.Icon.MUFFLE;
import static com.leobeliik.extremesoundmuffler.utils.Icon.RESET;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.ResourceLocation;

import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.leobeliik.extremesoundmuffler.Config;
import com.leobeliik.extremesoundmuffler.SoundMuffler;
import com.leobeliik.extremesoundmuffler.gui.buttons.ESMButton;
import com.leobeliik.extremesoundmuffler.gui.buttons.MuffledSlider;
import com.leobeliik.extremesoundmuffler.interfaces.IColorsGui;
import com.leobeliik.extremesoundmuffler.interfaces.ISoundLists;
import com.leobeliik.extremesoundmuffler.utils.Anchor;
import com.leobeliik.extremesoundmuffler.utils.ComparableResource;
import com.leobeliik.extremesoundmuffler.utils.DataManager;
import com.leobeliik.extremesoundmuffler.utils.Tips;

public class MainScreen extends GuiScreen implements ISoundLists, IColorsGui {

    private static final Minecraft minecraft = Minecraft.getMinecraft();
    private static final Pattern NUMBER_PATTERN = Pattern.compile("[0-9]*(?:[0-9]*)?");
    private static final String mainTitle = "ESM - Main Screen";
    private final List<GuiButton> filteredButtons = new ArrayList<>();
    private static boolean isMuffling = true;
    private static String searchBarText = "";
    private static String screenTitle = "";
    private final int xSize = 256;
    private final int ySize = 202;
    private final boolean isAnchorsDisabled = Config.getDisableAnchors();
    private final String tip = Tips.randomTip();
    private int minYButton, maxYButton, index;
    private ESMButton btnToggleMuffled;
    private ESMButton btnToggleSoundsList;
    private ESMButton btnSetAnchor;
    private ESMButton btnEditAnchor;
    private ESMButton btnAccept;
    private ESMButton btnCancel;
    private ESMButton btnDelete;
    private GuiTextField searchBar, editAnchorTitleBar, editAnchorRadiusBar;
    private Anchor anchor;
    private final List<GuiTextField> textFields = new ArrayList<>();
    private static ListMode listMode;

    enum ListMode {

        RECENT,
        ALL,
        MUFFLED;

        private static final ListMode[] vals = values();

        public ListMode next() {
            return vals[(this.ordinal() + 1) % vals.length];
        }
    }

    private static void open(String title, ListMode mode, String searchMessage) {
        screenTitle = title;
        listMode = mode;
        searchBarText = searchMessage;
        minecraft.displayGuiScreen(new MainScreen());
    }

    public static void open() {
        open(mainTitle, RECENT, "");
    }

    public static boolean isMuffled() {
        return isMuffling;
    }

    public static boolean isMain() {
        return screenTitle.equals(mainTitle);
    }

    @Nullable
    public static Anchor getAnchorByName(String name) {
        return anchorList.stream()
            .filter(
                a -> a.getName()
                    .equals(name))
            .findFirst()
            .orElse(null);
    }

    @Nullable
    public static Anchor getCurrentAnchor() {
        return getAnchorByName(screenTitle);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        renderGui();
        drawTexturedModalRect(getX(), getY(), 0, 0, xSize, ySize); // Main screen bounds
        drawCenteredString(fontRendererObj, screenTitle, getX() + 128, getY() + 8, whiteText); // Screen title
        renderButtonsTextures(mouseX, mouseY);
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderTip();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void initGui() {
        super.initGui();
        Keyboard.enableRepeatEvents(true);
        MuffledSlider.showSlider = true;
        textFields.clear();
        index = 0;
        minYButton = getY() + 46;
        maxYButton = getY() + 164;
        addSoundButtons();
        addAnchorButtons();

        String display = StringUtils.capitalize(
            listMode.toString()
                .toLowerCase());
        buttonList.add(
            btnToggleSoundsList = new ESMButton(0, getX() + 13, getY() + 181, 52, 13, display, this::toggleSoundList)
                .setRenderText(true)
                .setTooltip("Showing " + display + " Sounds", false));
        buttonList.add(
            btnToggleMuffled = new ESMButton(0, getX() + 229, getY() + 179, 17, 17, () -> isMuffling = !isMuffling)
                .setTooltip(isMuffling ? "Stop Muffling" : "Start Muffling", false));
        buttonList.add(btnDelete = new ESMButton(1, getX() + 205, getY() + 179, 17, 17, () -> {
            anchor = getAnchorByName(screenTitle);
            if (clearRecentSounds()) {
                recentSoundsList.clear();
                if (isMain()) {
                    open(mainTitle, listMode, searchBar.getText());
                } else if (anchor != null) {
                    open(anchor.getName(), listMode, searchBar.getText());
                }
                return;
            }

            if (isMain()) {
                muffledSounds.clear();
                open(mainTitle, listMode, searchBar.getText());
            } else {
                if (anchor != null) {
                    anchor.deleteAnchor();
                    buttonList.clear();
                    open(anchor.getName(), listMode, searchBar.getText());
                }
            }
        }).setTooltip(this::getDeleteTooltip, false));

        buttonList.add(
            btnSetAnchor = new ESMButton(
                2,
                getX() + 260,
                getY() + 62,
                11,
                11,
                () -> Objects.requireNonNull(getAnchorByName(screenTitle))
                    .setAnchor()).setVisible(!isMain())
                        .setTooltip(
                            () -> btnSetAnchor.mouseOver && !editAnchorTitleBar.getVisible() ? "Set Anchor" : "",
                            true));

        buttonList.add(
            btnEditAnchor = new ESMButton(
                3,
                getX() + 274,
                getY() + 62,
                11,
                11,
                () -> editTitle(Objects.requireNonNull(getAnchorByName(screenTitle))))
                    .setVisible(() -> !isMain() && anchor != null && anchor.getAnchorPos() != null)
                    .setIcon(EDIT_ANCHOR)
                    .setTooltip(
                        () -> btnEditAnchor.mouseOver && !editAnchorTitleBar.getVisible() ? "Edit Anchor" : "",
                        true));

        addEditAnchorButtons();

        textFields.add(searchBar = new GuiTextField(fontRendererObj, getX() + 74, getY() + 183, 119, 13));
        searchBar.setText(searchBarText);
        searchBar.setEnableBackgroundDrawing(false);
        buttonList.add(
            new ESMButton(
                0,
                getX() + 10,
                getY() + 22,
                13,
                20,
                () -> listScroll(
                    !searchBar.getText()
                        .isEmpty() ? filteredButtons : buttonList,
                    -1)).setTooltip("Previous Sounds", true));

        buttonList.add(
            new ESMButton(
                0,
                getX() + 233,
                getY() + 22,
                13,
                20,
                () -> listScroll(
                    !searchBar.getText()
                        .isEmpty() ? filteredButtons : buttonList,
                    1)).setTooltip("Next Sounds", true));
        updateText();
    }

    @SuppressWarnings("unchecked")
    private void addSoundButtons() {
        int buttonH = minYButton;
        anchor = getAnchorByName(screenTitle);

        if (!isMain() && anchor == null) {
            return;
        }

        soundsList.clear();
        switch (listMode) {
            case RECENT -> {
                if (!Config.hideMuffledFromRecent) {
                    soundsList.addAll(getMuffledSounds().keySet());
                }
                soundsList.addAll(recentSoundsList);
            }
            case ALL -> {
                ((Set<ResourceLocation>) Minecraft.getMinecraft()
                    .getSoundHandler().sndRegistry.getKeys()).forEach(e -> soundsList.add(new ComparableResource(e)));
                if (Config.getLawfulAllList()) {
                    forbiddenSounds.forEach(
                        fs -> soundsList.removeIf(
                            sl -> sl.toString()
                                .contains(fs)));
                }
            }
            case MUFFLED -> {
                soundsList.addAll(getMuffledSounds().keySet());
            }
        }

        if (soundsList.isEmpty()) {
            return;
        }

        int id = 0;
        for (ComparableResource sound : soundsList) {
            float maxVolume = 1F;
            float volume = getMuffledSounds().get(sound) == null ? maxVolume : getMuffledSounds().get(sound);
            MuffledSlider volumeSlider = getMuffledSlider(sound, id++, buttonH, volume);

            buttonH += volumeSlider.height + 2;
            buttonList.add(volumeSlider);
            volumeSlider.setVisible(buttonList.indexOf(volumeSlider) < index + 10);
        }
    }

    private MuffledSlider getMuffledSlider(ComparableResource sound, int id, int buttonH, float volume) {
        int x = Config.getLeftButtons() ? getX() + 36 : getX() + 11;
        boolean muffled = getMuffledSounds().containsKey(sound);
        return new MuffledSlider(id, x, buttonH, 205, 11, volume, sound, anchor).setMuffled(muffled);
    }

    private void addAnchorButtons() {
        int buttonW = getX() + 30;
        for (int i = 0; i <= 9; i++) {
            ESMButton btnAnchor;
            if (isAnchorsDisabled) {
                String[] disabledMsg = { "-", "D", "i", "s", "a", "b", "l", "e", "d", "-" };
                btnAnchor = new ESMButton(0, buttonW, getY() + 24, 16, 16, disabledMsg[i]).setRenderText(true)
                    .setEnabled(false);
            } else {
                int finalI = i;
                btnAnchor = new ESMButton(0, buttonW, getY() + 24, 16, 16, (String.valueOf(finalI)), () -> {
                    anchor = anchorList.get(finalI);
                    if (anchor == null) return;
                    if (screenTitle.equals(anchor.getName())) {
                        screenTitle = mainTitle;
                    } else {
                        screenTitle = anchor.getName();
                    }
                    buttonList.clear();
                    open(screenTitle, listMode, searchBar.getText());
                }).setRenderText(true);

                if (!anchorList.isEmpty()) {
                    btnAnchor.setTextColor(
                        anchorList.get(i)
                            .getAnchorPos() != null ? greenText : whiteText)
                        .setIcon(isActiveAnchor(btnAnchor) ? ANCHOR : null, -5, -2, 27, 22);
                }
            }

            buttonList.add(
                btnAnchor.setTooltip(
                    isAnchorsDisabled ? "Anchors are disabled"
                        : anchorList.get(i)
                            .getName(),
                    true));
            buttonW += 20;
        }
    }

    private void addEditAnchorButtons() {
        textFields.add(
            editAnchorTitleBar = new GuiTextField(fontRendererObj, getX() + 302, btnEditAnchor.yPosition + 20, 84, 11));
        editAnchorTitleBar.setVisible(false);

        textFields.add(
            editAnchorRadiusBar = new GuiTextField(
                fontRendererObj,
                getX() + 302,
                editAnchorTitleBar.yPosition + 15,
                30,
                11) {

                @Override
                public void writeText(String string) {
                    if (NUMBER_PATTERN.matcher(string)
                        .matches()) {
                        super.writeText(string);
                    }
                }

                @Override
                public void setText(String p_146180_1_) {
                    if (NUMBER_PATTERN.matcher(p_146180_1_)
                        .matches()) {
                        super.setText(p_146180_1_);
                    }
                }
            });

        editAnchorRadiusBar.setVisible(false);
        buttonList.add(
            btnAccept = new ESMButton(100, getX() + 259, editAnchorRadiusBar.yPosition + 15, 40, 20, "Accept", () -> {
                anchor = getAnchorByName(screenTitle);
                if (!editAnchorTitleBar.getText()
                    .isEmpty()
                    && !editAnchorRadiusBar.getText()
                        .isEmpty()
                    && anchor != null) {
                    int radius = Integer.parseInt(editAnchorRadiusBar.getText());

                    if (radius > 32) {
                        radius = 32;
                    } else if (radius < 1) {
                        radius = 1;
                    }

                    anchor.editAnchor(editAnchorTitleBar.getText(), radius);
                    screenTitle = editAnchorTitleBar.getText();
                    editTitle(anchor);
                }
            }).renderNormalButton(true)
                .setVisible(false));
        buttonList.add(
            btnCancel = new ESMButton(
                101,
                getX() + 300,
                editAnchorRadiusBar.yPosition + 15,
                40,
                20,
                "Cancel",
                () -> editTitle(Objects.requireNonNull(getAnchorByName(screenTitle)))).renderNormalButton(true)
                    .setVisible(false));
    }

    private void renderButtonsTextures(int mouseX, int mouseY) {
        int x; // start x point of the button
        int y; // start y point of the button
        String message; // Button message
        int stringW; // text width

        if (buttonList.size() < soundsList.size()) {
            return;
        }

        btnDelete.setIcon(clearRecentSounds() ? RESET : null, 2, 2, 13, 13);
        btnToggleMuffled.setIcon(isMuffling ? MUFFLE : null, 1, 1, 15, 15);

        // Anchor coordinates and set coord button
        String dimensionName = "";
        String radius;
        x = btnSetAnchor.xPosition;
        y = btnSetAnchor.yPosition;

        if (anchor != null) {
            stringW = fontRendererObj.getStringWidth("Dimension: ");
            radius = anchor.getRadius() == 0 ? "" : String.valueOf(anchor.getRadius());
            if (anchor.getDimension() != null) {
                stringW += fontRendererObj.getStringWidth(anchor.getDimension());
                dimensionName = anchor.getDimension();
            }
            drawRect(x - 5, y - 56, x + stringW + 6, y + 16, darkBG);
            drawString(fontRendererObj, "X: " + anchor.getX(), x + 1, y - 50, whiteText);
            drawString(fontRendererObj, "Y: " + anchor.getY(), x + 1, y - 40, whiteText);
            drawString(fontRendererObj, "Z: " + anchor.getZ(), x + 1, y - 30, whiteText);
            drawString(fontRendererObj, "Radius: " + radius, x + 1, y - 20, whiteText);
            drawString(fontRendererObj, "Dimension: " + dimensionName, x + 1, y - 10, whiteText);
            renderGui();
            func_146110_a(x, y, 0, 69.45F, 11, 11, 88, 88); // set coordinates button

            // Indicates the Anchor has to be set before muffling sounds
            for (GuiButton btn : buttonList) {
                if (btn instanceof MuffledSlider slider) {
                    if (slider.getBtnToggleSound()
                        .isMouseOver(mouseX, mouseY) && anchor.getAnchorPos() == null) {
                        drawRect(x - 5, y + 16, x + 65, y + 40, darkBG);
                        fontRendererObj.drawString("Set the", x, y + 18, whiteText);
                        fontRendererObj.drawString("Anchor first", x, y + 29, whiteText);
                    }
                }
            }
        }

        // Show Radius and Title text when editing Anchor and bg
        x = btnSetAnchor.xPosition;
        y = editAnchorTitleBar.yPosition;
        if (editAnchorRadiusBar.getVisible()) {
            drawRect(
                x - 4,
                y - 4,
                editAnchorTitleBar.xPosition + editAnchorTitleBar.getWidth() + 10,
                btnAccept.yPosition + 23,
                darkBG);
            fontRendererObj.drawString("Title: ", x - 2, y + 1, whiteText);
            fontRendererObj.drawString("Radius: ", x - 2, editAnchorRadiusBar.yPosition + 1, whiteText);

            x = editAnchorRadiusBar.xPosition + editAnchorRadiusBar.getWidth();
            y = editAnchorRadiusBar.yPosition;
            message = "Range: 1 - 32";
            stringW = fontRendererObj.getStringWidth(message);
            if (editAnchorRadiusBar.isFocused()) {
                drawRect(x + 3, y, x + stringW + 9, y + 12, darkBG);
                fontRendererObj.drawString(message, x + 10, y + 2, whiteText);
            }
        }

        // Draw Searchbar prompt text
        x = searchBar.xPosition;
        y = searchBar.yPosition;
        String searchHint = "Search";
        if (!searchBar.isFocused() && searchBar.getText()
            .isEmpty()) {
            drawString(fontRendererObj, searchHint, x + 1, y + 1, -1);
        }

        for (GuiTextField textField : textFields) {
            if (!textField.getVisible()) continue;
            textField.drawTextBox();
        }
    }

    private boolean clearRecentSounds() {
        return listMode.equals(RECENT) && isShiftKeyDown();
    }

    private void renderTip() {
        if (!Config.getShowTip()) return;
        // Show a tip
        List<String> tips = fontRendererObj.listFormattedStringToWidth(tip, xSize);
        drawHoveringText(tips, getX() - 5, getY() + 223, fontRendererObj);
    }

    private void editTitle(Anchor anchor) {
        editAnchorTitleBar.setText(anchor.getName());
        editAnchorTitleBar.setVisible(!editAnchorTitleBar.getVisible());

        editAnchorRadiusBar.setText(String.valueOf(anchor.getRadius()));
        editAnchorRadiusBar.setVisible(!editAnchorRadiusBar.getVisible());

        btnAccept.setVisible(!btnAccept.visible);
        btnCancel.setVisible(!btnCancel.visible);

        editAnchorRadiusBar.setTextColor(whiteText);
    }

    @Override
    public void handleMouseInput() {
        int scroll = Mouse.getEventDWheel();
        if (scroll != 0) {
            if (searchBar.getText()
                .isEmpty()) {
                listScroll(buttonList, scroll * -1);
            } else {
                listScroll(filteredButtons, scroll * -1);
            }
        }
        super.handleMouseInput();
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }

    private void listScroll(List<GuiButton> buttonList, double direction) {
        int buttonH = minYButton;

        if (index <= 0 && direction < 0) {
            return;
        }

        if ((index >= buttonList.size() - 10 || index >= soundsList.size() - 10) && direction > 0) {
            return;
        }

        index += direction > 0 ? 10 : -10;

        for (GuiButton btn : buttonList) {
            if (btn instanceof MuffledSlider slider) {
                int buttonIndex = buttonList.indexOf(btn);
                slider.setVisible(buttonIndex < index + 10 && buttonIndex >= index);

                if (slider.isVisible()) {
                    slider.yPosition = buttonH;
                    buttonH += slider.height + 2;
                }
                slider.refreshButtons();
            }
        }
    }

    private void updateText() {
        if (!searchBar.isFocused()) return;
        int buttonH = minYButton;
        filteredButtons.clear();

        for (GuiButton button : buttonList) {
            if (button instanceof MuffledSlider btn) {
                if (btn.displayString.contains(
                    searchBar.getText()
                        .toLowerCase())) {
                    if (!filteredButtons.contains(btn)) {
                        filteredButtons.add(btn);
                    }

                    btn.yPosition = buttonH;
                    buttonH += btn.height + 2;

                    btn.setVisible(btn.yPosition < maxYButton);
                } else {
                    btn.setVisible(false);
                }
                btn.refreshButtons();
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {

        // Search bar, Edit title bar & Edit Anchor Radius bar looses focus when pressed "Enter" or "Intro"
        if (keyCode == Keyboard.KEY_RETURN) {
            searchBar.setFocused(false);
            editAnchorTitleBar.setFocused(false);
            editAnchorRadiusBar.setFocused(false);
            return;
        }

        for (GuiTextField textField : textFields) {
            if (textField.getVisible()) {
                if (textField.textboxKeyTyped(typedChar, keyCode)) {
                    updateText();
                    return;
                }
            }
        }

        // Close screen when press "E" or the mod hotkey outside the search bar or edit title bar
        if (!searchBar.isFocused() && !editAnchorTitleBar.isFocused()
            && !editAnchorRadiusBar.isFocused()
            && (minecraft.gameSettings.keyBindInventory.getKeyCode() == keyCode
                || keyCode == SoundMuffler.getHotkey())) {
            this.onGuiClosed();
            mc.setIngameFocus();
            filteredButtons.clear();
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        for (GuiTextField textField : textFields) {
            if (textField.getVisible()) {
                textField.mouseClicked(mouseX, mouseY, mouseButton);
            }

            if (mouseButton == 1 && textField.isFocused()) {
                textField.setText("");
                updateText();
                break;
            }
        }

        if (mouseButton == 1) {
            MuffledSlider.showSlider = !MuffledSlider.showSlider;
            MuffledSlider.stopTickSound();
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void updateScreen() {
        for (GuiTextField textField : textFields) {
            textField.updateCursorCounter();
        }
    }

    @Override
    public void onGuiClosed() {
        DataManager.saveData();
        MuffledSlider.stopTickSound();
        super.onGuiClosed();
    }

    private int getX() {
        return (width - xSize) / 2;
    }

    private int getY() {
        return (height - ySize) / 2;
    }

    private void toggleSoundList() {
        listMode = listMode.next();
        if (listMode.equals(MUFFLED) && getMuffledSounds().isEmpty()) {
            listMode = listMode.next();
        }

        btnToggleSoundsList.displayString = StringUtils.capitalize(
            listMode.toString()
                .toLowerCase());
        buttonList.clear();
        open(screenTitle, listMode, searchBar.getText());
    }

    private Map<ComparableResource, Float> getMuffledSounds() {
        return isMain() ? muffledSounds : anchor.getMuffledSounds();
    }

    private boolean isActiveAnchor(ESMButton anchorButton) {
        return anchor != null && anchorButton.displayString.equals(String.valueOf(anchor.getAnchorId()));
    }

    private String getDeleteTooltip() {
        if (!clearRecentSounds()) {
            return isMain() ? "Delete Muffled List" : "Delete Anchor";
        } else {
            return "Clear recent sounds list";
        }
    }
}
