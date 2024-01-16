package com.zygzag.zygzagsmod.common.registry.base;


import com.zygzag.zygzagsmod.common.registry.*;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegisterEvent;

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
            ActionRegistry.INSTANCE::registerTo,
            TransitionActionRegistry.INSTANCE::registerTo,
            AttributeRegistry.INSTANCE::registerTo,
            AttachmentTypeRegistry.INSTANCE::registerTo,
            ItemUpgradeRegistry.INSTANCE::registerTo
    );
    public final DeferredRegister<T> register;

    public AkomiRegistry(DeferredRegister<T> register) {
        this.register = register;
    }

    public static void register(IEventBus bus) {
        for (Consumer<IEventBus> consumer : REGISTRATION_QUEUE.get()) {
            consumer.accept(bus);
        }
    }

    public ResourceKey<? extends Registry<T>> registryKey() {
        return register.getRegistryKey();
    }

    public void registerTo(IEventBus bus) {
        register.register(bus);
    }

    public final void callRegisterEvent(RegisterEvent event) {
        if (event.getRegistryKey() == registryKey()) onRegisterEvent(event);
    }

    public void onRegisterEvent(RegisterEvent event) { }

    public <P extends T> Supplier<P> register(String id, Supplier<P> supplier) {
        return register.register(id, supplier);
    }
}
