package com.leobeliik.extremesoundmuffler.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.storage.ThreadedFileIOBase;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.leobeliik.extremesoundmuffler.Config;
import com.leobeliik.extremesoundmuffler.SoundMuffler;
import com.leobeliik.extremesoundmuffler.interfaces.ISoundLists;

import net.minecraftforge.fml.common.FMLCommonHandler;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class DataManager implements ISoundLists {

    public static String identifier;

    private static final Gson gson = new GsonBuilder().setPrettyPrinting()
        .create();

    public static void loadData(String address) {
        identifier = getIdentifier(address);
        loadMuffledMap().forEach((R, F) -> muffledSounds.put(new ComparableResource(R), F));
        if (!Config.getDisableAnchors()) {
            anchorList.clear();
            anchorList.addAll(loadAnchors());
        }
    }

    public static void saveData() {
        saveMuffledMap();

        if (!Config.getDisableAnchors()) {
            saveAnchors();
        }
    }

    private static String getIdentifier(String address) {
        if (Minecraft.getMinecraft()
            .isSingleplayer()) {
            return FMLCommonHandler.instance()
                .getMinecraftServerInstance()
                .getFolderName();
        }

        int index = address.indexOf("/") + 1;
        return address.substring(index)
            .replace(":", ".");
    }

    private static NBTTagCompound serializeAnchor(Anchor anchor) {

        NBTTagCompound anchorNBT = new NBTTagCompound();
        NBTTagCompound muffledNBT = new NBTTagCompound();

        anchorNBT.setInteger("ID", anchor.getAnchorId());
        anchorNBT.setString("NAME", anchor.getName());

        if (anchor.getAnchorPos() == null) {
            return anchorNBT;
        }

        anchorNBT.setInteger("X", (int) anchor.getAnchorPos().x);
        anchorNBT.setInteger("Y", (int) anchor.getAnchorPos().y);
        anchorNBT.setInteger("Z", (int) anchor.getAnchorPos().z);
        anchorNBT.setString("DIM", anchor.getDimension());
        anchorNBT.setInteger("RAD", anchor.getRadius());
        anchor.getMuffledSounds()
            .forEach((R, F) -> muffledNBT.setFloat(R.toString(), F));
        anchorNBT.setTag("MUFFLED", muffledNBT);

        return anchorNBT;
    }

    public static Anchor deserializeAnchor(NBTTagCompound nbt) {
        SortedMap<String, Float> muffledSounds = new TreeMap<>();
        NBTTagCompound muffledNBT = nbt.getCompoundTag("MUFFLED");

        for (String key : muffledNBT.getKeySet()) {
            muffledSounds.put(key, muffledNBT.getFloat(key));
        }

        if (!nbt.hasKey("X") || !nbt.hasKey("Y") || !nbt.hasKey("Z")) {
            return new Anchor(nbt.getInteger("ID"), nbt.getString("NAME"));
        } else {
            return new Anchor(
                nbt.getInteger("ID"),
                nbt.getString("NAME"),
                new Vec3d(nbt.getInteger("X"), nbt.getInteger("Y"), nbt.getInteger("Z")),
                nbt.getString("DIM"),
                nbt.getInteger("RAD"),
                muffledSounds);
        }
    }

    private static void saveMuffledMap() {
        new File("ESM/").mkdir();
        try (Writer writer = new OutputStreamWriter(
            new FileOutputStream("ESM/soundsMuffled.dat"),
            StandardCharsets.UTF_8)) {
            writer.write(gson.toJson(muffledSounds));
        } catch (IOException ignored) {}
    }

    private static Map<String, Float> loadMuffledMap() {
        try (InputStreamReader reader = new InputStreamReader(
            new FileInputStream("ESM/soundsMuffled.dat"),
            StandardCharsets.UTF_8)) {
            return gson.fromJson(new JsonReader(reader), new TypeToken<Map<String, Float>>() {}.getType());
        } catch (JsonSyntaxException | IOException e) {
            return new HashMap<>();
        }
    }

    private static void saveAnchors() {
        File file = new File("ESM/" + identifier, "anchor.dat");
        NBTTagCompound anchorsNBT = new NBTTagCompound();
        for (Anchor anchor : anchorList) {
            anchorsNBT.setTag("Anchor" + anchor.getAnchorId(), serializeAnchor(anchor));
        }
        writeNBT(file, anchorsNBT);
    }

    private static List<Anchor> loadAnchors() {
        File file = new File("ESM/" + identifier, "anchor.dat");
        NBTTagCompound anchorsNBT = readNBT(file);
        if (anchorsNBT == null) {
            return IntStream.range(0, 10)
                .mapToObj(i -> new Anchor(i, "Anchor " + i))
                .collect(Collectors.toList());
        }

        List<Anchor> temp = new ArrayList<>();
        for (int i = 0; i < anchorsNBT.getKeySet()
            .size(); i++) {
            temp.add(deserializeAnchor(anchorsNBT.getCompoundTag("Anchor" + i)));
        }
        return temp;
    }

    public static void writeNBT(File file, NBTTagCompound tag) {
        ThreadedFileIOBase.getThreadedIOInstance().queueIO(() -> {
            try (FileOutputStream stream = FileUtils.openOutputStream(file)) {
                CompressedStreamTools.writeCompressed(tag, stream);
            } catch (Exception ex) {
                SoundMuffler.LOGGER.warn("Failed to save file: {}", file.getName(), ex);
            }
            return false;
        });
    }

    @Nullable
    public static NBTTagCompound readNBT(File file) {
        if (!file.exists() || !file.isFile()) {
            return null;
        }
        try (InputStream stream = FileUtils.openInputStream(file)) {
            return CompressedStreamTools.readCompressed(stream);
        } catch (Exception ex) {
            try {
                return CompressedStreamTools.read(file);
            } catch (Exception ex1) {
                return null;
            }
        }
    }
}
