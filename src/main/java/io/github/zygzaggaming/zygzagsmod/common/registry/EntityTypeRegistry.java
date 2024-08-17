package io.github.zygzaggaming.zygzagsmod.common.registry;

import io.github.zygzaggaming.zygzagsmod.common.entity.*;
import io.github.zygzaggaming.zygzagsmod.common.entity.assembly.ShurikenAssembly;
import io.github.zygzaggaming.zygzagsmod.common.registry.base.AkomiRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static io.github.zygzaggaming.zygzagsmod.common.Main.MODID;

public class EntityTypeRegistry extends AkomiRegistry<EntityType<?>> {
    public static final EntityTypeRegistry INSTANCE = new EntityTypeRegistry(DeferredRegister.create(Registries.ENTITY_TYPE, MODID));
    public static final Supplier<EntityType<ThrownTransmutationCharge>> TRANSMUTATION_CHARGE_ENTITY = INSTANCE.register(
            "transmutation_charge",
            () -> EntityType.Builder.<ThrownTransmutationCharge>of(ThrownTransmutationCharge::new, MobCategory.MISC)
                    .sized(0.25f, 0.25f)
                    .build("transmutation_bottle")
    );
    public static final Supplier<EntityType<HomingWitherSkull>> HOMING_WITHER_SKULL = INSTANCE.register(
            "homing_wither_skull",
            () -> EntityType.Builder.<HomingWitherSkull>of(HomingWitherSkull::new, MobCategory.MISC)
                    .sized(0.3125F, 0.3125F)
                    .clientTrackingRange(4)
                    .build("homing_wither_skull")
    );
    public static final Supplier<EntityType<PlayerAlliedSkeleton>> PLAYER_ALLIED_SKELETON = INSTANCE.register(
            "player_allied_skeleton",
            () -> EntityType.Builder.<PlayerAlliedSkeleton>of(PlayerAlliedSkeleton::new, MobCategory.MONSTER)
                    .fireImmune()
                    .immuneTo(Blocks.WITHER_ROSE)
                    .sized(0.7F, 2.4F)
                    .clientTrackingRange(4)
                    .build("player_allied_skeleton")
    );
    public static final Supplier<EntityType<IridiumGolem>> IRIDIUM_GOLEM = INSTANCE.register(
            "iridium_golem",
            () -> EntityType.Builder.of(IridiumGolem::new, MobCategory.MISC)
                    .fireImmune()
                    .sized(1.4F, 2.7F)
                    .build("iridium_golem")
    );
    public static final Supplier<EntityType<BlazeSentry>> BLAZE_SENTRY = INSTANCE.register(
            "blaze_sentry",
            () -> EntityType.Builder.of(BlazeSentry::new, MobCategory.MONSTER)
                    .fireImmune()
                    .sized(1f, 2.8f)
                    .eyeHeight(2.2f)
                    .build("blaze_sentry")
    );
    public static final Supplier<EntityType<OverheatBeamAreaEffectCloud>> OVERHEAT_BEAM_AREA_EFFECT_CLOUD = INSTANCE.register(
            "overheat_beam_area_effect_cloud",
            () -> EntityType.Builder.<OverheatBeamAreaEffectCloud>of(OverheatBeamAreaEffectCloud::new, MobCategory.MISC)
                    .fireImmune()
                    .sized(1f, 1f)
                    .clientTrackingRange(10)
                    .updateInterval(Integer.MAX_VALUE)
                    .build("overheat_beam_area_effect_cloud")
    );
    public static final Supplier<EntityType<BeamAreaEffectCloud>> BEAM_AREA_EFFECT_CLOUD = INSTANCE.register(
            "beam_area_effect_cloud",
            () -> EntityType.Builder.<BeamAreaEffectCloud>of(BeamAreaEffectCloud::new, MobCategory.MISC)
                    .fireImmune()
                    .sized(1f, 1f)
                    .clientTrackingRange(10)
                    .updateInterval(Integer.MAX_VALUE)
                    .build("beam_area_effect_cloud")
    );
    public static final Supplier<EntityType<SphereAreaEffectCloud>> SPHERE_AREA_EFFECT_CLOUD = INSTANCE.register(
            "sphere_area_effect_cloud",
            () -> EntityType.Builder.<SphereAreaEffectCloud>of(SphereAreaEffectCloud::new, MobCategory.MISC)
                    .fireImmune()
                    .sized(0f, 0f)
                    .clientTrackingRange(10)
                    .updateInterval(Integer.MAX_VALUE)
                    .build("sphere_area_effect_cloud")
    );
    public static final Supplier<EntityType<FlailProjectile>> FLAIL_PROJECTILE = INSTANCE.register(
            "flail_projectile",
            () -> EntityType.Builder.<FlailProjectile>of(FlailProjectile::new, MobCategory.MISC)
                    .fireImmune()
                    .sized(0.6f, 0.6f)
                    .clientTrackingRange(10)
                    //.updateInterval(Integer.MAX_VALUE)
                    .build("flail_projectile")
    );
    public static final Supplier<EntityType<ShurikenAssembly>> SHURIKEN_ASSEMBLY_A = INSTANCE.register(
            "shuriken_assembly_a",
            () -> EntityType.Builder.<ShurikenAssembly>of((type, level) -> new ShurikenAssembly(type, level, false, false, 3, 4), MobCategory.MISC)
                    .fireImmune()
                    .sized(2.3f, 0.25f)
                    .build("shuriken_assembly_a")
    );
    public static final Supplier<EntityType<ShurikenAssembly>> SHURIKEN_ASSEMBLY_B = INSTANCE.register(
            "shuriken_assembly_b",
            () -> EntityType.Builder.<ShurikenAssembly>of((type, level) -> new ShurikenAssembly(type, level, true, true, 3, 4), MobCategory.MISC)
                    .fireImmune()
                    .sized(2.3f, 0.25f)
                    .build("shuriken_assembly_b")
    );
    public static final Supplier<EntityType<ShurikenAssembly>> SHURIKEN_ASSEMBLY_C = INSTANCE.register(
            "shuriken_assembly_c",
            () -> EntityType.Builder.<ShurikenAssembly>of((type, level) -> new ShurikenAssembly(type, level, true, false, 5, 4), MobCategory.MISC)
                    .fireImmune()
                    .sized(2.3f, 0.25f)
                    .build("shuriken_assembly_c")
    );
    public EntityTypeRegistry(DeferredRegister<EntityType<?>> register) {
        super(register);
    }
}
