package io.github.zygzaggaming.zygzagsmod.client.gecko.geomodel;

import io.github.zygzaggaming.zygzagsmod.common.Main;
import io.github.zygzaggaming.zygzagsmod.common.entity.assembly.HelixBAssembly;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class HelixBAssemblyGeoModel extends GeoModel<HelixBAssembly> {
    private static final ResourceLocation modelResource = ResourceLocation.fromNamespaceAndPath(Main.MODID,  "geo/helix_b.geo.json");
    private static final ResourceLocation textureResource = ResourceLocation.fromNamespaceAndPath(Main.MODID,  "textures/entity/small_rod.png");
    private static final ResourceLocation animationResource = ResourceLocation.fromNamespaceAndPath(Main.MODID,  "animations/helix_b.animation.json");

    @Override
    public ResourceLocation getModelResource(HelixBAssembly animatable) {
        return modelResource;
    }

    @Override
    public ResourceLocation getTextureResource(HelixBAssembly animatable) {
        return textureResource;
    }

    @Override
    public ResourceLocation getAnimationResource(HelixBAssembly animatable) {
        return animationResource;
    }
}
