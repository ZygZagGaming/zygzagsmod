package com.zygzag.zygzagsmod.client.gecko.geomodel;

import com.zygzag.zygzagsmod.common.entity.IridiumGolem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

import static com.zygzag.zygzagsmod.common.Main.MODID;

public class IridiumGolemGeoModel extends GeoModel<IridiumGolem> {
    private static final ResourceLocation modelResource = new ResourceLocation(MODID, "geo/iridium_golem.geo.json");
    private static final ResourceLocation textureResource = new ResourceLocation(MODID, "textures/entity/iridium_golem/iridium_golem.png");
    private static final ResourceLocation animationResource = new ResourceLocation(MODID, "animations/iridium_golem/iridium_golem.animation.json");

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
