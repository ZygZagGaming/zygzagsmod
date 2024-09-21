package io.github.zygzaggaming.zygzagsmod.client.renderer;

import io.github.zygzaggaming.zygzagsmod.client.gecko.geomodel.BigShurikenAssemblyGeoModel;
import io.github.zygzaggaming.zygzagsmod.common.entity.assembly.BigShurikenAssembly;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BigShurikenAssemblyRenderer extends GeoEntityRenderer<BigShurikenAssembly> {
    public BigShurikenAssemblyRenderer(EntityRendererProvider.Context context) {
        super(context, new BigShurikenAssemblyGeoModel());
    }

    public float getMotionAnimThreshold(BigShurikenAssembly animatable) {
        return 0.0001f;
    }

    @Override
    protected int getBlockLightLevel(BigShurikenAssembly sentry, BlockPos pos) {
        return 15;
    }
}