package com.zygzag.zygzagsmod.common.registry;

import com.zygzag.zygzagsmod.common.entity.IridiumGolem;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Optional;

import static com.zygzag.zygzagsmod.common.Main.MODID;

public class EntityDataSerializerRegistry extends Registry<EntityDataSerializer<?>> {
    public static final EntityDataSerializerRegistry INSTANCE = new EntityDataSerializerRegistry(DeferredRegister.create(ForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, MODID));

    public static final RegistryObject<EntityDataSerializer<IridiumGolem.AnimationState>> IRIDIUM_GOLEM_ANIMATION_STATE = INSTANCE.register(
            "iridium_golem_animation_state",
            () -> EntityDataSerializer.simpleEnum(IridiumGolem.AnimationState.class)
    );
    public static final RegistryObject<EntityDataSerializer<IridiumGolem.MindState>> IRIDIUM_GOLEM_MIND_STATE = INSTANCE.register(
            "iridium_golem_mind_state",
            () -> EntityDataSerializer.simpleEnum(IridiumGolem.MindState.class)
    );
    public static final RegistryObject<EntityDataSerializer<Optional<IridiumGolem.CurrentAttack>>> IRIDIUM_GOLEM_ATTACK = INSTANCE.register(
            "iridium_golem_attack",
            () -> EntityDataSerializer.optional(
                    (buf, attack) -> buf.writeUtf(attack.name()),
                    (buf) -> IridiumGolem.CurrentAttack.valueOf(buf.readUtf())
            )
    );

    public EntityDataSerializerRegistry(DeferredRegister<EntityDataSerializer<?>> register) {
        super(register);
    }
}
