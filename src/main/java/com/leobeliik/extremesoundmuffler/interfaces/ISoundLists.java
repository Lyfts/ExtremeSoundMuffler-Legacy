package com.leobeliik.extremesoundmuffler.interfaces;

import com.leobeliik.extremesoundmuffler.utils.Anchor;
import it.unimi.dsi.fastutil.objects.Object2FloatAVLTreeMap;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.ObjectAVLTreeSet;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.minecraft.util.ResourceLocation;

public interface ISoundLists {

    ObjectSet<String> forbiddenSounds = new ObjectArraySet<>();
    ObjectSet<ResourceLocation> soundsList = new ObjectAVLTreeSet<>();
    ObjectSet<ResourceLocation> recentSoundsList = new ObjectAVLTreeSet<>();
    Object2FloatMap<ResourceLocation> muffledSounds = new Object2FloatAVLTreeMap<>();
    ObjectList<Anchor> anchorList = new ObjectArrayList<>();
}
