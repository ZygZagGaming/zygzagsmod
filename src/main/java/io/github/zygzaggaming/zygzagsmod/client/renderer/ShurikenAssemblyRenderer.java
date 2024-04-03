package io.github.zygzaggaming.zygzagsmod.client.renderer;

import io.github.zygzaggaming.zygzagsmod.client.gecko.geomodel.ShurikenAssemblyGeoModel;
import io.github.zygzaggaming.zygzagsmod.common.entity.assembly.ShurikenAssembly;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ShurikenAssemblyRenderer extends GeoEntityRenderer<ShurikenAssembly> {
    public ShurikenAssemblyRenderer(EntityRendererProvider.Context context) {
        super(context, new ShurikenAssemblyGeoModel());
    }

    public float getMotionAnimThreshold(ShurikenAssembly animatable) {
        return 0.0001f;
    }

    @Override
    protected int getBlockLightLevel(ShurikenAssembly sentry, BlockPos pos) {
        return 15;
    }
}
