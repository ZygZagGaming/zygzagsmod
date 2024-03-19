package io.github.zygzaggaming.zygzagsmod.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.zygzaggaming.zygzagsmod.common.entity.FlailProjectile;
import io.github.zygzaggaming.zygzagsmod.common.registry.BlockWithItemRegistry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.RenderTypeHelper;
import net.neoforged.neoforge.client.model.data.ModelData;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class FlailProjectileRenderer extends EntityRenderer<FlailProjectile> {
    private final BlockRenderDispatcher dispatcher;

    public FlailProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
        shadowRadius = 0.25f;
        dispatcher = context.getBlockRenderDispatcher();
    }

    public void render(FlailProjectile entity, float a, float b, PoseStack stack, MultiBufferSource source, int c) {
        BlockState blockstate = BlockWithItemRegistry.CORONAL_AMBER.getDefaultBlockState();
        if (blockstate.getRenderShape() == RenderShape.MODEL) {
            Level level = entity.level();
            if (blockstate != level.getBlockState(entity.blockPosition()) && blockstate.getRenderShape() != RenderShape.INVISIBLE) {
                stack.pushPose();
                BlockPos blockpos = BlockPos.containing(entity.getX(), entity.getBoundingBox().maxY, entity.getZ());
                stack.translate(-0.5, 0, -0.5);
                var model = this.dispatcher.getBlockModel(blockstate);
                for (var renderType : model.getRenderTypes(blockstate, RandomSource.create(blockstate.getSeed(BlockPos.ZERO)), ModelData.EMPTY))
                    dispatcher.getModelRenderer().tesselateBlock(
                            level,
                            this.dispatcher.getBlockModel(blockstate),
                            blockstate,
                            blockpos,
                            stack,
                            source.getBuffer(RenderTypeHelper.getMovingBlockRenderType(renderType)),
                            false,
                            RandomSource.create(),
                            blockstate.getSeed(BlockPos.ZERO),
                            OverlayTexture.NO_OVERLAY,
                            ModelData.EMPTY,
                            renderType
                    );
                stack.popPose();
                super.render(entity, a, b, stack, source, c);
            }
        }
    }

    @Override
    public ResourceLocation getTextureLocation(FlailProjectile projectile) { return InventoryMenu.BLOCK_ATLAS; }
}
