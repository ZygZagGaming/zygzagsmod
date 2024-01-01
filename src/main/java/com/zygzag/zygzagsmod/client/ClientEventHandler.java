package com.zygzag.zygzagsmod.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.zygzag.zygzagsmod.common.Main;
import com.zygzag.zygzagsmod.common.effect.SightEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;

import java.awt.*;
import java.util.Map;

@Mod.EventBusSubscriber(modid = Main.MODID, value = Dist.CLIENT)
@SuppressWarnings("unused")
public class ClientEventHandler {

    @SubscribeEvent
    public static void onRender(RenderLevelStageEvent event) {
        Minecraft mc = Minecraft.getInstance();
        PoseStack stack = event.getPoseStack();
        MultiBufferSource.BufferSource src = event.getLevelRenderer().renderBuffers.bufferSource();

        VertexConsumer buffer = src.getBuffer(ClientConstants.SIGHT_RENDER_TYPE);
        Player player = mc.player;
        var sizeReduction = 0.15;
        VoxelShape box = Shapes.box(sizeReduction, sizeReduction, sizeReduction, 1 - sizeReduction, 1 - sizeReduction, 1 - sizeReduction);
        if (player != null && event.getStage() == RenderLevelStageEvent.Stage.AFTER_TRIPWIRE_BLOCKS) {

            stack.pushPose();
            Vec3 cameraPosition = mc.gameRenderer.getMainCamera().getPosition();
            stack.translate(-cameraPosition.x, -cameraPosition.y, -cameraPosition.z);
            ClientConstants.SIGHT_RENDER_TYPE.setupRenderState();
            RenderSystem.disableDepthTest();

            var activeEffects = player.getActiveEffectsMap();
            var map = Main.CURRENT_PLAYER_CACHE.blockStateColorMap();
            for (SightEffect effect : map.keySet()) {
                var inst = activeEffects.get(effect);
                if (inst != null) {
                    var minimap = map.get(effect);
                    for (Map.Entry<BlockPos, Color> entry : minimap.entrySet()) {
                        var blockPos = entry.getKey();
                        var color = entry.getValue();
                        float r = color.getRed() / 255f;
                        float g = color.getGreen() / 255f;
                        float b = color.getBlue() / 255f;
                        float a = color.getAlpha() / 255f;
                        if (inst.endsWithin(20)) a *= (float) (inst.getDuration() / 20.0);
                        LevelRenderer.renderShape(stack, buffer, box, blockPos.getX(), blockPos.getY(), blockPos.getZ(), r, g, b, a);
                    }
                }
            }

            RenderSystem.enableDepthTest();
            ClientConstants.SIGHT_RENDER_TYPE.clearRenderState();
            src.endBatch(ClientConstants.SIGHT_RENDER_TYPE);
            stack.popPose();
        }

    }

    @SubscribeEvent
    public static void effectRemove(final MobEffectEvent.Remove event) {
        var world = event.getEntity().level();
        var player = Minecraft.getInstance().player;
        if (world.isClientSide && player != null && event.getEntity().is(player)) {
            var activeEffects = player.getActiveEffectsMap();
            var map = Main.CURRENT_PLAYER_CACHE.blockStateColorMap();
            for (MobEffect e : activeEffects.keySet()) if (e instanceof SightEffect effect) {
                var inst = activeEffects.get(effect);
                Main.CURRENT_PLAYER_CACHE.update(effect, player, inst.getAmplifier());
            }
        }
    }

    @SubscribeEvent
    public static void effectAdded(final MobEffectEvent.Added event) {
        var world = event.getEntity().level();
        var player = Minecraft.getInstance().player;
        if (world.isClientSide && player != null && event.getEntity().is(player)) {
            var activeEffects = player.getActiveEffectsMap();
            var map = Main.CURRENT_PLAYER_CACHE.blockStateColorMap();
            for (MobEffect e : activeEffects.keySet()) if (e instanceof SightEffect effect) {
                var inst = activeEffects.get(effect);
                Main.CURRENT_PLAYER_CACHE.update(effect, player, inst.getAmplifier());
            }
        }
    }

    @SubscribeEvent
    public static void effectExpired(final MobEffectEvent.Expired event) {
        var world = event.getEntity().level();
        var player = Minecraft.getInstance().player;
        if (world.isClientSide && player != null && event.getEntity().is(player)) {
            var activeEffects = player.getActiveEffectsMap();
            var map = Main.CURRENT_PLAYER_CACHE.blockStateColorMap();
            for (MobEffect e : activeEffects.keySet()) if (e instanceof SightEffect effect) {
                var inst = activeEffects.get(effect);
                Main.CURRENT_PLAYER_CACHE.update(effect, player, inst.getAmplifier());
            }
        }
    }
}