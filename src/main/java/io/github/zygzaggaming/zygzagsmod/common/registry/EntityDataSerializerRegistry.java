package io.github.zygzaggaming.zygzagsmod.common.registry;

import io.github.zygzaggaming.zygzagsmod.common.entity.IridiumGolem;
import io.github.zygzaggaming.zygzagsmod.common.entity.animation.Actor;
import io.github.zygzaggaming.zygzagsmod.common.registry.base.AkomiRegistry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

import static io.github.zygzaggaming.zygzagsmod.common.Main.MODID;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class EntityDataSerializerRegistry extends AkomiRegistry<EntityDataSerializer<?>> {
    public static final EntityDataSerializerRegistry INSTANCE = new EntityDataSerializerRegistry(DeferredRegister.create(NeoForgeRegistries.ENTITY_DATA_SERIALIZERS, MODID));

    public static final Supplier<EntityDataSerializer<Actor.State>> ACTOR_STATE = INSTANCE.register(
            "actor_state",
            () -> EntityDataSerializer.forValueType(
                    StreamCodec.of(
                        (buf, state) -> state.toNetwork(buf),
                        Actor.State::fromNetwork
                    )
            )
    );
    public static final Supplier<EntityDataSerializer<IridiumGolem.MindState>> IRIDIUM_GOLEM_MIND_STATE = INSTANCE.register(
            "iridium_golem_mind_state",
            () -> EntityDataSerializer.forValueType(
                    StreamCodec.of(
                            (buf, state) -> buf.writeUtf(state.name()),
                            (buf) -> IridiumGolem.MindState.valueOf(buf.readUtf())
                    )
            )
    );
//    public static final Supplier<EntityDataSerializer<SimplEntityRotation>> ROTATION = INSTANCE.register(
//            "entity_rotation",
//            () -> fromCodec(SimplEntityRotation.CODEC, new SimplEntityRotation())
//    );
//
//    private static <T> EntityDataSerializer<T> fromCodec(Codec<T> codec, T defaultInstance) {
//        return new EntityDataSerializer<T>() {
//            @Override
//            public void write(FriendlyByteBuf buffer, T instance) {
//                codec.encodeStart(NbtOps.INSTANCE, instance).get().ifLeft(buffer::writeNbt);
//            }
//
//            @Override
//            public T read(FriendlyByteBuf buffer) {
//                var result = codec.parse(NbtOps.INSTANCE, buffer.readNbt()).get();
//                return result.left().orElse(defaultInstance);
//            }
//
//            @Override
//            public T copy(T instance) {
//                var result1 = codec.encodeStart(JavaOps.INSTANCE, instance).get().left();
//                if (result1.isPresent()) {
//                    var result2 = codec.parse(JavaOps.INSTANCE, result1.get()).get().left();
//                    if (result2.isPresent()) return result2.get();
//                }
//                return defaultInstance;
//            }
//        };
//    }

    public EntityDataSerializerRegistry(DeferredRegister<EntityDataSerializer<?>> register) {
        super(register);
    }
}
