package com.leobeliik.extremesoundmuffler.interfaces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.util.ResourceLocation;

import com.leobeliik.extremesoundmuffler.utils.Anchor;

public interface ISoundLists {

    Set<String> forbiddenSounds = new HashSet<>();
    Set<ResourceLocation> recentSoundsList = new LinkedHashSet<>();
    Map<ResourceLocation, Float> muffledSounds = new HashMap<>();
    List<Anchor> anchorList = new ArrayList<>();
}
