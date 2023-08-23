package com.zygzag.zygzagsmod.common.registries;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Registry<T> implements IDeferredRegistry<T> {
    final DeferredRegister<T> register;
    public DeferredRegister<T> getRegister() {
        return register;
    }

    public void registerTo(IEventBus bus) {
        register.register(bus);
    }

    public static final Supplier<List<Consumer<IEventBus>>> REGISTRATION_QUEUE = () -> List.of(
            BlockRegistry.INSTANCE::registerTo,
            ItemRegistry.INSTANCE::registerTo,
            BlockEntityRegistry.INSTANCE::registerTo,
            BlockItemEntityRegistry.INSTANCE::registerTo,
            BlockWithItemRegistry.INSTANCE::registerTo,
            EnchantmentRegistry.INSTANCE::registerTo,
            EntityRegistry.INSTANCE::registerTo,
            FeatureRegistry.INSTANCE::registerTo,
            GlobalLootModifierSerializerRegistry.INSTANCE::registerTo,
            IridiumGearRegistry.INSTANCE::registerTo,
            LootItemFunctionTypeRegistry.INSTANCE::registerTo,
            MobEffectRegistry.INSTANCE::registerTo,
            ParticleTypeRegistry.INSTANCE::registerTo,
            RecipeSerializerRegistry.INSTANCE::registerTo,
            RecipeTypeRegistry.INSTANCE::registerTo,
            SoundEventRegistry.INSTANCE::registerTo
    );

    public Registry(DeferredRegister<T> register) {
        this.register = register;
    }

    public <P extends T> RegistryObject<P> register(String id, Supplier<P> supplier) {
        return register.register(id, supplier);
    }

    public static void register(IEventBus bus) {
        for (Consumer<IEventBus> registration : REGISTRATION_QUEUE.get()) {
            registration.accept(bus);
        }
    }
}
