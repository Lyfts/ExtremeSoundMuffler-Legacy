package com.leobeliik.extremesoundmuffler.interfaces;

import net.minecraft.util.ResourceLocation;

import com.leobeliik.extremesoundmuffler.utils.Anchor;

import it.unimi.dsi.fastutil.objects.Object2FloatAVLTreeMap;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectSet;

public interface ISoundLists {

    ObjectSet<String> forbiddenSounds = new ObjectArraySet<>();
    ObjectSet<ResourceLocation> recentSoundsList = new ObjectLinkedOpenHashSet<>();
    Object2FloatMap<ResourceLocation> muffledSounds = new Object2FloatAVLTreeMap<>();
    ObjectList<Anchor> anchorList = new ObjectArrayList<>();
}
