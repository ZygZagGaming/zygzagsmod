package com.zygzag.zygzagsmod.client.gecko.geomodel;

import com.zygzag.zygzagsmod.common.block.entity.CacheBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

import static com.zygzag.zygzagsmod.common.Main.MODID;

public class RedNetherBrickCacheGeoModel extends GeoModel<CacheBlockEntity> {
    private static final ResourceLocation modelResource = new ResourceLocation(MODID, "geo/red_nether_brick_cache.geo.json");
    private static final ResourceLocation textureResource = new ResourceLocation(MODID, "textures/entity/red_nether_brick_cache.png");
    private static final ResourceLocation animationResource = new ResourceLocation(MODID, "animations/red_nether_brick_cache.animation.json");

    @Override
    public ResourceLocation getModelResource(CacheBlockEntity animatable) {
        return modelResource;
    }

    @Override
    public ResourceLocation getTextureResource(CacheBlockEntity animatable) {
        return textureResource;
    }

    @Override
    public ResourceLocation getAnimationResource(CacheBlockEntity animatable) {
        return animationResource;
    }
}
