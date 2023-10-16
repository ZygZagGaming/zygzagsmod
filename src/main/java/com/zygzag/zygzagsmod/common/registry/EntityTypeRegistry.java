package com.zygzag.zygzagsmod.common.registry;

import com.zygzag.zygzagsmod.common.entity.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static com.zygzag.zygzagsmod.common.Main.MODID;

public class EntityTypeRegistry extends Registry<EntityType<?>> {
    public static final EntityTypeRegistry INSTANCE = new EntityTypeRegistry(DeferredRegister.create(Registries.ENTITY_TYPE, MODID));
    public EntityTypeRegistry(DeferredRegister<EntityType<?>> register) {
        super(register);
    }

    public static final RegistryObject<EntityType<ThrownTransmutationCharge>> TRANSMUTATION_CHARGE_ENTITY = INSTANCE.register(
            "transmutation_charge",
            () -> EntityType.Builder.<ThrownTransmutationCharge>of(ThrownTransmutationCharge::new, MobCategory.MISC)
                    .sized(0.25f, 0.25f)
                    .build("transmutation_bottle")
    );
    public static final RegistryObject<EntityType<HomingWitherSkull>> HOMING_WITHER_SKULL = INSTANCE.register(
            "homing_wither_skull",
            () -> EntityType.Builder.<HomingWitherSkull>of(HomingWitherSkull::new, MobCategory.MISC)
                    .sized(0.3125F, 0.3125F)
                    .clientTrackingRange(4)
                    .build("homing_wither_skull")
    );
    public static final RegistryObject<EntityType<PlayerAlliedSkeleton>> PLAYER_ALLIED_SKELETON = INSTANCE.register(
            "player_allied_skeleton",
            () -> EntityType.Builder.<PlayerAlliedSkeleton>of(PlayerAlliedSkeleton::new, MobCategory.MONSTER)
                    .fireImmune()
                    .immuneTo(Blocks.WITHER_ROSE)
                    .sized(0.7F, 2.4F)
                    .clientTrackingRange(4)
                    .build("player_allied_skeleton")
    );
    public static final RegistryObject<EntityType<IridiumGolem>> IRIDIUM_GOLEM = INSTANCE.register(
            "iridium_golem",
            () -> EntityType.Builder.of(IridiumGolem::new, MobCategory.MISC)
                    .fireImmune()
                    .sized(1.4F, 2.7F)
                    .build("iridium_golem")
    );
    public static final RegistryObject<EntityType<BlazeSentry>> BLAZE_SENTRY = INSTANCE.register(
            "blaze_sentry",
            () -> EntityType.Builder.<BlazeSentry>of(BlazeSentry::new, MobCategory.MONSTER)
                    .fireImmune()
                    .sized(1f, 0.8f)
                    .build("blaze_sentry")
    );

    public static final RegistryObject<EntityType<BeamAreaEffectCloud>> BEAM_AREA_EFFECT_CLOUD = INSTANCE.register(
            "beam_area_effect_cloud",
            () -> EntityType.Builder.<BeamAreaEffectCloud>of(BeamAreaEffectCloud::new, MobCategory.MISC)
                    .fireImmune()
                    .sized(0.4f, 7f)
                    .clientTrackingRange(10)
                    .updateInterval(Integer.MAX_VALUE)
                    .build("beam_area_effect_cloud")
    );
    public static final RegistryObject<EntityType<SphereAreaEffectCloud>> SPHERE_AREA_EFFECT_CLOUD = INSTANCE.register(
            "sphere_area_effect_cloud",
            () -> EntityType.Builder.<SphereAreaEffectCloud>of(SphereAreaEffectCloud::new, MobCategory.MISC)
                    .fireImmune()
                    .sized(5f, 5f)
                    .clientTrackingRange(10)
                    .updateInterval(Integer.MAX_VALUE)
                    .build("sphere_area_effect_cloud")
    );
}
