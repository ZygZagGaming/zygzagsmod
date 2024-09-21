package io.github.zygzaggaming.zygzagsmod.client.gecko.geomodel;

import io.github.zygzaggaming.zygzagsmod.common.Main;
import io.github.zygzaggaming.zygzagsmod.common.entity.BlazeSentry;
import io.github.zygzaggaming.zygzagsmod.common.entity.assembly.ShurikenAssembly;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ShurikenAssemblyGeoModel extends GeoModel<ShurikenAssembly> {
    private static final ResourceLocation modelResource = ResourceLocation.fromNamespaceAndPath(Main.MODID,  "geo/four_rod_a.geo.json");
    private static final ResourceLocation textureResource = ResourceLocation.fromNamespaceAndPath(Main.MODID,  "textures/entity/small_rod.png");
    private static final ResourceLocation animationResource = ResourceLocation.fromNamespaceAndPath(Main.MODID,  "animations/shuriken_ab.animation.json");

    @Override
    public ResourceLocation getModelResource(ShurikenAssembly animatable) {
        return modelResource;
    }

    @Override
    public ResourceLocation getTextureResource(ShurikenAssembly animatable) {
        return textureResource;
    }

    @Override
    public ResourceLocation getAnimationResource(ShurikenAssembly animatable) {
        return animationResource;
    }
}
