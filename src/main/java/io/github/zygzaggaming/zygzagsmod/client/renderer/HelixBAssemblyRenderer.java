package io.github.zygzaggaming.zygzagsmod.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.zygzaggaming.zygzagsmod.client.gecko.geomodel.HelixBAssemblyGeoModel;
import io.github.zygzaggaming.zygzagsmod.common.entity.assembly.HelixBAssembly;
import io.github.zygzaggaming.zygzagsmod.common.util.Rotation;
import io.github.zygzaggaming.zygzagsmod.common.util.RotationArray;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class HelixBAssemblyRenderer extends GeoEntityRenderer<HelixBAssembly> {
    public HelixBAssemblyRenderer(EntityRendererProvider.Context context) {
        super(context, new HelixBAssemblyGeoModel());
    }

    public float getMotionAnimThreshold(HelixBAssembly animatable) {
        return 0.0001f;
    }

    @Override
    protected int getBlockLightLevel(HelixBAssembly assembly, BlockPos pos) {
        return 15;
    }
}
