package io.github.zygzaggaming.zygzagsmod.client.gecko.geomodel;

import io.github.zygzaggaming.zygzagsmod.common.Main;
import io.github.zygzaggaming.zygzagsmod.common.entity.assembly.RocketAAssembly;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class RocketAAssemblyGeoModel extends GeoModel<RocketAAssembly> {
    private static final ResourceLocation modelResource = ResourceLocation.fromNamespaceAndPath(Main.MODID,  "geo/rocket_a.geo.json");
    private static final ResourceLocation textureResource = ResourceLocation.fromNamespaceAndPath(Main.MODID,  "textures/entity/small_rod.png");
    private static final ResourceLocation animationResource = ResourceLocation.fromNamespaceAndPath(Main.MODID,  "animations/rocket_a.animation.json");

    @Override
    public ResourceLocation getModelResource(RocketAAssembly animatable) {
        return modelResource;
    }

    @Override
    public ResourceLocation getTextureResource(RocketAAssembly animatable) {
        return textureResource;
    }

    @Override
    public ResourceLocation getAnimationResource(RocketAAssembly animatable) {
        return animationResource;
    }
}
