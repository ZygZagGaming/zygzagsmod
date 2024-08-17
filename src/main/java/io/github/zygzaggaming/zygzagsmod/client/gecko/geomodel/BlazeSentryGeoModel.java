package io.github.zygzaggaming.zygzagsmod.client.gecko.geomodel;

import io.github.zygzaggaming.zygzagsmod.common.Main;
import io.github.zygzaggaming.zygzagsmod.common.entity.BlazeSentry;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BlazeSentryGeoModel extends GeoModel<BlazeSentry> {
    private static final ResourceLocation modelResource = ResourceLocation.fromNamespaceAndPath(Main.MODID,  "geo/blaze_sentry.geo.json");
    private static final ResourceLocation textureResource = ResourceLocation.fromNamespaceAndPath(Main.MODID,  "textures/entity/blaze_sentry.png");
    private static final ResourceLocation animationResource = ResourceLocation.fromNamespaceAndPath(Main.MODID,  "animations/blaze_sentry.animation.json");

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
