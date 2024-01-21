package com.zygzag.zygzagsmod.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.zygzag.zygzagsmod.client.gecko.geomodel.BlazeSentryGeoModel;
import com.zygzag.zygzagsmod.common.entity.BlazeSentry;
import com.zygzag.zygzagsmod.common.util.Rotation;
import com.zygzag.zygzagsmod.common.util.RotationArray;
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
public class BlazeSentryRenderer extends GeoEntityRenderer<BlazeSentry> {
    public BlazeSentryRenderer(EntityRendererProvider.Context context) {
        super(context, new BlazeSentryGeoModel());
    }

    public float getMotionAnimThreshold(BlazeSentry animatable) {
        return 0.0001f;
    }

    @Override
    public void preRender(PoseStack poseStack, BlazeSentry sentry, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.preRender(poseStack, sentry, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        RotationArray rotations = sentry.rotations;
        Rotation head = rotations.get(0), body = rotations.get(1);
        model.getBone("rods").ifPresent((rods) -> {
            rods.updateRotation(body.getXRot(partialTick) + 0.5f * (float) Math.PI, body.getYRot(partialTick), 0);
        });
        model.getBone("head2").ifPresent((headBone) -> {
            headBone.updateRotation(head.getXRot(partialTick), head.getYRot(partialTick), 0);
        });
    }

    @Override
    protected int getBlockLightLevel(BlazeSentry sentry, BlockPos pos) {
        return 15;
    }
}
