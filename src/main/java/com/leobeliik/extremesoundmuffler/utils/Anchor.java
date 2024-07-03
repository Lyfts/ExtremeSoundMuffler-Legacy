package com.leobeliik.extremesoundmuffler.utils;

import com.leobeliik.extremesoundmuffler.interfaces.ISoundLists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
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
    private BlockPos anchorPos;

    public Anchor(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Anchor(int id, String name, BlockPos anchorPos, String dimension, int radius,
                  SortedMap<String, Float> muffledSounds) {
        this.id = id;
        this.name = name;
        this.anchorPos = anchorPos;
        this.dimension = dimension;
        this.radius = radius;
        this.muffledSounds = muffledSounds;
    }

    public BlockPos getAnchorPos() {
        return anchorPos;
    }

    private void setAnchorPos(BlockPos pos) {
        anchorPos = pos;
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

    public SortedMap<ResourceLocation, Float> getMuffledSounds() {
        SortedMap<ResourceLocation, Float> temp = new TreeMap<>();
        this.muffledSounds.forEach((R, F) -> temp.put(new ResourceLocation(R), F));
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

    public int getX() {
        return anchorPos == null ? 0 : anchorPos.getX();
    }

    public int getY() {
        return anchorPos == null ? 0 : anchorPos.getY();
    }

    public int getZ() {
        return anchorPos == null ? 0 : anchorPos.getZ();
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
        EntityPlayerSP player = Objects.requireNonNull(Minecraft.getMinecraft().player);
        setAnchorPos(player.getPosition());
        setDimension(
            DimensionManager.getProvider(player.dimension).getDimensionType().getName());
        setRadius(this.getRadius() == 0 ? 32 : this.getRadius());
    }

    public void deleteAnchor() {
        setName("Anchor: " + this.getAnchorId());
        anchorPos = null;
        setDimension(null);
        setRadius(0);
        muffledSounds.clear();
    }

    public void editAnchor(String title, int Radius) {
        setName(title);
        setRadius(Radius);
    }

    public static Anchor getAnchor(ISound sound) {
        BlockPos soundPos = new BlockPos(sound.getXPosF(), sound.getYPosF(), sound.getZPosF());
        for (Anchor anchor : ISoundLists.anchorList) {
            WorldClient world = Minecraft.getMinecraft().world;
            if (anchor.getAnchorPos() != null && world != null
                && world.provider.getDimensionType().getName()
                    .equals(anchor.getDimension())
                && Math.sqrt(soundPos.distanceSq(anchor.getAnchorPos())) <= anchor.getRadius()
                && anchor.getMuffledSounds()
                    .containsKey(sound.getSoundLocation())) {
                return anchor;
            }
        }
        return null;
    }
}
