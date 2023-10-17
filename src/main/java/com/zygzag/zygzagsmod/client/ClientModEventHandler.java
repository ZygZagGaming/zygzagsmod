package com.zygzag.zygzagsmod.client;

import com.zygzag.zygzagsmod.client.particle.EndSandParticle;
import com.zygzag.zygzagsmod.client.particle.OverheatBeamParticle;
import com.zygzag.zygzagsmod.client.particle.OverheatSphereParticle;
import com.zygzag.zygzagsmod.client.particle.ShockwaveParticle;
import com.zygzag.zygzagsmod.client.renderer.BlazeSentryRenderer;
import com.zygzag.zygzagsmod.client.renderer.CustomBrushableBlockRenderer;
import com.zygzag.zygzagsmod.client.renderer.HomingWitherSkullRenderer;
import com.zygzag.zygzagsmod.client.renderer.IridiumGolemRenderer;
import com.zygzag.zygzagsmod.common.registry.BlockItemEntityRegistry;
import com.zygzag.zygzagsmod.common.registry.EntityTypeRegistry;
import com.zygzag.zygzagsmod.common.registry.ParticleTypeRegistry;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.entity.WitherSkeletonRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.zygzag.zygzagsmod.common.Main.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEventHandler {
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityTypeRegistry.TRANSMUTATION_CHARGE_ENTITY.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityTypeRegistry.HOMING_WITHER_SKULL.get(), HomingWitherSkullRenderer::new);
        event.registerEntityRenderer(EntityTypeRegistry.PLAYER_ALLIED_SKELETON.get(), WitherSkeletonRenderer::new);
        event.registerEntityRenderer(EntityTypeRegistry.IRIDIUM_GOLEM.get(), IridiumGolemRenderer::new);
        event.registerEntityRenderer(EntityTypeRegistry.BLAZE_SENTRY.get(), BlazeSentryRenderer::new);
        event.registerEntityRenderer(EntityTypeRegistry.BEAM_AREA_EFFECT_CLOUD.get(), NoopRenderer::new);
        event.registerEntityRenderer(EntityTypeRegistry.OVERHEAT_BEAM_AREA_EFFECT_CLOUD.get(), NoopRenderer::new);
        event.registerEntityRenderer(EntityTypeRegistry.SPHERE_AREA_EFFECT_CLOUD.get(), NoopRenderer::new);
        event.registerBlockEntityRenderer(BlockItemEntityRegistry.SUSPICIOUS_END_SAND.getBlockEntityType(), CustomBrushableBlockRenderer::new);
    }

    @SubscribeEvent
    public static void registerParticleProviders(final RegisterParticleProvidersEvent event) {
        event.registerSpecial(ParticleTypeRegistry.END_SAND_PARTICLES.get(), new EndSandParticle.Provider());
        event.registerSpriteSet(ParticleTypeRegistry.SHOCKWAVE_PARTICLES.get(), (sprites) -> new ShockwaveParticle.Provider(sprites, 2, 6));
        event.registerSpriteSet(ParticleTypeRegistry.OVERHEAT_BEAM_PARTICLES.get(), OverheatBeamParticle.Provider::new);
        event.registerSpriteSet(ParticleTypeRegistry.OVERHEAT_SPHERE_PARTICLES.get(), OverheatSphereParticle.Provider::new);
    }
}
