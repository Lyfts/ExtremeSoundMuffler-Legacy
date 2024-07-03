package com.leobeliik.extremesoundmuffler.utils;

import net.minecraft.util.ResourceLocation;

public class ComparableResource extends ResourceLocation implements Comparable<ResourceLocation> {

    public ComparableResource(String domain, String path) {
        super(domain, path);
    }

    public ComparableResource(String path) {
        super(path);
    }

    public ComparableResource(ResourceLocation resourceLocation) {
        super(resourceLocation.getNamespace(), resourceLocation.getPath());
    }

    @Override
    public int compareTo(ResourceLocation o) {
        return this.toString()
            .compareTo(o.toString());
    }
}
