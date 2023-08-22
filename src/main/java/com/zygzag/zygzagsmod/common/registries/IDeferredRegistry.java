package com.zygzag.zygzagsmod.common.registries;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;

public interface IDeferredRegistry<T> {
    DeferredRegister<T> getRegister();
    void registerTo(IEventBus bus);
}
