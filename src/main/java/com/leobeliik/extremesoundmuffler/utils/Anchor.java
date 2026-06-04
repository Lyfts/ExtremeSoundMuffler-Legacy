package com.leobeliik.extremesoundmuffler.utils;

import java.util.Objects;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

import com.leobeliik.extremesoundmuffler.interfaces.ISoundLists;

import it.unimi.dsi.fastutil.objects.Object2FloatAVLTreeMap;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;

public class Anchor {

    private final int id;
    private String name;
    private int dimensionId;
    private int radius;
    private Object2FloatMap<String> muffledSounds = new Object2FloatAVLTreeMap<>();
    private BlockPos anchorPos;

    public Anchor(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Anchor(int id, String name, BlockPos anchorPos, int dimensionId, int radius,
                  Object2FloatMap<String> muffledSounds) {
        this.id = id;
        this.name = name;
        this.anchorPos = anchorPos;
        this.dimensionId = dimensionId;
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

    public Object2FloatMap<ResourceLocation> getMuffledSounds() {
        Object2FloatMap<ResourceLocation> temp = new Object2FloatAVLTreeMap<>();
        this.muffledSounds.forEach((R, F) -> temp.put(new ResourceLocation(R), F));
        return temp;
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

    public int getDimensionId() {
        return this.dimensionId;
    }

    public String getDimensionName() {
        EntityPlayerSP player = Objects.requireNonNull(Minecraft.getMinecraft().player);
        String DimensionName = player.world.provider.getDimensionType().getName();
        return DimensionName == null ? "???" : DimensionName;
    }

    private void setDimensionId(int dimensionId) {
        this.dimensionId = dimensionId;
    }

    public void removeSound(ResourceLocation sound) {
        muffledSounds.remove(sound.toString());
    }

    public void setAnchor() {
        EntityPlayerSP player = Objects.requireNonNull(Minecraft.getMinecraft().player);
        setAnchorPos(player.getPosition());
        setDimensionId(player.dimension);
        setRadius(this.getRadius() == 0 ? 32 : this.getRadius());
        DataManager.setDirty();
    }

    public void deleteAnchor() {
        setName("Anchor: " + this.getAnchorId());
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
        BlockPos soundPos = new BlockPos(sound.getXPosF(), sound.getYPosF(), sound.getZPosF());
        for (Anchor anchor : ISoundLists.anchorList) {
            WorldClient world = Minecraft.getMinecraft().world;
            if (anchor.getAnchorPos() != null && world != null &&
                    world.provider.getDimension() == anchor.getDimensionId() &&
                    Math.sqrt(soundPos.distanceSq(anchor.getAnchorPos())) <= anchor.getRadius() &&
                    anchor.getMuffledSounds()
                            .containsKey(sound.getSoundLocation())) {
                return anchor;
            }
        }
        return null;
    }
}
