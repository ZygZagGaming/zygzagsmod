package com.zygzag.zygzagsmod.common.registry;

import com.zygzag.zygzagsmod.common.entity.HomingWitherSkull;
import com.zygzag.zygzagsmod.common.entity.PlayerAlliedSkeleton;
import com.zygzag.zygzagsmod.common.entity.ThrownTransmutationCharge;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.zygzag.zygzagsmod.common.Main.MODID;

public class EntityRegistry extends Registry<EntityType<?>> {
    public static final EntityRegistry INSTANCE = new EntityRegistry(DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID));
    public EntityRegistry(DeferredRegister<EntityType<?>> register) {
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

}
