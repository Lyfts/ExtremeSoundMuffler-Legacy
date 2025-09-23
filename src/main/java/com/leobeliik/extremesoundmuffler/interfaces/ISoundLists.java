package com.leobeliik.extremesoundmuffler.interfaces;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import net.minecraft.util.ResourceLocation;

import com.leobeliik.extremesoundmuffler.utils.Anchor;

public interface ISoundLists {

    Set<String> forbiddenSounds = new HashSet<>();
    Set<ResourceLocation> soundsList = new TreeSet<>(Comparator.comparing(ResourceLocation::toString));
    Set<ResourceLocation> recentSoundsList = new TreeSet<>(Comparator.comparing(ResourceLocation::toString));
    Map<ResourceLocation, Float> muffledSounds = new HashMap<>();
    List<Anchor> anchorList = new ArrayList<>();
}
