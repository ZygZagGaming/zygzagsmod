package com.zygzag.zygzagsmod.common.registry;

import com.zygzag.zygzagsmod.common.entity.IridiumGolem;
import com.zygzag.zygzagsmod.common.entity.animation.Animator;
import com.zygzag.zygzagsmod.common.registry.base.AkomiRegistry;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

import static com.zygzag.zygzagsmod.common.Main.MODID;

public class EntityDataSerializerRegistry extends AkomiRegistry<EntityDataSerializer<?>> {
    public static final EntityDataSerializerRegistry INSTANCE = new EntityDataSerializerRegistry(DeferredRegister.create(NeoForgeRegistries.ENTITY_DATA_SERIALIZERS, MODID));

    public static final Supplier<EntityDataSerializer<Animator.State>> ANIMATOR_STATE = INSTANCE.register(
            "animator_state",
            () -> EntityDataSerializer.simple(
                    (buf, state) -> state.toNetwork(buf),
                    Animator.State::fromNetwork
            )
    );
    public static final Supplier<EntityDataSerializer<IridiumGolem.MindState>> IRIDIUM_GOLEM_MIND_STATE = INSTANCE.register(
            "iridium_golem_mind_state",
            () -> EntityDataSerializer.simpleEnum(IridiumGolem.MindState.class)
    );

    public EntityDataSerializerRegistry(DeferredRegister<EntityDataSerializer<?>> register) {
        super(register);
    }
}
