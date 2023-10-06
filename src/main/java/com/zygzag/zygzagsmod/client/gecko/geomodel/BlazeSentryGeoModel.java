package com.zygzag.zygzagsmod.client.gecko.geomodel;

import com.zygzag.zygzagsmod.common.entity.BlazeSentry;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

import static com.zygzag.zygzagsmod.common.Main.MODID;

public class BlazeSentryGeoModel extends GeoModel<BlazeSentry> {
    private static final ResourceLocation modelResource = new ResourceLocation(MODID, "geo/blaze_sentry.geo.json");
    private static final ResourceLocation textureResource = new ResourceLocation(MODID, "textures/entity/blaze_sentry/blaze_sentry.png");
    private static final ResourceLocation animationResource = new ResourceLocation(MODID, "animations/blaze_sentry/blaze_sentry.animation.json");

    @Override
    public ResourceLocation getModelResource(BlazeSentry animatable) {
        return modelResource;
    }

    @Override
    public ResourceLocation getTextureResource(BlazeSentry animatable) {
        return textureResource;
    }

    @Override
    public ResourceLocation getAnimationResource(BlazeSentry animatable) {
        return animationResource;
    }
}
