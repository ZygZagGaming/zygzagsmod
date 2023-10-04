package com.zygzag.zygzagsmod.common.registry;

import com.zygzag.zygzagsmod.common.entity.Animator;
import com.zygzag.zygzagsmod.common.entity.IridiumGolem;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.zygzag.zygzagsmod.common.Main.MODID;

public class EntityDataSerializerRegistry extends Registry<EntityDataSerializer<?>> {
    public static final EntityDataSerializerRegistry INSTANCE = new EntityDataSerializerRegistry(DeferredRegister.create(ForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, MODID));

    public static final RegistryObject<EntityDataSerializer<Animator.State>> ANIMATOR_STATE = INSTANCE.register(
            "animator_state",
            () -> EntityDataSerializer.simple(
                    (buf, state) -> state.toNetwork(buf),
                    Animator.State::fromNetwork
            )
    );
    public static final RegistryObject<EntityDataSerializer<IridiumGolem.MindState>> IRIDIUM_GOLEM_MIND_STATE = INSTANCE.register(
            "iridium_golem_mind_state",
            () -> EntityDataSerializer.simpleEnum(IridiumGolem.MindState.class)
    );

    public EntityDataSerializerRegistry(DeferredRegister<EntityDataSerializer<?>> register) {
        super(register);
    }
}
