package io.github.zygzaggaming.zygzagsmod.client.gecko.geomodel;

import io.github.zygzaggaming.zygzagsmod.common.Main;
import io.github.zygzaggaming.zygzagsmod.common.entity.IridiumGolem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class IridiumGolemGeoModel extends GeoModel<IridiumGolem> {
    private static final ResourceLocation modelResource = ResourceLocation.fromNamespaceAndPath(Main.MODID,  "geo/iridium_golem.geo.json");
    private static final ResourceLocation textureResource = ResourceLocation.fromNamespaceAndPath(Main.MODID,  "textures/entity/iridium_golem.png");
    private static final ResourceLocation animationResource = ResourceLocation.fromNamespaceAndPath(Main.MODID,  "animations/iridium_golem.animation.json");

    @Override
    public ResourceLocation getModelResource(IridiumGolem animatable) {
        return modelResource;
    }

    @Override
    public ResourceLocation getTextureResource(IridiumGolem animatable) {
        return textureResource;
    }

    @Override
    public ResourceLocation getAnimationResource(IridiumGolem animatable) {
        return animationResource;
    }
}
