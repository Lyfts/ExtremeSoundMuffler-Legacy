package com.leobeliik.extremesoundmuffler.interfaces;

import com.leobeliik.extremesoundmuffler.utils.Anchor;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public interface ISoundLists {

    Set<String> forbiddenSounds = new HashSet<>();
    Set<ResourceLocation> soundsList = new TreeSet<>();
    Set<ResourceLocation> recentSoundsList = new TreeSet<>();
    Map<ResourceLocation, Float> muffledSounds = new HashMap<>();
    List<Anchor> anchorList = new ArrayList<>();
}
