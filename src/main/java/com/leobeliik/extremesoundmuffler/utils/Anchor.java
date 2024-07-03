package com.leobeliik.extremesoundmuffler.utils;

import com.leobeliik.extremesoundmuffler.interfaces.ISoundLists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.DimensionManager;

import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;

public class Anchor {

    private final int id;
    private String name;
    private String dimension;
    private int radius;
    private SortedMap<String, Float> muffledSounds = new TreeMap<>();
    private Vec3d anchorPos;

    public Anchor(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Anchor(int id, String name, Vec3d anchorPos, String dimension, int radius,
                  SortedMap<String, Float> muffledSounds) {
        this.id = id;
        this.name = name;
        this.anchorPos = anchorPos;
        this.dimension = dimension;
        this.radius = radius;
        this.muffledSounds = muffledSounds;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public void addSound(ResourceLocation sound, float volume) {
        muffledSounds.put(sound.toString(), volume);
    }

    public void replaceSound(ResourceLocation sound, float volume) {
        muffledSounds.replace(sound.toString(), volume);
    }

    public int getX() {
        return anchorPos == null ? 0 : (int) anchorPos.x;
    }

    public int getY() {
        return anchorPos == null ? 0 : (int) anchorPos.y;
    }

    public int getZ() {
        return anchorPos == null ? 0 : (int) anchorPos.z;
    }

    public void removeSound(ResourceLocation sound) {
        muffledSounds.remove(sound.toString());
    }

    public void setAnchor() {
        EntityPlayerSP player = Objects.requireNonNull(Minecraft.getMinecraft().player);
        setAnchorPos((int) player.posX, (int) player.posY, (int) player.posZ);
        setDimension(
            DimensionManager.getProvider(player.dimension).getDimensionType().getName());
        setRadius(this.getRadius() == 0 ? 32 : this.getRadius());
    }

    private void setAnchorPos(int x, int y, int z) {
        anchorPos = new Vec3d(x, y, z);
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int Radius) {
        this.radius = Radius;
    }

    public void deleteAnchor() {
        setName("Anchor: " + this.getAnchorId());
        anchorPos = null;
        setDimension(null);
        setRadius(0);
        muffledSounds.clear();
    }

    public int getAnchorId() {
        return id;
    }

    public void editAnchor(String title, int Radius) {
        setName(title);
        setRadius(Radius);
    }

    public static Anchor getAnchor(ISound sound) {

        Vec3d soundPos = new Vec3d(sound.getXPosF(), sound.getYPosF(), sound.getZPosF());
        for (Anchor anchor : ISoundLists.anchorList) {
            WorldClient world = Minecraft.getMinecraft().world;
            if (anchor.getAnchorPos() != null && world != null
                && world.provider.getDimensionType().getName().equals(anchor.getDimension())
                && soundPos.distanceTo(anchor.getAnchorPos()) < anchor.getRadius()
                && anchor.getMuffledSounds()
                    .containsKey(new ComparableResource(sound.getSoundLocation()))) {
                return anchor;
            }
        }
        return null;
    }

    public Vec3d getAnchorPos() {
        return anchorPos;
    }

    public String getDimension() {
        return dimension;
    }

    public SortedMap<ComparableResource, Float> getMuffledSounds() {
        SortedMap<ComparableResource, Float> temp = new TreeMap<>();
        this.muffledSounds.forEach((R, F) -> temp.put(new ComparableResource(R), F));
        return temp;
    }

    public void setMuffledSounds(SortedMap<ResourceLocation, Float> muffledSounds) {
        muffledSounds.forEach((R, F) -> this.muffledSounds.put(R.toString(), F));
    }

    private void setDimension(String dimension) {
        this.dimension = dimension;
    }
}
