package io.github.zygzaggaming.zygzagsmod.client.gecko.geomodel;

import io.github.zygzaggaming.zygzagsmod.common.Main;
import io.github.zygzaggaming.zygzagsmod.common.entity.assembly.HelixAAssembly;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class HelixAAssemblyGeoModel extends GeoModel<HelixAAssembly> {
    private static final ResourceLocation modelResource = ResourceLocation.fromNamespaceAndPath(Main.MODID,  "geo/helix_a.geo.json");
    private static final ResourceLocation textureResource = ResourceLocation.fromNamespaceAndPath(Main.MODID,  "textures/entity/small_rod.png");
    private static final ResourceLocation animationResource = ResourceLocation.fromNamespaceAndPath(Main.MODID,  "animations/helix_a.animation.json");

    @Override
    public ResourceLocation getModelResource(HelixAAssembly animatable) {
        return modelResource;
    }

    @Override
    public ResourceLocation getTextureResource(HelixAAssembly animatable) {
        return textureResource;
    }

    @Override
    public ResourceLocation getAnimationResource(HelixAAssembly animatable) {
        return animationResource;
    }
}
