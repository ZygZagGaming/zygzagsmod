package io.github.zygzaggaming.zygzagsmod.client.gecko.geomodel;

import io.github.zygzaggaming.zygzagsmod.common.Main;
import io.github.zygzaggaming.zygzagsmod.common.entity.assembly.SentryAAssembly;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SentryAAssemblyGeoModel extends GeoModel<SentryAAssembly> {
    private static final ResourceLocation modelResource = ResourceLocation.fromNamespaceAndPath(Main.MODID,  "geo/four_rod_a.geo.json");
    private static final ResourceLocation textureResource = ResourceLocation.fromNamespaceAndPath(Main.MODID,  "textures/entity/small_rod.png");
    private static final ResourceLocation animationResource = ResourceLocation.fromNamespaceAndPath(Main.MODID,  "animations/sentry_a.animation.json");

    @Override
    public ResourceLocation getModelResource(SentryAAssembly animatable) {
        return modelResource;
    }

    @Override
    public ResourceLocation getTextureResource(SentryAAssembly animatable) {
        return textureResource;
    }

    @Override
    public ResourceLocation getAnimationResource(SentryAAssembly animatable) {
        return animationResource;
    }
}
