package io.github.zygzaggaming.zygzagsmod.client.gecko.geomodel;

import io.github.zygzaggaming.zygzagsmod.common.Main;
import io.github.zygzaggaming.zygzagsmod.common.entity.assembly.HelixBAssembly;
import io.github.zygzaggaming.zygzagsmod.common.entity.assembly.SpawnerAAssembly;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SpawnerAAssemblyGeoModel extends GeoModel<SpawnerAAssembly> {
    private static final ResourceLocation modelResource = ResourceLocation.fromNamespaceAndPath(Main.MODID,  "geo/spawner_a.geo.json");
    private static final ResourceLocation textureResource = ResourceLocation.fromNamespaceAndPath(Main.MODID,  "textures/entity/small_rod.png");
    private static final ResourceLocation animationResource = ResourceLocation.fromNamespaceAndPath(Main.MODID,  "animations/spawner_a.animation.json");

    @Override
    public ResourceLocation getModelResource(SpawnerAAssembly animatable) {
        return modelResource;
    }

    @Override
    public ResourceLocation getTextureResource(SpawnerAAssembly animatable) {
        return textureResource;
    }

    @Override
    public ResourceLocation getAnimationResource(SpawnerAAssembly animatable) {
        return animationResource;
    }
}
