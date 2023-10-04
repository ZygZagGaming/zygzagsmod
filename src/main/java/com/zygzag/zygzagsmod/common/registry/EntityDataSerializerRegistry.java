package com.zygzag.zygzagsmod.common.registry;

import com.zygzag.zygzagsmod.common.Main;
import com.zygzag.zygzagsmod.common.entity.Animation;
import com.zygzag.zygzagsmod.common.entity.IridiumGolem;
import com.zygzag.zygzagsmod.common.entity.TransitionAnimation;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Optional;

import static com.zygzag.zygzagsmod.common.Main.MODID;

public class EntityDataSerializerRegistry extends Registry<EntityDataSerializer<?>> {
    public static final EntityDataSerializerRegistry INSTANCE = new EntityDataSerializerRegistry(DeferredRegister.create(ForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, MODID));

    public static final RegistryObject<EntityDataSerializer<Animation>> ANIMATION = INSTANCE.register(
            "iridium_golem_animation",
            () -> EntityDataSerializer.simple(
                    (buf, animation) -> {
                        ResourceLocation loc = Main.animationRegistry().getKey(animation);
                        assert loc != null;
                        buf.writeUtf(loc.toString());
                    }, (buf) -> Main.animationRegistry().getValue(new ResourceLocation(buf.readUtf()))
            )
    );
    public static final RegistryObject<EntityDataSerializer<Optional<TransitionAnimation>>> TRANSITION_ANIMATION = INSTANCE.register(
            "iridium_golem_transition_animation",
            () -> EntityDataSerializer.optional(
                    (buf, animation) -> {
                        ResourceLocation loc = Main.transitionAnimationRegistry().getKey(animation);
                        assert loc != null;
                        buf.writeUtf(loc.toString());
                    }, (buf) -> Main.transitionAnimationRegistry().getValue(new ResourceLocation(buf.readUtf()))
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
