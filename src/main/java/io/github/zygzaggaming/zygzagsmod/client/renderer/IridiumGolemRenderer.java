package io.github.zygzaggaming.zygzagsmod.client.renderer;

import io.github.zygzaggaming.zygzagsmod.client.gecko.geomodel.IridiumGolemGeoModel;
import io.github.zygzaggaming.zygzagsmod.common.entity.IridiumGolem;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class IridiumGolemRenderer extends GeoEntityRenderer<IridiumGolem> {
    public IridiumGolemRenderer(EntityRendererProvider.Context context) {
        super(context, new IridiumGolemGeoModel());
    }

    public float getMotionAnimThreshold(IridiumGolem animatable) {
        return 0.0001f;
    }
}
