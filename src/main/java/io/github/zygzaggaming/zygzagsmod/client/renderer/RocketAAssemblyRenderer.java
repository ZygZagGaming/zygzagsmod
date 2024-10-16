package io.github.zygzaggaming.zygzagsmod.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.zygzaggaming.zygzagsmod.client.gecko.geomodel.RocketAAssemblyGeoModel;
import io.github.zygzaggaming.zygzagsmod.client.gecko.geomodel.SentryAAssemblyGeoModel;
import io.github.zygzaggaming.zygzagsmod.common.entity.assembly.RocketAAssembly;
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
public class RocketAAssemblyRenderer extends GeoEntityRenderer<RocketAAssembly> {
    public RocketAAssemblyRenderer(EntityRendererProvider.Context context) {
        super(context, new RocketAAssemblyGeoModel());
    }

    public float getMotionAnimThreshold(RocketAAssembly animatable) {
        return 0.0001f;
    }

    @Override
    public void preRender(PoseStack poseStack, RocketAAssembly rod, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int color) {
        super.preRender(poseStack, rod, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, color);
        RotationArray rotations = rod.rotations;
        Rotation body = rotations.get(0);
        final float p = partialTick;
        model.getBone("body2").ifPresent((headBone) -> {
            //System.out.println("rod rot is " + GeneralUtil.radiansToDegrees(head.getXRot(partialTick)) + ", " + GeneralUtil.radiansToDegrees(head.getYRot(partialTick)));
            headBone.updateRotation(body.getXRot(p), body.getYRot(p), 0);
        });
    }

    @Override
    protected int getBlockLightLevel(RocketAAssembly assembly, BlockPos pos) {
        return 15;
    }
}
