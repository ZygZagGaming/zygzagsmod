package com.zygzag.zygzagsmod.common.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.zygzag.zygzagsmod.common.entity.IridiumGolem;
import com.zygzag.zygzagsmod.common.entity.animation.Animator;
import com.zygzag.zygzagsmod.common.registry.base.AkomiRegistry;
import com.zygzag.zygzagsmod.common.util.EntityRotation;
import com.zygzag.zygzagsmod.common.util.SimplEntityRotation;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.util.JavaOps;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

import static com.zygzag.zygzagsmod.common.Main.MODID;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
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
    public static final Supplier<EntityDataSerializer<SimplEntityRotation>> ENTITY_ROTATION = INSTANCE.register(
            "entity_rotation",
            () -> fromCodec(SimplEntityRotation.CODEC, new SimplEntityRotation())
    );

    private static <T> EntityDataSerializer<T> fromCodec(Codec<T> codec, T defaultInstance) {
        return new EntityDataSerializer<T>() {
            @Override
            public void write(FriendlyByteBuf buffer, T instance) {
                codec.encodeStart(NbtOps.INSTANCE, instance).get().ifLeft(buffer::writeNbt);
            }

            @Override
            public T read(FriendlyByteBuf buffer) {
                var result = codec.parse(NbtOps.INSTANCE, buffer.readNbt()).get();
                return result.left().orElse(defaultInstance);
            }

            @Override
            public T copy(T instance) {
                var result1 = codec.encodeStart(JavaOps.INSTANCE, instance).get().left();
                if (result1.isPresent()) {
                    var result2 = codec.parse(JavaOps.INSTANCE, result1.get()).get().left();
                    if (result2.isPresent()) return result2.get();
                }
                return defaultInstance;
            }
        };
    }

    public EntityDataSerializerRegistry(DeferredRegister<EntityDataSerializer<?>> register) {
        super(register);
    }
}
