package io.github.zygzaggaming.zygzagsmod.client.gecko.geomodel;

import io.github.zygzaggaming.zygzagsmod.common.Main;
import io.github.zygzaggaming.zygzagsmod.common.entity.BlazeSentry;
import io.github.zygzaggaming.zygzagsmod.common.entity.SmallRod;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SmallRodGeoModel extends GeoModel<SmallRod> {
    private static final ResourceLocation modelResource = ResourceLocation.fromNamespaceAndPath(Main.MODID,  "geo/small_rod.geo.json");
    private static final ResourceLocation textureResource = ResourceLocation.fromNamespaceAndPath(Main.MODID,  "textures/entity/small_rod.png");
    private static final ResourceLocation animationResource = ResourceLocation.fromNamespaceAndPath(Main.MODID,  "animations/small_rod.animation.json");

    @Override
    public ResourceLocation getModelResource(SmallRod animatable) {
        return modelResource;
    }

    @Override
    public ResourceLocation getTextureResource(SmallRod animatable) {
        return textureResource;
    }

    @Override
    public ResourceLocation getAnimationResource(SmallRod animatable) {
        return animationResource;
    }
}
