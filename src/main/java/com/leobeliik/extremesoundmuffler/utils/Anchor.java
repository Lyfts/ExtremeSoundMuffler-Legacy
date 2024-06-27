package com.leobeliik.extremesoundmuffler.utils;

import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.DimensionManager;

@SuppressWarnings("WeakerAccess")
public class Anchor {

    private final int id;
    private String name;
    private String dimension;
    private int radius;
    private SortedMap<String, Float> muffledSounds = new TreeMap<>();
    private int x, y, z;

    public Anchor(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Anchor(int id, String name, int x, int y, int z, String dimension, int radius,
        SortedMap<String, Float> muffledSounds) {
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimension = dimension;
        this.radius = radius;
        this.muffledSounds = muffledSounds;
    }

    public Vec3 getAnchorPos() {
        return Vec3.createVectorHelper(x, y, z);
    }

    private void setAnchorPos(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
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

    public SortedMap<ComparableResource, Float> getMuffledSounds() {
        SortedMap<ComparableResource, Float> temp = new TreeMap<>();
        this.muffledSounds.forEach((R, F) -> temp.put(new ComparableResource(R), F));
        return temp;
    }

    public void setMuffledSounds(SortedMap<ResourceLocation, Float> muffledSounds) {
        muffledSounds.forEach((R, F) -> this.muffledSounds.put(R.toString(), F));
    }

    public void addSound(ResourceLocation sound, float volume) {
        muffledSounds.put(sound.toString(), volume);
    }

    public void replaceSound(ResourceLocation sound, float volume) {
        muffledSounds.replace(sound.toString(), volume);
    }

    public String getX() {
        return String.valueOf(x);
    }

    public String getY() {
        return String.valueOf(y);
    }

    public String getZ() {
        return String.valueOf(z);
    }

    public String getDimension() {
        return dimension;
    }

    private void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public void removeSound(ResourceLocation sound) {
        muffledSounds.remove(sound.toString());
    }

    public void setAnchor() {
        EntityClientPlayerMP player = Objects.requireNonNull(Minecraft.getMinecraft().thePlayer);
        setAnchorPos(player.serverPosX, player.serverPosY, player.serverPosZ);
        setDimension(
            DimensionManager.getProvider(player.dimension)
                .getDimensionName());
        setRadius(this.getRadius() == 0 ? 32 : this.getRadius());
    }

    public void deleteAnchor() {
        setName("Anchor: " + this.getAnchorId());
        setAnchorPos(0, 0, 0);
        setDimension(null);
        setRadius(0);
        muffledSounds.clear();
    }

    public void editAnchor(String title, int Radius) {
        setName(title);
        setRadius(Radius);
    }

    public static Anchor getAnchor(ISound sound) {

        // BlockPos soundPos = new BlockPos(sound.getX(), sound.getY(), sound.getZ());
        // for (Anchor anchor : ISoundLists.anchorList) {
        // ClientLevel world = Minecraft.getInstance().level;
        // if (anchor.getAnchorPos() != null
        // && world != null
        // && world.dimension().location().equals(anchor.getDimension())
        // && soundPos.closerThan(anchor.getAnchorPos(), anchor.getRadius())
        // && anchor.getMuffledSounds().containsKey(sound.getLocation())) {
        // return anchor;
        // }
        // }
        return null;
    }
}
