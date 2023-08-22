package com.zygzag.zygzagsmod.common.registries;

import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static com.zygzag.zygzagsmod.common.Main.MODID;

public class LootItemFunctionTypeRegistry extends Registry<LootItemFunctionType> {
    public static final LootItemFunctionTypeRegistry INSTANCE = new LootItemFunctionTypeRegistry(DeferredRegister.create(net.minecraft.core.Registry.LOOT_FUNCTION_REGISTRY, MODID));

    public LootItemFunctionTypeRegistry(DeferredRegister<LootItemFunctionType> register) {
        super(register);
    }

    /*public static final RegistryObject<LootItemFunctionType> SET_SOUND_EVENT = INSTANCE.register(
            "set_sound_event",
            () -> new LootItemFunctionType(new SetSoundEvent.Serializer())
    );*/
}
