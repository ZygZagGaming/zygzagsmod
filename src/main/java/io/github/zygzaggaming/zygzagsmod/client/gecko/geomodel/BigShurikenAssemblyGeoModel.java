package io.github.zygzaggaming.zygzagsmod.client.gecko.geomodel;

import io.github.zygzaggaming.zygzagsmod.common.Main;
import io.github.zygzaggaming.zygzagsmod.common.entity.assembly.BigShurikenAssembly;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BigShurikenAssemblyGeoModel extends GeoModel<BigShurikenAssembly> {
    private static final ResourceLocation modelResource = ResourceLocation.fromNamespaceAndPath(Main.MODID,  "geo/eight_shuriken_c.geo.json");
    private static final ResourceLocation textureResource = ResourceLocation.fromNamespaceAndPath(Main.MODID,  "textures/entity/small_rod.png");
    private static final ResourceLocation animationResource = ResourceLocation.fromNamespaceAndPath(Main.MODID,  "animations/shuriken_c.animation.json");

    @Override
    public ResourceLocation getModelResource(BigShurikenAssembly animatable) {
        return modelResource;
    }

    @Override
    public ResourceLocation getTextureResource(BigShurikenAssembly animatable) {
        return textureResource;
    }

    @Override
    public ResourceLocation getAnimationResource(BigShurikenAssembly animatable) {
        return animationResource;
    }
}
