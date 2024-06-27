package com.leobeliik.extremesoundmuffler.interfaces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.leobeliik.extremesoundmuffler.utils.Anchor;
import com.leobeliik.extremesoundmuffler.utils.ComparableResource;

public interface ISoundLists {

    Set<String> forbiddenSounds = new HashSet<>();
    Set<ComparableResource> soundsList = new TreeSet<>();
    Set<ComparableResource> recentSoundsList = new TreeSet<>();
    Map<ComparableResource, Float> muffledSounds = new HashMap<>();
    List<Anchor> anchorList = new ArrayList<>();
}
