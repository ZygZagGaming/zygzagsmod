package io.github.zygzaggaming.zygzagsmod.client.gecko.geomodel;

import io.github.zygzaggaming.zygzagsmod.common.Main;
import io.github.zygzaggaming.zygzagsmod.common.block.entity.CacheBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class RedNetherBrickCacheGeoModel extends GeoModel<CacheBlockEntity> {
    private static final ResourceLocation modelResource = new ResourceLocation(Main.MODID, "geo/red_nether_brick_cache.geo.json");
    private static final ResourceLocation textureResource = new ResourceLocation(Main.MODID, "textures/entity/red_nether_brick_cache.png");
    private static final ResourceLocation animationResource = new ResourceLocation(Main.MODID, "animations/red_nether_brick_cache.animation.json");

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