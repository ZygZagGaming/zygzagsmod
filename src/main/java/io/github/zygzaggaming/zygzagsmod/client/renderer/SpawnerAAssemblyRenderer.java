package io.github.zygzaggaming.zygzagsmod.client.renderer;

import io.github.zygzaggaming.zygzagsmod.client.gecko.geomodel.SpawnerAAssemblyGeoModel;
import io.github.zygzaggaming.zygzagsmod.common.entity.assembly.SpawnerAAssembly;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SpawnerAAssemblyRenderer extends GeoEntityRenderer<SpawnerAAssembly> {
    public SpawnerAAssemblyRenderer(EntityRendererProvider.Context context) {
        super(context, new SpawnerAAssemblyGeoModel());
    }

    public float getMotionAnimThreshold(SpawnerAAssembly animatable) {
        return 0.0001f;
    }

    @Override
    protected int getBlockLightLevel(SpawnerAAssembly assembly, BlockPos pos) {
        return 15;
    }
}
