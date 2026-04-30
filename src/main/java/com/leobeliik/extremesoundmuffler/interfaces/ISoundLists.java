package com.leobeliik.extremesoundmuffler.interfaces;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.util.ResourceLocation;

import com.leobeliik.extremesoundmuffler.utils.Anchor;

import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;

public interface ISoundLists {

    Set<String> forbiddenSounds = new HashSet<>();
    Set<ResourceLocation> recentSoundsList = new LinkedHashSet<>();
    Object2FloatMap<ResourceLocation> muffledSounds = new Object2FloatOpenHashMap<>();
    List<Anchor> anchorList = new ArrayList<>();
}
