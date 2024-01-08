package com.zygzag.zygzagsmod.common.registry.base;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.zygzag.zygzagsmod.common.Main.MODID;

public class CustomAkomiRegistry<T> extends AkomiRegistry<T> {
    ResourceKey<? extends Registry<T>> registryKey;
    Registry<T> backingRegistry;
    public CustomAkomiRegistry(DeferredRegister<T> register) {
        super(register);
        backingRegistry = register.makeRegistry(builder -> {});
        registryKey = backingRegistry.key();
    }
    public CustomAkomiRegistry(ResourceLocation registryLocation, String registerModid) {
        this(DeferredRegister.create(ResourceKey.createRegistryKey(registryLocation), registerModid));
    }

    public CustomAkomiRegistry(String registryPath, String registerModid) {
        this(new ResourceLocation(MODID, registryPath), registerModid);
    }

    public CustomAkomiRegistry(String registryPath) {
        this(registryPath, MODID);
    }

    public Registry<T> backingRegistry() {
        return backingRegistry;
    }

    public ResourceKey<? extends Registry<T>> registryKey() {
        return registryKey;
    }
}
