package com.zygzag.zygzagsmod.common.registries;

import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
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

    public static final ArrayList<Consumer<IEventBus>> REGISTRATION_QUEUE = new ArrayList<>();
    // Registries not extending this class are expected to add themselves

    public Registry(DeferredRegister<T> register) {
        this.register = register;
        REGISTRATION_QUEUE.add(register::register);
    }

    public <P extends T> RegistryObject<P> register(String id, Supplier<P> supplier) {
        return register.register(id, supplier);
    }

    public static void register(IEventBus bus) {
        for (Consumer<IEventBus> registration : REGISTRATION_QUEUE) {
            registration.accept(bus);
        }
    }
}
