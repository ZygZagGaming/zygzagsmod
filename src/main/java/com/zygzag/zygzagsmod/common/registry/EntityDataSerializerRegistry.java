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

    public static final RegistryObject<EntityDataSerializer<IridiumGolem.State>> IRIDIUM_GOLEM_STATE = INSTANCE.register(
            "iridium_golem_state",
            () -> EntityDataSerializer.simpleEnum(IridiumGolem.State.class)
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
