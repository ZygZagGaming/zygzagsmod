package com.zygzag.zygzagsmod.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.zygzag.zygzagsmod.common.block.CacheBlock;
import com.zygzag.zygzagsmod.common.registry.BlockItemEntityRegistry;
import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BrightnessCombiner;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.phys.AABB;

import java.util.Calendar;

import static com.zygzag.zygzagsmod.common.Main.MODID;

public class CacheRenderer<T extends BlockEntity & LidBlockEntity> implements BlockEntityRenderer<T> {
    private static final String BOTTOM = "bottom";
    private static final String LID = "lid";
    private static final String LOCK = "lock";
    private final ModelPart lid;
    private final ModelPart bottom;
    private final ModelPart lock;
    public static final ModelLayerLocation CACHE = new ModelLayerLocation(new ResourceLocation(MODID, "cache"), "main");

    public CacheRenderer(BlockEntityRendererProvider.Context context) {
        ModelPart part = context.bakeLayer(CACHE);
        bottom = part.getChild("bottom");
        lid = part.getChild("lid");
        lock = part.getChild("lock");
    }

    public static LayerDefinition createSingleBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("bottom", CubeListBuilder.create().texOffs(0, 19).addBox(1.0F, 0.0F, 1.0F, 14.0F, 10.0F, 14.0F), PartPose.ZERO);
        partdefinition.addOrReplaceChild(
                "lid", CubeListBuilder.create().texOffs(0, 0).addBox(1.0F, 0.0F, 0.0F, 14.0F, 5.0F, 14.0F), PartPose.offset(0.0F, 9.0F, 1.0F)
        );
        partdefinition.addOrReplaceChild(
                "lock", CubeListBuilder.create().texOffs(0, 0).addBox(7.0F, -2.0F, 14.0F, 2.0F, 4.0F, 1.0F), PartPose.offset(0.0F, 9.0F, 1.0F)
        );
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void render(T blockEntity, float partialTick, PoseStack stack, MultiBufferSource source, int light, int idk) {
        Level level = blockEntity.getLevel();
        boolean levelNonNull = level != null;
        BlockState blockState = levelNonNull ? blockEntity.getBlockState() : BlockItemEntityRegistry.RED_NETHER_BRICK_CACHE.getBlock().defaultBlockState().setValue(CacheBlock.FACING, Direction.SOUTH);
        stack.pushPose();
        float rotation = blockState.getValue(ChestBlock.FACING).toYRot();
        stack.translate(0.5f, 0.5f, 0.5f);
        stack.mulPose(Axis.YP.rotationDegrees(-rotation));
        stack.translate(-0.5f, -0.5f, -0.5f);

        float openness = 1 - blockEntity.getOpenNess(partialTick);
        openness = 1 - openness * openness * openness;
        int lightColor = LevelRenderer.getLightColor(blockEntity.getLevel(), blockEntity.getBlockPos());
        int lightness = LightTexture.pack(LightTexture.block(lightColor), LightTexture.sky(lightColor));
        Material material = getMaterial(blockEntity);
        VertexConsumer consumer = material.buffer(source, RenderType::entityCutout);
        render(stack, consumer, lid, lock, bottom, openness, lightness, idk);

        stack.popPose();
    }

    private void render(
            PoseStack stack,
            VertexConsumer consumer,
            ModelPart lid,
            ModelPart lock,
            ModelPart bottom,
            float openness,
            int lightness,
            int idk
    ) {
        lid.xRot = -(openness * (float) (Math.PI / 2));
        lock.xRot = lid.xRot;
        lid.render(stack, consumer, lightness, idk);
        lock.render(stack, consumer, lightness, idk);
        bottom.render(stack, consumer, lightness, idk);
    }

    protected Material getMaterial(T blockEntity) {
        return new Material(new ResourceLocation(MODID, "textures/atlas/red_nether_brick_cache.png"), new ResourceLocation(MODID, "entity/red_nether_brick_cache/normal"));
    }

    @Override
    public AABB getRenderBoundingBox(T blockEntity) {
        BlockPos pos = blockEntity.getBlockPos();
        return AABB.encapsulatingFullBlocks(pos.offset(-1, 0, -1), pos.offset(1, 1, 1));
    }
}

