package com.leobeliik.extremesoundmuffler.utils;

import java.util.Comparator;
import java.util.Objects;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.DimensionManager;

import com.leobeliik.extremesoundmuffler.interfaces.ISoundLists;

import it.unimi.dsi.fastutil.objects.Object2FloatAVLTreeMap;
import it.unimi.dsi.fastutil.objects.Object2FloatSortedMap;

public class Anchor {

    private final int id;
    private String name;
    private int dimensionId;
    private int radius;
    private Object2FloatSortedMap<String> muffledSounds = new Object2FloatAVLTreeMap<>();
    private Vec3 anchorPos;

    public Anchor(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Anchor(int id, String name, Vec3 anchorPos, int dimensionId, int radius,
        Object2FloatSortedMap<String> muffledSounds) {
        this.id = id;
        this.name = name;
        this.anchorPos = anchorPos;
        this.dimensionId = dimensionId;
        this.radius = radius;
        this.muffledSounds = muffledSounds;
    }

    public Vec3 getAnchorPos() {
        return anchorPos;
    }

    private void setAnchorPos(int x, int y, int z) {
        anchorPos = Vec3.createVectorHelper(x, y, z);
    }

    public int getAnchorId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int Radius) {
        this.radius = Radius;
    }

    private void setName(String name) {
        this.name = name;
    }

    public Object2FloatSortedMap<ResourceLocation> getMuffledSounds() {
        Object2FloatSortedMap<ResourceLocation> temp = new Object2FloatAVLTreeMap<>(
            Comparator.comparing(ResourceLocation::toString));
        this.muffledSounds.object2FloatEntrySet()
            .forEach(entry -> temp.put(new ResourceLocation(entry.getKey()), entry.getFloatValue()));
        return temp;
    }

    public void addSound(ResourceLocation sound, float volume) {
        muffledSounds.put(sound.toString(), volume);
    }

    public void replaceSound(ResourceLocation sound, float volume) {
        muffledSounds.replace(sound.toString(), volume);
    }

    public int getX() {
        return anchorPos == null ? 0 : (int) anchorPos.xCoord;
    }

    public int getY() {
        return anchorPos == null ? 0 : (int) anchorPos.yCoord;
    }

    public int getZ() {
        return anchorPos == null ? 0 : (int) anchorPos.zCoord;
    }

    public int getDimensionId() {
        return this.dimensionId;
    }

    public String getDimensionName() {
        if (this.dimensionId == Integer.MIN_VALUE) return "";
        String name = DimensionManager.createProviderFor(this.dimensionId)
            .getDimensionName();
        return name == null ? "???" : name;
    }

    private void setDimensionId(int id) {
        this.dimensionId = id;
    }

    public void removeSound(ResourceLocation sound) {
        muffledSounds.removeFloat(sound.toString());
    }

    public void setAnchor() {
        EntityClientPlayerMP player = Objects.requireNonNull(Minecraft.getMinecraft().thePlayer);
        setAnchorPos((int) player.posX, (int) player.posY, (int) player.posZ);
        setDimensionId(player.dimension);
        setRadius(this.getRadius() == 0 ? 32 : this.getRadius());
        DataManager.setDirty();
    }

    public void deleteAnchor() {
        setName("Anchor " + this.getAnchorId());
        anchorPos = null;
        setDimensionId(Integer.MIN_VALUE);
        setRadius(0);
        muffledSounds.clear();
        DataManager.setDirty();
    }

    public void editAnchor(String title, int Radius) {
        setName(title);
        setRadius(Radius);
        DataManager.setDirty();
    }

    public static Anchor getAnchor(ISound sound) {
        Vec3 soundPos = Vec3.createVectorHelper(sound.getXPosF(), sound.getYPosF(), sound.getZPosF());
        for (Anchor anchor : ISoundLists.anchorList) {
            WorldClient world = Minecraft.getMinecraft().theWorld;
            if (anchor.getAnchorPos() != null && world != null
                && world.provider.dimensionId == anchor.getDimensionId()
                && soundPos.distanceTo(anchor.getAnchorPos()) < anchor.getRadius()
                && anchor.getMuffledSounds()
                    .containsKey(sound.getPositionedSoundLocation())) {
                return anchor;
            }
        }
        return null;
    }
}
