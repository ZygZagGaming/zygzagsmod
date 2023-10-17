package com.zygzag.zygzagsmod.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.zygzag.zygzagsmod.client.gecko.geomodel.BlazeSentryGeoModel;
import com.zygzag.zygzagsmod.common.entity.BlazeSentry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.zygzag.zygzagsmod.common.util.GeneralUtil.angleLerp;
import static com.zygzag.zygzagsmod.common.util.GeneralUtil.degreesToRadians;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BlazeSentryRenderer extends GeoEntityRenderer<BlazeSentry> {
    public BlazeSentryRenderer(EntityRendererProvider.Context context) {
        super(context, new BlazeSentryGeoModel());
    }

    public float getMotionAnimThreshold(BlazeSentry animatable) {
        return 0.0001f;
    }

    @Override
    public void preRender(PoseStack poseStack, BlazeSentry animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        model.getBone("rods").ifPresent((rods) -> {
            rods.updateRotation(degreesToRadians(90 + angleLerp(animatable.oldBodyXRot, animatable.bodyXRot, partialTick)), degreesToRadians(angleLerp(animatable.oldBodyYRot, animatable.bodyYRot, partialTick)), 0);
        });
        model.getBone("bb_main").ifPresent((bbMain) -> {
            bbMain.updateRotation(degreesToRadians(90 + angleLerp(animatable.oldHeadXRot, animatable.headXRot, partialTick)), degreesToRadians(angleLerp(animatable.oldHeadYRot, animatable.headYRot, partialTick)), 0);
        });
    }

    @Override
    protected int getBlockLightLevel(BlazeSentry sentry, BlockPos pos) {
        return 15;
    }
}
