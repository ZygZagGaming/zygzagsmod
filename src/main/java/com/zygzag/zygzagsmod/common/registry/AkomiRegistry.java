package com.zygzag.zygzagsmod.common.registry;


import net.minecraft.core.Registry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class AkomiRegistry<T> {
    public static final Supplier<List<Consumer<IEventBus>>> REGISTRATION_QUEUE = () -> List.of(
            BlockRegistry.INSTANCE::registerTo,
            ItemRegistry.INSTANCE::registerTo,
            BlockWithItemRegistry.INSTANCE::registerTo,
            BlockEntityRegistry.INSTANCE::registerTo,
            BlockItemEntityRegistry.INSTANCE::registerTo,
            EnchantmentRegistry.INSTANCE::registerTo,
            EntityTypeRegistry.INSTANCE::registerTo,
            GlobalLootModifierSerializerRegistry.INSTANCE::registerTo,
            IridiumGearRegistry.INSTANCE::registerTo,
            MobEffectRegistry.INSTANCE::registerTo,
            ParticleTypeRegistry.INSTANCE::registerTo,
            RecipeSerializerRegistry.INSTANCE::registerTo,
            RecipeTypeRegistry.INSTANCE::registerTo,
            SoundEventRegistry.INSTANCE::registerTo,
            CreativeModeTabRegistry.INSTANCE::registerTo,
            PaintingVariantRegistry.INSTANCE::registerTo,
            EntityDataSerializerRegistry.INSTANCE::registerTo,
            StructureTypeRegistry.INSTANCE::registerTo,
            StructurePieceTypeRegistry.INSTANCE::registerTo,
            AnimationRegistry.INSTANCE::registerTo,
            TransitionAnimationRegistry.INSTANCE::registerTo,
            AttributeRegistry.INSTANCE::registerTo
    );
    final DeferredRegister<T> register;
    Registry<T> backingRegistry;

    public AkomiRegistry(DeferredRegister<T> register) {
        this.register = register;
    }

    public static void register(IEventBus bus) {
        for (Consumer<IEventBus> consumer : REGISTRATION_QUEUE.get()) {
            consumer.accept(bus);
        }
    }

    public void registerTo(IEventBus bus) {
        register.register(bus);
    }

    public <P extends T> Supplier<P> register(String id, Supplier<P> supplier) {
        return register.register(id, supplier);
    }

    public Registry<T> backingRegistry() {
        if (backingRegistry == null) backingRegistry = register.makeRegistry(builder -> {});
        return backingRegistry;
    }
}
