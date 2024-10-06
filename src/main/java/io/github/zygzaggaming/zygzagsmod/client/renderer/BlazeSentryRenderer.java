package io.github.zygzaggaming.zygzagsmod.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.zygzaggaming.zygzagsmod.client.gecko.geomodel.BlazeSentryGeoModel;
import io.github.zygzaggaming.zygzagsmod.common.entity.BlazeSentry;
import io.github.zygzaggaming.zygzagsmod.common.util.Rotation;
import io.github.zygzaggaming.zygzagsmod.common.util.RotationArray;
import io.github.zygzaggaming.zygzagsmod.common.util.WorldlyRotation;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import static java.lang.Math.PI;

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
    public void preRender(PoseStack poseStack, BlazeSentry sentry, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int color) {
        super.preRender(poseStack, sentry, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, color);
        WorldlyRotation[] rotations = sentry.newRotations;
        WorldlyRotation head = rotations[0], body = rotations[1];
        final float p = partialTick;
        Level world = sentry.level();
        model.getBone("rod_wrapper").ifPresent((rods) -> {
            //System.out.println("rod rot is " + body.getXRot(partialTick) + ", " + body.getYRot(partialTick));
            rods.updateRotation((float) body.getXRot(world, p).inRadians(), (float) body.getYRot(world, p).inRadians(), (float) body.getZRot(world, p).inRadians());
        });
        model.getBone("head2").ifPresent((headBone) -> {
            //System.out.println("head rot is " + head.getXRot(partialTick) + ", " + head.getYRot(partialTick));
//            headBone.updateRotation(0, (float) (PI * 0.25), (float) (PI * 0.25));
            headBone.updateRotation((float) head.getXRot(world, p).inRadians(), (float) head.getYRot(world, p).inRadians(), (float) head.getZRot(world, p).inRadians());
        });
    }

    @Override
    protected int getBlockLightLevel(BlazeSentry sentry, BlockPos pos) {
        return 15;
    }
}
